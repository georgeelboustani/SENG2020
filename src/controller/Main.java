package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

import exception.CancelException;
import exception.InvalidIdException;

import model.*;
import view.*;


public class Main {
	
	private static int storeId = 1;
	public static Employee currentEmployee = null;
	public static Member currentMember = null;
	private static boolean activeUser = false;
	
	public static void main(String[] args) throws SQLException {
		try {
			PosSystem.initialise(new Database("user","pass"), storeId);
		} catch (InvalidIdException e) {	
			System.err.println("Pos system failed to run. Invalid store id configuration.");
		}
		
		while (PosSystem.isInitialised()) {
			if (!activeUser) {
				activeUser = requestLogin();
			}
			
			try {
				if (activeUser) {
					if (currentMember != null) {
						CustomerOptions.performCustomerOptions(true);					          				
					} else {
						EmployeeOptions.performEmployeeOptions(currentEmployee.getEmployeeType());
					}
				} else {
					CustomerOptions.performCustomerOptions(false);
				}
			} catch (CancelException e) {
				
			}
			
			PosSystem.refreshConnection();
		}
	}
	
	/**
	 * Requests a user to login. If logged in, the user will be stored
	 * in currentEmployee or currentMember
	 * 
	 * @return true if a member or admin logs in
	 */
	public static boolean requestLogin() {
		boolean logIn = CommandLine.getYesOrNo("Would you like to log into the system?");
		boolean logInSuccessful = false;
		
		try {
			if (logIn) {
				ArrayList<String> questions = new ArrayList<String>();
				questions.add("Employee");
				questions.add("Member");
				int option = CommandLine.getUserOption(questions);
				
				if (option == 1) {
					logInSuccessful = loginEmployee();
				} else if (option == 2) {
					logInSuccessful = loginMember();
				}
			}
		} catch (CancelException e) {
			logInSuccessful = requestLogin();
		}
		
		return logInSuccessful;
	}

	private static boolean loginMember() throws CancelException {
		boolean logInSuccessful = false;
		int id = CommandLine.getAnswerAsInt("Please enter your member id:");
		
		Member mem = Member.getMemberById(id);
		while (mem == null || !mem.getActiveStatus()) {
			if (mem == null) {
				System.out.println("A member with id " + id + " does not exist, please type a valid id");
			} else {
				System.out.println("The member account with id " + id + " has been disabled, enter an active account id or 'cancel'");
			}
			id = CommandLine.getAnswerAsInt("Please enter your member id:");
			mem = Member.getMemberById(id);
		}
		
		if (mem != null) {
			String password = CommandLine.getAnswerAsString("Please type in your password:");
			while (!mem.getPassword().equals(password)) {
				System.out.println("Invalid password, you need to type in a valid password or cancel");
				password = CommandLine.getAnswerAsString("Password:");
			}
			currentMember = mem;
			logInSuccessful = true;
		}
		return logInSuccessful;
	}

	private static boolean loginEmployee() throws CancelException {
		boolean logInSuccessful = false;
		
		int id = CommandLine.getAnswerAsInt("Please enter your employee id:");
		Employee emp = Employee.getEmployeeById(id);
		
		while (emp == null || !emp.getActiveStatus()) {
			if (emp == null) {
				System.out.println("An employee with id " + id + " does not exist, please type a valid id");
			} else {
				System.out.println("The employee account with id " + id + " has been disabled, enter an active account id or 'cancel'");
			}
			id = CommandLine.getAnswerAsInt("Id:");
			emp = Employee.getEmployeeById(id);
		}
		
		if (emp != null) {
			String password = CommandLine.getAnswerAsString("Please type in your password:");
			while (!emp.getPassword().equals(password)) {
				System.out.println("Invalid password, you need to type in a valid password or cancel");
				password = CommandLine.getAnswerAsString("Password:");
			}
			currentEmployee = emp;
			logInSuccessful = true;
		}
		return logInSuccessful;
	}	

	public static boolean requestLogout() {
		// Ask user if they would like to logout.
		boolean logOut = CommandLine.getYesOrNo("Are you sure you would like to log out of the system?");
		if (logOut) {
			currentEmployee = null;
			currentMember = null;
			activeUser = false;
			
			return true;
		}
		
		return false;
	}
	
	public static boolean hasActiveUser() {
		return activeUser;
	}
}
