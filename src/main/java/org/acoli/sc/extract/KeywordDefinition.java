package org.acoli.sc.extract;

public class KeywordDefinition {
	
	private String id;
	private String journalTitle;
	private String title;
	private String author;
	private String volume;
	private String issue;
	private String year;
	private String beginPage;
	private String endPage;
	private String place;
	private String date;
	
	
	public KeywordDefinition(String id, String journalTitle, String title, String author, String volume, String issue,
			String year, String beginPage, String endPage, String place, String date) {
		super();
		this.id = id;
		this.journalTitle = journalTitle;
		this.title = title;
		this.author = author;
		this.volume = volume;
		this.issue = issue;
		this.year = year;
		this.beginPage = beginPage;
		this.endPage = endPage;
		this.place = place;
		this.date = date;
	}
	
	public KeywordDefinition() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJournalTitle() {
		return journalTitle;
	}
	public void setJournalTitle(String journalTitle) {
		this.journalTitle = journalTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getBeginPage() {
		return beginPage;
	}
	public void setBeginPage(String beginPage) {
		this.beginPage = beginPage;
	}
	public String getEndPage() {
		return endPage;
	}
	public void setEndPage(String endPage) {
		this.endPage = endPage;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public static KeywordDefinition getDefaults() {
		
		KeywordDefinition defaults = new KeywordDefinition();
		defaults.setJournalTitle(".+");
		defaults.setTitle(".+");
		defaults.setAuthor(".+");
		defaults.setDate(".+");
		defaults.setPlace(".+");
		defaults.setVolume("\\d+");
		defaults.setIssue("\\d+");
		defaults.setYear("\\d+");
		defaults.setBeginPage("\\d+");
		defaults.setEndPage("\\d+");
		
		return defaults;
	}

	

}
