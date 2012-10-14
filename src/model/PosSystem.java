package model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import database.Database;
import exception.InvalidIdException;
import org.apache.commons.lang3.*;

public class PosSystem {
	
	private static Database db;
	private static int storeId;
	private static int registerId;
	private static boolean initialisationSuccessful;
	private static Connection con;
	private PosSystem() {
		
	}
	
	public static void initialise(Database db, int storeId, int registerId) throws InvalidIdException, SQLException {
		PosSystem.db = db;
		PosSystem.con = db.createConnection();
		if (Store.getStoreById(storeId) == null) {
//			initialisationSuccessful = false;
//			throw new InvalidIdException();
		    initialisationSuccessful = true;
		    PosSystem.storeId = storeId;
		    Store newStore = new Store(storeId);
		    newStore.persist();
		    System.out.println("Created store with id from the configuration");
		} else {
			PosSystem.storeId = storeId;
			initialisationSuccessful = true;
		}
		
		if (Register.getRegisterById(registerId) == null) {
//            initialisationSuccessful = false;
//            throw new InvalidIdException();
		      initialisationSuccessful = true;
	          PosSystem.registerId = registerId;
	          Register newRegister = new Register(registerId,0,null);
	          newRegister.persist();
	          System.out.println("Created register with id from the configuration");
        } else {
            PosSystem.registerId = registerId;
            initialisationSuccessful = true;
        }
		
		if (Employee.getEmployeesFromStore(EmployeeType.ADMIN) == null) {
		    Employee firstAdmin = Store.getStoreById(storeId).addEmployee("first", "admin", EmployeeType.ADMIN, "pass");
		    System.out.println("First Admin has been created with id " + firstAdmin.getEmployeeId());
		}
	}
	
	public static int addStore() throws SQLException {
		Store newStore = new Store(PosSystem.generateNextId(TableName.STORE));
		newStore.persist();
		return newStore.getStoreId();
	}
	
	public static Database getDatabase() {
		return db;
	}
	
	public static int getStoreId() {
		return storeId;
	}
	
	public static boolean isInitialised() {
		return initialisationSuccessful;
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static int generateNextId(TableName table) throws SQLException {
		ResultSet rows = con.prepareStatement("SELECT * FROM " + db.getDbName() + "." + table.toString()).executeQuery();
		
		int maxId = 0;
		while (rows.next()) {
			if (rows.getInt(1) > maxId) {
				maxId = rows.getInt(1);
			}
		}
		
		return ++maxId;
	}
	
	
	public static void refreshConnection() throws SQLException {
		try {
			con.close();
			PosSystem.con = db.createConnection();
		} catch (SQLException e) {
			System.err.println("Error: Failed to refresh connection");
			throw e;
		}
	}
}
