package model;
import java.util.ArrayList;
import java.util.Date;

public class Sale {
	
	private int saleId;
	private Date date;
	private ProductBasket products;
	
	/**
	 * A sale is a past transaction
	 * 
	 * @param id
	 * @param date
	 * @param products - make sure this is a copy
	 */
	public Sale(int id, Date date, ProductBasket products) {
		this.saleId = id;
		this.date = date;
		this.products = products;
	}

	public int getSaleId() {
		return saleId;
	}

	public Date getDate() {
		return date;
	}

	public ProductBasket getProducts() {
		return products;
	}	
}
