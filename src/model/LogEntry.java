package model;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.jdbc.Connection;

import database.Database;


public class LogEntry {
	
	private int logId;
	private Date date;
	private int currentEmployeeId;
	private String logDescription;
	private int registerId;
	
	public LogEntry(int id, Date date, int currentEmployeeId, String description, int registerId) {
		this.logId = id;
		this.date = date;
		this.currentEmployeeId = currentEmployeeId;
		this.logDescription = description;
		this.registerId = registerId;
	}
	
	public void persist(Database db) throws SQLException {
		PreparedStatement stmt = null;
		Connection con = db.getConnection();
		
		String query = "INSERT into " + db.getDatabase() + ".logentry (`logId`,`date`,`employeeId`,`description`,`registerId`) " +
				"VALUES (?,?,?,?,?)";
		
    	stmt = con.prepareStatement(query);
		stmt.setInt(1, this.logId);
		stmt.setDate(2, db.convertDate(this.date));
		stmt.setInt(3, this.currentEmployeeId);
		stmt.setString(4, this.logDescription);
		stmt.setInt(5, this.registerId);
		db.executeQuery(stmt);

	}
	
}
