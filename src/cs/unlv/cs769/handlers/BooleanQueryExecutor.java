package cs.unlv.cs769.handlers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import cs.unlv.cs769.engine.BooleanEngine;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.Operator;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class is executes boolean queries. Validates, cleans,
 * consumes based on priority and generates set of doc ids as
 * result.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class BooleanQueryExecutor {

	private SearchEngine _searchEngine;
	private BooleanEngine _bEngine;

	public BooleanQueryExecutor(SearchEngine e) {
		this._searchEngine = e;
		this._bEngine = new BooleanEngine(this._searchEngine._dictionary);
	}

	public Set<Integer> process(String query) throws Exception {

		/*
		 * Validate query.
		 */
		BooleanQueryLexer lexer = new BooleanQueryLexer();
		String error = lexer.validate(query);
		if (Utils.isNotEmpty(error)) {
			System.out.println(error);
			return null;
		}

		Set<Integer> result = null;

		/*
		 * Scrub
		 */
		query = scrub(query);

		String postfixQuery = "";
		try {
			/*
			 * Convert infix to postfix
			 */
			postfixQuery = convertInfixToPostfix(query);

			/*
			 * Execute query
			 */
			result = executePostfixQuery(postfixQuery);

		} catch (Exception e) {
			/*
			 * After scrubbing, so query terms are removed like articles etc.
			 * e.g.panama or is => panama or  
			 */
			System.out.println("Cannot process query:" + postfixQuery);
			return null;
		}

		return result;
	}

	public Set<Integer> executePostfixQuery(String postfixQuery) throws Exception {

		Stack<Object> stack = new Stack<Object>();

		StringTokenizer tokenizer = new StringTokenizer(postfixQuery);

		while (tokenizer.hasMoreTokens()) {

			String token = tokenizer.nextToken();

			if (Operator.isNotOp(token)) {
				stack.push(token);
			} else if (token.equals(Operator.NOT)) {
				Object operand1 = stack.pop();
				stack.push(this._bEngine.consumePostFix(operand1, null, (String) token));
			} else {
				Object operand1 = stack.pop();
				Object operand2 = stack.pop();
				stack.push(this._bEngine.consumePostFix(operand1, operand2, (String) token));
			}
		}

		if (!stack.isEmpty()) {
			if (stack.peek() instanceof String) {
				return this._bEngine.consumePostFix(stack.pop(), null, null);
			} else if (stack.peek() instanceof Set<?>) {
				return (Set<Integer>) stack.pop();
			}
		}

		return null;
	}

	public String convertInfixToPostfix(String query) throws Exception {

		Stack<String> op_stack = new Stack<String>();

		StringTokenizer tokenizer = new StringTokenizer(query);
		StringBuffer buffer = new StringBuffer();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();

			if (Operator.isNotOp(token)) {
				buffer.append(token + " ");
			} else {
				if (op_stack.isEmpty() || op_stack.peek().equals(Operator.LPAREN)) {
					op_stack.push(token);
				} else if (token.equals(Operator.LPAREN)) {
					op_stack.push(token);
				} else if (token.equals(Operator.RPAREN)) {
					while (!op_stack.isEmpty() && !op_stack.peek().equals(Operator.LPAREN)) {
						buffer.append(op_stack.pop() + " ");
					}
					op_stack.pop();//get rid of LPAREN
				} else if (Operator.prec(token) > Operator.prec(op_stack.peek())) {
					op_stack.push(token);
				} else if (Operator.prec(token) == Operator.prec(op_stack.peek())) { //using left to right association
					buffer.append(op_stack.pop() + " ");
					op_stack.push(token);
				} else if (Operator.prec(token) < Operator.prec(op_stack.peek())) {
					while (!op_stack.isEmpty()
							&& Operator.prec(token) < Operator.prec(op_stack.peek())) {
						buffer.append(op_stack.pop() + " ");
					}
					op_stack.push(token);
				}
			}

		}

		while (!op_stack.isEmpty())
			buffer.append(op_stack.pop() + " ");

		System.out.println("postfix:: " + buffer.toString());
		return buffer.toString();
	}

	public String scrub(String query) throws Exception {
		query = this._searchEngine._janitor.removeStopWords(query,Arrays.asList("and", "or", "not"));
		query = this._searchEngine._janitor.stem(query);
		return query;
	}

	public static void main(String[] args) {

		try {
			//some tests
			SearchEngine engine = new SearchEngine();
			BooleanQueryExecutor exe = new BooleanQueryExecutor(engine);

			//print results
			Set<Integer> result = exe.process("consolid or const");
			System.out.println(Utils.printableSet(result));

			exe.process("consolid");
			exe.process("( simplify and considerable )");
			exe.process("panama OR NOT user");
			exe.process("panama OR NOT user AND vary");
			exe.process("( user AND vary )");
			exe.process("panama OR NOT ( user AND vary )");
			exe.process("panama OR NOT ( is AND the )");
			exe.process("( maddening OR crowd ) AND ( ignoble OR strife )");
			exe.process("consolid and ( user AND vary ) or ( panama OR NOT ( simplify and considerable ) )");
			exe.process("vari and not user");
			exe.process("vari and user");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
