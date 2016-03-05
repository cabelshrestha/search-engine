package cs.unlv.cs769.assignments;

import java.util.ArrayList;
import java.util.List;

import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.TimeLogger;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This is the main class for all executions.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Main {

	public static void main(String[] args) {

		List<String> output = new ArrayList<String>(); 
		String filename = null;
		BaseAssignment a;

		TimeLogger t = new TimeLogger("[Main] Program");
		t.start();
		try {
			
			args = new String[] {"a3"};
			if(args.length == 0 ) {
				Main.printUsage();
				return;
			}

			SearchEngine engine = new SearchEngine();
			
			for(int i=0; i<args.length; i++) {
				if (args.length != 0) {
					if (args[i].equals("a1")) {
						a = new Assignment1(1, engine);
						filename = (String) a.execute();
						output.add(filename);
					} 
					if (args[i].equals("a2")) {
						a = new Assignment2(2, engine);
						filename = (String) a.execute();
						output.add(filename);
					} 
					if (args[i].equals("a3")) {
						a = new Assignment3(3, engine);
						filename = (String) a.execute();
						output.add(filename);
					} 
					if (args[i].equals("a3.realtime")) {
						Assignment3 a3 = new Assignment3(3, engine);
						a3.executeUserQuery();
					}
					if (args[i].equals("a4")) {
						a = new Assignment4(4, engine);
						filename = (String) a.execute();
						output.add(filename);
					}
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		t.stop();

		for(String name : output)
			System.out.println("Output File Path: " + name );
		
	}
	
	public static void printUsage() {
		StringBuilder sb = new StringBuilder();
		sb.append("ERROR : missing parameters : give one or more parameters.\n");
		sb.append("----------------------------------------------------\n");
		sb.append("COMMAND: java -jar search.engine.jar <parameter/s>\n");
		sb.append("PARAMETERS:\n");
		sb.append("\ta1 - to build assignment 1 output file.\n");
		sb.append("\ta2 - to build assignment 2 output file.\n");
		sb.append("\ta3 - to build assignment 3 output file.\n");
		sb.append("\ta4 - to build assignment 4 output file.\n");
		sb.append("\ta3.realtime - to execute boolean queries inputted by the user.\n");
		sb.append("----------------------------------------------------\n");
		
		System.out.println(sb.toString());
	}

}
