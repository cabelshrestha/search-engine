package cs.unlv.cs769.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Scratch {

	/*
	 * My implementation of AND according to notes.
	 */
	public Set<Integer> AND(Set<Integer> a, Set<Integer> b) {
		
		Set<Integer> result = new TreeSet<Integer>();
		List<Integer> a1 = new ArrayList<Integer>(a);
		List<Integer> b1 = new ArrayList<Integer>(b);
		
		Integer docId1, docId2;
		int ai = 0, bi = 0;
		while(true) {
			docId1 = a1.get(ai);
			docId2 = b1.get(bi);
			if(docId1.equals(docId2)) {
				result.add(docId1);
				ai++;bi++;
			} else if (docId1.compareTo(docId2) < 0) {
				ai++;
			} else {
				bi++;
			}
			
			if(ai==a1.size() || bi==b1.size())
				break;
		}
		
		return result;
	}
	
	/*
	 * My implementation of OR according to notes.
	 */
	public Set<Integer> OR(Set<Integer> a, Set<Integer> b) {
		TreeSet<Integer> result = new TreeSet<Integer>();
		for (Integer docId : a) {
			result.add(docId);
		}
		for (Integer docId : b) {
			result.add(docId);
		}
		return result;
	}
}
