package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Irmak Kavasoglu
 * 2013400090
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO: Delete this temporary file operation and implement the project.
		try (BufferedReader br = new BufferedReader(new FileReader("Dataset/README.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
			    	System.out.println(line);
		    }
		}
	}

}
