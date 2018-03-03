package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Irmak Kavasoglu
 * 2013400090
 */
public class Main {
	public static void main(String[] args) {
		// Read the stories from documents.
		ArrayList<ArrayList<NewsStory>> documents = readStoriesFromDocuments();
		// Read the stop words.
		StoryTokenizer.setStopWords(readStopWords());
		// Tokenize the stories.
		documents = tokenizeStories(documents);
		//printAllDocuments(documents);
	}
	
	/**
	 * Reads stop words from "Dataset/stopwords.txt".
	 */
	private static ArrayList<String> readStopWords() {
		ArrayList<String> stopwords = new ArrayList<>();
		System.out.println("Reading stop words...");
		try (BufferedReader br = new BufferedReader(new FileReader("Dataset/stopwords.txt"))) {
			String line;
		    while ((line = br.readLine()) != null) {
		    		stopwords.add(line.trim());
		    }
		} catch (IOException e) {
			System.out.println("Error while reading stopwords form Dataset/stopwords.txt");
			e.printStackTrace();
		}
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
			System.out.println("Reading document " + (i+1) + "/" + 22);
			documents.add(StoryExtractor.getStoriesFromDocument(fileName));
		}
		return documents;
	}
	
	/**
	 * Given an array containing the story arrays for each document,
	 * iterates the array and tokenizes each story.
	 */
	private static ArrayList<ArrayList<NewsStory>> tokenizeStories(ArrayList<ArrayList<NewsStory>> documents) {
		ArrayList<ArrayList<NewsStory>> tokenizedDocuments = new ArrayList<>();
		System.out.println("Tokenizing documents...");
		// Tokenize documents;
		for (int i = 0; i < documents.size(); i++) {
			System.out.println("Tokenizing document " + (i+1) + "/" + documents.size());
			tokenizedDocuments.add(StoryTokenizer.tokenizeStories(documents.get(i)));
		}
		return tokenizedDocuments;
	}
	
	/**
	 * Prints all stories with their titles and bodies.
	 * Puts ==== barrier between stories.
	 * Puts ---- barrier between title and body
	 * Prints the document and story number for each story.
	 */
	private static void printAllDocuments(ArrayList<ArrayList<NewsStory>> documents) {
		for (int i = 0; i < documents.size(); i++) {
			for (int j = 0 ; j < documents.get(i).size(); j++) {
				System.out.println("=======================");
				System.out.println("doc " + i + " story " + j);
				System.out.println(documents.get(i).get(j).title);
				System.out.println("-----------------------");
				System.out.println(documents.get(i).get(j).body);
			}
		}
	}

}
