package cs.unlv.cs769.tests;

import cs.unlv.cs769.engine.BooleanEngine;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.TimeLogger;

public class BooleanEngineTester extends BaseTester {


	private static String query1 = "vary";
	private static String query2 = "vary AND user";
	private static String query3 = "panama OR NOT user"; //TODO: Ask Dr. Taghva about this.
	private static String query4 = "panama OR NOT user AND vary";
	private static String query5 = "panama OR NOT ( user AND vary )";
	private static String query6 = "panama OR NOT ( is AND the )";
	private static String query7 = "panama AND ( hell AND no ) OR vary AND ( simpligy and consider )";
	
	/*
	 * Algorithm for creating full_queue parser
	 * -maintain PriorityQueue for parsed query and a DQueue(LIFO) as operator holder.
	 * 1. parse the query
	 * 2. Look at 1st elm, if Term, look and next, if Operator & not equal to 'LPAREN' add 1st elm. Hold 2nd.
	 * 3. Look at 3rd elm, if Term, add 2nd elm, hold 3rd elm.
	 * 					   if Operator, and not 'LPAREN', it should be 'NOT', hold it together with 2nd and move to next elm.
	 * 					   								  if not 'NOT' throw exception as invalid query
	 * 					   if Next elm is term, add the 2nd elm
	 */
	/*
	 * scrubbed 1:vari 
	 * scrubbed 2:vari and user 
	 * scrubbed 3:panama or not user 
	 * scrubbed 4:panama or not user and vari 
	 * scrubbed 5:panama or not ( user and vari ) 
	 * scrubbed 6:panama or not ( and ) 
	 */
	
	private BooleanEngine _bEngine;
	private TimeLogger _t;

	public BooleanEngineTester(SearchEngine engine) {
		super(engine, "BooleanEngineTester");
		this._bEngine = new BooleanEngine(this._engine._dictionary);
		this._t = new TimeLogger();
	}

	@Override
	public void run() {
		try {

			this._t.start("AND");
			System.out.println(_bEngine.AND("simplifi", "consider", false));
			this._t.stop();
			
			this._t.start("OR");
			System.out.println(_bEngine.OR("consolid", "const", false));
			this._t.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		TimeLogger t = new TimeLogger("Main");
		t.start();
		SearchEngine engine = null;
		try {
			engine = new SearchEngine();
			engine.run();
		} catch(Exception e) {
			e.printStackTrace();
		}

		BaseTester o = new BooleanEngineTester(engine);
		o.execute();
		
		t.stop();
	}



}
