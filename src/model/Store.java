package model;

import java.util.ArrayList;

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
		
		floorspace = new Storage();
		backroom = new Storage();
		warehouses = new ArrayList<Storage>();
	}
	
	// TODO - fix up the dodgy id creation
	
	void addEmployee(EmployeeType type, String firstName, String lastName) {
		employees.add(EmployeeFactory.createEmployee(employees.size(), type, firstName, lastName));
	}
	
	//the only thing is that im getting the user to pass in a number
	//cause strings are dodgy
	//and i put a constructor in the enum
	
	void addRegister() {
		registers.add(new Register(registers.size()));
	}
	
	void addWarehouse() {
		warehouses.add(new Storage());
	}
	
	void removeEmployee() {
		
	}
	
	void removeRegister() {
		
	}
	
	void removeStorage() {
		
	}
	
	public int getNextEmployeeId() {
		return employees.size();
	}
}
