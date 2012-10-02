package controller;

import java.util.ArrayList;

import exception.CancelException;

import view.CommandLine;

public class CustomerOptions {
	public static void performCustomerOptions(boolean isMember) {	
		ArrayList<String> questions = new ArrayList<String>();
		
		if( isMember ){
			questions.add("Check loyalty points balance");
			questions.add("Change details");
			questions.add("Close membership");
			questions.add("Log out");
		} else {
			questions.add("Become a member");
		}
	
		questions.add("Check product catalogue");
		
		try {
			int option = CommandLine.getUserOption(questions);
			
		} catch (CancelException e) {
			
		}
	}
}
