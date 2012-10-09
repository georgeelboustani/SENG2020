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
		Connection con = PosSystem.getDatabase().getConnection();
		
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
		con.close();
	}
	
	public void addLoyaltyPoints(int points) {
		this.loyaltyPoints += points;
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

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}	
	
	public String getPassword() {
		return this.password;
	}
	
	public void setActiveStatus(boolean active) {	
		try {
			if (active != this.activeStatus) {
				PreparedStatement stmt = null;
				Connection con = PosSystem.getDatabase().getConnection();
				
				String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".member " +
						       "SET active = ?" +
						       "WHERE id = ?";
				
		    	stmt = con.prepareStatement(query);
				stmt.setBoolean(1, active);
				stmt.setInt(2, this.memberId);
				
				PosSystem.getDatabase().executeQuery(stmt);
				con.close();
			}
		} catch (Exception e) {
			System.err.println("Failed to update active status");
		}
	}

	public static Member getMemberById(int id) {
		Member mem = null;
		
		try {
			ResultSet tables = PosSystem.getDatabase().getConnection().prepareStatement("SELECT * FROM seng2020.member WHERE id = " + id).executeQuery();
			mem = new Member(tables.getInt("id"),tables.getDate("signupDate"),tables.getString("fname"),tables.getString("lname"),tables.getString("password"));
			mem.loyaltyPoints = tables.getInt("points");
		} catch (SQLException e) {
			return null;
		}
		
		return mem;
	}
}
