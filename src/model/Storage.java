package model;

import java.sql.PreparedStatement;
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
	private Map<ProductType, Integer> maxProducts;
	private Map<ProductType, Integer> thresholds;
	
	public Storage(int id, StorageType type) {
		this.id = id;
		this.type = type;
		shelves = new ArrayList<Shelf>();
		maxProducts = new HashMap<ProductType, Integer>();
		thresholds = new HashMap<ProductType, Integer>();
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".storage (`id`,`type`) " +
				"VALUES (?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		stmt.setString(2, this.type.toString());
		
		db.executeQuery(stmt);
		con.close();
		
		persistShelfMapping(db);
	}
	
	private void persistShelfMapping(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "INSERT into " + db.getDbName() + ".storageshelf (`storageId`,`shelfId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		
		Iterator<Shelf> itr = shelves.iterator();
		while (itr.hasNext()) {
			stmt.setInt(2, itr.next().getShelfId());
			db.executeQuery(stmt);
		}
		con.close();
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
	
	public void addShelf(ProductCategory cat, int capacity) {
		Shelf newShelf = new Shelf(shelves.size(), capacity);
		shelves.add(newShelf);
		newShelf.assignCategory(cat);
	}
	
	public void addShelf(int capacity,int numShelves) {
		for (int i = 0; i < numShelves; i++) {
			Shelf newShelf = new Shelf(shelves.size(), capacity);
			shelves.add(newShelf);
		}
	}
	
	public int getRemainingSpace() {
		
		int max = 0;

		for (Entry<ProductType, Integer> num: maxProducts.entrySet()){
			max += num.getValue();
		}
		
		return max - getNumProducts();
	}

	//CTL
	public int getNumProductsOfType(ProductType type) {
		int numProducts = 0;
		
		for (Shelf currentShelf: shelves){
			numProducts += currentShelf.getNumProductsOfType(type);
		}
		
		return numProducts;
	}
	
	public int getNumProducts() {
		int numProducts = 0;
		
		for (Shelf currentShelf: shelves){
			numProducts += currentShelf.getCurrentAmount();
		}
		
		return numProducts;
	}
	
	public void addItem(int shelfId, ProductBatch product) {
		
		for (Shelf currentShelf: shelves){
			if (currentShelf.getShelfId() == shelfId) {
				currentShelf.addProductBatch(product);
			}
		}
		
	}
	
	public void addItem(int shelfId, ProductBatch product, int threshold) {
		thresholds.put(product.getProductType(), threshold);
		addItem(shelfId,product);
	}
	
	//CTL
	public boolean underThreshold(ProductType type){
		boolean underThreshold = false;
		
		if ((getNumProductsOfType(type)/maxProducts.get(type)) * 100 < thresholds.get(type)){
			underThreshold = true;
		}
		
		return underThreshold;
	}
	
	//TODO: Write Function to get difference between currentProductCount and maxProductcount
}