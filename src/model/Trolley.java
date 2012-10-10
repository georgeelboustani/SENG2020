package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Trolley {
	
	private ArrayList<ProductBatch> products;
	
	public Trolley() {
		this.products = new ArrayList<ProductBatch>();
	}
	
	public Trolley(ArrayList<ProductBatch> products) {
		this.products = products;
	}
	
	public Iterator<ProductBatch> getProductsIterator() {
		return products.iterator();
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
	
	/** 
	 * @param productBatch
	 * @return true if productBatct exists, false otherwise
	 */
	public boolean addProductBatch(ProductBatch productBatch) {
		boolean exists = false;
		for (ProductBatch batch: products) {
			if (batch.getProductType().equals(productBatch.getProductType())) {
				if (batch.getExpiry().equals(productBatch.getExpiry())) {
					exists = true;
					batch.setAmount(batch.getAmount() + productBatch.getAmount());
					break;
				}
			}
		}
		if (!exists) {
			products.add(productBatch);
		}
		
		return exists;
	}

	public void persist() throws SQLException {
		for(ProductBatch batch: products){
			batch.persist();
		}
	}
}
