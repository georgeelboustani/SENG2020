package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;

public class DatabaseTest {
	
	public static final String tableName = "product";
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		Database db = new Database("user","pass");
		QueryBuilder qb = new QueryBuilder(db);
		
		db.executeQuery(qb.newInsertTestQuery("cheese"));
		db.executeSelect(qb.newSelectTestQuery());
	}

}
