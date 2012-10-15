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
	
	public ArrayList<ProductBatch> getProducts() {
	    return this.products;
	}
	
	public boolean contains(ProductType type, Date expiry) {
	    boolean exists = false;
        for (ProductBatch batch: products) {
            if (batch.getProductType().equals(type.getType())) {
                if (batch.getExpiry().equals(expiry)) {
                    exists = true;
                }
            }
            
            if (exists) {
                break;
            }
        }
        
        return exists;
	}
	
	public void removeProducts(ProductType type, Date expiry, int amount) {
	    boolean exists = false;
	    ProductBatch tempBatch = null;
		for (ProductBatch batch: products) {
		    if (batch.getProductType().equals(type.getType())) {
                if (batch.getExpiry().equals(expiry)) {
                    exists = true;
                    batch.setAmount(batch.getAmount() - amount);
                    tempBatch = batch;
                }
            }
		    
		    if (exists) {
                break;
            }
		}
		
		if (exists && tempBatch.getAmount() == 0) {
		    products.remove(tempBatch);
		    tempBatch.delete();
		}
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
				}
			}
			
			if (exists) {
			    break;
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
