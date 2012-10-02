package controller;

public class DataValidator {
	public static boolean validateYesOrNo(String string) {
		if (string != null && string.length() > 0) {
			if ("y".equalsIgnoreCase(string) || "n".equalsIgnoreCase(string)) {
				return true;
			}
			
			if ("yes".equalsIgnoreCase(string) || "no".equalsIgnoreCase(string)) {
				return true;
			}
		}
		
		return false;		
	}
	
	public static boolean validateInt(String input) {
		boolean isValid = true;
		
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {	
			isValid = false;
		}
		
		return isValid;
	}
	
	public static boolean validateInt(String input, int min, int max) {
		boolean isValid = true;
		
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {	
			isValid = false;
		}

		if (isValid) {
			int selection = Integer.parseInt(input);
			if (selection < min || selection > max) {
				isValid = false;
			}
		}
		
		return isValid;
	}
}
