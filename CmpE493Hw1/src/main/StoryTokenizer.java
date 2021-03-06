package main;

import java.util.ArrayList;

public class StoryTokenizer {
	static ArrayList<String> stopWords;
	/**
	 * Takes in a story array and returns the tokenized and stemmed version of it.
	 */
	public static ArrayList<NewsStory> tokenizeStories(ArrayList<NewsStory> stories) {
		ArrayList<NewsStory> tokenizedStories = new ArrayList<>();
		for (NewsStory story : stories) {
			story.titleTokens = stem(tokenizeString(story.title));
			story.bodyTokens = stem(tokenizeString(story.body));
			tokenizedStories.add(story);
		}
		return tokenizedStories;
	}
	
	/**
	 * Takes in a String array and returns the stemmed version of it.
	 */
	public static ArrayList<String> stem(ArrayList<String> tokens) {
		ArrayList<String> stemmedTokens = new ArrayList<>();
		PorterStemmer stemmer;
	    // Stem each word.
		for (String token : tokens) {
			stemmer = new PorterStemmer();
			stemmer.add(token.toCharArray(), token.length());
			stemmer.stem();
			stemmedTokens.add(stemmer.toString());
		}
		return stemmedTokens;
	}
	
	/**
	 * Given a text, does case folding, removes punctuation marks and new lines,
	 * removes integers and one letter words, then returns the remaining words as
	 * a String array.
	 */
	public static ArrayList<String> tokenizeString(String text) {
		// Make text lowercase.
		text = text.toLowerCase();
		// Remove all punctuation marks and new lines.
		text = text.replaceAll("\\.", " ");
		text = text.replaceAll("\\,", " ");
		text = text.replaceAll("\\'", " ");
		text = text.replaceAll("\\/", " ");
		text = text.replaceAll("\\-", " ");
		text = text.replaceAll("<", " ");
		text = text.replaceAll(">", " ");
		text = text.replaceAll("\n", " ");
		// Remove all stop words if they exist. They will not exist when processing queries.
		if (stopWords != null && !stopWords.isEmpty()) {
			for (String stopWord : stopWords) {
				text = text.replaceAll("\\b" + stopWord + "\\b", "");
			}			
		}
		// Tokenize by space.
		ArrayList<String> tokens = new ArrayList<>();
		for (String token : text.split(" ")) {
			// Only accept tokens which are at least 2 chars and not integers.
			if (!token.isEmpty() && token.length() > 1 && !isInteger(token)) {
				tokens.add(token.trim());
			}
		}
		return tokens;
	}
	
	/**
	 * Tries to cast given string to an integer.
	 * Returns true if it succeeds, false otherwise.
	 */
	private static boolean isInteger(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Sets the stopWords of this class.
	 */
	public static void setStopWords(ArrayList<String> stopWords) {
		StoryTokenizer.stopWords = stopWords;
	}
}
