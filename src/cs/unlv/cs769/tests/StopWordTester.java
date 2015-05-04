package cs.unlv.cs769.tests;

import cs.unlv.cs769.components.CranDocument;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.TimeLogger;

public class StopWordTester extends BaseTester {

	public StopWordTester() {
		super(new SearchEngine(), "StopWordTester");
	}

	@Override
	public void run() {
		try {

			this._engine.buildDocumentList(Constants.CRAN_1400_FILE);
			this._engine._janitor.removeStopWords(this._engine._documents);

			int i = 0;
			for (CranDocument d : this._engine._documents) {
				i++;
				System.out.println(d);
				if (i == 5)
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		TimeLogger t = new TimeLogger("Main");
		t.start();

		BaseTester o = new StopWordTester();
		o.execute();

		t.stop();

	}
}
