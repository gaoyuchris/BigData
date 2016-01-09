package enity;

public class Industry {
	private double total_amount;
	private String growth;
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
	}
	public String getGrowth() {
		return growth;
	}
	public void setGrowth(String growth) {
		this.growth = growth;
	}
	
	public Industry(double total_amount) {
		super();
		this.total_amount = total_amount;
	}
	
	public Industry(float total_amount, String growth) {
		super();
		this.total_amount = total_amount;
		this.growth = growth;
	}
	
	
	
	
	

}
