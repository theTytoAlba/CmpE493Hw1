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
	/**
	 * Reads indexes object from file at given location.
	 */
	@SuppressWarnings("unchecked")
	public static void readIndexesFromDocument(String fileName) throws Exception {
		HashMap<Integer, HashMap<Integer, int[]>> optimizedIndexes = new HashMap<>();
		try {
			ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
			int size = obj.readInt();
			for (int wordId = 0; wordId < size; wordId++) {
				HashMap<Integer, int[]> pageIndexes = new HashMap<>();
				int pageId = obj.readInt();
				int[] pageOccurences = (int[]) obj.readObject();
				pageIndexes.put(pageId, pageOccurences);
				optimizedIndexes.put(wordId, pageIndexes);
			}
		    obj.close();
		} catch (Exception e) {
			throw new Exception("IndexesException");
		}		
		System.out.println("Preparing index object from read data...");
		// Cast to existing object.
		for (int wordId : optimizedIndexes.keySet()) {
			HashMap<Integer, ArrayList<Integer>> pageOccurences = new HashMap<>();
			for (int pageId : optimizedIndexes.get(wordId).keySet()) {
				int[] optimizedOccurences = optimizedIndexes.get(wordId).get(pageId);
				ArrayList<Integer> occurences = new ArrayList<>();
				for (int i = 0; i < optimizedOccurences.length; i++) {
					occurences.add(optimizedOccurences[i]);
				}
				pageOccurences.put(pageId, occurences);
			}
			indexes.put(wordId, pageOccurences);
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
		System.out.println("Preparing object to write.");
		HashMap<Integer, HashMap<Integer, int[]>> optimizedIndexes = new HashMap<>();
		for (int wordId : indexes.keySet()) {
			HashMap<Integer, int[]> pageOccurences = new HashMap<>();
			for (int pageId : indexes.get(wordId).keySet()) {
				ArrayList<Integer> occurences = indexes.get(wordId).get(pageId);
				int[] optimizedOccurences = new int[occurences.size()];
				for (int i = 0; i < occurences.size(); i++) {
					optimizedOccurences[i] = occurences.get(i);
				}
				pageOccurences.put(pageId, optimizedOccurences);
			}
			optimizedIndexes.put(wordId, pageOccurences);
		}
		System.out.println("Object ready. Writing...");
		ObjectOutputStream obj;
		try {
			obj = new ObjectOutputStream(new FileOutputStream(fileName));
			obj.writeInt(optimizedIndexes.size());
			for (int i = 0; i < optimizedIndexes.size(); i++) {
				for (int j : optimizedIndexes.get(i).keySet()) {
					obj.writeInt(j);
					obj.writeObject(optimizedIndexes.get(i).get(j));	
				}
			}
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
