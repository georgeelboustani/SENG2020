import java.util.Date;


public class Member extends Customer {
	
	private Date signUp;
	private String firstName;
	private String lastName;
	private int loyaltyPoints;
	
	public Member(int id, Date signUp, String firstName, String lastName) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
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
