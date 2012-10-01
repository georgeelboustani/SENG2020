package controller;

import java.util.ArrayList;

import view.CommandLine;

public class CustomerOptions {
	public static void performCustomerOptions(boolean isMember) {	
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
}
