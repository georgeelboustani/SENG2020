package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import database.Database;

public class Supplier {
	
	private int supplierId;
	private String companyName;
	private String address;
	private int phone;
	private String description;
	
	public Supplier(int supplierId, String companyName, String address, int phone, String description) {
		this.supplierId = supplierId;
		this.companyName = companyName;
		this.address = address;
		this.phone = phone;
		this.description = description;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".supplier (`supplierId`,`name`,`address`,`phone`,`description`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.supplierId);
		stmt.setString(2, this.companyName);
		stmt.setString(3, this.address);
		stmt.setInt(4, this.phone);
		stmt.setString(5, this.description);
		
		db.executeQuery(stmt);
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
