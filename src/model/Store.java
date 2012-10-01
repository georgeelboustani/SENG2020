package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.mysql.jdbc.Connection;

import database.Database;

public class Store {
	
	private int storeId;
	private ArrayList<Employee> employees;
	private ArrayList<Register> registers;
	
	private Storage floorspace;
	private Storage backroom;
	private ArrayList<Storage> warehouses;
	private ArrayList<Sale> sales;
	
	public Store(int id) {
		this.storeId = id;
		
		employees = new ArrayList<Employee>();
		registers = new ArrayList<Register>();
		sales = new ArrayList<Sale>();
		
		floorspace = new Storage(0,StorageType.FLOOR);
		backroom = new Storage(1,StorageType.BACKROOM);
		warehouses = new ArrayList<Storage>();
		warehouses.add(new Storage(2,StorageType.WAREHOUSE));
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + ".store (`id`) " +
				"VALUES (?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		
		db.executeQuery(stmt);
		con.close();
		
		persistStorageMapping(db,floorspace);
		persistStorageMapping(db,backroom);
		persistWarehouseMapping(db);
	}
	
	public void addEmployee(Database db, String firstName, String lastName, EmployeeType type,  String password) throws SQLException {
		// TODO - fix up shitty id creation
		Employee emp = new Employee(employees.size(),firstName,lastName,type,password);
		emp.persist(db);
		employees.add(emp);
		
		persistEmployeeMapping(db,emp);
	}
	
	public void addRegister(Database db) throws SQLException {
		Register reg = new Register(registers.size());
		registers.add(reg);
		reg.persist(db);
		
		persistRegisterMapping(db,reg);
	}
	
	public void addSale(Database db, Sale s) throws SQLException {
		sales.add(s);
		s.persist(db);
		
		persistSaleMapping(db,s);
	}
	
	// TODO - fix up the dodgy id creation !! IMPORTANT !!
	public void addWarehouse(Database db) throws SQLException {
		Storage w = new Storage(warehouses.size(),StorageType.WAREHOUSE);
		warehouses.add(w);
		w.persist(db);
		
		persistStorageMapping(db,w);
	} 
	
	private void persistWarehouseMapping(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "REPLACE into " + db.getDatabase() + ".storestorage (`storeId`,`storageId`) " +
				"VALUES (?,?)";
		stmt = con.prepareStatement(query);
		Iterator<Storage> itr = warehouses.iterator();
		while (itr.hasNext()) {
			stmt.setInt(2, itr.next().getId());
			db.executeQuery(stmt);
		}
		
		con.close();
	}
	
	private void persistStorageMapping(Database db, Storage s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "REPLACE into " + db.getDatabase() + ".storestorage (`storeId`,`storageId`) " +
				"VALUES (?,?)";
		stmt = con.prepareStatement(query);
		stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getId());
		
		db.executeQuery(stmt);
		con.close();
	}
	
	private void persistSaleMapping(Database db, Sale s) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "REPLACE into " + db.getDatabase() + ".storesale (`storeId`,`saleId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, s.getSaleId());
		
		db.executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	private void persistRegisterMapping(Database db, Register reg) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "REPLACE into " + db.getDatabase() + ".storeregister (`storeId`,`registerId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, reg.getId());
		
		db.executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	private void persistEmployeeMapping(Database db, Employee emp) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		String query = "REPLACE into " + db.getDatabase() + ".storeemployee (`storeId`,`employeeId`) " +
				"VALUES (?,?)";
    	stmt = con.prepareStatement(query);
    	stmt.setInt(1, this.storeId);
		stmt.setInt(2, emp.getEmployeeId());
		
		db.executeQuery(stmt);
		stmt.close();
		con.close();
	}
	
	public void addWarehouseSpace(int warehouseId, int shelves, int shelfCapacity) {
		for (int i = 0; i < warehouses.size(); i++) {
			Storage w = warehouses.get(i);
			if (w.getId() == warehouseId) {
				w.addShelf(shelfCapacity,shelves);
			}
		}
	}
	
	public void addFloorSpace(int shelves, int shelfCapacity) {
		floorspace.addShelf(shelfCapacity,shelves);
	}
	
	public void removeWarehouse(int id) {
		for( Storage s: warehouses){
			if(s.getId() == id){
				warehouses.remove(s);
			}
		}
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
	
	
	public int getNextEmployeeId() {
		return employees.size();
	}

	
}
