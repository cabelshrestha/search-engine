package cs.unlv.cs769.utils;

public class Operator {
	
	 //not really ops but let's just consider they are for ease...
	public static final String LPAREN = "(";
	public static final String RPAREN = ")";
	
	//here they come
	public static final String NOT = "not";
	public static final String AND = "and";
	public static final String OR = "or";

	public static boolean isNotOp(Object val) {
		return !isOp(val);
	}

	public static boolean isOp(Object val) {
		if (val instanceof String) {
			String op = (String) val;
			if (Utils.isNotEmpty(op)) {
				if (LPAREN.equalsIgnoreCase(op) || RPAREN.equalsIgnoreCase(op)
						|| AND.equalsIgnoreCase(op) || OR.equalsIgnoreCase(op)
						|| NOT.equalsIgnoreCase(op)) {
					return true;
				}
			}	
		}
		
		return false;
	}
	
	/**
	 * Precedence
	 */
	public static int prec(String op) throws Exception {
		switch(op) {
			case "AND":
			case "OR":
			case "and":
			case "or":
				return 1;
			case "NOT":
			case "not":
				return 2;
			case "(":
			case ")":
				return 3;
			default:
				throw new Exception("Unknown operator:" + op);
		}
	}
}
