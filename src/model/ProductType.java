package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
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
	private int categoryId;
	
	private ProductType(int i, String name, String description, int categoryId){
		typeId = i;
		this.name = name;
		this.description = description;
		this.categoryId = categoryId;
	}
	
	public static void addProductType(String type, String description, int categoryId) throws SQLException {
		try {	
			String dbName = PosSystem.getDatabase().getDbName();
			PreparedStatement stmt = null;
			Connection con = PosSystem.getConnection();
			
			if (getProductTypeByName(type) == null) {
				String query = "INSERT INTO " + dbName + ".producttype " +
						"(`typeId`,`name`,`description`,`categoryId`) VALUES (?,?,?,?)";
				
		    	stmt = con.prepareStatement(query);
		    	stmt.setInt(1, PosSystem.generateNextId(TableName.PRODUCTTYPE));
				stmt.setString(2, type);
				stmt.setString(3, description);
				stmt.setInt(4, categoryId);
								
				PosSystem.getDatabase().executeQuery(stmt);
			}
			
		} catch (Exception e) {
			System.err.println("Failed to create product type. It already exists");
		}
	}
	
	public static ArrayList<String> getAllAvailableProductTypes() {
		ArrayList<String> types = new ArrayList<String>();
		Connection con = PosSystem.getConnection();
		
		try {
			String query = "SELECT * FROM seng2020.producttype";
			
	    	PreparedStatement stmt = con.prepareStatement(query);
			
			ResultSet tables = stmt.executeQuery();
			while (tables.next()) {
				ProductType type = new ProductType(tables.getInt("typeId"),tables.getString("name"),tables.getString("description"),tables.getInt("categoryId"));
				types.add(type.name);
			}
		} catch (SQLException e) {
			return null;
		}
		
		return types;
	}
	
	public static ProductType getProductTypeByName(String name) {
		ProductType type = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.producttype WHERE name = '" + name + "'").executeQuery();
			tables.next();
			type = new ProductType(tables.getInt("typeId"),tables.getString("name"),tables.getString("description"),tables.getInt("categoryId"));
		} catch (SQLException e) {
			return null;
		}
		
		return type;
	}
	
	public static ProductType getProductTypeById(int id) {
		ProductType type = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.producttype WHERE typeId = " + id).executeQuery();
			tables.next();
			type = new ProductType(id,tables.getString(2),tables.getString(3),tables.getInt("categoryId"));
		} catch (SQLException e) {
			return null;
		}
		
		return type;
	}
	
	public String getType() {
		return this.name;
	}
	
	public String getCategoryName() {
		return ProductCategory.getProductCategoryById(this.categoryId).getCategoryName();
	}

	public String getDescription() {
		return this.description;
	}	
	
	public int getTypeId() {
		return this.typeId;
	}
}
