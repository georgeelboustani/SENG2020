package model;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".productbatch (`batchId`,`productType`,`expiry`,`price`,`amount`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.batchId);
		stmt.setInt(2, ProductType.getProductTypeByName(this.type).getTypeId());
		stmt.setDate(3, this.expiry);
		stmt.setDouble(4, this.price);
		stmt.setInt(5, this.amount);
		
		db.executeQuery(stmt);
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
        Connection con = PosSystem.getConnection();
        
        try {
            PreparedStatement stmt = null;
            
            String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".productbatch " +
                           "SET price = ? " +
                           "WHERE batchId = ?";
            
            stmt = con.prepareStatement(query);
            stmt.setDouble(1, price);
            stmt.setInt(2, this.batchId);

            PosSystem.getDatabase().executeQuery(stmt);
            this.price = price;
        } catch (Exception e) {
            System.err.println("Failed to set price");
        }
	}

	public int getAmount() {
		return amount;
	}


	public void delete() {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;

			String query = "DELETE FROM " + PosSystem.getDatabase().getDbName() + ".productbatch " +
					       "WHERE batchId = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setInt(1, this.batchId);

			PosSystem.getDatabase().executeQuery(stmt);
		} catch (Exception e) {
		    Database.printStackTrace(e);
			System.err.println("Failed to delete batch");
		}

	}
	
	public void setAmount(int amount) {
		Connection con = PosSystem.getConnection();
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".productbatch " +
					       "SET amount = ? " +
					       "WHERE batchId = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setInt(1, amount);
			stmt.setInt(2, this.batchId);

			PosSystem.getDatabase().executeQuery(stmt);

			this.amount = amount;
		} catch (Exception e) {
		    Database.printStackTrace(e);
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
	
	public ProductCategory getProductCategory() {
	    return ProductCategory.getProductCategoryByName(ProductType.getProductTypeByName(this.type).getCategoryName());
	}
	
	public static ArrayList<String> getBriefProductReport() {
	    ArrayList<String> info = new ArrayList<String>();
        Connection con = PosSystem.getConnection();
        try {
            ResultSet tables = con.prepareStatement("SELECT seng2020.productbatch.productType,SUM(seng2020.productbatch.amount) " +
            		                                "FROM seng2020.productbatch,seng2020.shelfbatch " +
            		                                "WHERE seng2020.productbatch.batchId = seng2020.shelfbatch.batchId " +
            		                                "GROUP BY seng2020.productbatch.productType").executeQuery();
            while (tables.next()) {
                info.add(ProductType.getProductTypeById(tables.getInt(1)).getType() + "   " + tables.getInt(2));
            }
            
            if (info.size() <= 0) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }

        return info;
	}
	
    public static ArrayList<String> getFullProductReport() {
        
        ArrayList<String> info = new ArrayList<String>();
        info.add("|---------------------------------------------------------------------------------------|");
        info.add("|                                Full Product Report                                    |");
        ArrayList<Integer> storages = new ArrayList<Integer>();
        storages.addAll(Storage.getStorageFromStore(StorageType.FLOOR));
        storages.addAll(Storage.getStorageFromStore(StorageType.BACKROOM));
        storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
        
        for(int storageId : storages){
            info.add("|---------|-----------------------------------------------------------------------------|");
            info.add("| Storage | " + Storage.getStorageById(storageId).getStorageType().toString() +"       \t\t\t\t\t\t\t\t|");
            for(int shelfId : Shelf.getShelvesFromStorage(storageId)){
                boolean firstProd=true;
                boolean empty=true;
                info.add("|---------|-----------------------------------------------------------------------------|");
                info.add("|         | Shelf [" + shelfId + "]\t("+ Shelf.getShelfById(shelfId).getCurrentAmount() + "/" +
                                                 Shelf.getShelfById(shelfId).getMaxProducts() + ")\t " +
                                                 "\t\t\t\t\t\t|");

                for(int productId : Shelf.getProductsFromShelf(shelfId)){
                    if(firstProd){
                        info.add("|         |-----------------------------------------------------------------------------|");
                        empty=false;
                        firstProd=false;
                    }else{
                        info.add("|         |------|--------------|---------------|---------------|-----------------------|");

                    }

                    info.add("|         | " + productId + "\t | " + ProductBatch.getBatchById(productId).getProductType() + "   \t| " +
                                                               ProductBatch.getBatchById(productId).getAmount() + " units\t| $" +
                                                               ProductBatch.getBatchById(productId).getPrice() + " each\t| Exp: " + 
                                                               ProductBatch.getBatchById(productId).getExpiry() + "\t|" );
                }
                
                if(empty){
                    info.add("|         |-----------------------------------------------------------------------------|");
                    info.add("|         | Empty \t\t\t\t\t\t\t\t\t|");
                }
            }
        
        }

        ArrayList<Integer> storageDepots = new ArrayList<Integer>();
        storageDepots.addAll(Storage.getStorageFromStore(StorageType.ORDERSDEPOT));
        storageDepots.addAll(Storage.getStorageFromStore(StorageType.RETURNSDEPOT));
        for(int storageId : storageDepots){
            info.add("|---------|-----------------------------------------------------------------------------|");
            for(int shelfId : Shelf.getShelvesFromStorage(storageId)){
                info.add("| Storage | " + Storage.getStorageById(storageId).getStorageType().toString() +": "+
                                          Shelf.getShelfById(shelfId).getCurrentAmount() + " units\t\t\t\t\t\t\t|");
                boolean firstProd=true;
                boolean empty=true;
                for(int productId : Shelf.getProductsFromShelf(shelfId)){
                    if(firstProd){
                        empty=false;
                        info.add("|         |-----------------------------------------------------------------------------|");
                        firstProd=false;
                    }else{
                        info.add("|         |------|--------------|---------------|---------------------------------------|");

                    }
                    info.add("|         | " + productId + "\t | " + ProductBatch.getBatchById(productId).getProductType() + "   \t| " +
                                                               ProductBatch.getBatchById(productId).getAmount() + " units\t\t\t\t\t\t|" );
                }
                if(empty){
                    info.add("|         |-----------------------------------------------------------------------------|");
                    info.add("|         | Empty \t\t\t\t\t\t\t\t\t|");
                }
            }
           
            
        }
        info.add("|---------|-----------------------------------------------------------------------------|");
        info.add("\n");
        return info;
    }
	
	public static ProductBatch getBatchById(int id) {
		ProductBatch batch = null;
		Connection con = PosSystem.getConnection();
		try {
			ResultSet tables = con.prepareStatement("SELECT * FROM seng2020.productbatch WHERE batchId = " + id).executeQuery();
			tables.next();
			batch = new ProductBatch(tables.getInt("batchId"),
					                 ProductType.getProductTypeById(tables.getInt("productType")).getType().toString(),
					                 tables.getDate("expiry"),
					                 tables.getDouble("price"),
					                 tables.getInt("amount"));
		} catch (SQLException e) {
			return null;
		}

		return batch;
	}
	
	/**
	 * @return batches that have not yet been sold, not in returns depot and not in orders depot
	 */
	   public static ArrayList<String> getAllAvailableBatchesOfType(ProductType type) {
	        ArrayList<String> batches = new ArrayList<String>();
	        Connection con = PosSystem.getConnection();
	        
	        try {
	            String query = "SELECT batchId FROM seng2020.productbatch WHERE productType = " + type.getTypeId();
	            
	            PreparedStatement stmt = con.prepareStatement(query);

	            ResultSet tables = stmt.executeQuery();
	            while (tables.next()) {
	                ProductBatch batch = getBatchById(tables.getInt(1));
	                
	                String storageType = null;
	                
	                ArrayList<Integer> storages = Storage.getStorageFromStore(StorageType.BACKROOM);
	                storages.addAll(Storage.getStorageFromStore(StorageType.FLOOR));
	                storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
	                for (Integer id: storages) {
	                    if (Storage.isInStorage(batch.getBatchId(), id)) {
	                        storageType = Storage.getStorageById(id).getStorageType().toString();
	                        break;
	                    }
	                }
	                
	                
	                if (!Sale.batchHasBeenSold(batch.getBatchId()) && storageType != null) {
	                    batches.add(batch.batchId + "  " + batch.expiry + "  " + batch.price);
	                }
	            }
	            
	            if (batches.size() == 0) {
	                return null;
	            }
	        } catch (SQLException e) {
	            Database.printStackTrace(e);
	            return null;
	        }
	        
	        return batches;
	    }
}
