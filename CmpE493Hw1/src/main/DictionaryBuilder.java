package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryBuilder {
	private static HashMap<String, Integer> dictionary = new HashMap<>();
	
	/**
	 * Updates current dictionary with new words from the given document.
	 */
	public static void updateDictionaryWithNewDocument(ArrayList<NewsStory> stories) {
		for (NewsStory story : stories) {
			// Add the tokens in the title
			for (String token : story.titleTokens) {
				if (!dictionary.containsKey(token)) {
					dictionary.put(token, dictionary.size());
				}
			}
			// Add the tokens in the body
			for (String token : story.bodyTokens) {
				if (!dictionary.containsKey(token)) {
					dictionary.put(token, dictionary.size());
				}
			}
		}
	}

	/**
	 * Writes the current dictionary hashmap into given file.
	 */
	public static void writeDictionaryToDocument(String fileName) {
		ObjectOutputStream obj;
		try {
			obj = new ObjectOutputStream(new FileOutputStream(fileName));
	        obj.writeObject(dictionary);
	        obj.close();
		} catch (Exception e) {
			System.out.println("There was a problem writing the dictionary object to file " + fileName);
			e.printStackTrace();
		} 
	}
	
	/**
	 * Reads given file as dictionary and stores in class variable.
	 */
	@SuppressWarnings("unchecked")
	public static void readDictionaryFromDocument(String fileName) throws Exception {
		try {
			ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
		    dictionary = (HashMap<String, Integer>)obj.readObject();
		    obj.close();
		} catch (Exception e) {
			throw new Exception("DictionaryException");
		}
	}
}
