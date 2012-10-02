package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import database.Database;

public class Supplier {
	
	private int supplierId;
	private String companyName;
	private String address;
	private String phone;
	private String description;
	
	public Supplier(int supplierId, String companyName, String address, String phone, String description) {
		this.supplierId = supplierId;
		this.companyName = companyName;
		this.address = address;
		this.phone = phone;
		this.description = description;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".supplier (`supplierId`,`name`,`address`,`phone`,`description`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.supplierId);
		stmt.setString(2, this.companyName);
		stmt.setString(3, this.address);
		stmt.setString(4, this.phone);
		stmt.setString(5, this.description);
		
		db.executeQuery(stmt);
		con.close();
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}	
}
