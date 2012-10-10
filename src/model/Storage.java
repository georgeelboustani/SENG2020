package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mysql.jdbc.Connection;

import database.Database;

public class Storage {
	
	private int id;
	private List<Shelf> shelves;
	private StorageType type;
	
	// Maps between product types and integers
	private Map<String, Integer> maxProducts;
	private Map<String, Integer> thresholds;
	
	public Storage(int id, StorageType type) {
		this.id = id;
		this.type = type;
		shelves = new ArrayList<Shelf>();
		maxProducts = new HashMap<String, Integer>();
		thresholds = new HashMap<String, Integer>();
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".storage (`id`,`type`) " +
				"VALUES (?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		stmt.setString(2, this.type.toString());
		
		db.executeQuery(stmt);
		
		persistShelfMapping();
	}
	
	private void persistShelfMapping() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();

		String query = "INSERT into " + db.getDbName() + ".storageshelf (`storageId`,`shelfId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		
		Iterator<Shelf> itr = shelves.iterator();
		while (itr.hasNext()) {
			stmt.setInt(2, itr.next().getShelfId());
			db.executeQuery(stmt);
		}
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public List<Shelf> getShelves() {
		return shelves;
	}
	
	public int getNumProducts() {
		int numProducts = 0;
		
		for (Shelf currentShelf: shelves){
			numProducts += currentShelf.getCurrentAmount();
		}
		
		return numProducts;
	}
	
	public static void transfer(int fromShelfId, int toShelfId, int batchId, int amount) throws SQLException {
		if(Shelf.isOnShelf(batchId,fromShelfId) && amount <= Shelf.getShelfById(toShelfId).getMaxProducts() - Shelf.getShelfById(toShelfId).getCurrentAmount()) {
			
			ProductBatch oldBatch = ProductBatch.getBatchById(batchId);
			if (oldBatch.getAmount() >= amount) {
				// TODO - Check if toShelf can fit this many of this product
				ProductBatch newBatch = new ProductBatch(PosSystem.generateNextId(TableName.PRODUCTBATCH),
						                                 oldBatch.getProductType(),
						                                 oldBatch.getExpiry(),
						                                 oldBatch.getPrice(),
						                                 amount);
				
				Shelf.addToShelf(toShelfId, newBatch);
				oldBatch.setAmount(oldBatch.getAmount() - amount);
				Shelf.setCurrentAmount(Shelf.getShelfById(fromShelfId).getCurrentAmount() - amount, fromShelfId);
			}
		}
	}
	
	public static Storage getStorageById(int id) {
		Storage storage = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.storage WHERE id = " + id).executeQuery();
			tables.next();
			storage = new Storage(tables.getInt("id"),StorageType.valueOf(tables.getString("type")));
		} catch (SQLException e) {
			return null;
		}
		
		return storage;
	}
}