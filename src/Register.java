import java.util.Collection;


public class Register {
	private double balance;
	private Collection<LogEntry> logEntries;
	private Employee currentEmployee; //not currently in class diagram
	
	public Employee activeEmployee() {
		return currentEmployee;
	}
	
	public void addEntry(LogEntry entry) {
		logEntries.add(entry);
	}
	
	public void balanceTill() {
		
	}
	
	public void addMoney
}
