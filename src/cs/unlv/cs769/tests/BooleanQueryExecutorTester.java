package cs.unlv.cs769.tests;

import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.handlers.BooleanQueryExecutor;
import cs.unlv.cs769.handlers.BooleanQueryLexer;
import cs.unlv.cs769.utils.TimeLogger;
import cs.unlv.cs769.utils.Utils;

public class BooleanQueryExecutorTester extends BaseTester {

	private static String query1 = "vary";
	private static String query2 = "vary AND user";
	private static String query3 = "panama OR NOT user"; //TODO: Ask Dr. Taghva about this.
	private static String query4 = "panama OR NOT user AND vary";
	private static String query5 = "panama OR NOT ( user AND vary )";
	private static String query6 = "panama OR NOT ( is AND the )";
	private static String query7 = "( maddening OR crowd ) AND ( ignoble OR strife ) AND ( killed OR slain )";
	
	/*
	 * scrubbed 1:vari 
	 * scrubbed 2:vari and user 
	 * scrubbed 3:panama or not user 
	 * scrubbed 4:panama or not user and vari 
	 * scrubbed 5:panama or not ( user and vari ) 
	 * scrubbed 6:panama or not ( and ) 
	 */
	
	private BooleanQueryExecutor executor = null;

	public BooleanQueryExecutorTester(SearchEngine engine) {
		super(engine, "QueryExecutorTester");
	}

	@Override
	public void run() {
		try {
			executor = new BooleanQueryExecutor(this._engine);
			lexerTests();
			executorTests();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void executorTests() throws Exception {
		executor.process("consolid or const");
		executor.process("consolid");
		executor.process("( simplify and considerable )");
		executor.process("panama OR NOT user"); //check!! panama not in dictionary??
		executor.process("panama OR NOT user AND vary");
		executor.process("( user AND vary )"); //check!! returns empty set??
		executor.process("panama OR NOT ( user AND vary )");
		executor.process("panama OR NOT ( is AND the )");
		executor.process("( maddening OR crowd ) AND ( ignoble OR strife )");
		executor.process("consolid and ( user AND vary ) or ( panama OR NOT ( simplify and considerable ) )");
		
	}
	
	private void lexerTests() {
		System.out.println("---Good Tests---");
		runGoodTests();
		
		System.out.println("---Bad Tests---");
		runBadTests();
		
	}

	private void runGoodTests() {
		runLexer("simplify");
		runLexer("consolid or const");
		runLexer("( simplify and considerable )");
		runLexer("simplify and ( hello or dead ) and orange");
		runLexer("vary");
		runLexer("vary AND user");
		runLexer("panama OR NOT user");//TODO: Ask Dr.Taghva
		runLexer("panama OR NOT user AND vary");
		runLexer("panama OR NOT ( user AND vary )");
		runLexer("panama OR NOT ( is AND the )"); //TODO: Ask Dr.Taghva
		runLexer("( maddening OR crowd ) AND ( ignoble OR strife ) AND ( killed OR slain )");
	}


	private void runBadTests() {
		runLexer("and simplify or that");
		//       "^--------------------"

		runLexer("simplify or and that");
		//       "------------^--------"

		runLexer("simplify or that and");
		//       "-------------------^"
		
		runLexer("consolid or ( apple and orange");
		//Missing parenthesis

		runLexer("consolid or apple and orange and ");
		//       "--------------------------------^"

		runLexer("consolid or apple and ( orange and not )"); 
		//       "--------------------------------------^--"

		runLexer("consolid or apple and orange and");
		//       "--------------------------------^"

		runLexer("consolid or apple and orange and not");//** showing no error
		//       "-----------------------------------^"     

		runLexer("consolid or apple and orange and )");
		//Missing parenthesis

		runLexer("consolid or apple and orange and not )");
		//       "-------------------------------------^"

		runLexer("consolid apple and orange");
		//       "---------^---------------");

		runLexer("consolid ( apple and orange");
		//      "----------^------------------"
		
		runLexer("simplify and ( brain or ( hello and my");
		//missing parenthesis
		
		runLexer("hills and not mountains or not and himalayas");
		//       "-------------------------------^-------------"

		runLexer("hills mountains or himalays");
		//       "------^--------------------"
		
		runLexer("hills or mountains himalays");
		//       "-------------------^-------"
		
		runLexer("hills and ( this and not that that )");
		//       "------------------------------^-------"

		runLexer("hills and ( this and not that that ) )");
		//       "-----------------------------^--------"

		runLexer("hills and ( this and not that ) )");
		//missing parenthesis
	}

	private void runLexer(String query) {
		System.out.println("[QUERY]:'" + query + "'");
		
		BooleanQueryLexer lexer = new BooleanQueryLexer();
		String error = lexer.validate(query);
		if(Utils.isNotEmpty(error))
			System.out.println(error);
		else
			System.out.println("Passed!");
	}

	private void runBadTests1() {
		runLexer("consolid or apple and orange and not");//** showing no error
		//       "-----------------------------------^"     
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

		BaseTester o = new BooleanQueryExecutorTester(engine);
		o.execute();
		
		t.stop();
	}

}