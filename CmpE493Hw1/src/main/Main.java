package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Irmak Kavasoglu
 * 2013400090
 */
public class Main {
	public static void main(String[] args) {
		prepareDictionaryAndIndexes();
		QueryManager.startQueryEngine();
	}
	
	/**
	 * Reads dictionary and indexes if they exist and if not, recreates.
	 */
	private static void prepareDictionaryAndIndexes() {
		try {
			System.out.println("Reading dictionary from file...");
			DictionaryBuilder.readDictionaryFromDocument(Constants.dictionaryLocation);
			System.out.println("Reading dictionary from file DONE.");
			System.out.println("Reading indexes from file...");
			IndexBuilder.readIndexesFromDocument(Constants.indexesLocation);
			System.out.println("Reading indexes from file DONE.");
		} catch (Exception e) {
			if (e.getMessage().equals("DictionaryException")) {
				System.out.println("Failed to read dictionary from file, recreating...");
			} else {
				System.out.println("Failed to read indexes from file, recreating...");
			}
			// Read the stories from documents.
			ArrayList<ArrayList<NewsStory>> documents = readStoriesFromDocuments();
			// Read the stop words.
			StoryTokenizer.setStopWords(readStopWords());
			// Tokenize the stories.
			documents = tokenizeStories(documents);
			// Create dictionary
			createDictionary(documents);
			// Create indexes
			createIndexes(documents, DictionaryBuilder.getDictionary());
		}
	}
	
	/**
	 * Creates a dictionary and writes it to file with words from given documents.
	 */
	private static void createDictionary(ArrayList<ArrayList<NewsStory>> documents) {
		System.out.println("Creating dictionary...");
		for (int i = 0; i < 22; i++) {
			printProgress("Processing document", i+1, 22);
			DictionaryBuilder.updateDictionaryWithNewDocument(documents.get(i));
		}
		System.out.println("Creating dictionary DONE.");
		System.out.println("Writing dictionary to file...");
		DictionaryBuilder.writeDictionaryToDocument(Constants.dictionaryLocation);
		System.out.println("Writing dictionary to file DONE.");
	}
	
	/**
	 * Create indexes of given files and dictionary and writes it to file.
	 */
	private static void createIndexes(ArrayList<ArrayList<NewsStory>> documents, HashMap<String, Integer> dictionary) {
		System.out.println("Creating indexes...");
		IndexBuilder.setDictionary(dictionary);
		for (int i = 0; i < 22; i++) {
			printProgress("Indexing document", i+1, 22);
			IndexBuilder.updateIndexesWithNewDocument(documents.get(i));
		}
		System.out.println("Creating indexes DONE.");
		System.out.println("Writing indexes to file...");
		IndexBuilder.writeIndexesToDocument(Constants.indexesLocation);
		System.out.println("Writing indexes to file DONE.");
	}

	/**
	 * Reads stop words from the location in Constants.
	 */
	private static ArrayList<String> readStopWords() {
		ArrayList<String> stopwords = new ArrayList<>();
		System.out.println("Reading stop words...");
		try (BufferedReader br = new BufferedReader(new FileReader(Constants.stopWordsLocation))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    		stopwords.add(line.trim());
		    }
		} catch (IOException e) {
			System.out.println("Error while reading stopwords form Dataset/stopwords.txt");
			e.printStackTrace();
		}
		System.out.println("Reading stop words DONE.");
		return stopwords;
	}
	
	/**
	 * Reads files from "Dataset/reut2-000.sgm" until "Dataset/reut2-021.sgm".
	 * Extracts stories with their title and bodies.
	 * Returns the news story arrays of every document in an array.
	 */
	private static ArrayList<ArrayList<NewsStory>> readStoriesFromDocuments() {
		ArrayList<ArrayList<NewsStory>> documents = new ArrayList<>();
		System.out.println("Reading documents...");
		// Read documents;
		for (int i = 0; i < 22; i++) {
			String fileName = "Dataset/reut2-0" + (i<10 ? "0" : "") + i + ".sgm";
			printProgress("Reading document", i+1, 22);
			documents.add(StoryExtractor.getStoriesFromDocument(fileName));
		}
		System.out.println("Reading documents DONE.");
		return documents;
	}

	/**
	 * Given an array containing the story arrays for each document,
	 * iterates the array and tokenizes and stems each story.
	 */
	private static ArrayList<ArrayList<NewsStory>> tokenizeStories(ArrayList<ArrayList<NewsStory>> documents) {
		ArrayList<ArrayList<NewsStory>> tokenizedDocuments = new ArrayList<>();
		System.out.println("Tokenizing documents...");
		// Tokenize documents;
		for (int i = 0; i < documents.size(); i++) {
			printProgress("Tokenizing document", i+1, 22);
			tokenizedDocuments.add(StoryTokenizer.tokenizeStories(documents.get(i)));
		}
		System.out.println("Tokenizing documents DONE.");
		return tokenizedDocuments;
	}

	/**
	 * Prints the pretext, prints a space, prints the progress as text like xx/yy,
	 * prints a semicolon and then prints the progress bar in form [#####----].
	 */
	private static void printProgress(String preText, int current, int total) {
		System.out.print(preText + " ");
		System.out.print((current<10 ? "0" : "") + current + "/" + total + ": [");
		for (int j = 0; j < current; j++) {
			System.out.print("#");
		}
		for (int j = current; j < total; j++) {
			System.out.print("-");
		}
		System.out.println("]");
	}
}
