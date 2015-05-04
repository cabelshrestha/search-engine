package cs.unlv.cs769.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Object Type: DOCUMENT SPECIFIC 
 * Description: Holds all term and frequency information of each document.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class InvertedIndex {

	public Map<Integer, List<InvertedIndexEntry>> _entryMap = null;

	public InvertedIndex() {
		this._entryMap = new HashMap<Integer, List<InvertedIndexEntry>>();
	}
	
	public void add(Integer docId, String term) {

		List<InvertedIndexEntry> entries = null;

		if (this._entryMap.containsKey(docId)) {
			entries = this._entryMap.get(docId);
		} else {
			entries = new ArrayList<InvertedIndexEntry>();
		}

		InvertedIndexEntry toCompareEntry = new InvertedIndexEntry(term);
		if (!entries.isEmpty() && entries.contains(toCompareEntry)) {
			entries.get(entries.indexOf(toCompareEntry)).incrementFrequency();
		} else {
			entries.add(new InvertedIndexEntry(term, 1));
		}
		
		this._entryMap.put(docId, entries);
	}
	
	public int getTermFrequency(Integer docId, String term) {
		List<InvertedIndexEntry> entries = this._entryMap.get(docId);
		int freq = 0;
		for(InvertedIndexEntry e : entries) {
			if (e._term.equals(term)) {
				freq = e._frequency;
				break;
			}
		}
		return freq;

	}
	
	public double getTF(Integer docId, String term) {
		List<InvertedIndexEntry> entries = this._entryMap.get(docId);
		double tf = 0d;
		for(InvertedIndexEntry e : entries) {
			if (e._term.equals(term)) {
				tf = e._TF;
				break;
			}
		}
		return tf;
	}


	public List<InvertedIndexEntry> getEntries(Integer docId) {
		return this._entryMap.get(docId);
	}
	
	public static class InvertedIndexEntry implements Comparable<InvertedIndexEntry>, Serializable {

		/**
		 * Generated ID.
		 */
		private static final long serialVersionUID = 6640241004743744911L;

		private static final String left_enclosure = "<";
		private static final String right_enclosure = ">";
		private static final String delim = ",";
		private double _TF = 0d;

		public String _term;
		public int _frequency = 0;
		
		public InvertedIndexEntry(String term) {
			this._term = term;
		}
		public InvertedIndexEntry(String term, int frequency) {
			this._term = term;
			this._frequency = frequency;
		}

		public String serialize() {
			StringBuilder sb = new StringBuilder();
			sb.append(left_enclosure);
			sb.append(this._term);
			sb.append(delim);
			sb.append(this._frequency);
			sb.append(right_enclosure);
			return sb.toString();
		}

		public static InvertedIndexEntry deserialize(String value) {

			value = value.replace(left_enclosure, "");
			value = value.replace(right_enclosure, "");
			String[] tokens = value.split(delim);

			if (tokens.length != 2)
				return null;

			String term = tokens[0];
			int frequency = Integer.valueOf(tokens[1]);

			return new InvertedIndexEntry(term, frequency);
		}
		
		public void incrementFrequency() {
			this._frequency++;
		}
		
		public void setTF(double tf) {
			this._TF = tf;
		}
		
		public double getTF() {
			return this._TF;
		}

		@Override
		public boolean equals(Object o) {
			boolean equality = false;
			if (o instanceof InvertedIndexEntry)
				equality = (this._term.equals(((InvertedIndexEntry) o)._term));
			return equality;
		}

		@Override
		public int hashCode() {
			int hash = 5;
			hash = 23 * hash + (this._term != null ? this._term.hashCode() : 0);
			return hash;
		}
		
		@Override
		public int compareTo(InvertedIndexEntry o) {
			return (this._term).compareTo(o._term);
		}

	}

}
