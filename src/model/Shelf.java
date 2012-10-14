package model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import database.Database;

public class Shelf {

	private int id;
	private int maxProducts;
	private int currentAmount;

	//TODO make sure that when adding to shelf, current amount increases and never exceeds storage/shelf max
	public Shelf(int id, int maxProducts,int currentAmount) {
		super();
		this.id = id;
		this.maxProducts = maxProducts;
		this.currentAmount = currentAmount;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".shelf (`shelfId`,`maxProducts`,`currentAmount`) " +
				"VALUES (?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		stmt.setInt(2, this.maxProducts);
		stmt.setInt(3, this.currentAmount);
		
		db.executeQuery(stmt);
	}
	
	// TODO - save category in system
	public void assignCategory(ProductCategory category) {
		
	}
	
	public int getShelfId() {
		return id;
	}
	
	public int getMaxProducts() {
		return maxProducts;
	}

	public int getCurrentAmount(){
		return currentAmount;
	}
	
	public static void removeShelfBatchMapping(int shelfId, int batchId)  throws SQLException {
	    PreparedStatement stmt = null;
        Database db = PosSystem.getDatabase();
        Connection con = PosSystem.getConnection();
        
        String query = "DELETE FROM " + db.getDbName() + ".shelfbatch " +
                "WHERE shelfId = ? AND batchId = ?";
        
        stmt = con.prepareStatement(query);
        stmt.setInt(1, shelfId);
        stmt.setInt(2, batchId);
        
        db.executeQuery(stmt);
	}
	
	public static void setCurrentAmount(int amount, int shelfId) throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "UPDATE " + db.getDbName() + ".shelf " +
				"SET currentAmount = ? WHERE shelfId = ?";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, amount);
		stmt.setInt(2, shelfId);
		
		db.executeQuery(stmt);
	}
	
//	public int getNumProductsOfType(ProductType type) {
//		int amount = 0;
//		
//		for (ProductBatch batch: batches){
//			if (batch.getProductType().equals(type)) {
//				amount += batch.getAmount();
//			}
//		}
//		
//		return amount;
//	}
	
	private static void addToShelfImmediate(int shelfId, int batchId) throws SQLException {
		PreparedStatement stmt;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".shelfbatch (`shelfId`,`batchId`) " +
				"VALUES (?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, shelfId);
		stmt.setInt(2, batchId);
		
		db.executeQuery(stmt);
		
	}
	
	public static void addToShelf(int shelfId, ProductBatch batch) throws SQLException {
		Connection con = PosSystem.getConnection();
		
		ResultSet batchId = con.prepareStatement("SELECT batchId FROM seng2020.shelfbatch WHERE shelfId = " + shelfId).executeQuery();
		ProductBatch currentBatch = null;
		boolean found = false;
		while (batchId.next()){
			currentBatch = ProductBatch.getBatchById(batchId.getInt("batchId"));
			
			if(currentBatch.getProductType().equals(batch.getProductType()) && 
			   currentBatch.getExpiry().equals(batch.getExpiry())){
			   found = true;
				break;
			}
		}
		
		//TODO Optional: add constraint to ensure amount does not exceed max
		if( Shelf.getShelfById(shelfId).getCurrentAmount() + batch.getAmount() > Shelf.getShelfById(shelfId).getMaxProducts() ){
		    throw new SQLException();
		}
		
		
		if(!found){
			batch.persist();
			addToShelfImmediate(shelfId,batch.getBatchId());
		} else {
			currentBatch.setAmount(currentBatch.getAmount() + batch.getAmount());
		}
			
		Shelf.setCurrentAmount(Shelf.getShelfById(shelfId).getCurrentAmount() + batch.getAmount(), shelfId);
	}
	
	public static boolean isOnShelf(int batchId) {
		PreparedStatement stmt;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "SELECT * FROM " + db.getDbName() + ".shelfbatch WHERE batchId = ?";
		try{
			stmt = con.prepareStatement(query);
			stmt.setInt(1, batchId);
			ResultSet shelfId = stmt.executeQuery();
			shelfId.next();
			shelfId.getString("shelfId");
		}catch(SQLException e){
		    Database.printStackTrace(e);
			return false;
		}
		return true;
	}
	
	/**
	 * @return This will return true only if batch id exists, shelfid exists and the batch id is mapped to the shelf id
	 */
	public static boolean isOnShelf(int batchId, int shelfId) {
		PreparedStatement stmt;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "SELECT * FROM " + db.getDbName() + ".shelfbatch WHERE batchId = ? AND shelfId = ?";
		try{
			stmt = con.prepareStatement(query);
			stmt.setInt(1, batchId);
			stmt.setInt(2, shelfId);
			ResultSet shelfSet = stmt.executeQuery();
			shelfSet.next();
			shelfSet.getString("shelfId");
		}catch(SQLException e){
		    Database.printStackTrace(e);
			return false;
		}
		return true;
	}
	
   public static boolean isInStorage(int shelfId, int storageId) {
        PreparedStatement stmt;
        Database db = PosSystem.getDatabase();
        Connection con = PosSystem.getConnection();
        
        String query = "SELECT * FROM seng2020.storageshelf " +
                       "WHERE shelfId = ? AND storageId = ?";
        try{
            stmt = con.prepareStatement(query);
            stmt.setInt(1, shelfId);
            stmt.setInt(2, storageId);
            
            ResultSet tables = stmt.executeQuery();
            tables.next();
            tables.getString("shelfId");
        } catch(SQLException e) {
            return false;
        }
        
        return true;
    }
	
	public static ArrayList<Integer> getShelvesFromStorage(int storageId) {
		ArrayList<Integer> shelves = new ArrayList<Integer>();
		
		try {
			String query = "SELECT shelfId FROM seng2020.storageshelf WHERE storageId = ?";
			PreparedStatement stmt = PosSystem.getConnection().prepareStatement(query);
			stmt.setInt(1, storageId);
			
			ResultSet tables = stmt.executeQuery();
			while(tables.next()) {
				shelves.add(new Integer(tables.getInt("shelfId")));
			}
		} catch (SQLException e) {
		    Database.printStackTrace(e);
			return null;
		}
		
		return shelves;
	}
	
	public static ArrayList<Integer> getProductsFromShelf(int shelfId) {
		ArrayList<Integer> batches = new ArrayList<Integer>();
		
		try {
			String query = "SELECT batchId FROM seng2020.shelfbatch WHERE shelfId = ?";
			PreparedStatement stmt = PosSystem.getConnection().prepareStatement(query);
			stmt.setInt(1, shelfId);
			
			ResultSet tables = stmt.executeQuery();
			while(tables.next()) {
				batches.add(new Integer(tables.getInt("batchId")));
			}
		} catch (SQLException e) {
		    Database.printStackTrace(e);
			return null;
		}
		
		return batches;
	}
	
	public static Shelf getShelfById(int id) {
		Shelf shelf = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.shelf WHERE shelfId = " + id).executeQuery();
			tables.next();
			shelf = new Shelf(tables.getInt("shelfId"),tables.getInt("maxProducts"),tables.getInt("currentAmount"));
		} catch (SQLException e) {
		    Database.printStackTrace(e);
			return null;
		}
		
		return shelf;
	}

    public static Integer getShelfIdByBatchId(int batchId) {
        Integer shelfId = null;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.shelfbatch WHERE batchId = " + batchId).executeQuery();
            tables.next();
            shelfId = tables.getInt("shelfId");
        } catch (SQLException e) {
            return null;
        }
        
        return shelfId;
    }
}