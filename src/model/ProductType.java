package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import database.Database;

/**
 * Actual products such as milk, bread, chocolate
 *  
 * @author George
 *
 */
public class ProductType {
	/*
	 * These should be populated when the system begins from the database
	 */

	private int typeId;
	private String name;
	private String description;
	
	private ProductType(int i, String name, String description){
		typeId = i;
		this.name = name;
		this.description = description;
	}
	
	public static void addProductType(String type, String description) throws SQLException {
		try {	
			String dbName = PosSystem.getDatabase().getDbName();
			PreparedStatement stmt = null;
			Connection con = PosSystem.getConnection();
			
			if (getProductTypeByName(type) == null) {
				String query = "INSERT INTO " + dbName + ".producttype " +
						"(`typeId`,`name`,`description`) VALUES (?,?,?)";
				
		    	stmt = con.prepareStatement(query);
		    	stmt.setInt(1, PosSystem.generateNextId(TableName.PRODUCTTYPE));
				stmt.setString(2, type);
				stmt.setString(3, description);
								
				PosSystem.getDatabase().executeQuery(stmt);
			}
			
		} catch (Exception e) {
			System.err.println("Failed to create product type. It already exists");
		}
	}
	
	public static ProductType getProductTypeByName(String name) {
		ProductType type = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.producttype WHERE name = '" + name + "'").executeQuery();
			tables.next();
			type = new ProductType(tables.getInt("typeId"),tables.getString("name"),tables.getString("description"));
		} catch (SQLException e) {
			return null;
		}
		
		return type;
	}
	
	public String getType() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}	
	
	public int getTypeId() {
		return this.typeId;
	}
}
