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
					CustomerOptions.performCustomerOptions(true);					          				
				} else {
					EmployeeOptions.performEmployeeOptions(currentEmployee.getEmployeeType());
				}
			} else {
				CustomerOptions.performCustomerOptions(false);
			}
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

	public static void requestLogout() {
		// Ask user if they would like to logout.
		boolean logOut = CommandLine.getYesOrNo("Are you sure you would like to log out of the system?");
		if (logOut) {
			currentEmployee = null;
			currentMember = null;
			activeUser = false;
		}
	}
}
