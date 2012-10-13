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

import org.apache.commons.lang3.StringUtils;

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
	
	public static ArrayList<ProductBatch> getProductBatchesInStorageTypeFromStore(StorageType type) {
		ArrayList<ProductBatch> products = new ArrayList<ProductBatch>();
		
		ArrayList<Integer> storages = getStorageFromStore(type);
		ArrayList<Integer> shelves = new ArrayList<Integer>();
		for (Integer id: storages) {
			for (Integer shelfId: Shelf.getShelvesFromStorage(id)) {
				shelves.add(shelfId);
			}
		}
		
		// Get all the product batches from the shelves
		for (Integer id: shelves) {
			for (Integer batchId: Shelf.getProductsFromShelf(id)) {
				products.add(ProductBatch.getBatchById(batchId));
			}
		}
		
		return products;
	}
	
	//TODO threshold management
	
	/**
	 * @return hash map of product type to amount available on the floor
	 */
	public static HashMap<String, Integer> getProductInfo() {
		HashMap<String, Integer> products = new HashMap<String, Integer>();
		for (String type: ProductType.getAllAvailableProductTypes()) {
			products.put(type, 0);
		}
		
		ArrayList<ProductBatch> batches = getProductBatchesInStorageTypeFromStore(StorageType.FLOOR);
		for (ProductBatch batch: batches) {
			//TODO: Fix product type id/name confusion up
			String key = ProductType.getProductTypeById(Integer.parseInt(batch.getProductType())).getType();
			products.put(key, products.get(key) + batch.getAmount());
		}
		
		return products;
	}
	
	public static ArrayList<Integer> getStorageFromStore(StorageType type) {
		ArrayList<Integer> storages = new ArrayList<Integer>();
		
		try {
			String query = "SELECT DISTINCT storestorage.storageId FROM seng2020.storestorage, seng2020.storage " +
					       "WHERE storestorage.storeId = ? AND storage.type = ?";
			PreparedStatement stmt = PosSystem.getConnection().prepareStatement(query);
			stmt.setInt(1, PosSystem.getStoreId());
			stmt.setString(2, type.toString());
			
			ResultSet tables = stmt.executeQuery();
			while(tables.next()) {
				storages.add(tables.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return storages;
	}
	
	public static Storage getStorageById(int id) {
		Storage storage = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.storage WHERE id = " + id).executeQuery();
			tables.next();
			storage = new Storage(tables.getInt("id"),StorageType.valueOf(tables.getString("type")));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return storage;
	}
}