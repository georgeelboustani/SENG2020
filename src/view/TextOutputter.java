package view;

import java.util.Scanner;

public class TextOutputter implements Outputter {
	public void printMainMenu() {
		System.out.println("System is now active");
		System.out.println();
		System.out.println("What do you want to do?");
		System.out.println("<1> Access as employee");
		System.out.println("<2> Access as customer");
		Scanner sc = new Scanner(System.in);
		int option = sc.nextInt();
		if (option == 1) {
			showEmployeeOptions();
		} else if (option == 2){
			showCustomerOptions();
		}
	}
	
	public void showEmployeeOptions() {
		
	}
	
	public void showCustomerOptions() {
		DONT FORGET TO PUSH!!! - chris
	}
}
