package view;

import java.util.Scanner;

import model.*;

public class Reader {
	

				
	private Employee makeEmployeeOfType(int type, int id, String fName, String lName) {
		Employee newEmployee = null;
		if (EmployeeType.ADMIN.getId() == type) {
			newEmployee = new Admin(id, fName, lName);
		} else if (EmployeeType.FLOORSTAFF.getId() == type) {
			newEmployee = new FloorStaff(id, fName, lName);
		} else if (EmployeeType.MANAGER.getId() == type) {
			newEmployee = new Manager(id, fName, lName);
		}
		return newEmployee;
	}
	//yea
	
	public Employee createEmployee(Store store) {
		Scanner sc = new Scanner(System.in);
		System.out.println("What type of employee?");
		
		
		int i = 1;
		for (EmployeeType type: EmployeeType.values()) {
			System.out.println("<" + i + "> " + type);
			i++;
		}
		int type = sc.nextInt();
		type--;
		
		int id = 0;
		System.out.println("First Name? ");
		String fName = sc.next();
		System.out.println("Last Name? ");
		String lName = sc.next();
		Employee newEmployee = EmployeeFactory.createEmployee(store, EmployeeType.values()[type], fName, lName);

		return newEmployee;
		
		
	}

}
