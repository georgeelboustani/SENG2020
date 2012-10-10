package tests;

import org.junit.Test;
import model.*;

public class testShelf {

	@Test
	public void constructor() {
		Shelf testShelfObj = new Shelf(0,50,0);
		
		assert(testShelfObj.getCurrentAmount()==0);
		assert(testShelfObj.getMaxProducts()==50);
		assert(testShelfObj.getShelfId()==0);
	}
	
	@Test
	public void setAndGetProductCategory() {
		//stub
	}
	
	@Test
	public void setAndGetProduct() {
		//stub
	}

	@Test
	public void getNumProductsOfType() {
		//stub
	}
	
	@Test
	public void addProductBatch() {
		//stub
	}
	
}
