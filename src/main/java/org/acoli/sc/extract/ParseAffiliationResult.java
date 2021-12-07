package org.acoli.sc.extract;

public class ParseAffiliationResult {

	public static final int NO_POSITION = 1000;
	public static final int NOT_FOUND = -1;
	String leftmostMatchingAffiliation = "";
	String leftmostMatchingStopword = "";
	int positionOfleftmostMatchingStopword = NOT_FOUND;
	int positionOfleftmostMatchingAffiliation = NOT_FOUND;
	
	
	public ParseAffiliationResult () {}
	
	public ParseAffiliationResult(
			String leftmostMatchingStopword,
			int positionOfleftmostMatchingStopword,
			String leftmostMatchingAffiliation,
			int positionOfleftmostMatchingAffiliation
			) {
		this.leftmostMatchingAffiliation = leftmostMatchingAffiliation;
		this.leftmostMatchingStopword = leftmostMatchingStopword;
		this.positionOfleftmostMatchingAffiliation = positionOfleftmostMatchingAffiliation;
		this.positionOfleftmostMatchingStopword = positionOfleftmostMatchingStopword;
	}
	
	
	public String getLeftmostMatchingAffiliation() {
		return leftmostMatchingAffiliation;
	}

	public void setLeftmostMatchingAffiliation(String matchedAffiliation) {
		this.leftmostMatchingAffiliation = matchedAffiliation;
	}

	public String getLeftmostMatchingStopword() {
		return leftmostMatchingStopword;
	}

	public void setLeftmostMatchingStopWord(String matchedStopword) {
		this.leftmostMatchingStopword = matchedStopword;
	}

	public int getPositionOfleftmostMatchingStopword() {
		if (positionOfleftmostMatchingStopword == NO_POSITION) return NOT_FOUND;
		else
		return positionOfleftmostMatchingStopword;
	}

	public void setPositionOfleftmostMatchingStopword(int positionOfleftmostMatchingStopword) {
		this.positionOfleftmostMatchingStopword = positionOfleftmostMatchingStopword;
	}

	public int getPositionOfleftmostMatchingAffiliation() {
		if (positionOfleftmostMatchingAffiliation == NO_POSITION) return NOT_FOUND;
		else
		return positionOfleftmostMatchingAffiliation;
	}

	public void setPositionOfleftmostMatchingAffiliation(int positionOfleftmostMatchingAffiliation) {
		this.positionOfleftmostMatchingAffiliation = positionOfleftmostMatchingAffiliation;
	}
	
	public int getLeftmostMatch() {
		
		if (positionOfleftmostMatchingAffiliation == NO_POSITION) {
			if (positionOfleftmostMatchingStopword < NO_POSITION) {
				return positionOfleftmostMatchingStopword;
			} else {
				return NOT_FOUND;
			}
		} else {
			if (positionOfleftmostMatchingAffiliation < positionOfleftmostMatchingStopword) {
				return positionOfleftmostMatchingAffiliation;
			} else {
				return positionOfleftmostMatchingStopword;
			}
		}
	}
	
	public boolean affiliationWasMatched() {
		return !leftmostMatchingAffiliation.isEmpty();
	}
	
	public boolean stopwordWasMatched() {
		return !leftmostMatchingStopword.isEmpty();
	}
	
	
}
