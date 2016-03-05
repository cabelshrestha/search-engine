package cs.unlv.cs769.components;

/*
 * Search Engine Assignment
 * CS769-Advanced Database Management-Dr.Kazem Taghva
 * University of Nevada, Las Vegas
 * Spring-2015
 * 
 * Holds all necessary information of a Cran Document.
 * 
 * @Author Cabel Dhoj Shrestha
 */
public class CranDocument {
	
	public Integer _id;
	public String _title;
	public String _author;
	public String _biblio;
	public String _rawcontent;
	public StringBuffer _scrubbedcontent;
	
	public CranDocument(Integer id, String title, String author, String biblio, String rawcontent) {
		this._id = id;
		this._title = title;
		this._author = author;
		this._biblio = biblio;
		this._rawcontent= rawcontent;
		this._scrubbedcontent = new StringBuffer();

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID:"+ this._id + "\n");
		sb.append("TITLE:" + this._title + "\n");
		sb.append("AUTHOR:" + this._author + "\n");
		sb.append("BIBLIOGRAPHY:" + this._biblio + "\n");
		sb.append("SCRUBBED CONTENT:\n" + this._scrubbedcontent);
		
		return sb.toString();
	}


}
