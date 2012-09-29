package model;

import java.util.ArrayList;

/**
 * Actual products such as milk, bread, chocolate
 *  
 * @author George
 *
 */
public class ProductType {
	
	private static ArrayList<String> productTypes = new ArrayList<String>();
	private static ArrayList<String> productDescriptions = new ArrayList<String>();
	
	private int typeId;
	
	private ProductType(int i){
		typeId = i;
	}
	
	public static ProductType createProductType(String type) {
		ProductType t = null;
		
		for (int i = 0; i < productTypes.size() && t == null; i++) {
			if (productTypes.get(i).equals(type)) {
				t = new ProductType(i);
			}
		}
		
		return t;
	}
			
	public static void addProductType(String type, String description) {
		productTypes.add(type);
		productDescriptions.add(description);
	}
	
	public String getType() {
		return productTypes.get(typeId);
	}

	public String getDescription() {
		return productDescriptions.get(typeId);
	}	
	
	public int getTypeId() {
		return this.typeId;
	}
}
