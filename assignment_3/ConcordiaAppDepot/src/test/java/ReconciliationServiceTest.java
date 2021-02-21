
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;//you need to use this import when completing the excercise
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;//you need to use this import when completing the excercise


public class ReconciliationServiceTest {
	
	ReconciliationService service;
	
	@Mock FinancialTransactionDAO financialTransactionDAO;
	@Mock MembershipDAO membershipDAO;
	@Mock PayPalFacade payPalFacade;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service = new ReconciliationService(financialTransactionDAO,
				membershipDAO, payPalFacade);
		MembershipStatusDto basicMembership =
				new MembershipStatusDto();
		basicMembership.setDeductible(.3);
		//TODO: mock membershipDAO.getStatusFor(any id) to return the basic membership
		when(membershipDAO.getStatusFor(Mockito.anyString())).thenReturn(basicMembership);//this is not mandatory
	}
	
		
	@Test
	//This test work should pass
	public void when_no_Transaction_to_process_Service_RETURNS_Processing_Count_Zero() throws Exception{
		assertEquals(0, service.reconcile());
	}
	
	
	@Test
	public void when_Transaction_to_process_Service_RETURNS_Processing_Count_One() throws Exception {
		// 1. We will concentrate on when DAO returns 1 transaction.
		// Stub the retrieval method to return a transaction:
		List<TransactionDto> listOfOneTransaction = new ArrayList<>(1);
		listOfOneTransaction.add(new TransactionDto());
		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(listOfOneTransaction);

		assertEquals(1, service.reconcile());
	}

	@Test
	public void when_transaction_exists_Then_membership_details_is_retrieved_for_the_developer() throws Exception {
		
		/* 2.
		 * Add a test to verify that the reconcile method calls membershipDAO to
		 * fetch the membership details for a developer. In the test, create a list of
		 * transactions and add only one transaction with the developer's ID as DEV001.
		 * Stub financialTransactioDAO to retrieve this list.
		 */
		TransactionDto txDto = new TransactionDto();
		txDto.setTargetId("DEV001");
		List<TransactionDto> listOfOneTransaction = new ArrayList<>(1);
		listOfOneTransaction.add(txDto);

		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(listOfOneTransaction);

		 assertEquals(1, service.reconcile());
		 /* During test execution, the service will get this list and then it should ask
		 * membershipDAO to get the details of developer DEV001. We will verify that in
		 * the test using Mockito's verify() API. Add the verify code below.
		 */
		verify(membershipDAO, times(1)).getStatusFor("DEV001");
	}
	
	@Test
	public void when_transactions_exist_then_membership_details_is_retrieved_for_each_developer() throws Exception {
		/*
		 * 3. Now add a test to verify multiple transactions. This is a very important
		 * stage in TDD. Once we are done with one, we should test our code against
		 * many. In a test we will create two transactions: one for John and another one
		 * for Bob. We will expect that for both developers membershipDAO will be
		 * called. We are going to use ArgumentCaptor and Times to verify the
		 * invocation. Verification will check the number of invocations by passing new
		 * Times(2), then the argument captor will capture arguments for all
		 * invocations. Finally, we will ask the argument captor to return the list of
		 * invocations and from that list we will verify whether membershipDAO was
		 * invoked for both Bob and John:
		 */
		
		List<TransactionDto> multipleTxs = new ArrayList<TransactionDto>();
		TransactionDto johnsTransaction = new TransactionDto();
		String johnsDeveloperId = "john001";
		johnsTransaction.setTargetId(johnsDeveloperId);

		TransactionDto bobsTransaction = new TransactionDto();
		String bobsDeveloperId = "bob999";
		bobsTransaction.setTargetId(bobsDeveloperId);

		multipleTxs.add(johnsTransaction);
		multipleTxs.add(bobsTransaction);

		//add the mock code to retrieve the unsettled transactions using multipleTxs
		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(multipleTxs);

		assertEquals(2, service.reconcile());
		
		//Declare an argument captor below
		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

		//Verify the number of invocations by passing new Times(2) below.
		verify(membershipDAO, times(2)).getStatusFor(argumentCaptor.capture());

		//Use the argCaptor to return the list of invocations below
		List<String> passedValues = argumentCaptor.getAllValues();
		
//		From that list verify whether membershipDAO was invoked for Bob and John below

		assertEquals(johnsDeveloperId, passedValues.get(0));
		assertEquals(bobsDeveloperId, passedValues.get(1));
		//uncomment when you fill the code above

	}
	
	@Test
	public void when_transaction_exists_Then_sends_Payble_TO_PayPal() throws Exception {
		/* TODO: 4. Verify that the pay advice was sent. Create a transactional list for
		 * David, a developer. The createTxDto(...) method creates a Transaction Dto
		 * instance from the developer ID, PayPal ID, and app price
		 */
		List<TransactionDto> davidsTransactionList = new ArrayList<TransactionDto>();
		String davidsDeveloperId = "dev999";
		String davidsPayPalId = "david@paypal.com";
		double davidsAppPrice = 100.00;
		davidsTransactionList.add(createTxDto(davidsDeveloperId, davidsPayPalId, davidsAppPrice));
		// mock the unsettled david's transaction below.
		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(davidsTransactionList);
		
		assertEquals(1, service.reconcile());

		//verify the call to the payPalFacade.sendAdvice method
		verify(payPalFacade, times(1)).sendAdvice(any(PaymentAdviceDto.class));
	}


	private TransactionDto createTxDto(String developerId, String payPalId,
			double appPrice) {
		/*
		 * The createTxDto(...) method creates a Transaction Dto instance from the
		 * developer ID, PayPal ID, and app price.
		 */
		TransactionDto transdto = new TransactionDto();
		transdto.setTargetId(developerId);
		transdto.setTargetPayPalId(payPalId);
		transdto.setAmount(appPrice);
		return transdto;
	}
	
	@Test
	public	void calculates_payable() throws Exception {
		/*
		 * 5. We need to calculate the amount payable. How do we test this? Stub
		 * MemebershipDAO to return a Basic membership dto object. This means 30 percent
		 * is deducted from the original app price. If the app price is USD 100.00,
		 * then PayPal payment advice should be USD 70.00. Use Mockito's ArgumentCaptor
		 * method to verify that.
		 */
		List<TransactionDto> ronaldosTransactions = new ArrayList<TransactionDto>();
		String ronaldosDeveloperId = "ronaldo007";
		String ronaldosPayPalId = "Ronaldo@RealMdrid.com";
		double ronaldosSoccerFee = 100.00;
		ronaldosTransactions.add(createTxDto(ronaldosDeveloperId, ronaldosPayPalId, ronaldosSoccerFee));
		//mock mock the unsettled Ronaldo's transaction below.
		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(ronaldosTransactions);
		
		assertEquals(1, service.reconcile());
		
		//instantiate an ArgumentCaptor for calculateAdvice below
		ArgumentCaptor<PaymentAdviceDto> calculatedAdvice = ArgumentCaptor.forClass(PaymentAdviceDto.class);

		//verify that a call to payPalFacade.sendAdvice was sent from the argumentCaptor
		verify(payPalFacade, times(1)).sendAdvice(calculatedAdvice.capture());
		
		//corroborate that calculatedAdvice.getValue().getAmount() == 70
		assertTrue(70.00 == calculatedAdvice.getValue().getAmount());//uncomment when you fill the code above
	}
	
	
	@Test
	public void calculates_payable_with_multiple_Transaction() throws Exception {
		/*
		 * 6. Now it's time to test multiple transactions—one with CAD 200.00 and
		 * another with CAD 150.00, and Standard and Premium memberships. The deductible
		 * is 15 percent and 10 percent respectively. The memberShip(double percent)
		 * method creates membershipStatusDto. Stub the membershipDAO instance to return
		 * membership deductible 15 percent for John and 10 percent for Dave. Use
		 * ArgumentCaptor to capture the PayPalFacade call. Then, verify that the
		 * correct deductible was computed and passed to facade for both the developers.
		 */

		List<TransactionDto> transactionList = new ArrayList<TransactionDto>();
		String johnsDeveloperId = "john001";
		String johnsPayPalId = "john@gmail.com";
		double johnsAppFee = 200;
		transactionList.add(createTxDto(johnsDeveloperId, johnsPayPalId, johnsAppFee));
		String davesDeveloperId = "dave888";
		String davesPayPalId = "IamDave009@yahoo.co.uk";
		int davesAppFee = 150;
		transactionList.add(createTxDto(davesDeveloperId, davesPayPalId, davesAppFee));
		// mock financialTransactionDAO to retrieve the unsettled transactions from transactionList
		when(financialTransactionDAO.retrieveUnSettledTransactions())
				.thenReturn(transactionList);
		
		//Use the memberShip(double percent) method to create an instance of membershipStatusDto and
		//set the corresponding deductible.
		//Note. When we use argument matchers, then all the arguments should use matchers. If we want to
		//use a specific value for an argument, then we can use eq() method.

		// mock the membership for johnDeveloperId to return .15
		MembershipStatusDto johnMemberShipStatus = memberShip(.15);
		when(membershipDAO.getStatusFor(eq(johnsDeveloperId))).thenReturn(johnMemberShipStatus);

		// mock the membership for davesDeveloperId to return .10
		MembershipStatusDto daveMemberShipStatus = memberShip(.10);
		when(membershipDAO.getStatusFor(eq(davesDeveloperId))).thenReturn(daveMemberShipStatus);

		assertEquals(2, service.reconcile());

		// Instantiate an ArgumentCaptor<PaymentAdviceDto> for calculatedAdvice
		ArgumentCaptor<PaymentAdviceDto> calculatedAdvice = ArgumentCaptor.forClass(PaymentAdviceDto.class);
		
		//verify the call to the method payPalFacade Times(2)
		verify(payPalFacade, times(2)).sendAdvice(calculatedAdvice.capture());

		//corroborate that the correct deductible was computed and passed to facade for both the developers:

		assertTrue(170.00 == calculatedAdvice.getAllValues().get(0).getAmount());
		assertTrue(135.00 == calculatedAdvice.getAllValues().get(1).getAmount());
		//uncomment when you fill the code above

	}


	private MembershipStatusDto memberShip(double d) {
		//The memberShip(double percent) method creates membershipStatusDto
		MembershipStatusDto msdto = new MembershipStatusDto();
		msdto.setDeductible(d);
		return msdto;
	}
	
	@Test
	public void calculates_payable_with_multiple_Transaction_For_same_developer() throws Exception {
		/*
		 * TODO: 7. One thing is still missing. How can we minimize the PayPal
		 * Transactions? If a developer develops two apps, we should invoke PayPal
		 * facade only once not twice. PayPal charges against each transaction and also
		 * multiple transaction calls can create performance issues. Add a test for the
		 * developer Janet, who has two apps: FishPond and TicTacToe. Default
		 * membership is Basic, with 30 percent deductible.
		 */
		List<TransactionDto> janetsAppFees = new ArrayList<TransactionDto>();
		String janetsDeveloperId = "janet12567";
		String janetsPayPalId = "JanetTheJUnitGuru@gmail.com";
		double fishPondAppFee = 200;
		double ticTacToeAppFee = 100;
		janetsAppFees.add(createTxDto(janetsDeveloperId, janetsPayPalId, fishPondAppFee));
		janetsAppFees.add(createTxDto(janetsDeveloperId, janetsPayPalId, ticTacToeAppFee));
		//// mock financialTransactionDAO to retrieve the unsettled transactions from janetsappFees
		when(financialTransactionDAO.retrieveUnSettledTransactions()).thenReturn(janetsAppFees);

		assertEquals(2, service.reconcile());

		// Instantiate an ArgumentCaptor<PaymentAdviceDto> for calculatedAdvice
		ArgumentCaptor<PaymentAdviceDto> calculatedAdvice = ArgumentCaptor.forClass(PaymentAdviceDto.class);
		
		//verify the call to the method payPalFacade Times(1)for janet:
		verify(payPalFacade, times(1)).sendAdvice(calculatedAdvice.capture());

		//corroborate that the correct advice was calculated 
		assertTrue(210.00 == calculatedAdvice.getValue().getAmount()); //uncomment when you fill the code above
	}
}
