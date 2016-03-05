package cs.unlv.cs769.assignments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.handlers.BooleanQueryExecutor;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.TimeLogger;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class builds the output file for Assignment 3.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Assignment3 extends BaseAssignment {
	
	private List<String> _queries = null;
 
	private BooleanQueryExecutor _executor = null;
	private TimeLogger t = new TimeLogger("[A3] Building File");	

	public Assignment3(int a, SearchEngine e) {

		super(a, e);

		this._executor = new BooleanQueryExecutor(this._searchEngine);

		this._queries = new ArrayList<String>();
//		this._queries.add("vary");
//		this._queries.add("vary AND user");
//		this._queries.add("panama OR NOT user");
//		this._queries.add("panama OR NOT user AND vary"); //no postfix result
//		this._queries.add("panama OR NOT ( user AND vary )"); //no postfix result
		this._queries.add("panama OR NOT ( is AND the )"); //postfix expression creation error
		// panama is the AND not or
		//TODO apparently this turns to: panama OR NOT ( AND ) 
		//so gives an error.
	}

	@Override
	public String execute() throws Exception {
		t.start();
		String outputFile = buildAssignmentFile(Constants.ASSIGNMENT3_FILE);
		t.stop();

		return outputFile;

	}
	
	@Override
	protected String buildAssignmentFile(String destFile) throws Exception {
		BufferedWriter writer;
		File file = new File(destFile);
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));

		for (String query : this._queries) {
			writer.write("[QUERY]:" + query + "\n");
			writer.write("RESULT DOCUMENT IDs:\n");
			writer.write(Utils.printableSet(this._executor.process(query)));
			writer.newLine();
			writer.newLine();
		}

		writer.flush();
		writer.close();
		
		return file.getAbsolutePath();
	}
	
	public void executeUserQuery() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String action = "continue";

		while (!(action.equals("quit"))) {

			System.out.println("\nType your query and press return key (or <quit> to exit): ");
			System.out.print(">");
			String input = in.readLine();

			if (!input.equalsIgnoreCase("quit")) {
				System.out.println("RESULT DOCUMENT IDs:\n");
				Utils.printSet(this._executor.process(input.trim()));
			} else {
				action = input;
			}
		}
	}
}
