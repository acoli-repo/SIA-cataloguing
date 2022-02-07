package org.acoli.sc.config;

public class AuthorNameFile {
	
	private String filePath = "";
	private int familyNameColumn = -1;
	private int givenNameColumn = -1;
	
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getFamilyNameColumn() {
		return familyNameColumn;
	}
	public void setFamilyNameColumn(int familyNameColumn) {
		this.familyNameColumn = familyNameColumn;
	}
	public int getGivenNameColumn() {
		return givenNameColumn;
	}
	public void setGivenNameColumn(int givenNameColumn) {
		this.givenNameColumn = givenNameColumn;
	}
}
