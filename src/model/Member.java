package model;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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
		
		String query = "INSERT into " + db.getDatabase() + ".member (`id`,`fname`,`lname`,`password`,`points`,`signupDate`) " +
				"VALUES (?,?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.memberId);
		stmt.setString(2, this.firstName);
		stmt.setString(3, this.lastName);
		stmt.setString(4, this.password);
		stmt.setInt(5, this.loyaltyPoints);
		stmt.setDate(6, db.convertDate(this.signUp));
		
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
}
