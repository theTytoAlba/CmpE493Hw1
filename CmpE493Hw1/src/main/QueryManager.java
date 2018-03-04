package main;

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
		while (!query.equals("0")) {
			query = in.nextLine();
			answerQuery(query);
		}
		System.out.println("Thank you for using this engine. Goodbye!");
		in.close();
	}

	private static void answerQuery(String query) {
		// TODO handle query.
	}
}
