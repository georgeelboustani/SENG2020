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
	
	public Member(int id, Date signUp, String firstName, String lastName, String password) {
		this.memberId = id;
		this.signUp = signUp;
		this.firstName = firstName;
		this.lastName = lastName;
		this.loyaltyPoints = 0;
		this.password = password;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".member (`id`,`fname`,`lname`,`password`,`points`,`signupDate`) " +
				"VALUES (?,?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.memberId);
		stmt.setString(2, this.firstName);
		stmt.setString(3, this.lastName);
		stmt.setString(4, this.password);
		stmt.setInt(5, this.loyaltyPoints);
		stmt.setDate(6, this.signUp);
		
		db.executeQuery(stmt);
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

	public static Member getMemberById(Database db, int id) {
		Member mem = null;
		
		try {
			ResultSet tables = db.getConnection().prepareStatement("SELECT * FROM seng2020.Member WHERE id = " + id).executeQuery();
			mem = new Member(tables.getInt("id"),tables.getDate("signupDate"),tables.getString("fname"),tables.getString("lname"),tables.getString("password"));
			mem.loyaltyPoints = tables.getInt("points");
		} catch (SQLException e) {
			return null;
		}
		
		return mem;
	}
}
