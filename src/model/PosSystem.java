package model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;
import exception.InvalidStoreIdException;

public class PosSystem {
	
	private static Database db;
	private static int storeId;
	private static boolean initialisationSuccessful;
	
	private PosSystem() {
		
	}
	
	public static void initialise(Database db, int storeId) throws InvalidStoreIdException {
		PosSystem.db = db;
		
		if (Store.getStoreById(storeId) == null) {
			initialisationSuccessful = false;
			throw new InvalidStoreIdException();
		} else {
			PosSystem.storeId = storeId;
			initialisationSuccessful = true;
		}
	}
	
	public static int addStore() throws SQLException {
		Store newStore = new Store(PosSystem.generateNextId(TableName.STORE));
		newStore.persist(db);
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
	
	public static int generateNextId(TableName table) throws SQLException {
		ResultSet tables = db.getConnection().prepareStatement("SELECT * FROM " + table.toString()).executeQuery();
		
		ResultSet idSet = tables.getArray(1).getResultSet();
		
		int maxId = 0;
		while (idSet.next()) {
			if (idSet.getInt(1) > maxId) {
				maxId = idSet.getInt(1);
			}
		}
		
		return maxId++;
	}
}
