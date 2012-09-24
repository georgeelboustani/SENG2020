package view;

import java.util.Scanner;

import model.*;

public class EmployeeReader {
	private String fName;
	private String lName;
	private EmployeeType type;
	
	public void fetchEmployeeInformation() {
		Scanner sc = new Scanner(System.in);
		System.out.println("What type of employee?");
		
		
		int i = 1;
		for (EmployeeType type: EmployeeType.values()) {
			System.out.println("<" + i + "> " + type);
			i++;
		}
		int type = sc.nextInt();
		type--;
		this.type = EmployeeType.values()[type];
		
		System.out.println("First Name? ");
		this.fName = sc.next();
		System.out.println("Last Name? ");
		this.lName = sc.next();
	}
	
	public String getFirstName() {
		return this.fName;
	}
	
	public String getLastName() {
		return this.lName;
	}
	
	public EmployeeType getEmployeeType() {
		return this.type;
	}

}
