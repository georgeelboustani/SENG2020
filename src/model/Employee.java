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
	
	public Employee(int id, String firstName, String lastName, EmployeeType type, String password) {
		this.employeeId = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.password = password;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".employee (`id`,`fname`,`lname`,`employeeType`,`password`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.employeeId);
		stmt.setString(2, this.firstName);
		stmt.setString(3, this.lastName);
		stmt.setString(4, this.type.toString());
		stmt.setString(5, this.password);
		
		db.executeQuery(stmt);
		con.close();
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public EmployeeType getType() {
		return type;
	}

	public void setType(EmployeeType type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
