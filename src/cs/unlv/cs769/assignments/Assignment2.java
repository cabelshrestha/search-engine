package cs.unlv.cs769.assignments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs.unlv.cs769.components.Dictionary;
import cs.unlv.cs769.components.InvertedIndex;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.TimeLogger;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class builds the output file for Assignment 2.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Assignment2 extends BaseAssignment {
	
	private TimeLogger t = new TimeLogger("[A2] Building File");
	
	public Assignment2(int a, SearchEngine e) {
		super(a, e);
	}
	
	@Override
	public String execute() throws Exception {
		t.start();
		String outputFile = buildAssignmentFile(Constants.ASSIGNMENT2_FILE);
		t.stop();

		return outputFile;
	}
	
	@Override
	protected String buildAssignmentFile(String destFile) throws Exception {

		InvertedIndex invertedIndex = this._searchEngine._invertedIndex;
		Dictionary dictionary = this._searchEngine._dictionary;

		Dictionary.DictionaryEntry dictEntry = null;
		String term = null;

		BufferedWriter writer;
		File file = new File(destFile);
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));

		writer.write("INV_FILE_HASH = {\n");

		int ctr1 = 0;
		for (Map.Entry<String, Dictionary.DictionaryEntry> dictPair : dictionary._entryMap.entrySet()) {

			ctr1++;

			term = dictPair.getKey();
			dictEntry = dictPair.getValue(); 

			List<Integer> docIds = new ArrayList<Integer>(dictEntry._inDocuments);
			int ctr2 = 0;

			writer.write("'" + term + "'=>[" + dictEntry._df + ", [");
			for(Integer dId : docIds) {
				ctr2++;
				writer.write("[" + dId + ",");
				writer.write(invertedIndex.getTermFrequency(dId, term) + "]");
				if(ctr2 != docIds.size())
					writer.write(", ");
			}
			writer.write("]]");
			if(ctr1 != dictionary._entryMap.size())
				writer.write(",");
			writer.newLine();
		}

		writer.write("}");

		writer.flush();
		writer.close();
		
		return file.getAbsolutePath();

	}
}
