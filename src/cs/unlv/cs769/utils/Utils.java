package cs.unlv.cs769.utils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Holds utility methods used throughtout the program.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Utils {

	public static Set<String> readStopWords(String filename) throws Exception {

		Set<String> wordset = new HashSet<>();

		Scanner scanner = new Scanner(new File(filename));
		while (scanner.hasNext()) {
			wordset.add(scanner.next().trim());
		}
		scanner.close();
		return wordset;
	}

	public static boolean isEmpty(String s) {
		return (s == null || s.length() == 0) ? true : false;
	}

	public static boolean isNotEmpty(String s) {
		return (s != null && s.length() > 0) ? true : false;
	}
	
	public static String printableSet(Set<Integer> result) {
		StringBuilder sb = new StringBuilder();
		
		if(result == null) 
			return sb.toString();

		int i = 0;
		for(Integer val : result) {
			sb.append(val + "\t");
			i++;
			if(i==10) {
				sb.append("\n");
				i=0;
			}
		}
		
		return sb.toString();
	}
	
	public static void printSet(Set<Integer> result) {
		System.out.println(printableSet(result));
	}
	
	public static String printableList(List<Integer> result) {
		StringBuilder sb = new StringBuilder();
		
		if(result == null) 
			return sb.toString();

		for(int j=0; j < result.size(); j++) {

			Integer val = result.get(j);
			sb.append(val);

			if (j < (result.size()-1))
				sb.append(", ");
		}
		return sb.toString();
	}

}
