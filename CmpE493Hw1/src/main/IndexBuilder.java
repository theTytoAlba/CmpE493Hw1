package main;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexBuilder {
	static HashMap<Integer, ArrayList<Integer>> indexes = new HashMap<>();
	public static void readIndexesFromDocument(String fileName) throws Exception {
		try {
			ObjectInputStream obj = new ObjectInputStream(new FileInputStream(fileName));
			indexes = (HashMap<Integer, ArrayList<Integer>>)obj.readObject();
		    obj.close();
		} catch (Exception e) {
			throw new Exception("IndexesException");
		}		
	}

}
