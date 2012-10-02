package model;

import java.util.ArrayList;

public enum EmployeeType {
	STAFF,
	MANAGER,
	ADMIN;
	
	public static ArrayList<String> valuesToString() {
		ArrayList<String> values = new ArrayList<String>();
		
		for (EmployeeType type: values()) {
			values.add(type.toString());
		}
		
		return values;
	}
}
