package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.EmployeeType;

import controller.DataValidator;
import exception.CancelException;

public class CommandLine {
	
	public static void printList(ArrayList<String> list) {
		for (String item: list) {
			System.out.println(item);
		}
	}
	
	public static int getAnswerAsInt(String question) throws CancelException {
		int answer  = 0;
		System.out.print(question + " ");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String selection = null;
		
		try {
			selection = in.readLine();
			while(!selection.equalsIgnoreCase("cancel") && !DataValidator.validateInt(selection)) {
				System.out.print("Answer must be an integer: ");
				selection = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error while submitting an answer, please try again");
			answer = getAnswerAsInt(question);
		}

		if (selection.equalsIgnoreCase("cancel")) {
			throw new CancelException();
		} else {
			answer = Integer.parseInt(selection);
		}
		
		return answer;
	}
	
	public static String getAnswerAsString(String question, ArrayList<String> types) throws CancelException {
		// TODO - print off the types for the user to choose from
		System.out.println(question);
		int selection = getUserOption(types);
		return types.get(selection - 1);
	}
	
	public static String getAnswerAsString(String question) throws CancelException {
		System.out.print(question + " ");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String selection = null;
		
		try {
			selection = in.readLine();
			while(!validateString(selection) && !selection.equalsIgnoreCase("cancel")) {
				System.out.print("Answer must be a valid string: ");
				selection = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error while submitting an answer, please try again");
			selection = getAnswerAsString(question);
		}
		
		if (selection.equalsIgnoreCase("cancel")) {
			throw new CancelException();
		}
		
		return selection;
	}
	
	private static boolean validateString(String selection) {
		boolean isValid = true;
		
		
		return isValid;
	}

	public static int getUserOption(ArrayList<String> options) throws CancelException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		printOptions(options);
		System.out.print("Please choose an option: ");
		String selection = null;
		int option = 0;
		
		try {
			selection = in.readLine();
			while(!selection.equalsIgnoreCase("cancel") && !DataValidator.validateInt(selection,1,options.size())) {
				System.out.print("Option must be an integer between " + 1 + " and " + options.size() + ": ");
				selection = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error while choosing an option, please try again");
			option = getUserOption(options);
		}
		
		if (selection.equalsIgnoreCase("cancel")) {
			throw new CancelException();
		} else {
			option = Integer.parseInt(selection);
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
		// TODO - add cancel option
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean answer;
		String input;
		
		System.out.print(question + " ");
		
		try {
			input = in.readLine();
			while (!DataValidator.validateYesOrNo(input)) {
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
}
