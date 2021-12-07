package org.acoli.sc.extract;

import java.util.List;

public class TOC {
	
	public static final String REGULAR_FONT="regular";
	public static final String BOLD_FONT="bold";
	public static final String ITALIC_FONT="italic";
	public static final String BLANK_LINE="blankline";

	
	private String tocStartsAfterText="";
	private String tocEndsBeforeText="";
	//private String tocEntryDelimiter="";
	private List<ContentDescription> tocEntryDescription;
	private boolean joinTocEntries = false;

	public TOC(String tocStartsAfterText, String tocEndsBeforeText, List<ContentDescription> tocEntryDescription) {
		this.tocStartsAfterText = tocStartsAfterText;
		this.tocEndsBeforeText = tocEndsBeforeText;
		this.tocEntryDescription = tocEntryDescription;
	}
	
	public String getTocStartsAfterText() {
		return tocStartsAfterText;
	}
	public void setTocStartsAfterText(String tocStartsAfterText) {
		this.tocStartsAfterText = tocStartsAfterText;
	}
	public String getTocEndsBeforeText() {
		return tocEndsBeforeText;
	}
	public void setTocEndsBeforeText(String tocEndsBeforeText) {
		this.tocEndsBeforeText = tocEndsBeforeText;
	}
//	public String getTocEntryDelimiter() {
//		return tocEntryDelimiter;
//	}
//	public void setTocEntryDelimiter(String tocEntryDelimiter) {
//		this.tocEntryDelimiter = tocEntryDelimiter;
//	}
	public List<ContentDescription> getTocEntryDescription() {
		return tocEntryDescription;
	}
	public void setTocEntryDescriptions(List<ContentDescription> tocEntryDescription) {
		this.tocEntryDescription = tocEntryDescription;
	}
	public boolean isJoinTocEntries() {
		return joinTocEntries;
	}
	public void setJoinTocEntries(boolean joinTocEntries) {
		this.joinTocEntries = joinTocEntries;
	}
}
