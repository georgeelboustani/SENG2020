package model;

import java.util.ArrayList;
import java.util.Date;

public class Trolley {
	
	private ArrayList<ProductBatch> products;
	
	public Trolley(ArrayList<ProductBatch> products) {
		this.products = products;
	}
	
	/**
	 * @param type
	 * @param expiry
	 * @param amount
	 * @return - returns true if succesfully remove products. Will return
	 *           false if no product basket which satisifies the parameters
	 *           exists in the basket, including when the amount given
	 *           is too big.
	 */
	public boolean removeProducts(ProductType type, Date expiry, int amount) {
		boolean valid = false;
		
		for (ProductBatch batch: products) {
			if (batch.getProductType().equals(type) && batch.getExpiry().equals(expiry)) {
				valid = batch.removeProducts(amount);
				break;
			}
		}
		
		return valid;
	}
	
}
