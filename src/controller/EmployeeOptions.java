package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

import exception.CancelException;
import exception.InvalidIdException;
import exception.LogoutException;

import model.Employee;
import model.EmployeeType;
import model.PosSystem;
import model.Store;
import model.TableName;
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
				newQuestions.add("Add employee");
				newQuestions.add("Disable employee");
				newQuestions.add("Enable employee");
				newQuestions.add("Add member");
				newQuestions.add("Disable member");
				newQuestions.add("Enable member");
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
		
		Store s = Store.getStoreById(PosSystem.getStoreId());
		
		int empId, memId;
		
		try {
			switch (question) {
				case "Add employee":
					String fname = CommandLine.getAnswerAsString("First Name:");
					String lname = CommandLine.getAnswerAsString("Last Name:");
					EmployeeType type = EmployeeType.valueOf(CommandLine.getAnswerAsString("Choose an employee type:", EmployeeType.valuesToString()));
					String password = CommandLine.getAnswerAsString("Password:");
					
					s.addEmployee(fname, lname, type, password);
					break;
				case "Disable employee":
					empId = CommandLine.getAnswerAsInt("Employee Id:");
					s.setEmployeeStatus(empId, false);
					
					break;
				case "Enable employee":
					empId = CommandLine.getAnswerAsInt("Employee Id:");
					s.setEmployeeStatus(empId, true);
				    
					break;
				case "Add member":
					String memfname = CommandLine.getAnswerAsString("First Name:");
					String memlname = CommandLine.getAnswerAsString("Last Name:");
					String mempassword = CommandLine.getAnswerAsString("Password:");
					
					s.addMember(memfname, memlname, mempassword, Database.getCurrentDate());
					break;
				case "Disable member":
					memId = CommandLine.getAnswerAsInt("Member Id:");
					s.setMemberStatus(memId, false);
					
					break;
				case "Enable member":
					memId = CommandLine.getAnswerAsInt("Member Id:");
					s.setMemberStatus(memId, true);
					
					break;
				case "Promote employee":
					System.err.println("Unimplemented function");
					break;
				case "Demote employee":
					System.err.println("Unimplemented function");
					break;
				case "Change product price":
					System.err.println("Unimplemented function");
					break;
				case "Order products":
					System.err.println("Unimplemented function");
					break;
				case "Add product to system":
					System.err.println("Unimplemented function");
					break;
				case "Sell product(Cash)":
					System.err.println("Unimplemented function");
					break;
				case "Sell product(Card)":
					System.err.println("Unimplemented function");
					break;
				case "Return product":
					System.err.println("Unimplemented function");
					break;
				case "Change First Name":
					System.err.println("Unimplemented function");
					break;	
				case "Change Last Name":
					System.err.println("Unimplemented function");
					break;
				case "Change Password":
					System.err.println("Unimplemented function");
					break;
				case "Cancel":
					throw new CancelException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			employeeQuestionHandlerLevelTwo(questions,option);
		} catch (InvalidIdException e) {
			e.printStackTrace();
			employeeQuestionHandlerLevelTwo(questions,option);
		}
	}
}
