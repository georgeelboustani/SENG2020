package model;

import java.util.ArrayList;
import java.util.Random;

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
		
		floorspace = new Storage(0);
		backroom = new Storage(0);
		warehouses = new ArrayList<Storage>();
	}
	

	
	public void addEmployee(EmployeeType type, String firstName, String lastName) {
		employees.add(new Employee(employees.size(),firstName,lastName,type));
		// TODO - need to save in the database
	}
	
	public void addRegister() {
		registers.add(new Register(registers.size()));
	}
	
	//TODO: add method add shelves, and not replace obj
	
	public void addWarehouseSpace(int warehouseId, int shelves, int shelfCapacity) {
		for (int i = 0; i < warehouses.size(); i++) {
			Storage w = warehouses.get(i);
			if (w.getId() == warehouseId) {
				w.addShelf(shelfCapacity,shelves);
			}
		}
	}
	
	// TODO - fix up the dodgy id creation !! IMPORTANT !!
	public void addWarehouse() {
		warehouses.add(new Storage(warehouses.size()));
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
