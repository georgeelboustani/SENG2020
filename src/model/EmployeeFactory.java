package model;

public class EmployeeFactory {
	
	public static Employee createEmployee(Store store, EmployeeType type, String firstName, String lastName) {
		int id = store.getNextEmployeeId();
		if (type == EmployeeType.FLOORSTAFF) {
			return new Staff(id,firstName,lastName);
		} else if (type == EmployeeType.MANAGER) {
			return new Manager(id,firstName,lastName);
		} else if (type == EmployeeType.ADMIN) {
			return new Admin(id,firstName,lastName);
		}
		
		return null;
	}
}
