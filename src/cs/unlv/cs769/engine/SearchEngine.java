package cs.unlv.cs769.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cs.unlv.cs769.components.CranDocument;
import cs.unlv.cs769.components.Dictionary;
import cs.unlv.cs769.components.InvertedIndex;
import cs.unlv.cs769.handlers.DocumentBuilder;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.Janitor;
import cs.unlv.cs769.utils.TimeLogger;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Implementation of the engine backbone.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class SearchEngine {

	private TimeLogger t = null;
	public List<CranDocument> _documents;
	public InvertedIndex _invertedIndex;
	public Dictionary _dictionary;
	public Janitor _janitor;

	boolean isTest = false;
	
	public SearchEngine() throws Exception {
		run();
	}

	private void initialize() throws Exception {
		this._documents = new ArrayList<CranDocument>();
		this._invertedIndex = new InvertedIndex();
		this._dictionary = new Dictionary();

		this._janitor = new Janitor(t);
		this._janitor.initialize();
		
	}

	public void run() throws Exception {
		
		t = new TimeLogger();

		/*
		 * Initializing the engine.
		 */
		initialize();

		/*
		 * Build original document map.
		 */
		t.start("[S.Engine] BuildingDocumentList");

		buildDocumentList(Constants.CRAN_1400_FILE);

		t.stop();

		/*
		 * Removing stop words and stemming.
		 */
		t.start("[S.Engine] Scrubbing");

		scrubDocuments();

		t.stop();

		/*
		 * Build term frequency file.
		 */
		t.start("[S.Engine] BuildDictionaryAndFrequencyTable");

		buildDictionaryAndFrequencyTable();

		t.stop();
		
	}

	public void buildDocumentList(String origFile) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(origFile);
		this._documents = builder.build();
	}

	protected void scrubDocuments() throws Exception {
		this._janitor.removeStopWords(this._documents);
		this._janitor.stem(this._documents);
	}

	protected void buildDictionaryAndFrequencyTable() throws Exception {

		for (CranDocument document : this._documents) {

			String[] lines = document._scrubbedcontent.toString().split("\n");

			for (int k = 0; k < lines.length; k++) {

				String line = lines[k];
				String token = null;
				StringTokenizer t = new StringTokenizer(line, "[ \n\t\r.,;:!?/(){}]'");

				while (t.hasMoreTokens()) {
					token = t.nextToken();
					if (token.contains("-")) {
						String[] hyphenatedParts = token.split("-");
						for (String part : hyphenatedParts) {
							addToDictionaryAndInvIndex(document._id, part);
						}
					}
					addToDictionaryAndInvIndex(document._id, token);
				}

			}
			
			if(isTest)
				break;

		}
		
	}

	protected void addToDictionaryAndInvIndex(Integer docId, String term) {
		this._dictionary.add(docId, term);
		this._invertedIndex.add(docId, term);
	}
}
