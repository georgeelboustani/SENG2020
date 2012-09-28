package model;
import java.util.Date;


public class LogEntry {
	
	private int logId;
	private Date date;
	private Employee empWorkingOnRegister;
	private String logDescription;
	
	public LogEntry(int id, Date date, Employee empWorkingOnRegister, String description) {
		this.logId = id;
		this.date = date;
		this.empWorkingOnRegister = empWorkingOnRegister;
		this.logDescription = description;
	}
	
}
