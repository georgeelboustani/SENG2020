package model;
import java.util.Date;

public class ProductBatch {
	
	private ProductType type;
	private Date expiry;
	private double price;
	private int amount;
		
	public ProductBatch(ProductType type, Date expiry, double price, int amount) {
		this.type = type;
		this.expiry = expiry;
		this.price = price;
		this.amount = amount;
	}

	public ProductType getType() {
		return type;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
