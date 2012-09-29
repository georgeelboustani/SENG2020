package controller;

import java.util.ArrayList;

import view.CommandLine;
import model.Employee;
import model.Member;

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
					performNormalUserOptions();
					performMemberOptions();					          				
				} else {
					 /* Admins will first be given admin options, and then manager options 
					 * and then staff options. They need to skip through all of them. */
					switch (currentEmployee.getEmployeeType()) {
			            case ADMIN:  performAdminOptions();
			            case MANAGER:  performManagerOptions();
			            case STAFF:  performStaffOptions();
			            	break;
					}
				}
								
				requestLogout();
			} else {
				performNormalUserOptions();
			}
		}
	}
	
	private static void requestLogout() {
		// Ask user if they would like to logout.
		// If so: currentEmployee = null currentEmployee = null activeUser = false
	}
	
	private static void performStaffOptions() {
		/*   TODO - finish this off
		 *   - add products to shelf 
		 *   - Move products between storage locations
		 *   - can sell and return products for customer
		 *   ----> if customer is member, can gain and use loyalty points during transactions
		 */
	}
	
	private static void performManagerOptions() {
		/*   TODO - finish this off
		 *   - change price of batches on the spot
		 *   - order products
		 */
	}
	
	private static void performAdminOptions() {
		/*	 TODO - finish this off
		 *   - can add users
		 *   - change classes of users
		 */
	}	
	private static void performNormalUserOptions() {
		/*   TODO - finish this off
		 *   - can buy products
		 *   - query products
		 *   - can become a member
		 */
	}
	private static void performMemberOptions() {
		/*   TODO - finish this off
		 *   - can check their loyalty points
		 *   - 
		 */
		ArrayList<String> questions = new ArrayList<String>();
		questions.add("Check loyalty points balance");
		int option = CommandLine.getUserOption(questions);
		
		
	}
	
	/**
	 * Requests a user to login. If logged in, the user will be stored
	 * in currentEmployee or currentMember
	 * 
	 * @return true if a member or admin logs in
	 */
	private static boolean requestLogin() {
		// TODO - finish this
		
		// Request login
		// See if someone wants to log in as a member or employee
		// currentMember or currentEmployee = new Whatever();
		// if (someone logged in) {
		// 	   activeUser = true;
		// }
		
		return false;
	}	
}
