package cs.unlv.cs769.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Object Type: TERM SPECIFIC
 * Description: Dictionary of unique terms and their related information.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Dictionary implements Serializable {

	/**
	 * Generated Serial Id
	 */
	private static final long serialVersionUID = 227592817640605685L;

	public TreeMap<String, DictionaryEntry> _entryMap = null;
	public List<Integer> _universe; 

	public Dictionary() {
		this._entryMap = new TreeMap<String, DictionaryEntry>();
		initializeUniverse();
	}
	
	private void initializeUniverse() {
		this._universe = new ArrayList<Integer>();
		for(int i = 1; i <= 1400; i++) {
			this._universe.add(i);
		}
	}

	public Integer add(int docId, String term) {
		DictionaryEntry e = null;
		if (this._entryMap.containsKey(term)) {
			e = this._entryMap.get(term);
			e.updateDf(docId);
		} else {
			e = new DictionaryEntry(term, docId);
			this._entryMap.put(term, e);
		}

		return e._id;
	}

	public DictionaryEntry getEntry(String term) {
		return this._entryMap.get(term);
	}

	@Override
	public String toString() {
		DictionaryEntry e = null;
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, DictionaryEntry> pair : this._entryMap.entrySet()) {
			e = pair.getValue();
			sb.append(e._id + " : " + pair.getKey() + " : " + e._df + " : [");
			int ctr = 0;
			for (Integer docId : e._inDocuments) {
				ctr++;
				sb.append(docId);
				if (ctr != pair.getValue()._inDocuments.size()) {
					sb.append(", ");
				}
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

	public static class DictionaryEntry {

		private static int currentIdValue = 1;
		private double _IDF = 0d;
		
		public Integer _id;
		public int _df = 0;
		public TreeSet<Integer> _inDocuments;

		public DictionaryEntry(String term, Integer docId) {
			this._id = Integer.valueOf(currentIdValue++);
			this._inDocuments = new TreeSet<Integer>();

			updateDf(docId);
		}

		public void updateDf(Integer docId) {
			this._inDocuments.add(docId);
			this._df = this._inDocuments.size();
		}
		
		public void setIDF(double idf) {
			this._IDF = idf;
		}
		
		public double getIDF() {
			return this._IDF;
		}

	}
}
