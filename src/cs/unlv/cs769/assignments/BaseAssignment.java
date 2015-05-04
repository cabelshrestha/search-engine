package cs.unlv.cs769.assignments;

import cs.unlv.cs769.engine.SearchEngine;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Base class for Assignments.
 * 
 * @Author Cabel Dhoj Shrestha
 */
abstract public class BaseAssignment {
	
	int _assignmentNumber = 0;
	SearchEngine _searchEngine = null;
	
	public BaseAssignment(int a, SearchEngine e) {
		this._assignmentNumber = a;
		this._searchEngine = e;
	}

	public abstract Object execute() throws Exception;
	protected abstract String buildAssignmentFile(String destFile) throws Exception;
}
