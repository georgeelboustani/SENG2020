package model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import database.Database;

public class Register {
	
	private int registerId;
	private double balance;
	private Employee currentEmployee; //not currently in class diagram
	
	public Register(int id, int balance, Integer employeeId) {
		this.registerId = id;
		this.balance = balance;
		if (employeeId == null) {
		    currentEmployee = null;
		} else {
		    currentEmployee = Employee.getEmployeeById(employeeId);
		}
	}
	
	public void persist() throws SQLException {
		PreparedStatement stmt = null;
		Database db = PosSystem.getDatabase();
		Connection con = PosSystem.getConnection();
		
		String query = "INSERT into " + db.getDbName() + ".register (`registerId`,`balance`,`currentEmployee`) " +
				"VALUES (?,?,?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.registerId);
		stmt.setDouble(2, this.balance);
		
		if (currentEmployee == null) {
			stmt.setNull(3, Types.INTEGER);
		} else {
			stmt.setInt(3, currentEmployee.getEmployeeId());
		}
	
		db.executeQuery(stmt);
	}
	
	public static Register getRegisterById(int registerId) {
	    Register register = null;
        
        try {
            ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT * FROM seng2020.register WHERE registerId = " + registerId).executeQuery();
            tables.next();
            register = new Register(tables.getInt("registerId"),tables.getInt("balance"),tables.getInt("currentEmployee"));
        } catch (SQLException e) {
            return null;
        }
        
        return register;
    }

	public Employee activeEmployee() {
		return currentEmployee;
	}

	public void balanceTill() {
		//TODO balance til
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getId(){
		return registerId;
	}

	public void setId(int id){
       registerId = id;
	}
}
