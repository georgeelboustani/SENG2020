package model;

import java.sql.PreparedStatement;
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
	private static ArrayList<String> productTypes = new ArrayList<String>();
	private static ArrayList<String> productDescriptions = new ArrayList<String>();
	
	private int typeId;
	
	private ProductType(int i){
		typeId = i;
	}
	
	public static void addProductType(Database db, String type, String description) throws SQLException {
		boolean typeExists = false;
		
		for (String productType: productTypes) {
			if (type.equalsIgnoreCase(productType)) {
				typeExists = true;
			}
		}
		
		if (!typeExists) {
			productTypes.add(type);
			productDescriptions.add(description);
			
			persist(db);
		}
	}
	
	private static void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + ".producttype (`typeId`,`name`,`description`) " +
				"VALUES (?,?,?)";
    	
		int index = productTypes.size() - 1;
		
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, index);
		stmt.setString(2, productTypes.get(index));
		stmt.setString(3, productDescriptions.get(index));
		
		db.executeQuery(stmt);
		con.close();
	}
	
	public static ProductType getProductType(String type) {
		ProductType t = null;
		
		for (int i = 0; i < productTypes.size() && t == null; i++) {
			if (productTypes.get(i).equalsIgnoreCase(type)) {
				t = new ProductType(i);
			}
		}
		
		return t;
	}
	
	public String getType() {
		return productTypes.get(typeId);
	}

	public String getDescription() {
		return productDescriptions.get(typeId);
	}	
	
	public int getTypeId() {
		return this.typeId;
	}
}
