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
	
   public static Supplier getSupplierById(int id) {
        Supplier supplier = null;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.supplier WHERE supplierId = " + id).executeQuery();
            tables.next();
            supplier = new Supplier(tables.getInt("supplierId"),tables.getString("name"),tables.getString("address"),tables.getInt("phone"),tables.getString("description"));
        } catch (SQLException e) {
            return null;
        }
        
        return supplier;
    }
   
   public static ArrayList<Integer> getAllSuppliersId() {
       ArrayList<Integer> suppliers = new ArrayList<Integer>();
       Connection con = PosSystem.getConnection();
       
       try {
           String query = "SELECT * FROM seng2020.supplier";
           
           PreparedStatement stmt = con.prepareStatement(query);
           
           ResultSet tables = stmt.executeQuery();
           while (tables.next()) {
               suppliers.add(tables.getInt("supplierId"));
           }
       } catch (SQLException e) {
           return null;
       }
       
       return suppliers;
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
