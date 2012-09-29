package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandLine {
	
	public static int getUserOption(ArrayList<String> options) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		printOptions(options);
		System.out.print("Please choose an option: ");
		String selection = null;
		int option = 0;
		
		try {
			selection = in.readLine();
			while(!validateInt(selection,1,options.size())) {
				System.out.print("Option must be an integer between " + 1 + " and " + options.size() + ": ");
				selection = in.readLine();
			}
			
			option = Integer.parseInt(selection);
		} catch (IOException e) {
			System.out.println("Error while choosing an option, please try again");
			option = getUserOption(options);
		}
		
		return option;
	}
	
	private static void printOptions(ArrayList<String> questions) {
		for (int i = 1; i <= questions.size(); i++) {
			System.out.println(i + ") " + questions.get(i - 1));
		}
	}
	
	/**
	 * @return true if yes, false if no.
	 */
	public static boolean getYesOrNo(String question) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean answer;
		String input;
		
		System.out.print(question + " ");
		
		try {
			input = in.readLine();
			while (!validateYesOrNo(input)) {
				System.out.print("Please answer yes or no: ");
				input = in.readLine();
			}
			
			if ("y".equalsIgnoreCase(String.valueOf(input.charAt(0)))) {
				answer = true;
			} else {
				answer = false;
			}
		} catch (IOException e) {
			System.out.println("Error while asking yes or no, try again");
			answer = getYesOrNo(question);
		}
		
		return answer;
	}
	
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
	
	private static boolean validateInt(String input, int min, int max) {
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
