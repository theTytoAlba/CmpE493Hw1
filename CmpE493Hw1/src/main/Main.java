package main;

/**
 * Irmak Kavasoglu
 * 2013400090
 */
public class Main {
	public static void main(String[] args) {
		// TODO: Generalize the parsing for all documents.
		String fileName = "Dataset/reut2-000.sgm";
		FileOperations.parseDocumentToJSON(fileName);
	}

}
