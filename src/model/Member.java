package model;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import database.Database;


public class Member {
	
	private int memberId;
	private Date signUp;
	private String firstName;
	private String lastName;
	private int loyaltyPoints;
	private String password;
	private boolean activeStatus;
	
	public Member(int id, Date signUp, String firstName, String lastName, String password) {
		this.memberId = id;
		this.signUp = signUp;
		this.firstName = firstName;
		this.lastName = lastName;
		this.loyaltyPoints = 0;
		this.password = password;
		this.activeStatus = true;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + PosSystem.getDatabase().getDbName() + ".member (`id`,`fname`,`lname`,`password`,`points`,`signupDate`,`active`) " +
				"VALUES (?,?,?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.memberId);
		stmt.setString(2, this.firstName);
		stmt.setString(3, this.lastName);
		stmt.setString(4, this.password);
		stmt.setInt(5, this.loyaltyPoints);
		stmt.setDate(6, this.signUp);
		stmt.setBoolean(7, this.activeStatus);
		
		PosSystem.getDatabase().executeQuery(stmt);
	}
	
	public int getId() {
		return this.memberId;
	}
	
	public void addLoyaltyPoints(int points) {
		this.loyaltyPoints += points;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
					       "SET fname = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, firstName);
			stmt.setInt(2, this.memberId);

			PosSystem.getDatabase().executeQuery(stmt);
			this.firstName = firstName;
		} catch (Exception e) {
			System.err.println("Sorry " + this.firstName + ", The system has failed to change your first name.");
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;	
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
					       "SET lname = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, lastName);
			stmt.setInt(2, this.memberId);

			PosSystem.getDatabase().executeQuery(stmt);
			
			this.lastName = lastName;
		} catch (Exception e) {
			System.err.println("Sorry " + this.firstName + ", The system has failed to change your last name.");
		}
	}

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;	
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
					       "SET points = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setInt(1, loyaltyPoints);
			stmt.setInt(2, this.memberId);

			PosSystem.getDatabase().executeQuery(stmt);
			
			this.loyaltyPoints = loyaltyPoints;
		} catch (Exception e) {
			System.err.println("Error while updating loyalty points");
		}
	}	
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		Connection con = PosSystem.getConnection();
		
		try {
			PreparedStatement stmt = null;
			
			String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
					       "SET password = ? " +
					       "WHERE id = ?";
			
	    	stmt = con.prepareStatement(query);
			stmt.setString(1, password);
			stmt.setInt(2, this.memberId);

			PosSystem.getDatabase().executeQuery(stmt);
			
			this.password = password;
		} catch (Exception e) {
			System.err.println("Sorry " + this.firstName + ", The system has failed to change your password.");
		}
	}
	
	public boolean getActiveStatus() {
		return this.activeStatus;
	}
	
	public void setActiveStatus(boolean active) {	
		try {
			if (active != this.activeStatus) {
				PreparedStatement stmt = null;
				Connection con = PosSystem.getConnection();
				
				String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
						       "SET active = ? " +
						       "WHERE id = ?";
				
		    	stmt = con.prepareStatement(query);
				stmt.setBoolean(1, active);
				stmt.setInt(2, this.memberId);
				
				PosSystem.getDatabase().executeQuery(stmt);
				
			}
		} catch (Exception e) {
			System.err.println("Failed to update active status");
		}
	}

	public static Member getMemberById(int id) {
		Member mem = null;
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.member WHERE id = " + id).executeQuery();
			tables.next();
			mem = new Member(tables.getInt("id"),tables.getDate("signupDate"),tables.getString("fname"),tables.getString("lname"),tables.getString("password"));
			mem.loyaltyPoints = tables.getInt("points");
			mem.activeStatus = tables.getBoolean("active");
		} catch (SQLException e) {
			return null;
		}
		
		return mem;
	}
}
