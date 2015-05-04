package cs.unlv.cs769.engine;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cs.unlv.cs769.components.Dictionary;
import cs.unlv.cs769.components.InvertedIndex;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.TimeLogger;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class build Vector Space Model using
 * Inverted Index and Dictionary of terms.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class VectorSpaceModel {

	private SearchEngine _searchEngine;
	private HashMap<Integer, Double> _documentWeightMap;

	public VectorSpaceModel(SearchEngine e) {
		this._searchEngine = e;
		this._documentWeightMap = new HashMap<Integer, Double>();
		build();
	}
	
	public void build() {
		
		TimeLogger t = new TimeLogger();
	
		/*
		 * Computing TF for each term in a document.
		 */
		t.start("[V.Model] UpdateInvertedIndexWithTF");

		try {
			updateInvertedIndexWithTF();
		} catch (Exception e) {
			e.printStackTrace();
		}

		t.stop();
		
		/*
		 * Computing IDF for each term in the dictionary.
		 */
		t.start("[V.Model] UpdateDictionaryWithTF");

		try {
			updateDictionaryWithIDF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		t.stop();

	}

	private void updateInvertedIndexWithTF() throws Exception {
		double tf = 0d;
		double documentWeight = 0d;

		InvertedIndex invertedIndex = this._searchEngine._invertedIndex;
		List<InvertedIndex.InvertedIndexEntry> entries = null;
		for (Map.Entry<Integer, List<InvertedIndex.InvertedIndexEntry>> entryPair : invertedIndex._entryMap.entrySet()) {

			double sumOfSquareTF = 0d;
			
			entries = entryPair.getValue(); 
			for(InvertedIndex.InvertedIndexEntry entry : entries) {
				tf = computeTF(entry._frequency); 
				entry.setTF(tf);
				
				sumOfSquareTF += tf * tf;
			}
	
			documentWeight = Math.sqrt(sumOfSquareTF);

			this._documentWeightMap.put(entryPair.getKey(), documentWeight);
			
		}
	}

	private void updateDictionaryWithIDF() throws Exception {
		double idf = 0d;
		Dictionary.DictionaryEntry dictionaryEntry = null;
		Dictionary dictionary = this._searchEngine._dictionary;

		for (Map.Entry<String, Dictionary.DictionaryEntry> dictPair : dictionary._entryMap.entrySet()) {
			dictionaryEntry = dictPair.getValue();
			idf = computeIDF(dictionary._universe.size(), dictionaryEntry._df);
			dictionaryEntry.setIDF(idf);
		}
	}
	
	private double computeTF(int frequency) throws Exception {
		/* 
		 * Formula: TF=1+loge(fdt) 
		 * where, fdt=the frequency of t in document d. 
		 */
		return (1 + Math.log(frequency)); 
	}
	
	private double computeIDF(int N, int df) throws Exception {
		/*
		 * Formula: IDF=loge (1+N/df) 
		 * where, N=total documents
		 *        df=number of documents containing the term t
		 */
		return (Math.log(1 + (N/df)));
	}
	
	public List<Integer> processQuery(List<String> query) throws Exception {
		
		double queryWeight = computeQueryWeight(query);
		List<DocumentCosine> docCosines = new ArrayList<DocumentCosine>();
		
		for (Map.Entry<Integer, Double> entryPair : this._documentWeightMap.entrySet()) {

			Integer docId = entryPair.getKey();
			Double documentWeight = entryPair.getValue();
			Double tf = 0d;
			Double sumTfIdf = 0d;
			
			for(String term : query) {
				
				tf = this._searchEngine._invertedIndex.getTF(docId, term);

				if(tf.equals(0d))
					continue;

				sumTfIdf += tf * queryWeight;
			}
			
			if (!documentWeight.equals(0d)) {
				Double docCos = sumTfIdf / (documentWeight * queryWeight);
				
				if(!docCos.equals(0d)) {
					docCosines.add(new DocumentCosine(docId, docCos));
				}
			}
		}
		
		Collections.sort(docCosines, Collections.reverseOrder());
		
		List<Integer> result = new ArrayList<Integer>();
		for(DocumentCosine dc : docCosines) {
			result.add(dc._id);
		}

		return result;
	}

	public double computeQueryWeight(List<String> query) throws Exception {
		
		Dictionary.DictionaryEntry dictEntry = null;
		double idf = 0d; 
		double sumOfSquareIDF = 0d;

		for(String term : query) {

			if(Utils.isNotEmpty(term)) {

				dictEntry = this._searchEngine._dictionary.getEntry(term);

				if(dictEntry != null) {
					idf = dictEntry.getIDF();
					sumOfSquareIDF += idf * idf;
				}

			}
		}
		
		return Math.sqrt(sumOfSquareIDF);
	}
	
	private class DocumentCosine implements Comparable<DocumentCosine>{
		private Integer _id;
		private Double _cosVal;
		private Double _precision;
		private Double _recall;
		
		public DocumentCosine(Integer id, double cosVal) {
			this._id = id;
			this._cosVal = cosVal;
		}
		
		@Override
		public int compareTo(DocumentCosine o) {
			return (this._cosVal).compareTo(o._cosVal);
		}
	}
	
	public class Result {
		private Integer qryId;
		private List<Integer> _qryResult;
		private double _precision;
		private double _recall;
		
		public Result() {
			this._qryResult = new ArrayList<Integer>();
			this._precision = 0d;
			this._recall = 0d;
		}

		public Integer getQryId() {
			return qryId;
		}

		public void setQryId(Integer qryId) {
			this.qryId = qryId;
		}

		public double getPrecision() {
			return _precision;
		}

		public void setPrecision(double _precision) {
			this._precision = _precision;
		}

		public double getRecall() {
			return _recall;
		}

		public void setRecall(double _recall) {
			this._recall = _recall;
		}

		public void setQryResult(List<Integer> qryResult) {
			this._qryResult = qryResult;
		}
		
		public List<Integer> getQryResult() {
			return _qryResult;
		}
		
		
	}
	

	
}
