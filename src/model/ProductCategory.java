package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.Connection;

public class ProductCategory {
	
	private int categoryId;
	private String categoryName;
	
	private ProductCategory(int id, String name) {
		this.categoryId = id;
		this.categoryName = name;
	}
	
	public int getCategoryId() {
		return this.categoryId;
	}
	
	public String getCategoryName() {
		return this.categoryName;
	}
	
	public static void addProductCategory(int id, String category) {
		try {	
			String dbName = PosSystem.getDatabase().getDbName();
			PreparedStatement stmt = null;
			Connection con = PosSystem.getConnection();
			
			if (getProductCategoryByName(category) == null) {
				String query = "INSERT INTO " + dbName + ".productCategory " +
						"(`categoryId`,`categoryName`) VALUES (?,?)";
				
		    	stmt = con.prepareStatement(query);
		    	stmt.setInt(1, PosSystem.generateNextId(TableName.PRODUCTCATEGORY));
				stmt.setString(2, category);
				
				PosSystem.getDatabase().executeQuery(stmt);
			}
		} catch (Exception e) {
			System.err.println("Failed to create product type. It already exists");
		}
	}
	
	public static ProductCategory getProductCategoryByName(String name) {
		ProductCategory category = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.productcategory " +
																		  "WHERE categoryName = '" + name + "'").executeQuery();
			tables.next();
			category = new ProductCategory(tables.getInt("categoryId"),tables.getString("categoryName"));
		} catch (Exception e) {
			return null;
		}
		
		return category;
	}
}