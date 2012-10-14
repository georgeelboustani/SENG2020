package tests;

import java.sql.SQLException;

import model.*;

import org.junit.Ignore;
import org.junit.Test;
import database.Database;
import exception.InvalidIdException;

public class testDatabase {
	
	@Test @Ignore
	public void insertEmployee() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		PosSystem.getDatabase().clearData();
		
		Employee testEmp = new Employee(4,"TEE","T",EmployeeType.ADMIN,"pass");
		
		try {
		testEmp.persist();
			
		//TODO: Persist test doesn't actually throw - fix test
		} catch (SQLException e) {
		    Database.printStackTrace(e);
		}
	}
	
	@Test @Ignore
	public void insertLogEntry() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		PosSystem.getDatabase().clearData();
		
		Employee testEmp = new Employee(5,"YOO","B",EmployeeType.STAFF,"pass");
		testEmp.persist();

		PosSystem.getDatabase().executeQuery( PosSystem.getConnection().prepareStatement("INSERT INTO seng2020.register (`registerId`, `balance`, `currentEmployee`)VALUES(2, 5, 5)"));
		
		LogEntry testLog = new LogEntry(0, Database.getCurrentDate(),5,"Test Description", 2);
		
		
		try {
			testLog.persist();
			//TODO: Persist test doesn't actually throw - fix test
		} catch (SQLException e) {
		    Database.printStackTrace(e);
		}
		
		System.out.println("LOG ENTRYYYY");
	}
	
	@Test @Ignore
	public void changeEmployeeClass() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		
		Employee testEmp = new Employee(10,"Matty","Johns",EmployeeType.STAFF,"pass");
		testEmp.persist();
		testEmp.promote();

	}
	
	@Test @Ignore
	public void disableEmployee() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		
		Employee testEmp = new Employee(11,"Andrew","Johns",EmployeeType.ADMIN,"pass");
		testEmp.persist();
		testEmp.setActiveStatus(false);

	}
	
	@Test @Ignore
	public void enableEmployee() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		
		Employee testEmp = new Employee(12,"Bill","Clinton",EmployeeType.MANAGER,"pass");
		testEmp.persist();
		testEmp.setActiveStatus(false);
		testEmp.setActiveStatus(true);
	}
	
	@Test @Ignore
	public void changeProductPrice() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		Store s = new Store(1);
		s.changeProductPrice(1, 1024);
	}
	
	
	@Test @Ignore
	public void changeDetails() throws SQLException, InvalidIdException {
		PosSystem.initialise(new Database("user","pass"), 1,1);
		
		Employee testEmp = new Employee(11,"Andrew","Johns",EmployeeType.ADMIN,"pass");
		testEmp.setFirstName("Steve");
		testEmp.setLastName("Jobs");
		testEmp.setPassword("Apple");
	}
	

}
