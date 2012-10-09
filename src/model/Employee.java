package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import database.Database;

public class Employee {

	private int employeeId;
	private String firstName;
	private String lastName;
	private EmployeeType type;
	private String password;
	private boolean activeStatus;
	
	public Employee(int id, String firstName, String lastName, EmployeeType type, String password) {
		this.employeeId = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.password = password;
		this.activeStatus = true;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getDatabase().getConnection();
		
		String query = "INSERT into " + PosSystem.getDatabase().getDbName() + ".employee (`id`,`fname`,`lname`,`employeeType`,`password`,`active`) " +
				"VALUES (?,?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.employeeId);
		stmt.setString(2, this.firstName);
		stmt.setString(3, this.lastName);
		stmt.setString(4, this.type.toString());
		stmt.setString(5, this.password);
		stmt.setBoolean(6, this.activeStatus);
		
		PosSystem.getDatabase().executeQuery(stmt);
		con.close();
	}

	public int getEmployeeId() {
		return employeeId;
	}
	
	public EmployeeType getType() {
		return type;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public EmployeeType getEmployeeType() {
		return this.type;	
	}
	
	public void setType(EmployeeType type) {
		this.type = type;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setActiveStatus(boolean active) {	
		try {
			if (active != this.activeStatus) {
				PreparedStatement stmt = null;
				Connection con = PosSystem.getDatabase().getConnection();
				
				String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
						       "SET active = ?" +
						       "WHERE id = ?";
				
		    	stmt = con.prepareStatement(query);
				stmt.setBoolean(1, active);
				stmt.setInt(2, this.employeeId);
				
				PosSystem.getDatabase().executeQuery(stmt);
				con.close();
			}
		} catch (Exception e) {
			System.err.println("Failed to update active status");
		}
	}
	
	public static Employee getEmployeeById(int id) {
		Employee emp = null;
		
		try {
			ResultSet tables = PosSystem.getDatabase().getConnection().prepareStatement("SELECT * FROM seng2020.employee WHERE id = " + id).executeQuery();
			tables.next();
			emp = new Employee(tables.getInt("id"),tables.getString("fname"),tables.getString("lname"),EmployeeType.valueOf(tables.getString("employeeType")),tables.getString("password"));
		} catch (SQLException e) {
			return null;
		}
		
		return emp;
	}
}
