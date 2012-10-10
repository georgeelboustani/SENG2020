package tests;
import model.*;

import org.junit.Test;

public class testStorage {

	@Test
	public void constructor() {
		Storage testStorageObj = new Storage(0,StorageType.FLOOR);
		
		assert(testStorageObj.getId()==0);
		assert(testStorageObj.getNumProducts()==0);
		assert(testStorageObj.getShelves().isEmpty());
	}

	@Test
	public void addShelves() {
		//stub
	}
	
	@Test
	public void getNumProductsOfType() {
		//stub
	}
	
	@Test
	public void getNumProducts() {
		//stub
	}
	
	@Test
	public void addItems() {
		//stub
	}
	
	@Test
	public void checkThreshold() {
		//stub
	}
	

}
