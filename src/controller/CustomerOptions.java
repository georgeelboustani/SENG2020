package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import database.Database;

import model.Employee;
import model.EmployeeType;
import model.Member;
import model.PosSystem;
import model.ProductBatch;
import model.ProductCategory;
import model.ProductType;
import model.Shelf;
import model.Storage;
import model.Store;

import exception.CancelException;
import exception.InvalidIdException;
import exception.LogoutException;

import view.CommandLine;

public class CustomerOptions {
	public static void performCustomerOptions(boolean isMember) throws CancelException {	
		ArrayList<String> questions = new ArrayList<String>();
		
		if(isMember){
			questions.add("Check loyalty points balance");
			questions.add("Change details");
			questions.add("Close membership");
			questions.add("Query Products");
			questions.add("Log out");
		} else {
			questions.add("Become a member");
			questions.add("Query Products");
		}
		
		int option = CommandLine.getUserOption(questions);
		
		try {
			customerQuestionHandlerLevelOne(questions,option);
		} catch (CancelException e){
			
		} catch (LogoutException e) {
			
		}
	}
	
	public static void customerQuestionHandlerLevelOne(ArrayList<String> questions, int option) throws LogoutException, CancelException {
		ArrayList<String> newQuestions = new ArrayList<String>();
		String question = questions.get(option - 1);
		
		Store s = Store.getStoreById(PosSystem.getStoreId());
		Member mem = null;
		if (Main.hasActiveUser()) {
			mem = Member.getMemberById(Main.currentMember.getId());
		}
		
		boolean goNextLevel = true;
		
		switch (question) {
			case "Check loyalty points balance":
				System.out.println(mem.getFirstName() + ", you currently have " + mem.getLoyaltyPoints() + " loyalty points in your account.");
				goNextLevel = false;
				break;
			case "Change details":
				newQuestions.add("Change First Name");
				newQuestions.add("Change Last Name");
				newQuestions.add("Change Password");
				break;
			case "Close membership":
				if (CommandLine.getYesOrNo("Are you sure you would like to close your account?")) {
					mem.setActiveStatus(false);
					Main.requestLogout();
					throw new LogoutException();
				}
				goNextLevel = false;
				break;
			case "Become a member":
				if (!CommandLine.getYesOrNo("Please confirm, have you previously created a member account (y,n):")) {
					String fname = CommandLine.getAnswerAsString("First Name:");
					String lname = CommandLine.getAnswerAsString("Last Name:");
					String password = CommandLine.getAnswerAsString("Password:");
					
					try {
						
						Member mem2 = s.addMember(fname, lname, password, Database.getCurrentDate());
						System.out.println("Your member account has been successfully created. Your member id is " + mem2.getId());
					} catch (SQLException e) {
						System.out.println("Error while creating member, please try again or type 'cancel'");
						customerQuestionHandlerLevelOne(questions,option);
					}
				}
				goNextLevel = false;
				break;
			case "Query Products":
				newQuestions.add("Search All");
				newQuestions.add("Search by Name");
				newQuestions.add("List by Category");
				break;
			case "Log out":
				Main.requestLogout();
				throw new LogoutException();
		}
		
		if (goNextLevel) {
			int newOption = CommandLine.getUserOption(newQuestions);
			
			try {
				customerQuestionHandlerLevelTwo(newQuestions,newOption);
			} catch (CancelException e){
				customerQuestionHandlerLevelOne(questions,option);
			}
		}
	}

	private static void customerQuestionHandlerLevelTwo(ArrayList<String> questions, int option) throws CancelException {
		String question = questions.get(option - 1);
		
		Store s = Store.getStoreById(PosSystem.getStoreId());
		Member mem = null;
		if (Main.hasActiveUser()) {
			mem = Member.getMemberById(Main.currentMember.getId());
		}
		
		try {
			switch (question) {
				case "Change First Name":
					String fname = CommandLine.getAnswerAsString("New First Name: ");
					mem.setFirstName(fname);
					break;
				case "Change Last Name":
					String lname = CommandLine.getAnswerAsString("New Last Name: ");
					mem.setLastName(lname);
					break;
				case "Change Password":
					String pass = CommandLine.getAnswerAsString("New password: ");
					mem.setPassword(pass);
					break;
				case "Search All":
					HashMap<String,Integer> products = Storage.getProductInfo();
					for (String key: products.keySet()){
						System.out.println(StringUtils.capitalize(key) + ": " + products.get(key));
					}
					break;
				case "Search by Name":
					String name = CommandLine.getAnswerAsString("Name: ");
					HashMap<String,Integer> allProducts = Storage.getProductInfo();
					for (String key: allProducts.keySet()){
						if(key.toLowerCase().contains(name.toLowerCase())){
							System.out.println(StringUtils.capitalize(key) + ": " + allProducts.get(key));
							System.out.println(ProductType.getProductTypeByName(key).getDescription());
						}
					}
					break;
				case "List by Category":
					String categoryName = CommandLine.getAnswerAsString("Enter a category name:").toLowerCase();
					
					HashMap<String,Integer> products1 = Storage.getProductInfo();
					for (String key: products1.keySet()){
						if (ProductType.getProductTypeByName(key).getCategoryName().toLowerCase().contains(categoryName)) {
							System.out.println("(" + ProductType.getProductTypeByName(key).getCategoryName() + ") " + StringUtils.capitalize(key) + ": " + products1.get(key));
						}
					}
					break;
			}
		} catch (CancelException e) {
			System.out.println("Cancelled out of level two");
			throw e;
		}
		
		throw new CancelException();
	}
}
