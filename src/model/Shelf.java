package model;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import database.Database;

public class Shelf {

	private int id;
	private List<ProductBatch> batches;
	private int maxProducts;
	private int currentAmount;
	private List<ProductCategory> categories;

	public Shelf(int id, int maxProducts) {
		super();
		this.categories = new ArrayList<ProductCategory>();
		this.id = id;
		this.batches = new ArrayList<ProductBatch>();
		this.maxProducts = maxProducts;
		this.currentAmount = 0;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".shelf (`shelfId`,`maxProducts`,`currentAmount`) " +
				"VALUES (?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.id);
		stmt.setInt(2, this.maxProducts);
		stmt.setInt(3, this.currentAmount);
		
		db.executeQuery(stmt);
		con.close();
	}
	
	// TODO - make sure the equals still works even when saved and laoded
	//        from database
	public void assignCategory(ProductCategory category) {
		if (!categories.contains(category)) {
			categories.add(category);
		}
	}
	
	public void removeCategory(ProductCategory category) {
		categories.remove(category);
	}
	
	public List getProducts(){
		return batches;
	}
	
	public int getShelfId() {
		return id;
	}
	
	public void addProductBatch(ProductBatch batch) {
		// TODO - Update shelfbatch table
		batches.add(batch);
	}
	
	public int getMaxProducts() {
		return maxProducts;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}
	
	public int getNumProductsOfType(ProductType type) {
		int amount = 0;
		
		for (ProductBatch batch: batches){
			if (batch.getProductType().equals(type)) {
				amount += batch.getAmount();
			}
		}
		
		return amount;
	}
	
	
}