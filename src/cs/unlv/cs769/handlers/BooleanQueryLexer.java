package cs.unlv.cs769.handlers;

import cs.unlv.cs769.engine.BooleanEngine;

public class BooleanQueryLexer {

	/*
	 * checks validity of the original query. 
	 * i.e. 
	 *  - wrong order of term: op term op term 
	 * - missing opening or closing parenthesis 
	 * - 2 operators in sequence and second op not being "NOT"
	 * -add a condition flag for proper ending : can be "term" or ")"
	 */
	public String validate(String query) {
		String[] lexemes = query.split(" ");
		
		boolean termCheck = true;
		boolean properTermination = false;

		int errIndex = -1;
		int parenCount = 0;
		int currentEndIndex = 0;
		String l;
		for (int i = 0; i < lexemes.length; i++) {

			l = lexemes[i].trim();
			currentEndIndex += l.length() + 1;

			if (termCheck) {
				if (BooleanEngine.isOperator(l)) {
					if (BooleanEngine.LPAREN.equals(l)) {
						parenCount++;
						properTermination = false;
						continue;
					} else if (BooleanEngine.RPAREN.equals(l)) {
						errIndex = currentEndIndex;
						properTermination = true;
						parenCount = 0;
						break;
					} else {
						errIndex = currentEndIndex - l.length();
						properTermination = true;
						parenCount= 0;
						break;
					}
				} else {
					termCheck = false;
					properTermination = true;
					continue;
				}
			} else {
				if (!BooleanEngine.isOperator(l)) {
					errIndex = currentEndIndex - l.length();
					properTermination = true;
					parenCount=0;
					break;
				} else {
					if (BooleanEngine.LPAREN.equals(l)) {
						errIndex = currentEndIndex;
						properTermination = true;
						parenCount = 0;
						break;
					} else if (BooleanEngine.RPAREN.equals(l)) {
						parenCount--;
						properTermination = true;
						termCheck = false;
						continue;
					} else {
						if((i+1) == lexemes.length) {
							errIndex = currentEndIndex;
							properTermination = true;
							parenCount = 0;
							break;
						} else if (BooleanEngine.isOperator(lexemes[i + 1])) {
							if (lexemes[i + 1].equalsIgnoreCase(BooleanEngine.NOT)) {
								currentEndIndex += lexemes[i+1].length();
								properTermination = false;
								i++;
							} else if(lexemes[i + 1].equalsIgnoreCase(BooleanEngine.LPAREN)) {
								parenCount++;
								currentEndIndex += lexemes[i+1].length();
								properTermination = false;
								i++;
							} else if(lexemes[i + 1].equalsIgnoreCase(BooleanEngine.RPAREN)) {
								parenCount--;
								currentEndIndex += lexemes[i+1].length();
								properTermination = true;
								i++;
								termCheck = false;
								continue;
							}else {
								errIndex = currentEndIndex;
								properTermination = true;
								parenCount=0;
								break;
							}
						}
					}
				}
				termCheck = true;
			}
		}

		return buildError(query, errIndex, parenCount, properTermination);

	}

	private String buildError(String query, int errIndex, int parenCount, boolean properTermination) {
		
		if(errIndex == -1 && parenCount == 0 && properTermination)
			return null;
		
		StringBuilder error = new StringBuilder();
		error.append("<ERROR> Invalid Query\n");
		error.append(query + "\n");

		if (errIndex != -1) {
			char[] errPointer = new char[errIndex + 1];
			for (int i = 0; i < errIndex + 1; i++) {
				if (i == errIndex) {
					error.append("^\n");
				} else {
					error.append(" ");
				}
			}
			error.append(new String(errPointer));
		}
		
		if(!properTermination) {
			char[] errPointer = new char[errIndex + 1];
			for (int i = 0; i < query.length()+1; i++) {
				if (i == query.length()) {
					error.append("^\n");
				} else {
					error.append(" ");
				}
			}
			error.append(new String(errPointer));
		}

		if (parenCount != 0) {
			error.append("Missing one or more parenthesis pair.\n");
		}

		return error.toString();
	}
	
	
}
