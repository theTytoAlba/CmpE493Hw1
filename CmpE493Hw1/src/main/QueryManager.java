package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class QueryManager {
	/**
	 * Will be running until user types 0.
	 * Takes in queries and prints the answers.
	 */
	public static void startQueryEngine() {
		Scanner in = new Scanner(System.in);
		System.out.println("Search ready. Please enter your query.");
		System.out.println("To exit, you can type 0.");
		String query;
		int queryType;
		while (true) {
			try {
				queryType = in.nextInt();
			} catch (Exception e) {
				System.out.println("Please enter the type of the query first.");
				continue;
			}
			// Exit if user wants to exit.
			if (queryType == 0) {
				System.out.println("Thank you for using this engine. Goodbye!");
				in.close();
				return;
			}
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
	}

	private static void processProximityQuery(String query) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Takes the intersection of the pageID's of each word.
	 */
	private static void processConjunctiveQuery(String query) {
		// Remove and's first.
		query = query.replaceAll("\\bAND\\b", "");
		// Get the search words.
		ArrayList<String> words = StoryTokenizer.stem(StoryTokenizer.tokenizeString(query));
		// Say, we have the first word's occurences as the main one.
		int wordId = DictionaryBuilder.getDictionary().get(words.get(0));
		Set<Integer> occurences = IndexBuilder.indexes.get(wordId).keySet();
		// Intersect with other sets
		for (String word : words) {
			wordId = DictionaryBuilder.getDictionary().get(word);
			occurences.retainAll(IndexBuilder.indexes.get(wordId).keySet());
		}
		// Print found pages in increasing order.
		ArrayList<Integer> list = new ArrayList<>(occurences);
		Collections.sort(list);
		System.out.println(list.toString());
	}

	/**
	 * Takes in a query. Finds the pages where those words are in given order, next to each other.
	 */
	private static void processPhraseQuery(String query) {
		// Get the search words.
		ArrayList<String> words = StoryTokenizer.stem(StoryTokenizer.tokenizeString(query));
		// We get the first word's occurences as the main one.
		int wordId = DictionaryBuilder.getDictionary().get(words.get(0));
		HashMap<Integer, ArrayList<Integer>> occurences = IndexBuilder.indexes.get(wordId);
		words.remove(0);
		// Process the next words.
		for (String word : words) {
			wordId = DictionaryBuilder.getDictionary().get(word);
			HashMap<Integer, ArrayList<Integer>> occurencesNextWord = IndexBuilder.indexes.get(wordId);
			// This is to avoid concurrent access errors.
			ArrayList<Integer> currentPages = new ArrayList<>();
			for (int pageId : occurences.keySet()) {
				currentPages.add(pageId);
			}
			// Check if existing occurences are still valid and add valid ones to merged occurences.
			for (int pageId : currentPages) {
				// Discard page if next word does not appear in it.
				if (!occurencesNextWord.containsKey(pageId)) {
					occurences.remove(pageId);
					continue;
				}
				// If it contains try to match orders.
				ArrayList<Integer> matchingOrders = new ArrayList<>();
				for (int order : occurences.get(pageId)) {
					if (occurencesNextWord.get(pageId).contains(order+1)) {
						matchingOrders.add(order+1);
					}
				}
				// If there are no matchings, this page is no match, discard.
				if (matchingOrders.isEmpty()) {
					occurences.remove(pageId);
					continue;
				}
				// If there are matchings, this page is a match, just update the indexes for the next word.
				occurences.put(pageId, matchingOrders);
			}
		}
		// In the end, occurences should only contain pages which went through all steps.
		// Print found pages in increasing order.
		ArrayList<Integer> list = new ArrayList<>(occurences.keySet());
		Collections.sort(list);
		System.out.println(list.toString());
	}
}
