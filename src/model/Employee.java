package model;

public class Employee {

	private int employeeId;
	private String firstName;
	private String lastName;
	private EmployeeType type;
	
	public Employee(int id, String firstName, String lastName, EmployeeType type) {
		this.employeeId = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public EmployeeType getEmployeeType() {
		return this.type;	
	}
}
