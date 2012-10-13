package controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

import exception.CancelException;
import exception.InvalidIdException;
import exception.LogoutException;

import model.Employee;
import model.EmployeeType;
import model.Member;
import model.Order;
import model.PosSystem;
import model.ProductBatch;
import model.ProductCategory;
import model.ProductType;
import model.Sale;
import model.Shelf;
import model.Storage;
import model.Store;
import model.Supplier;
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
				questions.add("Manage Storage");
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
		questions.add("Reporting");
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
				newQuestions.add("Add category to system");
				newQuestions.add("Transfer products");
				break;
			case "Manage Storage":
				newQuestions.add("Add supplier");
				newQuestions.add("Remove supplier");
				newQuestions.add("Add Warehouse");
				// TODO - add space to storage locations
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
			case "Reporting":
				newQuestions.add("Report Employees");
				newQuestions.add("Report Products");
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

	//TODO - ASK FOR REGID
	//TODO - add details to register table for staff login to register
	@SuppressWarnings({ "deprecation"})
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
					int orderId = PosSystem.generateNextId(TableName.ORDER);
					Date orderArrival = new java.sql.Date(0);
					
					System.out.println("The following 3 questions will gather the date of arrival");
					orderArrival.setDate(CommandLine.getAnswerAsInt("Day of arrival:"));
					orderArrival.setMonth(CommandLine.getAnswerAsInt("Month of arrival:"));
					orderArrival.setYear(CommandLine.getAnswerAsInt("year of arrival:"));
					
					Date receivedDate = null;
					int productTypeId2 = ProductType.getProductTypeByName(ProductType.getAllAvailableProductTypes().get(CommandLine.getUserOption(ProductType.getAllAvailableProductTypes()) - 1)).getTypeId();
					int quantity = CommandLine.getAnswerAsInt("Quantity ordered:");
					
					// TODO - validate supplier id
					int supplierId = CommandLine.getAnswerAsInt("Supplier Id:");
					
					Order newOrder = new Order(orderId,orderArrival,receivedDate,productTypeId2,quantity,supplierId);
					newOrder.persist();
					
					break;
					//TODO: MEDIUM
				case "Receive Order":
					System.err.println("Unimplemented function");
					break;
					//TODO: IMPORTANT increase stock counts on shelve when adding products
				case "Add product to system":
					String productName = CommandLine.getAnswerAsString("Product Name:");
					String productDescription = CommandLine.getAnswerAsString("Product Description:");
					
					// TODO - print a list of the valid categories
					String productCategory = CommandLine.getAnswerAsString("Product Category:");
					ProductCategory category = ProductCategory.getProductCategoryByName(productCategory);
					while (category == null) {
						productCategory = CommandLine.getAnswerAsString("Please enter a valid category name:");
						category = ProductCategory.getProductCategoryByName(productCategory);
					}
					
					ProductType.addProductType(productName, productDescription,category.getCategoryId());
					break;
				
				case "Add category to system":
					String categoryName = CommandLine.getAnswerAsString("Category Name:");
					
					ProductCategory category1 = ProductCategory.getProductCategoryByName(categoryName);
					while (category1 != null) {
						if (CommandLine.getYesOrNo("Category already exists, would you like to add another category?")) {
							categoryName = CommandLine.getAnswerAsString("Category Name:");
							category1 = ProductCategory.getProductCategoryByName(categoryName);
						} else {
							throw new CancelException();
						}
					}
					
					ProductCategory.addProductCategory(PosSystem.generateNextId(TableName.PRODUCTCATEGORY),categoryName);
					break;
					
				case "Transfer products":
					int fromShelfId = CommandLine.getAnswerAsInt("From shelf id:");
					while( Shelf.getShelfById(fromShelfId) == null){
						fromShelfId = CommandLine.getAnswerAsInt("Enter a from shelf id which exists:");
					}
					
					int toShelfId = CommandLine.getAnswerAsInt("To shelf id:");
					while( Shelf.getShelfById(toShelfId) == null){
						toShelfId = CommandLine.getAnswerAsInt("Enter a to shelf id which exists:");
					}
					
					int batchId = CommandLine.getAnswerAsInt("Product batch id:");
					while( ProductBatch.getBatchById(batchId) == null){
						batchId = CommandLine.getAnswerAsInt("Enter a product batch id which exists");
					}
					
					if (Shelf.isOnShelf(batchId, fromShelfId)) {
						String amount = CommandLine.getAnswerAsString("Quantity [1," + ProductBatch.getBatchById(batchId).getAmount() + "]: ");
						if(!DataValidator.validateInt(amount, 0, ProductBatch.getBatchById(batchId).getAmount())){
							amount = CommandLine.getAnswerAsString("Input a quantity between 1 and " + ProductBatch.getBatchById(batchId).getAmount() + ": ");
						}
						
						Storage.transfer(fromShelfId, toShelfId, batchId, Integer.parseInt(amount));
					} else {
						System.out.println("The product batch of id " + batchId + " is not on the shelf of id "+ fromShelfId);
						throw new CancelException();
					}
					break;
				case "Sell product(Cash)":
					handleSale();
					break;
				case "Sell product(Card)":
					handleSale();
					// TODO - do CREDIT CARD
					CommandLine.getAnswerAsString("Please input credit card number");
					break;
					//TODO: HARD
				case "Return product":
					// TODO - finish this
					// Get batch id and sale id.
					// Return is valid if batch id is linked to sale id in salebatches
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
				case "Add supplier":
					// TODO - Add supplier
					System.err.println("Unimplemented function");
					break;
				case "Remove supplier":
					// TODO - Remove supplier
					System.err.println("Unimplemented function");
					break;
				case "Add Warehouse":
					// TODO - Add Warehouse
					System.err.println("Unimplemented function");
					break;
				case "Report Employees":
					// TODO - report employees
					System.err.println("Unimplemented function");
					break;
				case "Report Products":
					// TODO - report products
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
					batchId = CommandLine.getAnswerAsInt("A batch with the given id must exist and be on the shelf: ");
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
