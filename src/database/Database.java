package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;

public class Database {
	
	private static String server = "jdbc:mysql://localhost:3306/";
	private static String db = "seng2020";
	
	private Properties connectionProps;
	
	public Database(String user, String pass) {
		connectionProps = new Properties();
		connectionProps.put("user", "user");
		connectionProps.put("password", "pass");
	}
	
	/**
	 * Executes inserts, updates and delete statements
	 * 
	 * @param query - Prepare statements using the QueryBuilder class
	 * @throws SQLException
	 */
	public void executeQuery(PreparedStatement query) throws SQLException {
		Connection con = getConnection();
		con.setAutoCommit(false);
    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
    	
		try {
	    	query.executeUpdate();
	    	con.commit();
	    } catch (SQLException e) {
	    	con.rollback();
	    	printSQLException(e);
	    } finally {
	    	if (query != null) { 
	    		query.close(); 
	    	}
	    }
	}
	
	/**
	 *  Prints out sql select queries
	 *  
	 * @param query - Create the select query with the QueryBuilder class
	 * @throws SQLException 
	 */
	public void executeSelect(String query) throws SQLException {
		Connection con = getConnection();
		Statement stmt = null;
		
		try {
			stmt = (Statement) con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        
	        while (rs.next()) {
	        	// Get the columns from the result set
		        String product = rs.getString(1);
		        System.out.println(product);
	        }	      
	    } catch (SQLException e) {
	    	printSQLException(e);
	    } finally {
	      if (stmt != null) { 
	    	  stmt.close(); 
	      }
	    }
	}
	
	public Properties getProperties() {
		return connectionProps;
	}
	
	public String getDatabase() {
		return db;
	}
	
	public Connection getConnection(){
		Connection conn = null;
		
		try {
			conn = (Connection)DriverManager.getConnection(server,connectionProps);
			System.out.println("Connected to database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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

}
