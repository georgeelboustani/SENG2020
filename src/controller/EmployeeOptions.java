package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

import exception.CancelException;
import exception.InvalidIdException;
import exception.LogoutException;

import model.Employee;
import model.EmployeeType;
import model.Member;
import model.PosSystem;
import model.ProductBatch;
import model.ProductType;
import model.Sale;
import model.Shelf;
import model.Store;
import model.TableName;
import model.Trolley;
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
				newQuestions.add("Change Employee First Name");
				newQuestions.add("Change Employee Last Name");
				newQuestions.add("Change Employee Password");
				break;
			case "Manage products":
				newQuestions.add("Change product price");
				newQuestions.add("Order products");
				newQuestions.add("Receive Order");
				newQuestions.add("Add product to system");
				newQuestions.add("Transfer products");
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
		Employee emp;
		String fname;
		String lname;
		String password;
		EmployeeType empType;
		try {
			switch (question) {
				case "Change Employee First Name":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					fname = CommandLine.getAnswerAsString("Employee First Name: ");
					emp = Employee.getEmployeeById(empId);
					emp.setFirstName(fname);
					break;
				case "Change Employee Last Name":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					lname = CommandLine.getAnswerAsString("Employee Last Name: ");
					emp = Employee.getEmployeeById(empId);
					emp.setFirstName(lname);
					break;
				case "Change Employee Password":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					password = CommandLine.getAnswerAsString("Employee Password: ");
					emp = Employee.getEmployeeById(empId);
					emp.setFirstName(password);
					break;
				case "Add employee":
					fname = CommandLine.getAnswerAsString("First Name: ");
					lname = CommandLine.getAnswerAsString("Last Name: ");
					empType = EmployeeType.valueOf(CommandLine.getAnswerAsString("Choose an employee type: ", EmployeeType.valuesToString()));
					password = CommandLine.getAnswerAsString("Password: ");
					
					s.addEmployee(fname, lname, empType, password);
					break;
				case "Disable employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.setEmployeeStatus(empId, false);
					
					break;
				case "Enable employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.setEmployeeStatus(empId, true);
				    
					break;
				case "Add member":
					fname = CommandLine.getAnswerAsString("First Name: ");
					lname = CommandLine.getAnswerAsString("Last Name: ");
					password = CommandLine.getAnswerAsString("Password: ");
					
					s.addMember(fname, lname, password, Database.getCurrentDate());
					break;
				case "Disable member":
					memId = CommandLine.getAnswerAsInt("Member Id: ");
					s.setMemberStatus(memId, false);
					break;
				case "Enable member":
					memId = CommandLine.getAnswerAsInt("Member Id: ");
					s.setMemberStatus(memId, true);
					break;
				case "Promote employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.changeEmployeeType(empId,true);
					break;
				case "Demote employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.changeEmployeeType(empId,false);
					break;
				case "Change product price":
					//TODO - print product types and if
					int productTypeId = CommandLine.getAnswerAsInt("Product Type Id: ");
					int price = CommandLine.getAnswerAsInt("New Price: ");
					s.changeProductPrice(productTypeId,price);
					break;
					//TODO: HARD
				case "Order products":
					System.err.println("Unimplemented function");
					break;
					//TODO: MEDIUM
				case "Receive Order":
					System.err.println("Unimplemented function");
					break;
				case "Add product to system":
					String productName = CommandLine.getAnswerAsString("Product Name:");
					String productDescription = CommandLine.getAnswerAsString("Product Description:");
					
					ProductType.addProductType(productName, productDescription);
					break;
					//TODO: Transfer products
				case "Transfer products":
					break;
					//TODO: HARD
				case "Sell product(Cash)":
					handleSale();
					break;
					//TODO: NEED TO TEST!
				case "Sell product(Card)":
					handleSale();
					// TODO - ask for credit card info
					break;
					//TODO: HARD
				case "Return product":
					System.err.println("Unimplemented function");
					break;
				case "Change First Name":
					fname = CommandLine.getAnswerAsString("New First Name: ");
					Main.currentEmployee.setFirstName(fname);
					break;	
				case "Change Last Name":
					lname = CommandLine.getAnswerAsString("New Last Name: ");
					Main.currentEmployee.setLastName(lname);
					break;
				case "Change Password":
					password = CommandLine.getAnswerAsString("New Password: ");
					Main.currentEmployee.setPassword(password);
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
		} catch (CancelException e) {
			System.out.println("Cancelled out of level two");
			throw e;
		}
	}

	private static void handleSale() throws NumberFormatException, SQLException, CancelException {
		ArrayList<ProductBatch> oldBatches = new ArrayList<ProductBatch>();
		ArrayList<ProductBatch> newBatches = new ArrayList<ProductBatch>();
		try {
			Trolley trolley = new Trolley();
			
			boolean addProducts = true;
			while (addProducts) {
				int batchId = CommandLine.getAnswerAsInt("Batch Id: ");
				ProductBatch oldBatch = ProductBatch.getBatchById(batchId);
				
				while(oldBatch == null || !Shelf.isOnShelf(batchId)){
					batchId = CommandLine.getAnswerAsInt("Enter a valid Batch Id: ");
					oldBatch = ProductBatch.getBatchById(batchId);
				}
				
				if (oldBatch.getAmount() != 0) {
					String quantity = CommandLine.getAnswerAsString("Quantity [1," + oldBatch.getAmount() + "]: ");
					while (!DataValidator.validateInt(quantity, 1, oldBatch.getAmount())) {
						System.out.print("Quantity entered is not valid. Please enter a number between 0 and " + oldBatch.getAmount() + ": ");
						quantity = CommandLine.getAnswerAsString("");
					}
					
					oldBatch.setAmount(oldBatch.getAmount() - Integer.parseInt(quantity));
					ProductBatch newBatch = new ProductBatch(PosSystem.generateNextId(TableName.PRODUCTBATCH),
                             					oldBatch.getProductType(),oldBatch.getExpiry(),oldBatch.getPrice(),Integer.parseInt(quantity));
					
					boolean exists = trolley.addProductBatch(newBatch);
					if (!exists) {
						newBatch.persist();
					}
					
					oldBatches.add(oldBatch);
					newBatches.add(newBatch);
					addProducts = CommandLine.getYesOrNo("Would you like to add more products?");
				} else {
					System.out.println("Batch chosen does not exist on floor");
				}
			} 
	
			boolean isMember = CommandLine.getYesOrNo("Is a member?");
			if (isMember) {
				int memId = CommandLine.getAnswerAsInt("Member Id: ");
				Member mem = Member.getMemberById(memId);
				
				int loyaltyPoints = 0;
				for (ProductBatch batch: newBatches) {
					loyaltyPoints += batch.getAmount() * batch.getPrice();
				}
				mem.addLoyaltyPoints(loyaltyPoints);
			}
			Sale sale = new Sale(PosSystem.generateNextId(TableName.SALE),PosSystem.getDatabase().getCurrentDate(),trolley);
			sale.persist();
		} catch (Exception e) {
			// Undo the subtracting of batch amount from above
			for (int i = 0; i < oldBatches.size(); i++) {
				oldBatches.get(i).setAmount(oldBatches.get(i).getAmount() + newBatches.get(i).getAmount());
				newBatches.get(i).delete();
			}
			throw new CancelException();
		}
	}
}
