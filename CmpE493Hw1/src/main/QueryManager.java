package main;

import java.util.ArrayList;
import java.util.Scanner;

public class QueryManager {
	private enum QueryType {PHRASE, CONJUNCTIVE, PROXIMITY}
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

	/**
	 * Calls the related query processor depending on query type.
	 */
	private static void answerQuery(String query) {
		// Handle query depending on it's type.
		switch (getQueryType(query)) {
			case PHRASE:
				processPhraseQuery(query);
				break;
			case CONJUNCTIVE:
				processConjunctiveQuery(query);
				break;
			case PROXIMITY:
				processProximityQuery(query);
				break;
		}
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

	/**
	 * Returns Conjunctive if query contains AND.
	 * Returns Proximity if query contains /.
	 * Returns Phrase otherwise.
	 */
	private static QueryType getQueryType(String query) {
		if (query.contains("AND"))
			return QueryType.CONJUNCTIVE;
		if (query.contains("/"))
			return QueryType.PROXIMITY;
		return QueryType.PHRASE;
	}
}
