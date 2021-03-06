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
import model.StorageType;
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
		
		System.out.println("MAIN MENU");
		int option = CommandLine.getUserOption(questions);
		
		try {
		    CommandLine.clearConsole();
			employeeQuestionHandlerLevelOne(questions,option);
		} catch (CancelException e) {
			
		} catch (LogoutException e) {
			
		}
		CommandLine.clearConsole();
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
				newQuestions.add("Check Expired products");
				break;
			case "Manage Storage":
				newQuestions.add("Add supplier");
				newQuestions.add("Remove supplier");
				newQuestions.add("Add Storage");
				newQuestions.add("Add Shelf");
				newQuestions.add("Assign category to shelf");
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
			    newQuestions.add("Report Storages");
				newQuestions.add("Report Employees");
				newQuestions.add("Report Products");
				newQuestions.add("Report Orders");
				break;
			case "Log out":
				Main.requestLogout();
				throw new LogoutException();
		}
		System.out.println(question.toUpperCase());
		int newOption = CommandLine.getUserOption(newQuestions);
		try {
		    CommandLine.clearConsole();
			employeeQuestionHandlerLevelTwo(newQuestions,newOption);
		} catch (CancelException e){
		    CommandLine.clearConsole();
			employeeQuestionHandlerLevelOne(questions,option);
		}
		CommandLine.clearConsole();
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
		int productTypeId;
		int orderId;
		ProductBatch batch;
		ArrayList<Integer> storages;
		ProductType productType;
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
					
					CommandLine.getAnswerAsString(Employee.getEmployeeById(empId).getFirstName() + " has been successfully promoted to " + Employee.getEmployeeById(empId).getEmployeeType() + ". Type anything to continue");
					break;
				case "Demote employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.changeEmployeeType(empId,false);
					CommandLine.getAnswerAsString(Employee.getEmployeeById(empId).getFirstName() + "has been successfully demoted to " + Employee.getEmployeeById(empId).getEmployeeType() + ". Type anything to continue");
					break;
				case "Change product price":
					boolean changeAll = CommandLine.getYesOrNo("Would you like to change the price of a product type for every batch?");
					
				    String typeName = CommandLine.getAnswerAsString("Product Type:",ProductType.getAllAvailableProductTypes());
					
					
					productType = ProductType.getProductTypeByName(typeName);
					if (changeAll) {
					    double price = CommandLine.getAnswerAsInt("New Price: ");
					    s.changeProductPrice(productType.getTypeId(),price);
					} else {
					    if (ProductBatch.getAllAvailableBatchesOfType(productType) != null) {
					        System.out.println("Batch Id | Expiry | Price | Location");
    					    CommandLine.printList(ProductBatch.getAllAvailableBatchesOfType(productType));
    					    
    					    int chosenBatchId = CommandLine.getAnswerAsInt("Batch Id:");
    					    ProductBatch chosenBatch = ProductBatch.getBatchById(chosenBatchId);
    					    while (chosenBatch == null 
    					           || !chosenBatch.getProductType().equals(productType.getType().toString())
    					           || Sale.batchHasBeenSold(chosenBatchId)) {
    					        chosenBatchId = CommandLine.getAnswerAsInt("Please enter a batch id that exists in the above list:");
    	                        chosenBatch = ProductBatch.getBatchById(chosenBatchId);
    					    }
    					    double price = CommandLine.getAnswerAsInt("New Price: ");
    					    chosenBatch.setPrice(price);
					    } else {
					        System.out.println("No batches of the given product type found on the floor, backroom or warehouses");
					    }
					}
					break;
				case "Order products":
					orderId = PosSystem.generateNextId(TableName.ORDER);
					
					boolean done = false;
					Date orderArrival = null;
					while (orderArrival == null || orderArrival.before(Database.getCurrentDate())) {
					    try {
					        orderArrival = Database.getSqlDate(CommandLine.getAnswerAsString("Arrival Date (yyyy-mm-dd):"));
					        
					        if (orderArrival != null && (orderArrival.after(Database.getCurrentDate()) || orderArrival.equals(Database.getCurrentDate()))) {
					            done = true;
					        }
					    } catch (IllegalArgumentException e) {
					    
					    } finally {
					        if (!done) {
					            System.out.println("Please input a valid date in the following format yyyy-mm-dd, which is not before today");
					        }
					    }
					}

					Date receivedDate = null;
					productTypeId = ProductType.getProductTypeByName(ProductType.getAllAvailableProductTypes().get(CommandLine.getUserOption(ProductType.getAllAvailableProductTypes()) - 1)).getTypeId();
					int quantity = CommandLine.getAnswerAsInt("Quantity ordered:");
					
					for (Integer suppId: Supplier.getAllSuppliersId()) {
					    System.out.println(suppId + "  " + Supplier.getSupplierById(suppId).getCompanyName());
					}
					int supplierId = CommandLine.getAnswerAsInt("Supplier Id:");
					while (Supplier.getSupplierById(supplierId) == null) {
					    supplierId = CommandLine.getAnswerAsInt("Please input a valid supplier id from the list above:");
					}
					
					Order newOrder = new Order(orderId,orderArrival,receivedDate,productTypeId,quantity,supplierId);
					try {
					    newOrder.persist();
					} catch (Exception e) {
					    System.err.println("Error while creating order");
					}
					
					CommandLine.getAnswerAsString("Ordered " + quantity + " cheese successfully. Order id is " + orderId + ". Type anything to continue");
					break;
				case "Receive Order":
				    orderId = CommandLine.getAnswerAsInt("Order Id:");
					while (Order.getOrderById(orderId) == null || Order.getOrderById(orderId).getReceivedDate() != null) {
					    orderId = CommandLine.getAnswerAsInt("Enter the id of an order that has not been previously received:");
					}
					
					Order order = Order.getOrderById(orderId);
					order.setReceivedDate(Database.getCurrentDate());
					
					Date expiry = null;
                    while (expiry == null) {
                        try {
                            expiry = Database.getSqlDate(CommandLine.getAnswerAsString("Expiry Date (yyyy-mm-dd):"));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Please input a valid date in the following format yyyy-mm-dd");
                        }
                    }
                    
                    double price = CommandLine.getAnswerAsInt("Price:");
					
                    try {
    					batch = new ProductBatch(PosSystem.generateNextId(TableName.PRODUCTBATCH), 
    					                                      ProductType.getProductTypeById(order.getProductType()).getType(),
    					                                      expiry,price,order.getQuantity());
    					int storageId = Storage.getStorageFromStore(StorageType.ORDERSDEPOT).get(0);
    					Storage.addBatchToStorageOrOrderDepot(storageId, batch);
                    } catch (Exception e) {
                        order.setReceivedDate(null);
                    }
					
					break;
				case "Add product to system":
					String productName = CommandLine.getAnswerAsString("Product Name:");
					String productDescription = CommandLine.getAnswerAsString("Product Description:");
					
					ArrayList<String> productCategories = ProductCategory.getAllAvailableCategories();
					String productCategory = CommandLine.getAnswerAsString("Product Category:",productCategories);
					
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
                    storages = Storage.getStorageFromStore(StorageType.FLOOR);
                    storages.addAll(Storage.getStorageFromStore(StorageType.BACKROOM));
                    storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
                    storages.addAll(Storage.getStorageFromStore(StorageType.RETURNSDEPOT));
                    storages.addAll(Storage.getStorageFromStore(StorageType.ORDERSDEPOT));
                    
                    System.out.println("Storage Id | Storage Type");
                    for (Integer id: storages) {
                        if (Shelf.getShelvesFromStorage(id) != null && 
                            Shelf.getShelvesFromStorage(id).size() != 0) {
                            System.out.println(id + "  " + Storage.getStorageById(id).getStorageType());
                        }
                    }
                    int fromStorageId = CommandLine.getAnswerAsInt("From Storage Id:");
                    while (Storage.getStorageById(fromStorageId) == null 
                            || Shelf.getShelvesFromStorage(fromStorageId) == null 
                            || Shelf.getShelvesFromStorage(fromStorageId).size() == 0) {
                        fromStorageId = CommandLine.getAnswerAsInt("Please input a storage Id from the above list:");
                    }
                    
                    int toStorageId = CommandLine.getAnswerAsInt("To Storage Id:");
                    while (Storage.getStorageById(fromStorageId) == null || 
                           Shelf.getShelvesFromStorage(toStorageId) == null ||
                           Shelf.getShelvesFromStorage(toStorageId).size() == 0) {
                        toStorageId = CommandLine.getAnswerAsInt("Please input a storage Id from the above list:");
                    }
				    
                    int nonEmptyShelves = 0;
                    while (nonEmptyShelves == 0) { // Make sure 'from' storage location contains products
                        System.out.println("Existing shelves in the from storage with id " + fromStorageId);
                        for (Integer id: Shelf.getShelvesFromStorage(fromStorageId)) {
                            if (Shelf.getProductsFromShelf(id) == null ||  Shelf.getProductsFromShelf(id).size() == 0) {
                                
                            } else {
                                System.out.print(id + "  [");
                                for (Integer batchId: Shelf.getProductsFromShelf(id)) {
                                    ProductBatch tempBatch = ProductBatch.getBatchById(batchId);
                                    System.out.print(tempBatch.getProductType() + "(" + tempBatch.getAmount() + ") ");
                                }
                                System.out.println("]");
                                nonEmptyShelves++;
                            }
                            
                            if (nonEmptyShelves == 0) {
                                System.out.println("The 'from' storage location you chose contains no shelves that contain products. please choose another 'from' storage location");
                                fromStorageId = CommandLine.getAnswerAsInt("From Storage Id:");
                                while (Storage.getStorageById(fromStorageId) == null 
                                        || Shelf.getShelvesFromStorage(fromStorageId) == null 
                                        || Shelf.getShelvesFromStorage(fromStorageId).size() == 0) {
                                    fromStorageId = CommandLine.getAnswerAsInt("Please input a valid 'from' storage Id from the above list:");
                                }
                            }
                        }
                    }
                    
					int fromShelfId = CommandLine.getAnswerAsInt("From shelf id:");
					while(!Shelf.isInStorage(fromShelfId, fromStorageId)){
						fromShelfId = CommandLine.getAnswerAsInt("Enter a 'from' shelf id from the list above:");
					}
					
					System.out.println("Existing shelves in the 'to' storage with id " + toStorageId);
					for (Integer id: Shelf.getShelvesFromStorage(toStorageId)) {
                        System.out.print(id + "  [");
                        for (Integer batchId: Shelf.getProductsFromShelf(id)) {
                            ProductBatch tempBatch = ProductBatch.getBatchById(batchId);
                            System.out.print(tempBatch.getProductType() + " (" + tempBatch.getAmount() + ")");
                        }
                        System.out.println("]");
                    }
                    
                    int toShelfId = CommandLine.getAnswerAsInt("To shelf id:");
                    while(!Shelf.isInStorage(toShelfId, toStorageId)){
                        toShelfId = CommandLine.getAnswerAsInt("Enter a to shelf id from the list above:");
                    }
					
                    // Print product batches on the "from" shelf
                    System.out.println("Batch Id | Product Type | Amount");
                    for (Integer id: Shelf.getProductsFromShelf(fromShelfId)) {
                        ProductBatch tempBatch = ProductBatch.getBatchById(id);
                        System.out.println(id + "  " + tempBatch.getProductType() + "  " + tempBatch.getAmount());
                    }
					int batchId = CommandLine.getAnswerAsInt("Product batch id:");
					while(!Shelf.isOnShelf(batchId, fromShelfId)){
						batchId = CommandLine.getAnswerAsInt("Enter a product batch id which exists on the from shelf, found from the list above:");
					}
					
					ProductBatch transferBatch = ProductBatch.getBatchById(batchId);
					if (Shelf.hasCategory(toShelfId, transferBatch.getProductCategory().getCategoryId()) ||
					    Shelf.associatedCategories(toShelfId).length() < 2) {
					    Shelf toShelf = Shelf.getShelfById(toShelfId);
    					if (Shelf.isOnShelf(batchId, fromShelfId)) {
    						int maxTransfer = min(ProductBatch.getBatchById(batchId).getAmount(), toShelf.getMaxProducts() - toShelf.getCurrentAmount());
    					    
    						if (maxTransfer != ProductBatch.getBatchById(batchId).getAmount()) {
    						    System.out.println("Transferring the whole batch (" + ProductBatch.getBatchById(batchId).getAmount() +
    						                       " products) would cause the 'to' shelf to exceed capacity, so the max amount you can transfer" + 
    						                       " has been limited to " + maxTransfer);
    						}
    						String amount = CommandLine.getAnswerAsString("Quantity [1," + maxTransfer + "]: ");
    						while (!DataValidator.validateInt(amount, 1, maxTransfer)){
    							amount = CommandLine.getAnswerAsString("Input a quantity between 1 and " + maxTransfer + ": ");
    						}
    						
    						Storage.transfer(fromShelfId, toShelfId, batchId, Integer.parseInt(amount));
    						
    						CommandLine.getAnswerAsString("Successfully moved " + amount + " products to shelf with id " + toShelfId + ". Type anything to continue.");
    					} else {
    						System.out.println("The product batch of id " + batchId + " is not on the shelf of id "+ fromShelfId);
    						throw new CancelException();
    					}
					} else {
					    CommandLine.getAnswerAsString("You are trying to transfer a product type of category " + transferBatch.getProductCategory().getCategoryId() +
					                       " to a shelf that can hold categories " + Shelf.associatedCategories(toShelfId) + ", type anything to return to menu.");
					}
					
					break;
				case "Sell product(Cash)":
					handleSale(false);
					break;
				case "Sell product(Card)":
					handleSale(true);
					break;
				case "Return product":
				    int saleId = CommandLine.getAnswerAsInt("Sale id: ");
				    while (Sale.getSaleById(saleId) == null) {
				        saleId = CommandLine.getAnswerAsInt("Please enter a valid sale id: ");
				    }
				    System.out.println("The following are batches associated with the enter sale");
				    
				    ArrayList<String> batches = Sale.getBatchesFromSale(saleId);
				    CommandLine.printList(batches);
				    batchId = CommandLine.getAnswerAsInt("Batch Id:");
				    while (!batches.contains(String.valueOf(batchId))) {
				        batchId = CommandLine.getAnswerAsInt("Please enter a valid batch Id from the list above:");
				    }
				   
				    batch = ProductBatch.getBatchById(batchId);
				    if (batch.getExpiry().after(Database.getCurrentDate()) || batch.getExpiry().equals(Database.getCurrentDate())) {
    				    int amount = CommandLine.getAnswerAsInt("How much of the product " + batch.getProductType().toLowerCase() + 
    				                                        " would you like to return [1," + batch.getAmount() + "]:");
    				    while (amount > batch.getAmount() || amount <= 0) {
    				        amount = CommandLine.getAnswerAsInt("Please enter a number between 1 and " + batch.getAmount() + ":");
    				    }
    				    
    				    batch.setAmount(batch.getAmount() - amount);
    				    
    				    // If a returns depot exists, place the products in the returns depot, else throw away
    				    ArrayList<Integer> returndepots = Storage.getStorageFromStore(StorageType.RETURNSDEPOT);
    				    if (returndepots.size() > 0) {
    				        Storage returnDepot = Storage.getStorageById(returndepots.get(0));
    				        
    				        ProductBatch returnedBatch = new ProductBatch(PosSystem.generateNextId(TableName.PRODUCTBATCH),
    				                                                      batch.getProductType(), batch.getExpiry(),batch.getPrice(),
    				                                                      amount);
    				        try {
        				        // Add the batch to the returns depot
        				        Storage.addBatchToStorageOrOrderDepot(returnDepot.getId(), returnedBatch);
        				        CommandLine.getAnswerAsString("Successfully return products and added to returns depot. Type anything to continue.");
    				        } catch (Exception e) {
    				            batch.setAmount(batch.getAmount() + amount);
    				        }
    				    }
				    } else {
				        CommandLine.getAnswerAsString("Unable to return select product batch as it is expired. Type anything to continue.");
				    }
				    
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
				    try{
				        String supName = CommandLine.getAnswerAsString("Company Name: ");
	                    String supAddress = CommandLine.getAnswerAsString("Address: ");
	                    int phone = CommandLine.getAnswerAsInt("Phone: ");
	                    String supDesc = CommandLine.getAnswerAsString("Description: ");
	                    
	                    Supplier sup = new Supplier(PosSystem.generateNextId(TableName.SUPPLIER),supName,supAddress,phone,supDesc);
	                    sup.persist();
				    }catch (Exception e){
                        System.out.println("Invalid supplier; supplier not created");
				    }
                    
					break;
				case "Remove supplier":
					// TODO - Remove supplier
					System.err.println("Unimplemented function");
					break;
				case "Report Orders":
				    reportOrders();
				    break;
				case "Assign category to shelf":
				    ArrayList<String> storageTypes = new ArrayList<String>();
				    storageTypes.add(StorageType.FLOOR.toString());
				    storageTypes.add(StorageType.BACKROOM.toString());
				    storageTypes.add(StorageType.WAREHOUSE.toString());
				    String storageType = CommandLine.getAnswerAsString("Storage type where shelf exists", storageTypes);
				    
				    ArrayList<Integer> storagesOfType = Storage.getStorageFromStore(StorageType.valueOf(storageType));
				    
				    if (storagesOfType.size() > 0) {
    				    System.out.println("The following are the id's of storages of type " + storageType);
    				    for (Integer storageId: storagesOfType) {
    				        System.out.println(storageId);
    				    }
    				    
    				    int id = CommandLine.getAnswerAsInt("Storage Id:");
    				    while (!storagesOfType.contains(id)) {
    				        id = CommandLine.getAnswerAsInt("Enter a valid storage id from the list above:");
    				    }
    				    
    				    ArrayList<Integer> shelfs = Shelf.getShelvesFromStorage(id);
    				    if (shelfs.size() > 0) {
        				    System.out.println("Shelf Id's in storage " + id);
                            for (Integer shelfId: shelfs) {
                                System.out.println(shelfId);
                            }
                            
                            int idShelf = CommandLine.getAnswerAsInt("Shelf Id:");
                            while (!shelfs.contains(idShelf)) {
                                idShelf = CommandLine.getAnswerAsInt("Enter a valid shelf id from the list above:");
                            }
                            
                            ArrayList<String> categories = ProductCategory.getAllAvailableCategories();
                            String chosenCategory = CommandLine.getAnswerAsString("Category to assign:", categories);
                            while (Shelf.hasCategory(idShelf, ProductCategory.getProductCategoryByName(chosenCategory).getCategoryId())) {
                                chosenCategory = CommandLine.getAnswerAsString("The shelf selected has already been assigned the category " + chosenCategory + ", please choose another category or type cancel", categories);
                            }
                            Shelf.getShelfById(idShelf).assignCategory(ProductCategory.getProductCategoryByName(chosenCategory));
                            CommandLine.getAnswerAsString("Successfully assigned shelf with id " + idShelf + " the category " + chosenCategory + ". Type anything to continue");
    				    } else {
    				        System.out.println("No shelves were found within the chosen storage.");
    				    }
				    } else {
				        System.out.println("No storages of type " + storageType + " were found.");
				    }
				    break;
				case "Add Storage":
				    ArrayList<String> sto = new ArrayList<String>();
				    sto.add(StorageType.values()[0].toString());
				    sto.add(StorageType.values()[1].toString());
				    sto.add(StorageType.values()[2].toString());
				    CommandLine.printList(sto);
				    
	                String storType = null;
                    while (storType == null) {
                        try {
                            storType = CommandLine.getAnswerAsString("Storage type: ").toUpperCase();
                            StorageType.valueOf(storType); // To cause an exception if invalid
                        } catch (IllegalArgumentException e) {
                            System.out.println("Please enter a valid storage type.");
                            storType = null;
                        }
                    }
                        
                    Storage newStorage = new Storage(PosSystem.generateNextId(TableName.STORAGE),StorageType.valueOf(storType));
                    newStorage.persist();
                    Store.getStoreById(PosSystem.getStoreId()).persistStorageMapping(newStorage);
                    break;
                case "Add Shelf":
                    storages = Storage.getStorageFromStore(StorageType.FLOOR);
                    storages.addAll(Storage.getStorageFromStore(StorageType.BACKROOM));
                    storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
                    storages.addAll(Storage.getStorageFromStore(StorageType.RETURNSDEPOT));
                    storages.addAll(Storage.getStorageFromStore(StorageType.ORDERSDEPOT));
                    
                    System.out.println("|-----------------------------------------------------------------------|");
                    System.out.println("|\tStorage Id\t|\tStorage Type\t|\tNum Shelves\t|");
                    System.out.println("|-----------------------------------------------------------------------|");
                    for (Integer id: storages) {
                        System.out.println("|\t" + id + "\t\t|\t" + Storage.getStorageById(id).getStorageType() + "   \t|\t" + Shelf.getShelvesFromStorage(id).size() + "\t\t|");
                    }
                    System.out.println("|-----------------------------------------------------------------------|");
                    int storageId = CommandLine.getAnswerAsInt("Storage Id:");
                  
                    while (Storage.getStorageById(storageId) == null) {
                        fromStorageId = CommandLine.getAnswerAsInt("Please input a storage Id from the above list:");
                    }
                    
                    int maxProducts = CommandLine.getAnswerAsInt("Max products:");
                    Storage.addShelfToStorage(storageId, maxProducts);
                    break;
				case "Report Storages":
				    storages = Storage.getStorageFromStore(StorageType.BACKROOM);
                    storages.addAll(Storage.getStorageFromStore(StorageType.FLOOR));
                    storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
                    storages.addAll(Storage.getStorageFromStore(StorageType.RETURNSDEPOT));
                    storages.addAll(Storage.getStorageFromStore(StorageType.ORDERSDEPOT));
                    
                    System.out.println("Storage Id | Storage Type");
                    for (Integer id: storages) {
                        System.out.println(id + "  " + Storage.getStorageById(id).getStorageType());
                    }
				    break;
				case "Report Employees":
					ArrayList<String> types = EmployeeType.valuesToString();
					String type = CommandLine.getAnswerAsString("Choose an employee type to search for:", types);
					
					ArrayList<Integer> employees = Employee.getEmployeesFromStore(EmployeeType.valueOf(type));
                    
                    System.out.println("Employee Id | Employee Name");
                    for (Integer id: employees) {
                        System.out.println(id + "  " + Employee.getEmployeeById(id).getFirstName() + Employee.getEmployeeById(id).getLastName());
                    }
                    
                    CommandLine.getAnswerAsString("Type anything to continue");
					break;
				case "Report Products":
					
				    ArrayList<String> reportType = new ArrayList<String>();
				    reportType.add("Full");
				    reportType.add("Brief");
				    int chosenOption = CommandLine.getUserOption(reportType);
				    
				    if (chosenOption == 1) {
    					ArrayList<String> briefReport = ProductBatch.getFullProductReport();
    					if (briefReport != null) {
    					    CommandLine.printList(briefReport);
    					}
				    } else {
				        ArrayList<String> briefReport = ProductBatch.getBriefProductReport();
                        if (briefReport != null) {
                            CommandLine.printList(briefReport);
                        }
				    }
					
					CommandLine.getAnswerAsString("Type anything to continue");
					break;
				case "Check Expired products":
				    checkExpiredProducts();
				    break;
				case "Cancel":
					throw new CancelException();
			}
		} catch (SQLException e) {
		    Database.printStackTrace(e);
			employeeQuestionHandlerLevelTwo(questions,option);
		} catch (InvalidIdException e) {
		    Database.printStackTrace(e);
			employeeQuestionHandlerLevelTwo(questions,option);
		} catch (CancelException e) {
			throw e;
		}
	}

    private static void checkExpiredProducts() throws SQLException,
            CancelException {
        ArrayList<ProductBatch> batches = Storage.getProductBatchesInStorageTypeFromStore(StorageType.FLOOR);
        batches.addAll(Storage.getProductBatchesInStorageTypeFromStore(StorageType.BACKROOM));
        batches.addAll(Storage.getProductBatchesInStorageTypeFromStore(StorageType.WAREHOUSE));
        batches.addAll(Storage.getProductBatchesInStorageTypeFromStore(StorageType.RETURNSDEPOT));
        
        ArrayList<ProductBatch> expiredBatches = new ArrayList<ProductBatch>();
        for (ProductBatch currentBatch: batches) {
            if (currentBatch.getExpiry().before(Database.getCurrentDate())) {
                expiredBatches.add(currentBatch);
            }
        }
        
        ArrayList<String> expiredBatchesString = new ArrayList<String>();
        expiredBatchesString.add("|---------------------------------------------------------------------------------------|");
        expiredBatchesString.add("|\tBatch Id\t|\tProduct Type\t|\tAmount\t|\tExpiry Date\t|");
        expiredBatchesString.add("|---------------------------------------------------------------------------------------|");
        for (ProductBatch currentBatch: expiredBatches) {
            expiredBatchesString.add("|\t" + currentBatch.getBatchId() + "\t\t|\t" + currentBatch.getProductType() + "      \t|\t" + currentBatch.getAmount()+ "   \t|\t" + currentBatch.getExpiry() + "\t|");
        }
        expiredBatchesString.add("|---------------------------------------------------------------------------------------|");
        
        if (expiredBatchesString.size() == 0) {
            System.out.println("No expired products were found in the system");
        } else {
            System.out.println("The following products are expired:");
            CommandLine.printList(expiredBatchesString);
            
            if (CommandLine.getYesOrNo("Remove all the products?")) {
                for (ProductBatch expiredBatch: expiredBatches) {
                    Integer expiredShelfId = Shelf.getShelfIdByBatchId(expiredBatch.getBatchId());
                    if (expiredShelfId != null) {
                        Shelf s1 = Shelf.getShelfById(expiredShelfId);
                        
                        Shelf.setCurrentAmount(s1.getCurrentAmount() - expiredBatch.getAmount(), expiredShelfId);
                        Shelf.removeShelfBatchMapping(expiredShelfId, expiredBatch.getBatchId());
                        expiredBatch.delete();
                    } else {
                        System.err.println("Error while removing batch with id " + expiredBatch.getBatchId() + " and shelf batch mapping");
                    }
                }
            } else if (CommandLine.getYesOrNo("Remove a single expired product?")){
                int expiredBatchId = CommandLine.getAnswerAsInt("Batch Id:");
                
                boolean validBatch = false;
                while (!validBatch) {
                    for (ProductBatch b: expiredBatches) {
                        if (expiredBatchId == b.getBatchId()) {
                            validBatch = true;
                            break;
                        }
                    }
                    
                    if (!validBatch) {
                        expiredBatchId = CommandLine.getAnswerAsInt("Please input a valid batch Id from the list above:");
                    }
                }
                
                Integer expiredShelfId = Shelf.getShelfIdByBatchId(expiredBatchId);
                if (expiredShelfId != null) {
                    Shelf s1 = Shelf.getShelfById(expiredShelfId);
                    ProductBatch b = ProductBatch.getBatchById(expiredBatchId);
                    
                    Shelf.setCurrentAmount(s1.getCurrentAmount() - b.getAmount(), expiredShelfId);
                    Shelf.removeShelfBatchMapping(expiredShelfId, expiredBatchId);
                    b.delete();
                } else {
                    System.err.println("Error while removing batch and shelf batch mapping");
                }
            }
        }
        
        CommandLine.getAnswerAsString("Type anything to continue:");
    }

    private static void reportOrders() throws CancelException {
        ProductType productType;
        ArrayList<Integer> orders = new ArrayList<Integer>();
        if (CommandLine.getYesOrNo("Show all pending orders?")) {
            for (String type: ProductType.getAllAvailableProductTypes()) {
                ArrayList<Integer> tempOrders = Order.getAllOrders(ProductType.getProductTypeByName(type));
                if (tempOrders!= null) {
                    orders.addAll(tempOrders);
                }
            }
            
            System.out.println("|---------------------------------------------------------------------------------------|");
            System.out.println("|\tOrder Id\t|\tProduct Type\t|\tAmount\t|\tArrival Date\t|");
            System.out.println("|---------------------------------------------------------------------------------------|");
            for (Integer id: orders) {
                System.out.println("|\t" + id + "\t\t|\t" + ProductType.getProductTypeById(Order.getOrderById(id).getProductType()).getType() + "      \t|\t" + Order.getOrderById(id).getQuantity()+ "   \t|\t" + Order.getOrderById(id).getOrderArrival() + "\t|");
            }
            System.out.println("|---------------------------------------------------------------------------------------|");
            
            CommandLine.getAnswerAsString("Type anything to continue");
        } else {
            CommandLine.clearConsole();
            boolean checkAnotherProduct = true;
            while (checkAnotherProduct) {
                String type = CommandLine.getAnswerAsString("Choose a product type to report on:",ProductType.getAllAvailableProductTypes());
                productType = ProductType.getProductTypeByName(type);
                ArrayList<Integer> tempOrders = Order.getAllOrders(productType);
                if (tempOrders != null) {
                    orders = tempOrders;
                }
                
                System.out.println("|---------------------------------------------------------------|");
                System.out.println("|\tOrder Id\t|\tAmount\t|\tArrival Date\t|");
                System.out.println("|---------------------------------------------------------------|");
                
                for (Integer id: orders) {
                    System.out.println("|\t" + id + "\t\t|\t" + Order.getOrderById(id).getQuantity() + "   \t|\t" + Order.getOrderById(id).getOrderArrival() + "  \t|");
                }
                System.out.println("|---------------------------------------------------------------|");
                
                checkAnotherProduct = CommandLine.getYesOrNo("Would you like to check another product?");
            }
        }
    }

	private static void handleSale(boolean credit) throws NumberFormatException, SQLException, CancelException {
		Trolley newBatches = new Trolley();
		ArrayList<ProductBatch> oldBatches = new ArrayList<ProductBatch>();
		try {
			boolean addProducts = true;
			boolean removeProducts = false;
			while (addProducts || removeProducts) {
			    if (addProducts) {
	                System.out.println("|---------------------------------------TROLLEY---------------------------------|");
                    System.out.println("| Batch Id\t| Product Type\t| Amount\t| Price\t\t| Total Cost\t|");
                    System.out.println("|-------------------------------------------------------------------------------|");
                    for (ProductBatch batch: newBatches.getProducts()) {
                        System.out.println("| " + batch.getBatchId() + "\t\t| " + batch.getProductType() + "     \t| " + batch.getAmount() + "\t\t| $" + batch.getPrice() + "\t\t| $" + batch.getAmount() * batch.getPrice() + "\t\t|");
                    }
                    System.out.println("|-------------------------------------------------------------------------------|\n");
			        
    			    ArrayList<Integer> floorBatchesId = new ArrayList<Integer>();
    			    ArrayList<ProductBatch> floorBatches = Storage.getProductBatchesInStorageTypeFromStore(StorageType.FLOOR);
    			    System.out.println("|----------------------------------------STORE------------------------------------------|");
    			    System.out.println("|\tBatch Id\t|\tProduct Type\t|\tPrice\t|\tExpiry Date\t|");
    			    System.out.println("|---------------------------------------------------------------------------------------|");
    		        for (ProductBatch currentBatch: floorBatches) {
    		            if (currentBatch.getAmount() > 0) {
    		                System.out.println("|\t" + currentBatch.getBatchId() + "\t\t|\t" + currentBatch.getProductType() + "      \t|\t" + currentBatch.getPrice()+ "   \t|\t" + currentBatch.getExpiry() + "\t|");
    		                floorBatchesId.add(currentBatch.getBatchId());
    		            }
    		        }
    		        System.out.println("|---------------------------------------------------------------------------------------|");
    			    
    				int batchId = CommandLine.getAnswerAsInt("Batch Id: ");
    				ProductBatch oldBatch = ProductBatch.getBatchById(batchId);
    				while(oldBatch == null || !Shelf.isOnShelf(batchId) || !floorBatchesId.contains(batchId)){
    					batchId = CommandLine.getAnswerAsInt("A batch with the given id must exist and be on the shelf: ");
    					oldBatch = ProductBatch.getBatchById(batchId);
    				}
    				
    				ProductBatch updatedOldBatch = ProductBatch.getBatchById(oldBatch.getBatchId());
    				if (updatedOldBatch.getAmount() != 0) {
    					String quantity = CommandLine.getAnswerAsString("Quantity [1," + oldBatch.getAmount() + "]: ");
    					while (!DataValidator.validateInt(quantity, 1, oldBatch.getAmount())) {
    						System.out.print("Quantity entered is not valid. Please enter a number between 1 and " + oldBatch.getAmount() + ": ");
    						quantity = CommandLine.getAnswerAsString("");
    					}
    					
    					
    					oldBatch.setAmount(updatedOldBatch.getAmount() - Integer.parseInt(quantity));
    					ProductBatch newBatch = new ProductBatch(PosSystem.generateNextId(TableName.PRODUCTBATCH),
                                 					oldBatch.getProductType(),oldBatch.getExpiry(),oldBatch.getPrice(),Integer.parseInt(quantity));
    					
    					boolean exists = newBatches.addProductBatch(newBatch);
    					if (!exists) {
    						newBatch.persist();
    						oldBatches.add(oldBatch);
    					}
    					
    				} else {
    					System.out.println("Batch chosen does not exist on floor");
    				}
			    } else if (removeProducts){
			        System.out.println("|---------------------------------------TROLLEY---------------------------------|");
		            System.out.println("| Batch Id\t| Product Type\t| Amount\t| Price\t\t| Total Cost\t|");
		            System.out.println("|-------------------------------------------------------------------------------|");
		            for (ProductBatch batch: newBatches.getProducts()) {
		                System.out.println("| " + batch.getBatchId() + "\t\t| " + batch.getProductType() + "     \t| " + batch.getAmount() + "\t\t| $" + batch.getPrice() + "\t\t| $" + batch.getAmount() * batch.getPrice() + "\t\t|");
		            }
		            System.out.println("|-------------------------------------------------------------------------------|");
		            
		            int removeBatchId = CommandLine.getAnswerAsInt("Batch Id");
	                boolean validId = false;
		            for (ProductBatch batch: newBatches.getProducts()) {
	                    if (batch.getBatchId() == removeBatchId) {
	                        validId = true;
	                        break;
	                    }
	                }
		            
		            if (validId) {
		                ProductBatch batchToRemove = ProductBatch.getBatchById(removeBatchId);
		                int amount = CommandLine.getAnswerAsInt("Please enter an amount to remove between 1 and " + batchToRemove.getAmount());
		                while (!DataValidator.validateInt(String.valueOf(amount), 1, batchToRemove.getAmount())) {
                            System.out.print("Amount entered is not valid. Please enter a number between 1 and " + batchToRemove.getAmount() + ": ");
                            amount = CommandLine.getAnswerAsInt("");
                        }
		                
		                newBatches.removeProducts(ProductType.getProductTypeByName(batchToRemove.getProductType()), batchToRemove.getExpiry(), amount);
		                ProductBatch tempBatch = null;
		                for (ProductBatch oldBatch: oldBatches) {
		                    if (oldBatch.getExpiry().equals(batchToRemove.getExpiry()) && oldBatch.getProductType().equals(batchToRemove.getProductType())) {
		                        ProductBatch updatedOldBatch = ProductBatch.getBatchById(oldBatch.getBatchId());
		                        oldBatch.setAmount(updatedOldBatch.getAmount() + amount);
		                        tempBatch = oldBatch;
		                        break;
		                    }
		                }
		                
		                if (!newBatches.contains(ProductType.getProductTypeByName(batchToRemove.getProductType()), batchToRemove.getExpiry())) {
		                    oldBatches.remove(tempBatch);
		                }
		            } else {
		                System.out.println("You provided and invalid id. It does not exist in the trolley");
		            }
			    }
			    
				addProducts = CommandLine.getYesOrNo("Would you like to add more products?");
                if (!addProducts) {
                    removeProducts = CommandLine.getYesOrNo("Would you like to remove products from trolley?");
                }
			} 
			
			CommandLine.clearConsole();
			
			System.out.println("|---------------------------------------RECEIPT---------------------------------|");
            System.out.println("| Batch Id\t| Product Type\t| Amount\t| Price\t\t| Total Cost\t|");
            System.out.println("|-------------------------------------------------------------------------------|");
            for (ProductBatch batch: newBatches.getProducts()) {
                System.out.println("| " + batch.getBatchId() + "\t\t| " + batch.getProductType() + "     \t| " + batch.getAmount() + "\t\t| $" + batch.getPrice() + "\t\t| $" + batch.getAmount() * batch.getPrice() + "\t\t|");
            }
            System.out.println("|-------------------------------------------------------------------------------|");
			
            int transactionCost = 0;
            for (ProductBatch batch: newBatches.getProducts()) {
                transactionCost += batch.getAmount() * batch.getPrice();
            }
            
            System.out.println("Total purchase cost: $" + transactionCost);
            
            if (credit) {
                CommandLine.getAnswerAsString("Please input credit card number");
            }
            
			boolean isMember = CommandLine.getYesOrNo("Is a member?");
			if (isMember) {
				int memId = CommandLine.getAnswerAsInt("Member Id: ");
				Member mem = Member.getMemberById(memId);
                
                if (mem.getLoyaltyPoints() > 0) {
    				boolean useLoyalty = CommandLine.getYesOrNo("Use loyalty points?");
    				if (useLoyalty) {
    				    if (transactionCost >= mem.getLoyaltyPoints()) {
    				        System.out.println(mem.getLoyaltyPoints() + " points have been used as complete payment for the transaction.");
    				        transactionCost = transactionCost - mem.getLoyaltyPoints();
    				        mem.setLoyaltyPoints(0);
    				    } else {
    				        System.out.println(mem.getLoyaltyPoints() + " points have been used as partial payment for the transaction.");
    				        transactionCost = 0;
                            mem.setLoyaltyPoints(mem.getLoyaltyPoints() - transactionCost);
    				    }
    				}
                }
				
				if (transactionCost > 0) {
				    mem.addLoyaltyPoints(transactionCost/10);
				    System.out.println(transactionCost/10 + " loyalty points have been added to your account.");
				}
				
				
				System.out.println("Total paid: $" + transactionCost + " Current loyalty point balance: " + mem.getLoyaltyPoints());
			}
			
			Sale sale = new Sale(PosSystem.generateNextId(TableName.SALE),Database.getCurrentDate(),newBatches);
			sale.persist();
			
			CommandLine.getAnswerAsString("\nType anything to return to menu.");
		} catch (Exception e) {
			// Undo the subtracting of batch amount from above
			for (int i = 0; i < oldBatches.size(); i++) {
			    ProductBatch updatedOldBatch = ProductBatch.getBatchById(oldBatches.get(i).getBatchId());
			    updatedOldBatch.setAmount(updatedOldBatch.getAmount() + newBatches.getProducts().get(i).getAmount());
				newBatches.getProducts().get(i).delete();
			}
			throw new CancelException();
		}
	}
	
	public static int min(int a, int b) {
	    if (a < b) {
	        return a;
	    } else {
	        return b;
	    }
	}
}
