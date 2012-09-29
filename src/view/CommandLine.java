package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CommandLine {
	
	public static int getUserOption(ArrayList<String> questions) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		printOptions(questions);
		System.out.print("Please choose an option: ");
		String selection = null;
		int option = 0;
		
		try {
			selection = in.readLine();
			while(!validateInt(selection,1,questions.size())) {
				System.out.println("Option must be an integer between " + 1 + " and " + questions.size());
				selection = in.readLine();
			}
			
			option = Integer.parseInt(selection);
		} catch (IOException e) {
			System.out.println("Error while choosing an option, please try again");
			option = getUserOption(questions);
		}
		
		return option;
	}
	
	private static void printOptions(ArrayList<String> questions) {
		for (int i = 1; i <= questions.size(); i++) {
			System.out.println(i + ") " + questions.get(i - 1));
		}
	}
	
	/**
	 * @return true is yes, false if no.
	 */
	public static boolean getYesOrNo() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		boolean answer;
		String input;
		
		try {
			while () {
				
			}
			
			if ("y".equals(input)) {
				answer = true;
			} else {
				answer = false;
			}
		} catch (IOException e) {
			System.out.println("Error while asking yes or no, try again");
			answer = getYesOrNo();
		}
		
		return answer;
	}
	
	private static boolean validateInt(String input, int min, int max) {
		boolean isValid = true;
		
		for (int i = 0; i < input.length() && isValid; i++) {
			if (!Character.isDigit(input.charAt(i))) {
				isValid = false;
			}
		}
		
		if (isValid) {
			int selection = Integer.parseInt(input);
			if (selection >= min && selection <= max) {
				isValid = false;
			}
		}
		
		return isValid;
	}
}
