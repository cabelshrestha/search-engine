package cs.unlv.cs769.handlers;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cs.unlv.cs769.components.CranDocument;
import cs.unlv.cs769.utils.Utils;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Class to parse the cran.all.1400 file and create
 * objects for each document.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class DocumentBuilder {

	private String filename;

	public DocumentBuilder(String filename) {
		this.filename = filename;
	}

	public List<CranDocument> build() throws Exception {

		List<CranDocument> list = new ArrayList<>();
		String data = new String(Files.readAllBytes(new File(filename).toPath()));
		String[] parts = Pattern.compile("(?m)^\\.I").split(data);

		int id;
		String title;
		String author;
		String biblio;
		String rawcontent;

		for (String part : parts) {
			if (Utils.isNotEmpty(part)) {
				id = extractId(part);
				title = extractTitle(part);
				author = extractAuthor(part);
				biblio = extractBiblio(part);
				rawcontent = extractRawcontent(part);
				list.add(new CranDocument(id, title, author, biblio, rawcontent));
			}
		}

		return list;
	}

	private int extractId(String part) {
		String[] lines = Pattern.compile("\n").split(part);
		return Integer.valueOf(lines[0].trim());
	}

	private String extractTitle(String part) {
		int start = part.indexOf(".T");
		int end = part.indexOf(".A");
		return part.substring(start + 2, end).replaceAll("\n", " ").trim();
	}

	private String extractAuthor(String part) {
		int start = part.indexOf(".A");
		int end = part.indexOf(".B");
		return part.substring(start + 2, end).replaceAll("\n", " ").trim();
	}

	private String extractBiblio(String part) {
		int start = part.indexOf(".B");
		int end = part.indexOf(".W");
		return part.substring(start + 2, end).replaceAll("\n", " ").trim();
	}

	private String extractRawcontent(String part) {
		String[] lines = Pattern.compile("\n").split(part);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if (i == 0)
				continue;
			sb.append(lines[i] + "\n");
		}
		return sb.toString();
	}
	
	public static void print(List<CranDocument> documents) {
		for(CranDocument d : documents) {
			System.out.println(d);
		}
	}
	
	public static void print(CranDocument document) {
		System.out.println(document);
	}
}
