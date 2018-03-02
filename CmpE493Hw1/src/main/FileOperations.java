package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperations {

	public static void parseDocumentToJSON(String fileName) {
		// Tokenize by tags and lines first.
		ArrayList<String> tagTokens = null;
		try {
			tagTokens = tokenizeByTagsAndStrings(fileName);
		} catch (IOException e) {
			System.out.println("Failed to tokenize document " + fileName + " by tags and strings.");
			e.printStackTrace();
			return;
		}
		// Organize tokens to merge non-token entries.
		tagTokens = organizeTagsAndStrings(tagTokens);
		// TODO: Construct the JSONObject.
	}
	
	/**
	 * Reads the file line by line and returns a String array.
	 * The returned array will have elements as follows:
	 * - <TAG> and </TAG> types.
	 * - Substrings of a line:
	 *   - Part up to first tag
	 *   - Part between two tags
	 *   - Part after last tag until the end of line.
	 */
	private static ArrayList<String> tokenizeByTagsAndStrings(String fileName) throws FileNotFoundException, IOException {
		ArrayList<String> tokens = new ArrayList<>();
		// Ready pattern to find tags.
		Pattern tagPattern = Pattern.compile("<(.*?)>");
		// Process document line by line.
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
		    while ((line = br.readLine()) != null) {
				// Get first match.
				Matcher m = tagPattern.matcher(line);
				while (m.find()) {
					// If there is text before match, add as another token.
					if (m.start() != 0) {
						tokens.add(line.substring(0, m.start()));
					}
					// Add the match.
					tokens.add(m.group());
					// Update the line.
					line = line.substring(m.end());
					m = tagPattern.matcher(line);
				}
				// Add the remaining text as token.
				if (!line.isEmpty()) {
					tokens.add(line);	
				}
		    }
		}
		return tokens;
	}
	
	/**
	 * Takes in an array list of strings with tags and non-tags.
	 * Merges every non-tag entry into one element.
	 * Preserves the order.
	 */
	private static ArrayList<String> organizeTagsAndStrings(ArrayList<String> tokens) {
		ArrayList<String> organizedTokens = new ArrayList<>();
		// Initial token should always be a tag (the DOCTYPE tag).
		organizedTokens.add(tokens.get(0));
		tokens.remove(0);
		boolean isPreviousTokenTag = true;
		// Process remaining tokens.
		for (String token : tokens) {
			if (isTag(token)) {
				// If it is a tag, add it directly
				organizedTokens.add(token);
				isPreviousTokenTag = true;
			} else if (isPreviousTokenTag) {
				// If previous token was a tag, add this one as the next token.
				organizedTokens.add(token);
				isPreviousTokenTag = false;
			} else {
				// If both previous and this tokens are not tags, merge them.
				String prev = organizedTokens.get(organizedTokens.size()-1);
				organizedTokens.remove(organizedTokens.size()-1);
				organizedTokens.add(prev + " " + token);
			}
		}
		return organizedTokens;
	}		
	
	/**
	 * Takes in a string.
	 * Returns true if it starts with < and ends with >.
	 * Returns false otherwise.
	 */
	private static boolean isTag(String token) {
		return token.charAt(0) == '<' && token.charAt(token.length()-1) == '>';
	}
}
