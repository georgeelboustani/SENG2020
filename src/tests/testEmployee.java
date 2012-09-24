package tests;
import model.*;

import org.junit.Test;

public class testEmployee {

	@Test
	public void constructor() {
		Admin adminTestObj = new Admin(0,"Chris","Tin-Loi");
		
		//Assert creation of object
		assert(adminTestObj.getEmployeeId()==0);
		assert(adminTestObj.getFirstName()=="Chris");
		assert(adminTestObj.getLastName()=="Tin-Loi");
	}
	
	@Test
	public void setAndGetEmployeeId() {
		Admin adminTestObj = new Admin(0,"Chris","Tin-Loi");
		
		adminTestObj.setEmployeeId(1);
		assert(adminTestObj.getEmployeeId()==1);
	}

	@Test
	public void setAndGetFirstName() {
		Admin adminTestObj = new Admin(0,"Chris","Tin-Loi");
		
		adminTestObj.setFirstName("George");
		assert(adminTestObj.getFirstName()=="George");
	}
	
	@Test
	public void setAndGetLastName() {
		Admin adminTestObj = new Admin(0,"Chris","Tin-Loi");
		
		adminTestObj.setLastName("Boustani");
		assert(adminTestObj.getLastName()=="Boustani");
	}
		


}
