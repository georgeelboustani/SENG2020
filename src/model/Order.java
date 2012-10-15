package model;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
		this.orderDate = Database.getCurrentDate();
		this.orderArrival = orderArrival;
		this.receivedDate = receivedDate;
		this.productTypeId = productTypeId;
		this.quantity = quantity;
		this.supplierId = supplierId;
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".order (`id`,`dateOrdered`,`orderArrival`,`receivedDate`,`productTypeId`,`quantity`,`supplierId`) " +
				"VALUES (?,?,?,?,?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.orderId);
		stmt.setDate(2, this.orderDate);
		stmt.setDate(3, this.orderArrival);
		stmt.setDate(4, this.receivedDate);
		stmt.setInt(5, this.productTypeId);
		stmt.setInt(6, this.quantity);
		stmt.setInt(7, this.supplierId);
		
		db.executeQuery(stmt);
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
        Connection con = PosSystem.getConnection();
        
        try {
            PreparedStatement stmt = null;
            
            String query = "UPDATE " + PosSystem.getDatabase().getDbName() + ".order " +
                           "SET receivedDate = ? " +
                           "WHERE id = ?";
            
            stmt = con.prepareStatement(query);
            stmt.setDate(1, receivedDate);
            stmt.setInt(2, this.orderId);

            PosSystem.getDatabase().executeQuery(stmt);
            
            this.receivedDate = receivedDate;
        } catch (Exception e) {
            Database.printStackTrace(e);
            System.err.println("Failed to set type");
        }
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
	
	public static ArrayList<Integer> getAllOrders(ProductType type) {
        ArrayList<Integer> orders = new ArrayList<Integer>();
        Connection con = PosSystem.getConnection();
        
        try {
            String query = "SELECT id FROM seng2020.order WHERE productTypeId = " + type.getTypeId();
            
            PreparedStatement stmt = con.prepareStatement(query);

            ResultSet tables = stmt.executeQuery();
            while (tables.next()) {
                Order order = getOrderById(tables.getInt(1));
                if (order.receivedDate == null) {
                    orders.add(order.getOrderId());
                }
            }
            
            if (orders.size() == 0) {
                return null;
            }
        } catch (SQLException e) {
            Database.printStackTrace(e);
            return null;
        }
        
        return orders;
    }
	
	public static Order getOrderById(int id) {
	    Order order = null;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.order WHERE id = " + id).executeQuery();
            tables.next();
            order = new Order(id, tables.getDate(3), tables.getDate(4), tables.getInt(5), tables.getInt(6), tables.getInt(7));
            order.orderDate = tables.getDate(2);
        } catch (Exception e) {
            return null;
        }
        
        return order;
	}
}
