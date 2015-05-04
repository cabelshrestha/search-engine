package cs.unlv.cs769.tests;

import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.TimeLogger;

public abstract class BaseTester implements Runnable {
	
	SearchEngine _engine;
	String _testName;
	TimeLogger _t;
	
	public BaseTester(SearchEngine e, String name) {
		this._engine = e;
		this._testName = name;
		this._t = new TimeLogger(name);
	}
	
	protected void execute() {
		this._t.start();
		run();
		this._t.stop();
	}

}
