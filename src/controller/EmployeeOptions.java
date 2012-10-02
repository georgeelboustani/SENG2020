package controller;

import java.util.ArrayList;

import exception.CancelException;
import exception.LogoutException;

import model.EmployeeType;
import model.PosSystem;
import view.CommandLine;

public class EmployeeOptions {
	public static void performEmployeeOptions(EmployeeType type) throws CancelException {
		ArrayList<String> questions = new ArrayList<String>();
		
		switch(type){
			case ADMIN:
				questions.add("Manage users");
				questions.add("Manage products");
				break;
			case MANAGER:
				questions.add("Manage products");
				break;
			case STAFF:
				break;
		}
		
		questions.add("Sell product");
		questions.add("Return product");
		questions.add("Change details");
		questions.add("Log out");
		
		int option = CommandLine.getUserOption(questions);
		
		try {
			employeeQuestionHandlerLevelOne(questions,option);
		} catch (CancelException e) {
			
		} catch (LogoutException e) {
			
		}
	}

	public static void employeeQuestionHandlerLevelOne(ArrayList<String> questions, int option) throws LogoutException, CancelException {
		ArrayList<String> newQuestions = new ArrayList<String>();
		String question = questions.get(option - 1);
		
		switch (question) {
			case "Manage users":
				newQuestions.add("Add user");
				newQuestions.add("Remove user");
				newQuestions.add("Promote employee");
				newQuestions.add("Demote employee");
				break;
			case "Manage products":
				newQuestions.add("Change product price");
				newQuestions.add("Order products");
				newQuestions.add("Add product to system");
				break;
			case "Sell product":
				newQuestions.add("Sell product(Cash)");
				newQuestions.add("Sell product(Card)");
				break;
			case "Return product":
				newQuestions.add("Return product");
				break;
			case "Change details":
				newQuestions.add("Change First Name");
				newQuestions.add("Change Last Name");
				newQuestions.add("Change Password");
				break;
			case "Log out":
				Main.requestLogout();
				throw new LogoutException();
		}
		
		int newOption = CommandLine.getUserOption(newQuestions);
		
		try {
			employeeQuestionHandlerLevelTwo(newQuestions,newOption);
		} catch (CancelException e){
			employeeQuestionHandlerLevelOne(questions,option);
		}
	}

	private static void employeeQuestionHandlerLevelTwo(ArrayList<String> questions, int option) throws CancelException {
		String question = questions.get(option - 1);
		
		switch (question) {
			case "Add user":
				int id = PosSystem.getStoreId();
				String fname = CommandLine.getAnswerAsString("First Name:");
				String lname = CommandLine.getAnswerAsString("Last Name:");
				EmployeeType type = EmployeeType.valueOf(CommandLine.getAnswerAsString("Employee Type:", EmployeeType.valuesToString()));
				String password = CommandLine.getAnswerAsString("Password:");
				
				
				break;
			case "Remove user":
				
				break;
			case "Promote employee":
				
				break;
			case "Demote employee":
				
				break;
			case "Change product price":
				
				break;
			case "Order products":
				
				break;
			case "Add product to system":
				
				break;
			case "Sell product(Cash)":
				
				break;
			case "Sell product(Card)":
				
				break;
			case "Return product":
				
				break;
			case "Change First Name":
				
				break;	
			case "Change Last Name":
				
				break;
			case "Change Password":
				
				break;
			case "Cancel":
				throw new CancelException();
		}
	}
}
