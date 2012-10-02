package controller;

import org.apache.commons.lang3.StringUtils;

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
	
	public static boolean validateInt(String string) {
		return StringUtils.isNumeric(string);
	}
	
	public static boolean validateInt(String input, int min, int max) {
		boolean isValid = validateInt(input);

		if (isValid) {
			int selection = Integer.parseInt(input);
			if (selection < min || selection > max) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
	public static boolean valdiateString(String string) {
		return StringUtils.isAlphanumeric(string) && StringUtils.isNotBlank(string);
	}
}
