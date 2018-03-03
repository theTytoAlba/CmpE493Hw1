package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexBuilder {
	/**
	 * Indexes are kept in form:
	 * <stem id, <document id, <index list>>.
	 */
	static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> indexes = new HashMap<>();
	private static HashMap<String, Integer> dictionary = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static void readIndexesFromDocument(String fileName) throws Exception {
		try {
			ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
			indexes = (HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>)obj.readObject();
		    obj.close();
		} catch (Exception e) {
			throw new Exception("IndexesException");
		}		
	}
	public static void updateIndexesWithNewDocument(ArrayList<NewsStory> stories) {
		for (NewsStory story : stories) {
			// Index the title.
			for (int i = 0; i < story.titleTokens.size(); i++) {
				// If word does not exist in indexes, create it.
				int wordId = dictionary.get(story.titleTokens.get(i));
				if (	!indexes.containsKey(wordId)) {
					indexes.put(wordId, new HashMap<>());
				}
				// Get current word's entry.
				HashMap<Integer, ArrayList<Integer>> wordIndex = indexes.get(wordId);
				// If this file is not in entry, add it.
				if (!wordIndex.containsKey(story.storyID)) {
					wordIndex.put(story.storyID, new ArrayList<Integer>());
				}
				// Get this file's array from index.
				ArrayList<Integer> storyOccurences = wordIndex.get(story.storyID);
				// Add new occurence.
				storyOccurences.add(i);
				// Update indexes object.
				wordIndex.put(story.storyID, storyOccurences);
				indexes.put(wordId, wordIndex);
			}
			
			// Index the body.
			for (int i = 0; i < story.bodyTokens.size(); i++) {
				// If word does not exist in indexes, create it.
				int wordId = dictionary.get(story.bodyTokens.get(i));
				if (	!indexes.containsKey(wordId)) {
					indexes.put(wordId, new HashMap<>());
				}
				// Get current word's entry.
				HashMap<Integer, ArrayList<Integer>> wordIndex = indexes.get(wordId);
				// If this file is not in entry, add it.
				if (!wordIndex.containsKey(story.storyID)) {
					wordIndex.put(story.storyID, new ArrayList<Integer>());
				}
				// Get this file's array from index.
				ArrayList<Integer> storyOccurences = wordIndex.get(story.storyID);
				// Add new occurence.
				storyOccurences.add(i + story.titleTokens.size());
				// Update indexes object.
				wordIndex.put(story.storyID, storyOccurences);
				indexes.put(wordId, wordIndex);
			}
		}
	}

	/**
	 * Writes the current index hashmap into given file.
	 */
	public static void writeIndexesToDocument(String fileName) {
		ObjectOutputStream obj;
		try {
			obj = new ObjectOutputStream(new FileOutputStream(fileName));
	        obj.writeObject(indexes);
	        obj.close();
		} catch (Exception e) {
			System.out.println("There was a problem writing the indexes object to file " + fileName);
			e.printStackTrace();
		} 
	}
	
	/**
	 * Sets the current dictionary.
	 */
	public static void setDictionary(HashMap<String, Integer> dictionary) {
		IndexBuilder.dictionary = dictionary;
	}
}
