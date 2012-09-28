package controller;

import java.util.Scanner;

import view.Outputter;
import view.TextOutputter;
import view.EmployeeReader;
import model.Employee;
import model.EmployeeFactory;
import model.EmployeeType;
import model.Store;

public class Main {
	private static EmployeeReader reader;
	
	public static void main(String[] args) {
		initialize();
		Outputter outputter = new TextOutputter();
		outputter.printMainMenu();
		/*
		 * while (the system is running) {
		 *     user can login
		 *     
		 *     if customer:
		 *         - can buy products
		 *         - query products
		 *         - can become a member
		 *         
		 *     ---> if member:
		 *             - can check their loyalty points
		 *             - can gain and use loyalty points during transactions
		 *     
		 *     if staff:
		 *         - add products to shelf 
		 *         - Move products between storage locations
		 *         
		 *     ---> if manager:
		 *              - change price of batches on the spot
		 *              - order products
		 *         
		 *     -------> if admin:
		 *                  - can add users
		 *                  - change classes of users
		 *                  - 
		 */
	}
	public static void initialize() {
		Store store = new Store(1);
		
		reader = new EmployeeReader();

		reader.fetchEmployeeInformation();
		createEmployee(store, reader.getFirstName(), reader.getLastName(), reader.getEmployeeType());
	}
	
	public static Employee createEmployee(Store store, String fName, String lName, EmployeeType type) {
		
		int id = store.getNextEmployeeId();
		
		Employee newEmployee = EmployeeFactory.createEmployee(id, type, fName, lName);

		return newEmployee;
		
		
	}
	
}
