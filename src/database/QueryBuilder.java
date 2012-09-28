package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class QueryBuilder {
	
	private static String testTable = "test";
	
	private Database db;
	
	public QueryBuilder(Database db) {
		this.db = db;
	}
	
	public PreparedStatement newInsertTestQuery(String productName) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + "." + testTable + "(`name`) VALUES (?)";
    	
    	stmt = con.prepareStatement(query);
		stmt.setString(1, productName);
		
		return stmt;
	}
	
	public String newSelectTestQuery() throws SQLException {
		return "SELECT * FROM " + db.getDatabase() + "." + testTable;
	}
}
