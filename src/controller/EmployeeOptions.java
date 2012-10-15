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
				// FIXME - add space to storage locations
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
					break;
				case "Demote employee":
					empId = CommandLine.getAnswerAsInt("Employee Id: ");
					s.changeEmployeeType(empId,false);
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
					
					Date orderArrival = null;
					while (orderArrival == null) {
					    try {
					        orderArrival = Database.getSqlDate(CommandLine.getAnswerAsString("Arrival Date (yyyy-mm-dd):"));
					    } catch (IllegalArgumentException e) {
					        System.out.println("Please input a valid date in the following format yyyy-mm-dd");
					    }
					}

					Date receivedDate = null;
					productTypeId = ProductType.getProductTypeByName(ProductType.getAllAvailableProductTypes().get(CommandLine.getUserOption(ProductType.getAllAvailableProductTypes()) - 1)).getTypeId();
					int quantity = CommandLine.getAnswerAsInt("Quantity ordered:");
					
					// FIXME - validate supplier id
					int supplierId = CommandLine.getAnswerAsInt("Supplier Id:");
					
					Order newOrder = new Order(orderId,orderArrival,receivedDate,productTypeId,quantity,supplierId);
					try {
					    newOrder.persist();
					} catch (Exception e) {
					    System.err.println("Error while creating order");
					}
					
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
                    storages = Storage.getStorageFromStore(StorageType.FLOOR);
                    storages.addAll(Storage.getStorageFromStore(StorageType.BACKROOM));
                    storages.addAll(Storage.getStorageFromStore(StorageType.WAREHOUSE));
                    storages.addAll(Storage.getStorageFromStore(StorageType.RETURNSDEPOT));
                    storages.addAll(Storage.getStorageFromStore(StorageType.ORDERSDEPOT));
                    
                    System.out.println("Storage Id | Storage Type");
                    for (Integer id: storages) {
                        if (Shelf.getShelvesFromStorage(id) != null && Shelf.getShelvesFromStorage(id).size() != 0) {
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
                    while (Storage.getStorageById(fromStorageId) == null || Shelf.getShelvesFromStorage(toStorageId) == null) {
                        toStorageId = CommandLine.getAnswerAsInt("Please input a storage Id from the above list:");
                    }
				    
                    System.out.println("Existing shelves in the from storage with id " + fromStorageId);
                    for (Integer id: Shelf.getShelvesFromStorage(fromStorageId)) {
                        System.out.print(id + "  [");
                        for (Integer batchId: Shelf.getProductsFromShelf(id)) {
                            ProductBatch tempBatch = ProductBatch.getBatchById(batchId);
                            System.out.print(tempBatch.getProductType() + "(" + tempBatch.getAmount() + ") ");
                        }
                        System.out.println("]");
                    }
                    
					int fromShelfId = CommandLine.getAnswerAsInt("From shelf id:");
					while(!Shelf.isInStorage(fromShelfId, fromStorageId)){
						fromShelfId = CommandLine.getAnswerAsInt("Enter a from shelf id from the list above:");
					}
					
					System.out.println("Existing shelves in the to storage with id " + toStorageId);
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
					
					if (Shelf.isOnShelf(batchId, fromShelfId)) {
						String amount = CommandLine.getAnswerAsString("Quantity [1," + ProductBatch.getBatchById(batchId).getAmount() + "]: ");
						if (!DataValidator.validateInt(amount, 0, ProductBatch.getBatchById(batchId).getAmount())){
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
					CommandLine.getAnswerAsString("Please input credit card number");
					break;
				case "Return product":
				    // TODO - test this
				    int saleId = CommandLine.getAnswerAsInt("Sale id");
				    batchId = CommandLine.getAnswerAsInt("Batch Id");
				    
				    while (!Sale.isBatchInSale(saleId, batchId)) {
				        System.out.println("The sale batch combination you entered does not exist, please try again.");
				        saleId = CommandLine.getAnswerAsInt("Sale id");
	                    batchId = CommandLine.getAnswerAsInt("Batch Id");
				    }
				    
				    batch = ProductBatch.getBatchById(batchId);
				    if (batch.getExpiry().before(Database.getCurrentDate())) {
    				    int amount = CommandLine.getAnswerAsInt("How much of the product " + batch.getProductType().toLowerCase() + 
    				                                        " would you like to return:");
    				    while (amount > batch.getAmount() || amount < 0) {
    				        amount = CommandLine.getAnswerAsInt("Please enter a number between 0 and " + batch.getAmount() + ":");
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
    				        } catch (Exception e) {
    				            batch.setAmount(batch.getAmount() + amount);
    				        }
    				    }
				    } else {
				        System.out.println("Unable to return select product batch as it is expired");
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
					break;
				case "Report Products":
					// TODO - report products
					System.err.println("Unimplemented function");
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
