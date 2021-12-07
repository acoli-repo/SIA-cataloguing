package org.acoli.sc.extract;

import java.util.List;

public class TOCDelimiterInfo {
	
	private int lineGroupContainingTocPrefix=10000000;
	private int lineGroupContainingTocPostfix=10000000;
	private String firstLineGroupRemainderText="";
	private String lastLineGroupRemainderText="";
	private TOC toc;
	
	public TOCDelimiterInfo(
					int lineGroupContainingTocPrefix,
					String firstLineGroupRemainderText,
					int lineGroupContainingTocPostfix, 
					String lastLineGroupRemainderText,
					TOC toc) {
		
		this.lineGroupContainingTocPrefix = lineGroupContainingTocPrefix;
		this.lineGroupContainingTocPostfix = lineGroupContainingTocPostfix;
		this.firstLineGroupRemainderText = firstLineGroupRemainderText;
		this.lastLineGroupRemainderText = lastLineGroupRemainderText;
		this.toc = toc;
	}
	
	public int getLineGroupContainingTocPrefix() {
		return lineGroupContainingTocPrefix;
	}
	public void setLineGroupContainingTocPrefix(int lineGroupContainingTocPrefix) {
		this.lineGroupContainingTocPrefix = lineGroupContainingTocPrefix;
	}
	public int getLineGroupContainingTocPostfix() {
		return lineGroupContainingTocPostfix;
	}
	public void setLineGroupContainingTocPostfix(int lineGroupContainingTocPostfix) {
		this.lineGroupContainingTocPostfix = lineGroupContainingTocPostfix;
	}
	public String getFirstLineGroupRemainderText() {
		return firstLineGroupRemainderText;
	}
	public void setFirstLineGroupRemainderText(String firstLineGroupRemainderText) {
		this.firstLineGroupRemainderText = firstLineGroupRemainderText;
	}
	public String getLastLineGroupRemainderText() {
		return lastLineGroupRemainderText;
	}
	public void setLastLineGroupRemainderText(String lastLineGroupRemainderText) {
		this.lastLineGroupRemainderText = lastLineGroupRemainderText;
	}
	
	public static TOCDelimiterInfo getBestTocDelimiterInfo(List<TOCDelimiterInfo> tdi) {
		
		TOCDelimiterInfo best = tdi.get(0);
		for (TOCDelimiterInfo td : tdi) {
			if (td.getLineGroupContainingTocPostfix() < best.getLineGroupContainingTocPostfix()) best = td;
		}
		return best;
	}

	public TOC getToc() {
		return toc;
	}

	public void setToc(TOC toc) {
		this.toc = toc;
	}

}






