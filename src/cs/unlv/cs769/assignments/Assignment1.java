package cs.unlv.cs769.assignments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
 * This class builds the output file for Assignment 1.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Assignment1 extends BaseAssignment {

	private TimeLogger t = new TimeLogger("[A1] Building File");

	public Assignment1(int a, SearchEngine e) {
		super(a, e);
	}

	@Override
	public String execute() throws Exception {
		t.start();
		String outputFile = buildAssignmentFile(Constants.ASSIGNMENT1_FILE);
		t.stop();
		return outputFile;
	}

	@Override
	protected String buildAssignmentFile(String destFile) throws Exception {

		InvertedIndex invertedIndex = this._searchEngine._invertedIndex;
		List<InvertedIndex.InvertedIndexEntry> entries = null;

		BufferedWriter writer;
		File file = new File(destFile);
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));

		for (Map.Entry<Integer, List<InvertedIndex.InvertedIndexEntry>> entryPair : invertedIndex._entryMap.entrySet()) {

			entries = entryPair.getValue(); 

			writer.write("----------------------------------------------------\n");
			writer.write("DOCUMENT-ID: " + entryPair.getKey() + "\n");
			writer.write("NUMBER OF UNIQUE TERMS: " + entries.size() + "\n");
			writer.write("----------------------------------------------------\n");
			
			Collections.sort(entries);
			for(InvertedIndex.InvertedIndexEntry entry : entries) {
				writer.write(entry.serialize());
				writer.newLine();
			}
			writer.newLine();
			writer.newLine();
		}

		writer.flush();
		writer.close();
		
		return file.getAbsolutePath();

	}

}
