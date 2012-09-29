package controller;

import java.util.ArrayList;

import model.*;
import view.*;


public class Main {
	
	private static Employee currentEmployee = null;
	private static Member currentMember = null;
	private static boolean activeUser = false;
	
	public static void main(String[] args) {
		
		// TODO - change true to something more functional maybe while(systemRunning)
		while (true) {
			if (!activeUser) {
				activeUser = requestLogin();
			}
			
			if (activeUser) {
				if (currentMember != null) {
					performCustomerOptions(true);					          				
				} else {
					 /* Admins will first be given admin options, and then manager options 
					 * and then staff options. They need to skip through all of them. */
					performEmployeeOptions(currentEmployee.getEmployeeType());
				}
								
				requestLogout();
			} else {
				performCustomerOptions(false);
			}
		}
	}
	
	private static void performEmployeeOptions(EmployeeType type) {
		/*   TODO - finish this off for staff
		 *   - add products to shelf 
		 *   - Move products between storage locations
		 *   - can sell and return products for customer
		 *   ----> if customer is member, can gain and use loyalty points during transactions
		 */
		/*   TODO - finish this off for manager
		 *   - change price of batches on the spot
		 *   - order products
		 */
		/*	 TODO - finish this off for admin
		 *   - can add users
		 *   - change classes of users
		 */
		ArrayList<String> questions = new ArrayList<String>();
		
		switch(type){
			case ADMIN:
				/*
				questions.add("Add user");
				questions.add("Remove user");
				questions.add("Promote employee");
				questions.add("Demote employee");
				*/
				questions.add("Manage users");
				questions.add("Manage products");
			case MANAGER:
				/*
				questions.add("Change product price");
				questions.add("Order products");
				questions.add("Add product to system");
				*/
				questions.add("Manage products");
			case STAFF:
		}
		/*
		questions.add("Sell product(Cash)");
		questions.add("Sell product(Card)");
		*/
		questions.add("Sell product");
		questions.add("Return product");
		questions.add("Change details");
		questions.add("Log out");
		
		int option = CommandLine.getUserOption(questions);
		
	}
	
	private static void performCustomerOptions(boolean isMember) {	
		ArrayList<String> questions = new ArrayList<String>();
		
		if( isMember ){
			questions.add("Check loyalty points balance");
			questions.add("Change details");
			questions.add("Close membership");
		} else {
			questions.add("Become a member");
		}
	
		questions.add("Check product catalogue");
		questions.add("Log out");
		
		int option = CommandLine.getUserOption(questions);
		
	}
	
	/**
	 * Requests a user to login. If logged in, the user will be stored
	 * in currentEmployee or currentMember
	 * 
	 * @return true if a member or admin logs in
	 */
	private static boolean requestLogin() {
		boolean logIn = CommandLine.getYesOrNo("Would you like to log into the system?");
		boolean logInSuccessful = false;
		
		if (logIn) {
			ArrayList<String> questions = new ArrayList<String>();
			questions.add("Employee");
			questions.add("Member");
			questions.add("Cancel");
			int option = CommandLine.getUserOption(questions);
			
			if (option == 1) {
				// TODO - actually login
				currentEmployee = null;
				logInSuccessful = true;
			} else if (option == 2) {
				// TODO - actually login
				currentMember = null;
				logInSuccessful = true;
			} else {
				requestLogin();
			}
		}
		
		return logInSuccessful;
	}	

	private static void requestLogout() {
		// Ask user if they would like to logout.
		boolean logOut = CommandLine.getYesOrNo("Would you like to log out of the system?");
		if (logOut) {
			currentEmployee = null;
			currentMember = null;
			activeUser = false;
		}
	}
}
