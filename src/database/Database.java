package database;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.PosSystem;

import com.mysql.jdbc.Connection;

public class Database {
	
	private static String localServer = "jdbc:mysql://localhost:3306/";
	private static String localDB = "seng2020";
	
	private String server;
	private String db;
	private Properties connectionProps;
	
	public Database(String user, String pass) {
		this(localServer, localDB, user, pass);
	}
	
	public Database(String host, String db, String user, String pass) {
		this.server = host;
		this.db = db;
		connectionProps = new Properties();
		connectionProps.put("user", "user");
		connectionProps.put("password", "pass");
	}
	

	//TODO: Fix clear data properly. and disable foreign keys then enable
	public void clearData() {
		
		try {
			ResultSet tables = PosSystem.getConnection().prepareStatement("SELECT TABLE_NAME FROM (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'seng2020') AS a").executeQuery();
			
			ArrayList<String> tableNames = new ArrayList<String>();
			while (tables.next()) {
				tableNames.add(tables.getString("TABLE_NAME"));
			}			
			
			int index = 0;
			while (tableNames.size() > 0) {
				try {
					PosSystem.getConnection().prepareStatement("DELETE FROM seng2020." + tableNames.get(index)).execute();
					tableNames.remove(index);
				} catch (SQLException e) {
					// Who cares
				}
				index++;
				
				if (index >= tableNames.size()) {
					index = 0;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes inserts, updates and delete statements
	 * 
	 * @param query - Prepare statements using the QueryBuilder class
	 * @throws SQLException
	 */
	public void executeQuery(PreparedStatement query) throws SQLException {
		Connection con = PosSystem.getConnection();
		con.setAutoCommit(false);
    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
    	
		try {
	    	query.executeUpdate();
	    	con.commit();
	    } catch (SQLException e) {
	    	con.rollback();
	    	printSQLException(e);
	    }
	}
	
	/**
	 *  Prints out sql select queries
	 *  
	 * @param query - Create the select query with the QueryBuilder class
	 * @throws SQLException 
	 */
	public void executeSelect(String query) throws SQLException {
		Connection con = PosSystem.getConnection();
		Statement stmt = null;
		
		try {
			stmt = (Statement) con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        
	        while (rs.next()) {
	        	// Get the columns from the result set
		        String product = rs.getString(1);
	        }	      
	    } catch (SQLException e) {
	    	printSQLException(e);
	    }
	}
	
	public Properties getProperties() {
		return connectionProps;
	}
	
	public String getDbName() {
		return db;
	}
	
	public Connection createConnection() throws SQLException{
		Connection conn = null;
		
		conn = (Connection)DriverManager.getConnection(server,connectionProps);
		System.out.println("Connected to database");
		
		return conn;
	}
	
	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
					e.printStackTrace(System.err);
					System.err.println("SQLState: " + ((SQLException)e).getSQLState());
					System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
					System.err.println("Message: " + e.getMessage());
					Throwable t = ex.getCause();
					while(t != null) {
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
				}
			}
		}
	  }
	
	public static boolean ignoreSQLException(String sqlState) {
	    if (sqlState == null) {
	    	System.out.println("The SQL state is not defined!");
	    	return false;
	    }
	    
	    // X0Y32: Jar file already exists in schema
	    if (sqlState.equalsIgnoreCase("X0Y32"))
	      return true;
	    
	    // 42Y55: Table already exists in schema
	    if (sqlState.equalsIgnoreCase("42Y55"))
	    	return true;
	    
	    return false;
	}

	public static Date getCurrentDate() {
		return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
	}

}
