package org.acoli.sc.extract;

public class Footer {
	
    private String footerDescription="";
	private String footerStartsWithText="";
	
	public Footer(String footerDescription, String footerStartsWithText) {
		this.footerDescription = footerDescription;
		this.footerStartsWithText = footerStartsWithText;
	}
	
	public String getFooterDescription() {
		return footerDescription;
	}
	public void setFooterDescription(String footerDescription) {
		this.footerDescription = footerDescription;
	}
	public String getFooterStartsWithText() {
		return footerStartsWithText;
	}
	public void setFooterStartsWithText(String footerStartsWithText) {
		this.footerStartsWithText = footerStartsWithText;
	}

}
