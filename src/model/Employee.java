package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		Connection con = PosSystem.getConnection();
		
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
	
	public boolean getActiveStatus() {
		return this.activeStatus;
	}
	
	public void setType(EmployeeType type) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
					       "SET employeeType = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, type.toString());
			stmt.setInt(2, this.employeeId);

			PosSystem.getDatabase().executeQuery(stmt);
			this.type = type;
		} catch (Exception e) {
			System.err.println("Failed to set type");
		}
	}
	
	public void setPassword(String password) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
					       "SET password = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, password);
			stmt.setInt(2, this.employeeId);

			PosSystem.getDatabase().executeQuery(stmt);
			
			this.password = password;
		} catch (Exception e) {
			System.err.println("Failed to set password");
		}
	}
	
	public void setFirstName(String firstName) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
					       "SET fname = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, firstName);
			stmt.setInt(2, this.employeeId);

			PosSystem.getDatabase().executeQuery(stmt);
			this.firstName = firstName;
		} catch (Exception e) {
			System.err.println("Failed to set firstname");
		}
	}
	
	public void setLastName(String lastName) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;	
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
					       "SET lname = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, lastName);
			stmt.setInt(2, this.employeeId);

			PosSystem.getDatabase().executeQuery(stmt);
			
			this.lastName = lastName;
		} catch (Exception e) {
			System.err.println("Failed to set lastname");
		}
	}
	
	public void setActiveStatus(boolean active) {	
		Connection con = PosSystem.getConnection();
		
		try {
			if (active != this.activeStatus) {
				PreparedStatement stmt = null;
				
				String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".employee " +
						       "SET active = ? " +
						       "WHERE id = ?";
				
				System.out.println(active);
		    	stmt = con.prepareStatement(query);
				stmt.setBoolean(1, active);
				stmt.setInt(2, this.employeeId);
				
				PosSystem.getDatabase().executeQuery(stmt);
				
				this.activeStatus = active;
			}
		} catch (Exception e) {
			System.err.println("Failed to update active status");
		}
	}
	
	public static Employee getEmployeeById(int id) {
		Employee emp = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.employee WHERE id = " + id).executeQuery();
			tables.next();
			emp = new Employee(tables.getInt("id"),tables.getString("fname"),tables.getString("lname"),EmployeeType.valueOf(tables.getString("employeeType")),tables.getString("password"));
			emp.activeStatus = tables.getBoolean("active");
		} catch (SQLException e) {
			return null;
		}
		
		return emp;
	}
	
	public static ArrayList<Integer> getEmployeesFromStore(EmployeeType type) {
        ArrayList<Integer> employees = new ArrayList<Integer>();
        
        try {
            String query = "SELECT DISTINCT storeemployee.employeeId FROM seng2020.storeemployee, seng2020.employee " +
                           "WHERE storeemployee.employeeId = employee.id AND storeemployee.storeId = ? AND employee.employeeType = ?";
            PreparedStatement stmt = PosSystem.getConnection().prepareStatement(query);
            stmt.setInt(1, PosSystem.getStoreId());
            stmt.setString(2, type.toString());
            
            ResultSet tables = stmt.executeQuery();
            while(tables.next()) {
                employees.add(tables.getInt(1));
            }
        } catch (SQLException e) {
            Database.printStackTrace(e);
            return null;
        }
        
        return employees;
    }

	public void demote() {
		switch (this.type) {
			case STAFF:
				break;
			case MANAGER:
				this.setType(EmployeeType.STAFF);
				break;
			case ADMIN:
				this.setType(EmployeeType.MANAGER);
				break;
		}
	}
	
	public void promote() {
		switch (this.type) {
			case STAFF:
				this.setType(EmployeeType.MANAGER);
				break;
			case MANAGER:
				this.setType(EmployeeType.ADMIN);
				break;
			case ADMIN:
				break;
		}
	}
}
