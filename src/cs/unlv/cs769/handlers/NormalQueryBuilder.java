package cs.unlv.cs769.handlers;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Class to parse the cran.qry file and build
 * list of strings containing all queries.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class NormalQueryBuilder {

	private String filename;

	public NormalQueryBuilder(String filename) {
		this.filename = filename;
	}

	public Map<Integer, String> build() throws Exception {

		Map<Integer, String> queries = new HashMap<>();
		String data = new String(Files.readAllBytes(new File(filename).toPath()));
		String[] parts = Pattern.compile("(?m)^\\.I").split(data);

		for (String part : parts) {
			if (Utils.isNotEmpty(part)) {
				queries.putAll(breakdown(part));
			}
		}

		return queries;
	}

	private Map<Integer, String> breakdown(String qry) throws Exception {
		Map<Integer, String> result = new HashMap<>(); 
		StringBuilder sb = new StringBuilder();
		
		String[] parts = qry.split("\n");
		Integer qryId = Integer.valueOf(parts[0].trim());
		for(int i = 2; i < parts.length; i++) {
			sb.append(parts[i] + "\n");
		}
		result.put(qryId, sb.toString());

		return result;
	}

}
