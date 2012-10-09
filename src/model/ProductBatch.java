package model;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import database.Database;

public class ProductBatch {
	
	private int batchId;
	private String type;
	private Date expiry;
	private double price;
	private int amount;
		
	public ProductBatch(int batchId, String type, Date expiry, double price, int amount) {
		this.batchId = batchId;
		this.type = type;
		this.expiry = expiry;
		this.price = price;
		this.amount = amount;
	}

	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".productbatch (`batchId`,`productType`,`expiry`,`price`,`amount`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.batchId);
		stmt.setObject(2, this.type);
		stmt.setDate(3, this.expiry);
		stmt.setDouble(4, this.price);
		stmt.setInt(5, this.amount);
		
		db.executeQuery(stmt);
		con.close();
	}
	
	public int getBatchId() {
		return this.batchId;
	}

	public String getProductType() {
		return type;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	//TODO: Test
	public void setAmount(int amount) {
		try {
			PreparedStatement stmt = null;
			Connection con = PosSystem.getDatabase().getConnection();
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".productbatch " +
					       "SET amount = ? " +
					       "WHERE batchId = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setInt(1, amount);
			stmt.setInt(2, this.batchId);

			PosSystem.getDatabase().executeQuery(stmt);
			con.close();
			this.amount = amount;
		} catch (Exception e) {
			System.err.println("Failed to set amount");
		}

	}

	public boolean removeProducts(int amount) {
		if (amount <= this.amount) {
			this.amount -= amount;
			return true;
		} else {
			return false;
		}
	}
	
	public static ProductBatch getBatchById(int id) {
		ProductBatch batch = null;
		
		try {
			ResultSet tables = PosSystem.getDatabase().getConnection().prepareStatement("SELECT * FROM seng2020.productbatch WHERE id = " + id).executeQuery();
			tables.next();
			batch = new ProductBatch(tables.getInt("batchId"),
					                 tables.getString("productType"),
					                 tables.getDate("expiry"),
					                 tables.getDouble("price"),
					                 tables.getInt("amount"));
		} catch (SQLException e) {
			return null;
		}
		
		return batch;
	}
}
