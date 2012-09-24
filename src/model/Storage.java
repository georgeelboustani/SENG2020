package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Storage {
	
	private List<Shelf> shelves;
	private Map<ProductType, Integer> maxProducts;
	private Map<ProductType, Integer> thresholds;
	
	public Storage() {
		shelves = new ArrayList<Shelf>();
		maxProducts = new HashMap<ProductType, Integer>();
		thresholds = new HashMap<ProductType, Integer>();
	}
	
	public List<Shelf> getShelves() {
		return shelves;
	}
	
	public void addShelf(ProductCategory cat, int capacity) {
		shelves.add(new Shelf(shelves.size(), cat, capacity));
	}
	
	public int getRemainingSpace() {
		
		int max = 0;

		for (Entry<ProductType, Integer> num: maxProducts.entrySet()){
			max += num.getValue();
		}
		
		return max - getNumProducts();
	}

	//CTL
	public int getNumProductsOfType(ProductType type) {
		int numProducts = 0;
		
		for (Shelf currentShelf: shelves){
			numProducts += currentShelf.getNumProductsOfType(type);
		}
		
		return numProducts;
	}
	
	public int getNumProducts() {
		int numProducts = 0;
		
		for (Shelf currentShelf: shelves){
			numProducts += currentShelf.getCurrentAmount();
		}
		
		return numProducts;
	}
	
	public void addItem(int shelfId, ProductBatch product) {
		
		for (Shelf currentShelf: shelves){
			if (currentShelf.getId() == shelfId) {
				currentShelf.addProductBatch(product);
			}
		}
		
	}
	
	public void addItem(int shelfId, ProductBatch product, int threshold) {
		thresholds.put(product.getProductType(), threshold);
		addItem(shelfId,product);
	}
	
	//CTL
	public boolean underThreshold(ProductType type){
		boolean underThreshold = false;
		
		if ((getNumProductsOfType(type)/maxProducts.get(type)) * 100 < thresholds.get(type)){
			underThreshold = true;
		}
		
		return underThreshold;
	}
	
	//TODO: Write Function to get difference between currentProductCount and maxProductcount
}