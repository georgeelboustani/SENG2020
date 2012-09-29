package model;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.Connection;

import database.Database;

public class Order {
	
	private int orderId;
	private Date orderDate;
	private Date orderArrival;
	private Date receivedDate;
	private int productTypeId;
	private int quantity;
	private int supplierId;
	
	public Order(int orderId,
			     Date orderArrival,
			     Date receivedDate, 
			     int productTypeId, 
			     int quantity,
			     int supplierId) {
		
		this.orderId = orderId;
		this.orderDate = Calendar.getInstance().getTime();
		this.orderArrival = orderArrival;
		this.receivedDate = receivedDate;
		this.productTypeId = productTypeId;
		this.quantity = quantity;
		this.supplierId = supplierId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getOrderArrival() {
		return orderArrival;
	}

	public void setOrderArrival(Date orderArrival) {
		this.orderArrival = orderArrival;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public int getProductType() {
		return productTypeId;
	}

	public void setProductType(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getSupplierId() {
		return supplierId;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + ".member (`id`,`dateOrdered`,`orderArrival`,`receivedDate`,`productTypeId`,`quantity`,`supplierId`) " +
				"VALUES (?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.orderId);
		stmt.setDate(2, db.convertDate(this.orderDate));
		stmt.setDate(3, db.convertDate(this.orderArrival));
		stmt.setDate(4, db.convertDate(this.receivedDate));
		stmt.setInt(5, this.productTypeId);
		stmt.setInt(6, this.quantity);
		stmt.setInt(7, this.supplierId);
		
		db.executeQuery(stmt);
	}
}
