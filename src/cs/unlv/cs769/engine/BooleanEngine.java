package cs.unlv.cs769.engine;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cs.unlv.cs769.components.Dictionary;
import cs.unlv.cs769.utils.Operator;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Implementation of boolean operations.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class BooleanEngine {

	private Dictionary _dictionary;

	public BooleanEngine() {

	}

	public BooleanEngine(Dictionary d) {
		this._dictionary = d;
	}

	public Set<Integer> consumePostFix(Object left, Object right, String op) {

		boolean isAnd = false;
		boolean isNot = false;
		Set<Integer> result = null;

		if (op != null) {
			if (Operator.AND.equalsIgnoreCase(op)) {
				isAnd = true;
			} else if (Operator.NOT.equalsIgnoreCase(op)) {
				isNot = true;
			}
		}

		if (left == null && right == null) {
			return new TreeSet<Integer>();
		} else if (left != null && right == null) {
			result = single(left, isNot);
		} else if (left == null && right != null) {
			result = single(right, isNot);
		} else if (left instanceof String && right instanceof String) {
			if (isAnd) {
				result = AND((String) left, (String) right, isNot);
			} else {
				result = OR((String) left, (String) right, isNot);
			}
		} else if (left instanceof Set<?> && right instanceof String) {
			if (isAnd) {
				result = AND((Set<Integer>) left, (String) right, isNot);
			} else {
				result = OR((Set<Integer>) left, (String) right, isNot);
			}
		} else if (left instanceof String && right instanceof Set<?>) {
			if (isAnd) {
				result = AND((String) left, (Set<Integer>) right, isNot);
			} else {
				result = OR((String) left, (Set<Integer>) right, isNot);
			}
		} else if (left instanceof Set<?> && right instanceof Set<?>) {
			if (isAnd) {
				result = AND((Set<Integer>) left, (Set<Integer>) right, isNot);
			} else {
				result = OR((Set<Integer>) left, (Set<Integer>) right, isNot);
			}
		}

		return result;
	}

	public Set<Integer> consume(Object left, Object right, List<Object> ops) {

		boolean isAnd = false;
		boolean isNot = false;
		Set<Integer> result = null;

		if (ops != null) {
			String op;
			for (int i = 0; i < ops.size(); i++) {
				op = (String) ops.get(i);
				if (Operator.AND.equalsIgnoreCase(op)) {
					isAnd = true;
				} else if (Operator.NOT.equalsIgnoreCase(op)) {
					isNot = true;
				}
			}
		}

		if (left == null && right == null) {
			return new TreeSet<Integer>();
		} else if (left != null && right == null) {
			result = single(left, isNot);
		} else if (left == null && right != null) {
			result = single(right, isNot);
		} else if (left instanceof String && right instanceof String) {
			if (isAnd) {
				result = AND((String) left, (String) right, isNot);
			} else {
				result = OR((String) left, (String) right, isNot);
			}
		} else if (left instanceof Set<?> && right instanceof String) {
			if (isAnd) {
				result = AND((Set<Integer>) left, (String) right, isNot);
			} else {
				result = OR((Set<Integer>) left, (String) right, isNot);
			}
		} else if (left instanceof String && right instanceof Set<?>) {
			if (isAnd) {
				result = AND((String) left, (Set<Integer>) right, isNot);
			} else {
				result = OR((String) left, (Set<Integer>) right, isNot);
			}
		} else if (left instanceof Set<?> && right instanceof Set<?>) {
			if (isAnd) {
				result = AND((Set<Integer>) left, (Set<Integer>) right, isNot);
			} else {
				result = OR((Set<Integer>) left, (Set<Integer>) right, isNot);
			}
		}

		return result;
	}

	public Set<Integer> single(Object a, boolean isNot) {
		Set<Integer> result = new TreeSet<Integer>();
		if (a instanceof String) {
			Dictionary.DictionaryEntry e = this._dictionary.getEntry((String) a);
			Set<Integer> inDocs = new TreeSet<Integer>();
			if (e != null)
				inDocs = e._inDocuments;
			if (isNot) {
				result = NOT(inDocs);
			} else {
				result = inDocs;
			}
		} else if (a instanceof Set<?>) {
			if (isNot) {
				result = NOT((Set<Integer>) a);
			} else {
				result = (Set<Integer>) a;
			}
		}
		return result;
	}

	public Set<Integer> AND(String l, String r, boolean isNot) {
		Dictionary.DictionaryEntry e1 = this._dictionary.getEntry(l);
		Dictionary.DictionaryEntry e2 = this._dictionary.getEntry(r);

		Set<Integer> left = new TreeSet<Integer>();
		Set<Integer> right = new TreeSet<Integer>();

		if (e1 != null) {
			left = e1._inDocuments;
		}

		if (e2 != null) {
			right = e2._inDocuments;
		}

		if (isNot) {
			right = NOT(right);
		}

		return AND(left, right);
	}

	public Set<Integer> AND(Set<Integer> l, String r, boolean isNot) {
		Dictionary.DictionaryEntry e2 = this._dictionary.getEntry(r);

		Set<Integer> right = new TreeSet<Integer>();

		if (l == null) {
			l = new TreeSet<Integer>();
		}

		if (e2 != null) {
			right = e2._inDocuments;
		}

		if (isNot) {
			right = NOT(right);
		}

		return AND(l, right);
	}

	public Set<Integer> AND(String l, Set<Integer> r, boolean isNot) {
		Dictionary.DictionaryEntry e1 = this._dictionary.getEntry(l);

		Set<Integer> left = new TreeSet<Integer>();

		if (e1 != null) {
			left = e1._inDocuments;
		}

		if (r == null) {
			r = new TreeSet<Integer>();
		}

		if (isNot) {
			r = NOT(r);
		}

		return AND(left, r);
	}

	public Set<Integer> AND(Set<Integer> l, Set<Integer> r, boolean isNot) {

		if (l == null) {
			l = new TreeSet<Integer>();
		}

		if (r == null) {
			r = new TreeSet<Integer>();
		}

		if (isNot) {
			r = NOT(r);
		}

		return AND(l, r);
	}

	public Set<Integer> OR(String l, String r, boolean isNot) {
		Dictionary.DictionaryEntry e1 = this._dictionary.getEntry(l);
		Dictionary.DictionaryEntry e2 = this._dictionary.getEntry(r);

		Set<Integer> left = new TreeSet<Integer>();
		Set<Integer> right = new TreeSet<Integer>();

		if (e1 != null) {
			left = e1._inDocuments;
		}

		if (e2 != null) {
			right = e2._inDocuments;
		}

		if (isNot) {
			right = NOT(right);
		}

		return OR(left, right);
	}

	public Set<Integer> OR(Set<Integer> l, String r, boolean isNot) {
		Dictionary.DictionaryEntry e2 = this._dictionary.getEntry(r);

		Set<Integer> right = new TreeSet<Integer>();

		if (l == null) {
			l = new TreeSet<Integer>();
		}

		if (e2 != null) {
			right = e2._inDocuments;
		}

		if (isNot) {
			right = NOT(right);
		}

		return OR(l, right);
	}

	public Set<Integer> OR(String l, Set<Integer> r, boolean isNot) {
		Dictionary.DictionaryEntry e1 = this._dictionary.getEntry(l);

		Set<Integer> left = new TreeSet<Integer>();

		if (e1 != null) {
			left = e1._inDocuments;
		}

		if (r == null) {
			r = new TreeSet<Integer>();
		}

		if (isNot) {
			r = NOT(r);
		}

		return OR(left, r);
	}

	public Set<Integer> OR(Set<Integer> l, Set<Integer> r, boolean isNot) {

		if (l == null) {
			l = new TreeSet<Integer>();
		}

		if (r == null) {
			r = new TreeSet<Integer>();
		}

		if (isNot) {
			r = NOT(r);
		}

		return OR(l, r);
	}

	public Set<Integer> AND(Set<Integer> a, Set<Integer> b) {
		Set<Integer> result = new TreeSet<Integer>();
		for (Integer i : a)
			if (b.contains(i))
				result.add(i);
		return result;
	}

	public Set<Integer> OR(Set<Integer> a, Set<Integer> b) {
		Set<Integer> result = new TreeSet<Integer>(a);
		result.addAll(b);
		return result;
	}

	public Set<Integer> NOT(Set<Integer> inDocs) {
		Set<Integer> result = new TreeSet<Integer>();
		for (Integer i : this._dictionary._universe) {
			if (!inDocs.contains(i))
				result.add(i);
		}
		return result;
	}

}
