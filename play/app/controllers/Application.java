package controllers;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import models.Task;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	static Form<Task> taskForm = form(Task.class);

	public static Result index() {
		return redirect(routes.Application.tasks());
	}

	public static Result tasks() {
		List<String> toPrint = new ArrayList<String>();
		try {
			toPrint = getNames();
		} catch (Exception e) {
			toPrint.add("nothing was added");
		}

		return ok(views.html.index.render(toPrint));
		
		
//		try {
//			toPrint = getNames();
//		} catch (Exception e) {
//			toPrint.add("nothing was added");
//		}
//		

	}
	
	public static List<String> getNames() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<String> list = new ArrayList<String>();
		
		Properties properties = new Properties();
		properties.put("user", "seng2020_daniel");
		properties.put("password", "pass1");
		// properties.put("characterEncoding", "ISO-8859-1");
		// properties.put("useUnicode", "true");
		String url = "jdbc:mysql://seng2020group7.heliohost.org:3306/seng2020_database";
		

		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection c = (Connection) DriverManager.getConnection(url, properties);
		Statement st = c.createStatement();
		// ResultSet employee = c.prepareStatement("SELECT TABLE_NAME FROM " +
		// "								(SELECT * FROM INFORMATION_SCHEMA.TABLES " +
		// "								WHERE TABLE_SCHEMA = 'seng2020') AS a").executeQuery();

		String sql = "SELECT * FROM `employee`";
		ResultSet rs = st.executeQuery(sql);

		try {
			while (rs.next()) {
//				int id = rs.getInt(1);
				String userName = rs.getString(2);
//				String userName = ""+id;
				// Timestamp timeReg = rs.getTimestamp(5);
				// ... do something with these variables ...
				list.add(userName);
				System.out.println(userName);
			}
		} finally {
			rs.close();
		}
		return list;
	}
	

}
