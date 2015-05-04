package cs.unlv.cs769.handlers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

import cs.unlv.cs769.engine.BooleanEngine;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.Utils;

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

		/*
		 * Scrub and build query list.
		 */
		Queue<Object> origQuery = cleanAndBuildQuery(query);

		/*
		 * Execute query
		 */
		Set<Integer> result = executeQuery(origQuery);
		
		return result;
	}

	

	private Set<Integer> executeQuery(Queue<Object> origQuery) throws Exception {
		Set<Integer> result = null;
		Queue<Object> activeQuery = new LinkedList<Object>();
		Queue<Object> consumeQuery = new LinkedList<Object>();

		boolean consume = false;
		boolean consumed = false;
		boolean inParen = false;

		while (!origQuery.isEmpty()) {
			
			Object elm = origQuery.poll();

			if (consumed) {
				activeQuery.add(elm);
			} else if (BooleanEngine.LPAREN.equals(elm)) {
				activeQuery.addAll(consumeQuery);
				consumeQuery.clear();
				consumeQuery.add(elm);
				inParen = true;
			} else {

				consumeQuery.add(elm);

				if (inParen) {
					if(BooleanEngine.RPAREN.equals(elm)) {
						inParen = false;
						consume = true;
					}
				} else {
					if( consumeQuery.contains(BooleanEngine.NOT)) {
						if ( consumeQuery.size() == 4)
							consume = true;
					} else { 
						if(consumeQuery.size() == 3) {
							consume = true;
						} else if (origQuery.peek() == null) {
							consume = true;
						}	
					}
				}

			}

			if (consume) {
				activeQuery.add(consume(consumeQuery));

				consumeQuery.clear();
				consume = false;
				consumed = true;
			}
		}
		
		if(activeQuery.size() > 1) {
			result = executeQuery(activeQuery);
		} else {
			result = (Set<Integer>)activeQuery.poll();
		}

		return result;
	}

	private Queue<Object> cleanAndBuildQuery(String query) throws Exception {

		Queue<Object> origQuery = new LinkedList<Object>();

		query = scrub(query);

		StringTokenizer tokenizer = new StringTokenizer(query);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			origQuery.add(token);
		}

		return origQuery;
	}

	private Set<Integer> consume(Queue<Object> consumeQuery) {
		Object left = null;
		Object right = null;
		List<Object> ops = new LinkedList<Object>();
		boolean l = false;
		boolean r = false;

//		System.out.println("Consuming==>" + consumeQuery);
		
		while (!consumeQuery.isEmpty()) {
			if (BooleanEngine.LPAREN.equals(consumeQuery.peek())
					|| BooleanEngine.RPAREN.equals(consumeQuery.peek())) {
				consumeQuery.remove();
			} else if (BooleanEngine.isOperator(consumeQuery.peek())) {
				ops.add(consumeQuery.poll());
			} else if (!l) {
				left = consumeQuery.poll();
				l = true;
			} else if (!r) {
				right = consumeQuery.poll();
				r = true;
			}

		}

		return this._bEngine.consume(left, right, ops);
	}

	public String scrub(String query) throws Exception {
		query = this._searchEngine._janitor.removeStopWords(query, Arrays.asList("and", "or", "not"));
		query = this._searchEngine._janitor.stem(query);
		return query;
	}
	
}
