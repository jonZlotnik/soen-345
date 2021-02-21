
public class TransactionDto {
	private String targetId;
	private String targetPayPalId;
	public String getTargetPayPalId() {
		return targetPayPalId;
	}

	public void setTargetPayPalId(String targetPayPalId) {
		this.targetPayPalId = targetPayPalId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	private double amount;

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String string) {
		this.targetId = string;
	}
	

}
