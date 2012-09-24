package model;
import java.util.Date;

public class Order {
	private int orderId;
	private Date orderDate;
	private Date orderArrival;
	private Date receivedDate;
	private int quantity;
	
	public Order(int orderId, int quantity) {
		this.orderId = orderId;
		this.quantity = quantity;
	}
}
