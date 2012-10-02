package tests;

import java.sql.SQLException;

import model.*;
import org.junit.Test;
import database.Database;

public class testDatabase {
	
	@Test
	public void insertEmployee() throws SQLException {
		Database db = new Database("user","pass");
		db.clearData();
//		
//		Employee testEmp = new Employee(4,"TEE","T",EmployeeType.ADMIN,"pass");
//		
//		try {
//		testEmp.persist(db);
//			
//		//TODO: Persist test doesn't actually throw - fix test
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("EMPLOYEEEE");
	}
	
	@Test
	public void insertLogEntry() throws SQLException {
		Database db = new Database("user","pass");
		db.clearData();
		
//		Employee testEmp = new Employee(5,"YOO","B",EmployeeType.STAFF,"pass");
//		testEmp.persist(db);
//
//		db.executeQuery( db.getConnection().prepareStatement("INSERT INTO seng2020.register (`registerId`, `balance`, `currentEmployee`)VALUES(2, 5, 5)"));
//		
//		LogEntry testLog = new LogEntry(0, Database.getCurrentDate(),5,"Test Description", 2);
//		
//		
//		try {
//			testLog.persist(db);
//			//TODO: Persist test doesn't actually throw - fix test
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("LOG ENTRYYYY");
	}
	
}
