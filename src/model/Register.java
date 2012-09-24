package model;
import java.util.ArrayList;
import java.util.List;


public class Register {
	
	private int registerId;
	private double balance;
	private List<LogEntry> logEntries;
	private Employee currentEmployee; //not currently in class diagram
	
	public Register(int id) {
		this.registerId = id;
		logEntries = new ArrayList<LogEntry>();
	}
	
	public Employee activeEmployee() {
		return currentEmployee;
	}
	
	public void addEntry(LogEntry entry) {
		logEntries.add(entry);
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
