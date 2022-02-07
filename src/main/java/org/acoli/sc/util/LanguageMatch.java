package org.acoli.sc.util;

public class LanguageMatch {
	
	private String languageISO639Identifier = "";
	private float minProb = 0.0f;		// lowest probability measured for a test sentence
	private float maxProb = 0.0f;		// highest probability measured for a test sentence
	private float averageProb = 0.0f;	// average probability measured for all test sentences
	private boolean selected = false;

	
	public LanguageMatch(String lc) {
		this.languageISO639Identifier = lc;
	}


	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean b) {
		this.selected = b;
	}


	public float getMinProb() {
		return minProb;
	}


	public void setMinProb(float minProb) {
		this.minProb = minProb;
	}


	public float getMaxProb() {
		return maxProb;
	}


	public void setMaxProb(float maxProb) {
		this.maxProb = maxProb;
	}


	public String getLanguageISO639Identifier() {
		return languageISO639Identifier;
	}
	
	public void setLanguageISO639Identifier(String code) {
		this.languageISO639Identifier = code;
	}


	public float getAverageProb() {
		return averageProb;
	}


	public void setAverageProb(float averageProb) {
		this.averageProb = averageProb;
	}
	
}