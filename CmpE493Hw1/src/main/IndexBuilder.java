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
	private static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> indexes = new HashMap<>();
	private static HashMap<String, Integer> dictionary = new HashMap<>();
	/**
	 * Reads indexes object from file at given location.
	 */
	public static void readIndexesFromDocument(String fileName) throws Exception {
		try {
			ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
			int size = obj.readInt();
			for (int wordId = 0; wordId < size; wordId++) {
				HashMap<Integer, ArrayList<Integer>> pageOccurences = new HashMap<>();
				int numOfPages = obj.readInt();
				for (int page = 0; page < numOfPages; page++) {
					int pageId = obj.readInt();
					int pageSize = obj.readInt();
					ArrayList<Integer> occurences = new ArrayList<>();
					for (int pageIndex = 0; pageIndex < pageSize; pageIndex++) {
						occurences.add(obj.readInt());
					}
					pageOccurences.put(pageId, occurences);	
				}
				indexes.put(wordId, pageOccurences);
			}
		    obj.close();
		} catch (Exception e) {
			throw new Exception("IndexesException");
		}
	}
	
	/**
	 * Adds new indexes and words to indexes object using the new stories.
	 */
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
			obj.writeInt(indexes.size());
			for (int wordId = 0; wordId < indexes.size(); wordId++) {
				HashMap<Integer, ArrayList<Integer>> pageOccurences = indexes.get(wordId);
				obj.writeInt(pageOccurences.size());
				for (int pageId : pageOccurences.keySet()) {
					obj.writeInt(pageId);
					ArrayList<Integer> pageIndexes = pageOccurences.get(pageId); 
					obj.writeInt(pageIndexes.size());
					for (int k = 0; k < pageIndexes.size(); k++) {
						obj.writeInt(pageIndexes.get(k));
					}
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
	
	public static HashMap<Integer, ArrayList<Integer>> getWordIndex(int wordId) {
		HashMap<Integer, ArrayList<Integer>> result = new HashMap<>();
		for (int key : indexes.get(wordId).keySet()) {
			ArrayList<Integer> keyIndexes = new ArrayList<>();
			for (int index : indexes.get(wordId).get(key)) {
				keyIndexes.add(index);
			}
			result.put(key, keyIndexes);
		}
		return result;
	}
}
