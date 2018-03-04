package main;

import java.util.ArrayList;
import java.util.Scanner;

public class QueryManager {
	/**
	 * Will be running until user types 0.
	 * Takes in queries and prints the answers.
	 */
	public static void startQueryEngine() {
		Scanner in = new Scanner(System.in);
		System.out.println("Search ready. Please enter your query.");
		System.out.println("To exit, you can type 0.");
		String query = "";
		int queryType;
		while (!query.equals("0")) {
			queryType = in.nextInt();
			query = in.nextLine();
			// Handle query depending on it's type.
			switch (queryType) {
				case 1:
					processConjunctiveQuery(query);
					break;
				case 2:
					processPhraseQuery(query);
					break;
				case 3:
					processProximityQuery(query);
					break;
			}
		}
		System.out.println("Thank you for using this engine. Goodbye!");
		in.close();
	}

	private static void processProximityQuery(String query) {
		// TODO Auto-generated method stub
		
	}

	private static void processConjunctiveQuery(String query) {
		// TODO Auto-generated method stub
		
	}

	private static void processPhraseQuery(String query) {
		// TODO Auto-generated method stub
		
	}
}
