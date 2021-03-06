package model;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.mysql.jdbc.Connection;

import database.Database;

public class Sale {
	
	private int saleId;
	private Date date;
	private Trolley products;
	
	/**
	 * A sale is a past transaction
	 * 
	 * @param id
	 * @param date
	 * @param products - make sure this is a copy
	 */
	public Sale(int id, Date date, Trolley products) {
		this.saleId = id;
		this.date = date;
		this.products = products;
	}
	
	public Sale(int id, Date date) {
	    this.saleId =id;
	    this.date = date;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".sale (`saleId`,`date`) " +
				"VALUES (?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.saleId);
		stmt.setDate(2, this.date);
		
		db.executeQuery(stmt);
		
		persistProductMapping();
	}
	
	public void persistProductMapping() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".salebatches (`saleId`,`batchId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.saleId);
    	
		Iterator<ProductBatch> itr = products.getProductsIterator();
		while (itr.hasNext()) {
			stmt.setInt(2, itr.next().getBatchId());
			db.executeQuery(stmt);
		}
	}
	
	public static boolean isBatchInSale(int saleId, int batchId) {
	    boolean isInSale = true;
	    
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.salebatches " +
            		                                                      "WHERE saleId = " + saleId +
            		                                                      "AND batchId = " + batchId).executeQuery();
            tables.next();
            tables.getInt(1);
        } catch (Exception e) {
            isInSale = false;
        }
        
        return isInSale;
	}
	
    public static boolean batchHasBeenSold(int batchId) {
        boolean isInSale = true;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.salebatches " +
                                                                          "WHERE batchId = " + batchId).executeQuery();
            tables.next();
            tables.getInt(1);
        } catch (Exception e) {
            isInSale = false;
        }
        
        return isInSale;
    }
    
    public static Sale getSaleById(int saleId) {
        Sale sale = null;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.sale WHERE saleId = " + saleId).executeQuery();
            tables.next();
            sale = new Sale(saleId, tables.getDate("date"));
        } catch (SQLException e) {
            return null;
        }
        
        return sale;
    }

	public int getSaleId() {
		return saleId;
	}

	public Date getDate() {
		return date;
	}

	public Trolley getProducts() {
		return products;
	}

    public static ArrayList<String> getBatchesFromSale(int saleId2) {
        ArrayList<String> batches = new ArrayList<String>();
        
        try {
            String query = "SELECT batchId FROM seng2020.salebatches WHERE saleId = ?";
            PreparedStatement stmt = PosSystem.getConnection().prepareStatement(query);
            stmt.setInt(1, saleId2);
            
            ResultSet tables = stmt.executeQuery();
            while(tables.next()) {
                batches.add(String.valueOf(tables.getInt("batchId")));
            }
        } catch (SQLException e) {
            return batches;
        }
        
        return batches;
    }	
}
