package tests;
import model.*;

import org.junit.Test;

public class testStorage {

	@Test
	public void constructor() {
		Storage testStorageObj = new Storage();
		
		assert(testStorageObj.getNumProducts()==0);
		assert(testStorageObj.getRemainingSpace()==0);
		assert(testStorageObj.getShelves().isEmpty());
	}

}
