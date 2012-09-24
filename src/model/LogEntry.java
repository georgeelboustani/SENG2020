package model;
import java.util.Date;


public class LogEntry {
	
	private Date date;
	private Employee empWorkingOnRegister;
	
	public LogEntry(Date date, Employee empWorkingOnRegister) {
		this.date = date;
		this.empWorkingOnRegister = empWorkingOnRegister;
	}
	
}
