package tests;

import org.junit.Test;

public class testAssertion {

	@Test
	public void assertsEnabled() throws Exception {	
		boolean enabled = false;
		
		try {
			assert(false);
		} catch(AssertionError e) {
			enabled = true;
		}
		
		if (!enabled) {
			System.out.println("Enable assertions");
			throw new Exception();
		}
	}
}
