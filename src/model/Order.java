package model;
import java.util.Date;

public class Order {
	private int orderId;
	private Date orderDate;
	private Date orderArrival;
	private Date receivedDate;
	private ProductType productType;
	private int quantity;
	
	public Order(int orderId, 
			     Date orderDate, 
			     Date orderArrival,
			     Date receivedDate, 
			     ProductType productType, 
			     int quantity) {
		
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderArrival = orderArrival;
		this.receivedDate = receivedDate;
		this.productType = productType;
		this.quantity = quantity;
	}
	
	
	
}
