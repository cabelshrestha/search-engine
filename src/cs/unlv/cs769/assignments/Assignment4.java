package cs.unlv.cs769.assignments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.engine.VectorSpaceModel.Result;
import cs.unlv.cs769.handlers.NormalQueryProcessor;
import cs.unlv.cs769.utils.Constants;
import cs.unlv.cs769.utils.TimeLogger;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class builds the output file for Assignment 4.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class Assignment4 extends BaseAssignment {

	private TimeLogger t = new TimeLogger();
	private NormalQueryProcessor _queryProcessor = null;

	public Assignment4(int a, SearchEngine e) {
		super(a, e);

		this._queryProcessor = new NormalQueryProcessor(this._searchEngine);
	}

	@Override
	public String execute() throws Exception {

		t.start("[A4] Building File");
		String outputFile = buildAssignmentFile(Constants.ASSIGNMENT4_FILE);
		t.stop();

		return outputFile;
	}

	@Override
	protected String buildAssignmentFile(String destFile) throws Exception {

		List<Result> results = this._queryProcessor.process(Constants.CRAN_QRY_FILE,
				Constants.CRAN_QRYREL_FILE);

		BufferedWriter writer;
		File file = new File(destFile);
		file.createNewFile();
		writer = new BufferedWriter(new FileWriter(file));

		for (Result result : results) {
			writer.write("\n========================================\n");
			writer.write("[Query No.]: " + result.getQryId() + "\n");
			writer.write("[Total Documents Retrieved]: " + result.getQryResult().size() + "\n");
			writer.write("[Precision]: " + result.getPrecision() + "\n");
			writer.write("[Recall]: " + result.getRecall() + "\n");
			writer.write("[");
			writer.write(Utils.printableList(result.getQryResult()));
			writer.write("]");
		}

		writer.flush();
		writer.close();

		return file.getAbsolutePath();
	}
}
