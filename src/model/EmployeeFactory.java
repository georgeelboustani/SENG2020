package model;

public class EmployeeFactory {
	
	public static Employee createEmployee(int id, EmployeeType type, String firstName, String lastName) {
		if (type == EmployeeType.STAFF) {
			return new Staff(id,firstName,lastName);
		} else if (type == EmployeeType.MANAGER) {
			return new Manager(id,firstName,lastName);
		} else if (type == EmployeeType.ADMIN) {
			return new Admin(id,firstName,lastName);
		}
		
		return null;
	}
}
