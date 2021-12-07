package org.acoli.sc.extract;

import java.util.List;

public class Citation {
	
	private String citationStartsAfterText="";
	private String citationEndsBeforeText="";
	private List<ContentDescription> citationVariants=null;
	
	
	public String getCitationStartsAfterText() {
		return citationStartsAfterText;
	}
	
	public void setCitationStartsAfterText(String citationStartsAfterText) {
		this.citationStartsAfterText = citationStartsAfterText;
	}
	
	public String getCitationEndsBeforeText() {
		return citationEndsBeforeText;
	}
	
	public void setCitationEndsBeforeText(String citatioEndsBeforeText) {
		this.citationEndsBeforeText = citatioEndsBeforeText;
	}
	
	public List<ContentDescription> getCitationVariants() {
		return citationVariants;
	}
	
	public void setCitationVariants(List<ContentDescription> citationVariants) {
		this.citationVariants = citationVariants;
	}

}
