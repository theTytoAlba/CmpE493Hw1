package main;

import java.util.ArrayList;

/**
 * Irmak Kavasoglu
 * 2013400090
 */
public class Main {
	public static void main(String[] args) {
		// Read the stories from documents.
		ArrayList<ArrayList<NewsStory>> documents = readStoriesFromDocuments();
		printAllDocuments(documents);
	}
	
	private static ArrayList<ArrayList<NewsStory>> readStoriesFromDocuments() {
		ArrayList<ArrayList<NewsStory>> documents = new ArrayList<>();
		System.out.println("Reading documents...");
		// Read documents;
		for (int i = 0; i < 22; i++) {
			String fileName = "Dataset/reut2-0" + (i<10 ? "0" : "") + i + ".sgm";
			documents.add(StoryExtractor.getStoriesFromDocument(fileName));
			System.out.println("Document " + (i+1) + "/" + 22);
		}
		return documents;
	}
	
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
