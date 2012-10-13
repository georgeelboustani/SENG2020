package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.mysql.jdbc.Connection;

import database.Database;
import exception.InvalidIdException;

public class Store {
	
	private int storeId;
	
	
	private Storage floorspace;
	private Storage backroom;
	private Storage warehouse;
	private Storage returnsdepot;
	  private Storage orderdepot;
	private ArrayList<Sale> sales;
	
	public Store(int id) {
		this.storeId = id;
		
		sales = new ArrayList<Sale>();
		
		floorspace = new Storage(0,StorageType.FLOOR);
		backroom = new Storage(1,StorageType.BACKROOM);
		warehouse = new Storage(2,StorageType.WAREHOUSE);
		returnsdepot = new Storage(3,StorageType.RETURNSDEPOT);
		orderdepot = new Storage(4,StorageType.ORDERSDEPOT);
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".store (`id`) " +
				"VALUES (?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		
		db.executeQuery(stmt);
		
		persistStorageMapping(floorspace);
		persistStorageMapping(backroom);
		persistStorageMapping(warehouse);
	    persistStorageMapping(returnsdepot);
	    persistStorageMapping(orderdepot);
	}
	
	public void addEmployee(String firstName, String lastName, EmployeeType type,  String password) throws SQLException {
		Employee emp = new Employee(PosSystem.generateNextId(TableName.EMPLOYEE),firstName,lastName,type,password);
		emp.persist();		
		persistEmployeeMapping(emp);
	}
	
	public Member addMember(String firstName, String lastName, String password, Date signup) throws SQLException {
		Member mem = new Member(PosSystem.generateNextId(TableName.MEMBER), signup,firstName,lastName,password);
		mem.persist();		
		return mem;
	}
	
	public void addRegister() throws SQLException {
		Register reg = new Register(PosSystem.generateNextId(TableName.REGISTER));
		reg.persist();
		
		persistRegisterMapping(reg);
	}
	
	public void setEmployeeStatus(int id, boolean active) throws InvalidIdException {
		Employee emp = Employee.getEmployeeById(id);
		
		if (emp == null) {
			throw new InvalidIdException();
		} else {
			emp.setActiveStatus(active);
		}
	}
	
	public void setMemberStatus(int id, boolean active) throws InvalidIdException {
		Member mem = Member.getMemberById(id);
		
		if (mem == null) {
			throw new InvalidIdException();
		} else {
			mem.setActiveStatus(active);
		}
	}
	
	public void addSale(Date date, Trolley products) throws SQLException {
		Sale newSale = new Sale(PosSystem.generateNextId(TableName.SALE), date, products);
		newSale.persist();
		
		persistSaleMapping(newSale);
	}
	
	public void addWarehouse() throws SQLException {
		Storage w = new Storage(PosSystem.generateNextId(TableName.STORAGE),StorageType.WAREHOUSE);
		w.persist();
		
		persistStorageMapping(w);
	} 
	
	private void persistStorageMapping(Storage s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storestorage (`storeId`,`storageId`) " +
				"VALUES (?,?)";
		stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getId());
		
		PosSystem.getDatabase().executeQuery(stmt);
	}
	
	private void persistSaleMapping(Sale s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storesale (`storeId`,`saleId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getSaleId());
		
		PosSystem.getDatabase().executeQuery(stmt);
	}
	
	private void persistRegisterMapping(Register reg) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storeregister (`storeId`,`registerId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, reg.getId());
		
		PosSystem.getDatabase().executeQuery(stmt);
	}
	
	private void persistEmployeeMapping(Employee emp) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storeemployee (`storeId`,`employeeId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, emp.getEmployeeId());
		
		PosSystem.getDatabase().executeQuery(stmt);
	}
	
	
	public void addWarehouseSpace(int warehouseId, int shelves, int shelfCapacity) {
		// TODO - run update query and fix this shit up.
		// check if warehouse exists with given id, then creat object, update attributes
		// and update database record
	}
	
	public void removeEmployee(int id) {
		// TODO - remove from database
	}
	
	public void removeRegister(int id) {
		// TODO - remove from database
	}
	
	public int getStoreId() {
		return this.storeId;
	}

	public static Store getStoreById(int storeId) {
		Store store = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.store WHERE id = " + storeId).executeQuery();
			tables.next();
			store = new Store(tables.getInt("id"));
		} catch (SQLException e) {
			return null;
		}
		
		return store;
	}

	public void changeEmployeeType(int empId, boolean promote) throws InvalidIdException {
		Employee emp = Employee.getEmployeeById(empId);
		
		if (emp == null) {
			throw new InvalidIdException();
		} else {
			if (promote) {
				emp.promote();
			} else {
				emp.demote();
			}
		}
	}
	
	/**
	 * Changes the price of all batches of this product type
	 * @param productTypeId
	 * @param price
	 */
	public void changeProductPrice(int productTypeId, int price) {
		try {	
			PreparedStatement stmt = null;
			Database db = PosSystem.getDatabase();
			Connection con = PosSystem.getConnection();
			
			String query = "UPDATE " + db.getDbName() + ".productbatch " +
					"SET price = ? " +
					"WHERE (productType = ?" +
					") AND (NOT EXISTS (select * from " + db.getDbName() + ".salebatches " +
							         "WHERE salebatches.batchId = productbatch.batchId))";
	    	stmt = con.prepareStatement(query);
	    	stmt.setInt(1, price);
			stmt.setInt(2, productTypeId);
			
			db.executeQuery(stmt);
		} catch (Exception e) {
			System.err.println("Failed to update product price");
		}
	}
}
