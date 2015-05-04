package cs.unlv.cs769.utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Utility methods to measure execution time.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class TimeLogger {

	long _startTime = 0L;
	long _endTime = 0L;
	String _process;

	public TimeLogger() {
	};

	public TimeLogger(String process) {
		this._process = process;
	}
	
	public void start(String process) {
		this._process = process;
		start();
	}

	public void start() {
		_startTime = System.currentTimeMillis();
	}

	public void stop() {
		_endTime = System.currentTimeMillis();
		double totalExecutionTime = (_endTime - _startTime) / 1000.0;
		System.err.println($(_process) + "total execution time: " + totalExecutionTime + "s");
	}
	
	private String $(String process) {
		StringBuilder sb = new StringBuilder();
		int l = process.length();
		int j = 47 - l;
		sb.append(process);
		for (int i = 0; i < j; i++) {
			sb.append(".");
		}
		return sb.toString();
	}

}
