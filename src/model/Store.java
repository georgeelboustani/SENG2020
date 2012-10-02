package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.mysql.jdbc.Connection;

import database.Database;

public class Store {
	
	private int storeId;
	
	// 
	// TODO - FUCKING IMPORTANT!! Can get rid of these lists and add methods to the respective classes to
	// grab them by their id's
	private ArrayList<Employee> employees;
	private ArrayList<Register> registers;
	
	private Storage floorspace;
	private Storage backroom;
	private Storage warehouse;
	private ArrayList<Sale> sales;
	
	public Store(int id) {
		this.storeId = id;
		
		employees = new ArrayList<Employee>();
		registers = new ArrayList<Register>();
		sales = new ArrayList<Sale>();
		
		floorspace = new Storage(0,StorageType.FLOOR);
		backroom = new Storage(1,StorageType.BACKROOM);
		warehouse = new Storage(2,StorageType.WAREHOUSE);
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		
		String query = "INSERT into " + PosSystem.getDatabase().getDbName() + ".store (`id`) " +
				"VALUES (?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		
		PosSystem.getDatabase().executeQuery(stmt);
		con.close();
		
		persistStorageMapping(floorspace);
		persistStorageMapping(backroom);
		persistStorageMapping(warehouse);
	}
	
	public void addEmployee(String firstName, String lastName, EmployeeType type,  String password) throws SQLException {
		Employee emp = new Employee(PosSystem.generateNextId(TableName.EMPLOYEE),firstName,lastName,type,password);
		emp.persist();		
		persistEmployeeMapping(emp);
	}
	
	public void addMember(String firstName, String lastName, String password, Date signup) throws SQLException {
		Member mem = new Member(PosSystem.generateNextId(TableName.MEMBER), signup,firstName,lastName,password);
		mem.persist();		
	}
	
	public void addRegister() throws SQLException {
		Register reg = new Register(PosSystem.generateNextId(TableName.REGISTER));
		registers.add(reg);
		reg.persist(PosSystem.getDatabase());
		
		persistRegisterMapping(reg);
	}
	
	public void addSale(Date date, Trolley products) throws SQLException {
		Sale newSale = new Sale(PosSystem.generateNextId(TableName.SALE), date, products);
		newSale.persist(PosSystem.getDatabase());
		
		persistSaleMapping(newSale);
	}
	
	public void addWarehouse() throws SQLException {
		Storage w = new Storage(PosSystem.generateNextId(TableName.STORAGE),StorageType.WAREHOUSE);
		w.persist(PosSystem.getDatabase());
		
		persistStorageMapping(w);
	} 
	
	private void persistStorageMapping(Storage s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storestorage (`storeId`,`storageId`) " +
				"VALUES (?,?)";
		stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getId());
		
		PosSystem.getDatabase().executeQuery(stmt);
		con.close();
	}
	
	private void persistSaleMapping(Sale s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storesale (`storeId`,`saleId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getSaleId());
		
		PosSystem.getDatabase().executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	private void persistRegisterMapping(Register reg) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storeregister (`storeId`,`registerId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, reg.getId());
		
		PosSystem.getDatabase().executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	private void persistEmployeeMapping(Employee emp) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		String query = "REPLACE into " + PosSystem.getDatabase().getDbName() + ".storeemployee (`storeId`,`employeeId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, emp.getEmployeeId());
		
		PosSystem.getDatabase().executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	
	public void addWarehouseSpace(int warehouseId, int shelves, int shelfCapacity) {
		// TODO - run update query and fix this shit up.
		// check if warehouse exists with given id, then creat object, update attributes
		// and update database record
	}
	
	public void addFloorSpace(int shelves, int shelfCapacity) {
		floorspace.addShelf(shelfCapacity,shelves);
	}
	
	public void removeEmployee(int id) {
		for( Employee e: employees){
			if(e.getEmployeeId()==id){
				employees.remove(e);
			}
		}
	}
	
	public void removeRegister(int id) {
		for( Register r: registers){
			if(r.getId()==id){
				registers.remove(r);
			}
		}
	}
	
	public int getStoreId() {
		return this.storeId;
	}

	public static Store getStoreById(int storeId) {
		Store store = null;
		
		try {
			ResultSet tables = PosSystem.getDatabase().getConnection().prepareStatement("SELECT * FROM seng2020.store WHERE id = " + storeId).executeQuery();
			tables.next();
			store = new Store(tables.getInt("id"));
		} catch (SQLException e) {
			return null;
		}
		
		return store;
	}
}
