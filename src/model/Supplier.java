package model;
public class Supplier {
	
	private int supplierId;
	private String companyName;
	private String contactDetails;
	
	public Supplier(int supplierId, String companyName, String contactDetails) {
		this.supplierId = supplierId;
		this.companyName = companyName;
		this.contactDetails = contactDetails;
	}
	

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}
	
	
}
