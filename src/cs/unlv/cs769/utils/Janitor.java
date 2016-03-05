package cs.unlv.cs769.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import cs.unlv.cs769.components.CranDocument;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class has utility functions to remove stop
 * words and do stemming.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Janitor {

	List<String> _stop_words = null;
	Stemmer _stemmer = null;
	TimeLogger t = null;

	public Janitor(TimeLogger t) {
		this.t = t;
	}

	public void initialize() throws Exception {
		this._stemmer = new Stemmer();
		
		t.start("[Janitor] ReadingStopWords");
		this._stop_words = new ArrayList<String>(Utils.readStopWords(Constants.STOP_WORD_FILE));
		t.stop();
	}

	public void removeStopWords(List<CranDocument> documents) {

		String rawcontent = null;
		String[] lines = null;

		for (CranDocument d : documents) {
			rawcontent = d._rawcontent;
			lines = Pattern.compile("\n").split(rawcontent);

			for (String line : lines) {
				d._scrubbedcontent.append(removeStopWords(line) + "\n");
			}
		}
	}

	//REMINDER: any change to stop words logic will affect query handler also.
	public String removeStopWords(String s) {
		return removeStopWords(s, null);
	}
	
	public String removeStopWords(String s, List<String> skipList) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer t = new StringTokenizer(s);
		String token = null;
		while (t.hasMoreTokens()) {
			token = t.nextToken();
			
			if(skipList != null) {
				if (skipList.contains(token) || !isStopWord(token)) {
					sb.append(token + " ");
					continue;
				}
			}

			if (!isStopWord(token)) {
				sb.append(token + " ");
			}
		}
		return sb.toString();
	}

	public boolean isStopWord(String word) {
		return this._stop_words.contains(word);
	}

	public void stem(List<CranDocument> documents) throws Exception {
		String scrubbedContent = null;
		StringBuffer stemmed = null;
		String[] lines = null;

		for (CranDocument d : documents) {
			stemmed = new StringBuffer();

			scrubbedContent = d._scrubbedcontent.toString();

			lines = Pattern.compile("\n").split(scrubbedContent);
			for (String line : lines) {
				stemmed.append(stem(line) + "\n");
			}

			d._scrubbedcontent = stemmed;
		}

	}

	//REMINDER: any change to stemming logic will affect query handler also.
	public String stem(String line) throws Exception {
		return this._stemmer.stem(line);
	}
}
