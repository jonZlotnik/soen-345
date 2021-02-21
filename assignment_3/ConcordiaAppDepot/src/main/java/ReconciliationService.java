import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReconciliationService {
	private final FinancialTransactionDAO financialTxDAO;
	private final MembershipDAO membershipDAO;
	private final PayPalFacade payPalFacade;
	

	public ReconciliationService(FinancialTransactionDAO financialTxDAO,
			MembershipDAO membershipDAO, PayPalFacade payPalFacade) {
		this.financialTxDAO = financialTxDAO;
		this.membershipDAO =membershipDAO;
		this.payPalFacade = payPalFacade;
	}

	public int reconcile() {
		List<TransactionDto> unSettledTxs = financialTxDAO.retrieveUnSettledTransactions();
		Map<String, List<TransactionDto>> developerTxMap = new LinkedHashMap<String, List<TransactionDto>>();
		// Setting a developer wise Transaction map.
		for (TransactionDto transactionDto : unSettledTxs) {
			List<TransactionDto> transactions = developerTxMap.get(transactionDto.getTargetId());
			if (transactions == null) {
				transactions = new ArrayList<TransactionDto>();
			}
			transactions.add(transactionDto);
			developerTxMap.put(transactionDto.getTargetId(), transactions);
		}

		// Looping through the developer Id , only once paypal is called
		for (String developerId : developerTxMap.keySet()) {
			MembershipStatusDto membership = membershipDAO.getStatusFor(developerId);
			String payPalId = null;
			double totalTxAmount = 0.00;
			for (TransactionDto tx : developerTxMap.get(developerId)) {
				totalTxAmount += tx.getAmount();
				payPalId = tx.getTargetPayPalId();
			}
			double payableAmount = totalTxAmount - totalTxAmount * membership.getDeductible();
			payPalFacade.sendAdvice(new PaymentAdviceDto(payableAmount, payPalId, null));
		}
		return unSettledTxs.size();
	}
}