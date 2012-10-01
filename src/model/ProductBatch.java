package model;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.mysql.jdbc.Connection;

import database.Database;

public class ProductBatch {
	
	private int batchId;
	private ProductType type;
	private Date expiry;
	private double price;
	private int amount;
		
	public ProductBatch(int batchId, ProductType type, Date expiry, double price, int amount) {
		this.batchId = batchId;
		this.type = type;
		this.expiry = expiry;
		this.price = price;
		this.amount = amount;
	}

	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + ".productbatch (`batchId`,`productType`,`expiry`,`price`,`amount`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.batchId);
		stmt.setObject(2, this.type);
		stmt.setDate(3, db.convertDate(this.expiry));
		stmt.setDouble(4, this.price);
		stmt.setInt(5, this.amount);
		
		db.executeQuery(stmt);
		con.close();
	}
	
	public int getBatchId() {
		return this.batchId;
	}

	public ProductType getProductType() {
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

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean removeProducts(int amount) {
		if (amount <= this.amount) {
			this.amount -= amount;
			return true;
		} else {
			return false;
		}
	}
}
