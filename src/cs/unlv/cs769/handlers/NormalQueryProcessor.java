package cs.unlv.cs769.handlers;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import cs.unlv.cs769.engine.BooleanEngine;
import cs.unlv.cs769.engine.SearchEngine;
import cs.unlv.cs769.engine.VectorSpaceModel;
import cs.unlv.cs769.engine.VectorSpaceModel.Result;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * This class processes normal query against
 * Vector Space Model.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class NormalQueryProcessor {

	private SearchEngine _searchEngine;
	private VectorSpaceModel _vectorSpaceModel;
	private BooleanEngine _bEngine;
	private HashMap<Integer, List<Integer>> _cranrelMap;

	public NormalQueryProcessor(SearchEngine e) {
		this._searchEngine = e;
		this._vectorSpaceModel = new VectorSpaceModel(e);
		this._bEngine = new BooleanEngine();
		this._cranrelMap = new HashMap<Integer, List<Integer>>(); 
	}
	
	public List<Result> process(String qryFile, String relFile) throws Exception {
	
		List<Result> results = new ArrayList<Result>();
		Result result = null; 

		buildCranrelMap(relFile);

		NormalQueryBuilder builder = new NormalQueryBuilder(qryFile);
		Map<Integer, String> queries = builder.build();
		List<Integer> searchResult = null;
		
		for (Map.Entry<Integer, String> entryPair : queries.entrySet()) {
			
			result = this._vectorSpaceModel.new Result();
			
			Integer qryId = entryPair.getKey();
			String qry = entryPair.getValue();

			String cleanQuery = scrub(qry);	
			searchResult = this._vectorSpaceModel.processQuery(tokenize(cleanQuery));
			
			result.setQryId(qryId);
			result.setQryResult(searchResult);
			result.setPrecision(calculatePrecision(qryId, searchResult));
			result.setRecall(calculateRecall(qryId, searchResult));
			
			results.add(result);
		}
		
		return results;
		
	}
	
	private double calculatePrecision(Integer qryId, List<Integer> searchResult) {
		double precision = 0d;
		List<Integer> relevantDocs = this._cranrelMap.get(qryId);
		
		if(relevantDocs != null && !relevantDocs.isEmpty()) {
			Set<Integer> retrievedRelevantDocs = this._bEngine.AND(new HashSet<Integer>(relevantDocs), new HashSet<Integer>(searchResult));
			precision = (double)retrievedRelevantDocs.size()/(double)searchResult.size();
		}
		return precision;
	}

	private double calculateRecall(Integer qryId, List<Integer> searchResult) {
		double recall = 0d;
		List<Integer> relevantDocs = this._cranrelMap.get(qryId);
		
		if(relevantDocs != null && !relevantDocs.isEmpty()) {
			Set<Integer> retrievedRelevantDocs = this._bEngine.AND(new HashSet<Integer>(relevantDocs), new HashSet<Integer>(searchResult));
			recall = (double)retrievedRelevantDocs.size()/(double)relevantDocs.size();
			
		}
		return recall;
	}

	private void buildCranrelMap(String relFile) throws Exception {
		String data = new String(Files.readAllBytes(new File(relFile).toPath()));
		String[] parts = Pattern.compile("\n").split(data);

		for (String part : parts) {
			String[] columns = Pattern.compile(" ").split(part);
			Integer qryId = Integer.valueOf(columns[0]);
			Integer docId = Integer.valueOf(columns[1]);
			
			if (this._cranrelMap.containsKey(qryId)) {
				this._cranrelMap.get(qryId).add(docId);
			} else {
				List<Integer> docIds = new ArrayList<Integer>();
				docIds.add(docId);
				this._cranrelMap.put(qryId, docIds);
			}
		}
	}
	
	public String scrub(String query) throws Exception {
		query = this._searchEngine._janitor.removeStopWords(query);
		query = this._searchEngine._janitor.stem(query);
		return query;
	}
	
	private List<String> tokenize(String query) {
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(query);
		String term = null;
		while (tokenizer.hasMoreTokens()) {

			term = tokenizer.nextToken();
			if(Utils.isNotEmpty(term)) {
				result.add(term.trim());
			}
		}
		return result;
	}
}
