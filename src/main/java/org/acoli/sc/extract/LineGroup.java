package org.acoli.sc.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.acoli.sc.config.Config;
import org.acoli.sc.util.OpenNlpTools;

import edu.stanford.nlp.ling.TaggedWord;

public class LineGroup {
	
    ArrayList<String> lines = new ArrayList<String>();
	int order=-1; 
	int font=-1;
	int fontSize = 0;
	boolean bold=false;
	boolean italic=false;
	TextType textType = TextType.UNKNOWN;
	
	
	LineGroup(String line, int font, int fontSize, int order, boolean bold, boolean italic) {
		addLine(line);
		this.font = font;
		this.fontSize=fontSize;
		this.order=order;
		this.bold = bold;
		this.italic = italic;
	}
	
	public void addLine(String line) {
		lines.add(line);
	}
	public ArrayList<String> getLines() {
		return lines;
	}
	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}
	public String getLinesAsString() {
		return String.join(" ", lines);
	}
	public int getFont() {
		return font;
	}
	public void setFont(int font) {
		this.font = font;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	
	public String print() {
		
		String text="";
		text+="Line group:"+order+"\n";
		text+="font:"+font+"\n";
		text+="font size:"+fontSize+"\n";
		text+="bold:"+bold+"\n";
		text+="italic:"+italic+"\n";
		text+="text:\n";
		text+= getLinesAsString()+"\n";
		
		return text;
	}
	public TextType getTextType() {
		return textType;
	}
	public void setTextType(TextType textType) {
		this.textType = textType;
	}
	
	public void appendLineGroup (LineGroup lg2) {
		lines.addAll(lg2.getLines());
	}
	
	
	public static List<LineGroup> clusterGroups(List<LineGroup> lgs, PDFExtractionConfiguration config) {
		
		System.out.println("# clusterGroups #");
		System.out.println("bindBy : "+ config.getBindLineGroupsBy());
		System.out.println("operator : "+ config.getBindLineGroupsOperator());
		
		int i = 0;
		LineGroup lastGroup = null;
		boolean groupCondition=false;
		while (i < lgs.size()) {
		
			if (i+2 < lgs.size()) {	// end reached ?
				
				if (lgs.get(i+1).getLinesAsString().trim().length()<5) { 
					
					
					switch (config.getBindLineGroupsOperator().toLowerCase().trim()) {
					
					case "or":
						
						groupCondition = false; // init
						
						if (config.bindLineGroupsByFontIsActive()) {
							groupCondition = groupCondition || (lgs.get(i).getFont() == lgs.get(i+2).getFont());
						}
						if (config.bindLineGroupsByFontSizeIsActive()) {
							groupCondition = groupCondition || (lgs.get(i).getFontSize() == lgs.get(i+2).getFontSize());
						}
						if (config.bindLineGroupsByBoldFontIsActive()) {
							groupCondition = groupCondition || (lgs.get(i).isBold() == lgs.get(i+2).isBold());
						}
						if (config.bindLineGroupsByItalicFontIsActive()) {
							groupCondition = groupCondition || (lgs.get(i).isItalic() == lgs.get(i+2).isItalic());
						}
						break;
						
					case "and":
						
						groupCondition = true; // init
						
						if (config.bindLineGroupsByFontIsActive()) {
							groupCondition = groupCondition && (lgs.get(i).getFont() == lgs.get(i+2).getFont());
						}
						if (config.bindLineGroupsByFontSizeIsActive()) {
							groupCondition = groupCondition && (lgs.get(i).getFontSize() == lgs.get(i+2).getFontSize());
						}
						if (config.bindLineGroupsByBoldFontIsActive()) {
							groupCondition = groupCondition && ((lgs.get(i).isBold() == lgs.get(i+2).isBold()));
						}
						if (config.bindLineGroupsByItalicFontIsActive()) {
							groupCondition = groupCondition && (lgs.get(i).isItalic() == lgs.get(i+2).isItalic());
						}
						break;
						
					default:
						System.out.println("Error, invalid value of config-parameter bindSequentialLinesOperator : "+config.getBindSequentialLinesOperator());
						System.exit(1);
					}
					
					
					// Merge group_i and group_i+2 if group_i contains at most one character AND
					// group_i and group_i+2 share the same font and fontSize
					if (groupCondition) {
						
							// merge groups i and i+2
							lgs.get(i).appendLineGroup(lgs.get(i+2));
							System.out.println("Cluster -> skipping line group "+(i+1)+" :"+lgs.get(i+1).getLinesAsString());
							// remove i+1 and i+2
							lgs.remove(i+1);lgs.remove(i+1); // remove index i+1, i+2
							continue; // continue with same index
					}
					
					
//					// Merge group_i and group_i+2 if group_i contains at most one character AND
//					// group_i and group_i+2 share the same font and fontSize
//					if (lgs.get(i).getFont() == lgs.get(i+2).getFont() &&
//						lgs.get(i).getFontSize() == lgs.get(i+2).getFontSize()) {
//						
//							// merge groups i and i+2
//							lgs.get(i).appendLineGroup(lgs.get(i+2));
//							System.out.println("Cluster -> skipping line group "+(i+1)+" :"+lgs.get(i+1).getLinesAsString());
//							// remove i+1 and i+2
//							lgs.remove(i+1);lgs.remove(i+1); // remove index i+1, i+2
//							continue; // continue with same index
//					}
				}
			}
			
			i++;
		}
		
		return lgs;
	}
	
	
	public static List<LineGroup> clusterGroups_(List<LineGroup> lgs) {
		
		List<LineGroup> clustered = new ArrayList<LineGroup>();
		int i = 0;
		LineGroup lastGroup = null;
		while (i < lgs.size()) {
		
			if (i+2 < lgs.size()) {	// end reached ?
				
				if (lgs.get(i+1).getLinesAsString().trim().length()<5) { 
					
					// Merge group_i and group_i+2 if group_i contains at most one character AND
					// group_i and group_i+2 share the same font and fontSize
					if (lgs.get(i).getFont() == lgs.get(i+2).getFont() &&
						lgs.get(i).getFontSize() == lgs.get(i+2).getFontSize()) {
						
							// merge groups i and i+2
							lgs.get(i).appendLineGroup(lgs.get(i+2));
							clustered.add(lgs.get(i));
							System.out.println("Cluster -> skipping line group "+(i+1)+" :"+lgs.get(i+1).getLinesAsString());
							i+=3;
							continue;
					}
				}
			}
			
			clustered.add(lgs.get(i));
			i++;
		}
		
		
		return clustered;
	}
	
	// does not join first and third line group !
	public static List<LineGroup> clusterGroupsOld(List<LineGroup> lgs) {
		
		List<LineGroup> clustered = new ArrayList<LineGroup>();
		int i = 0;
		LineGroup lastGroup = null;
		while (i < lgs.size()) {
		
			// Merge group_i and group_i+2 if group_i contains at most one character AND
			// group_i and group_i+2 share the same font and fontSize
			if (lgs.get(i).getLinesAsString().trim().length()<2) {
				if (i > 0 && i+1 < lgs.size()) {	// end reached ?
					if (lgs.get(i-1).getFont() == lgs.get(i+1).getFont() &&
						lgs.get(i-1).getFontSize() == lgs.get(i+1).getFontSize()) {
						
							// merge groups i-1 and i+1
							lastGroup.appendLineGroup(lgs.get(i+1));
							i+=2; // skip merged group i+1
							continue;
					}
				}
			} else {
				// group to result
				clustered.add(lgs.get(i));
			}
			
			lastGroup = lgs.get(i);
			i++;
		}
		
		
		return clustered;
	}
	
	
	/**
	 * Join groups into a single group
	 * @param lgs
	 * @return
	 */
	public static LineGroup joinGroups(List<LineGroup> lgs) {
		
		if (lgs.isEmpty()) return null;
		LineGroup joined = lgs.get(0);
		int i = 1;
		while (i < lgs.size()) {
			
			joined.appendLineGroup(lgs.get(i));
			i++;
		}

		return joined;
	}
	
	
	// not used
	public static List<LineGroup> joinGroupsByFontTypeAndFontSize(List<LineGroup> lgs) {
		
		List<LineGroup> joined = new ArrayList<LineGroup>();
		int i = 0;
		while (i < lgs.size()) {
			
			// merge subsequent line groups with same font and font size
			if (i+1 < lgs.size()) {
				
				if (lgs.get(i).getFont() == lgs.get(i+1).getFont() &&
					lgs.get(i).getFontSize() == lgs.get(i+1).getFontSize()) {
					lgs.get(i).appendLineGroup(lgs.get(i+1)); 
					joined.add(lgs.get(i));
					i+=2;
					continue;
				}
			}
			
			joined.add(lgs.get(i));
			i++;
		}

		return joined;
	}
	
	
	
	public static Metadata extractMDfromLineGroups(List<LineGroup> lgs, PDFExtractionConfiguration config) {
		
		boolean footerFound = false;
		boolean titleFound = false;
		boolean authorFound = false;
		int accumlatedTokenLength=0;
		int maxFontSize = 0;
		String lastTitle = "";
		for (LineGroup lg : lgs) {
			if (lg.getFontSize() > maxFontSize) maxFontSize = lg.getFontSize();
		}
		
		Metadata md = new Metadata();

		for (LineGroup lg : lgs) {
			
			String [] tokens = OpenNlpTools.tokenize(lg.getLinesAsString());
			int tokenCount = tokens.length;
			System.out.println("processing line group: "+lg.order);
			//System.out.println("line group "+lg.order+" : "+lg.getLinesAsString());
			//System.out.println("tokenCount: "+tokenCount);
			accumlatedTokenLength=0;
			for (String token : tokens) {
				accumlatedTokenLength+=token.length();
			}

			
			// global variables
			if ((tokenCount > 30 && accumlatedTokenLength > 70) || tokenCount < 2 || accumlatedTokenLength < 5) {
				System.out.println("skipping line group "+lg.order+" !");
				System.out.println("tokenCount : "+tokenCount);
				System.out.println("accumlatedTokenLength :"+accumlatedTokenLength);
				continue; // Skip long text from further evaluation
			}
			
//			if (lg.getLinesAsString().contains("@")) {
//				System.out.println("Skipping line group that contains @ !!!");
//				continue;
//			}
			
			if (!footerFound) { // check footer only once
				// match footer regex
				boolean isFooter = false;
						
				if (!config.getFooterDescriptions().isEmpty()) {
				//	if (config.footerDescriptionIsActive()) 
					isFooter = Metadata.parseFooter(lg.getLinesAsString(), md, config);
				}
				if (isFooter) {
					lg.setTextType(TextType.FOOTER);
					System.out.println("assignTextType FOOTER to : "+lg.getLinesAsString());
					footerFound=true;
					
					
					// correct wrong title
					// title is set but author has not been set => found title is incorrect, maybe footer prefix
					if (titleFound && !authorFound) {
						System.out.println("skipping previously found title : "+lastTitle);
						titleFound = false;}
					continue;
				}
				
				
//				if (config.footerDescriptionIsActive()) {
//					isFooter = Metadata.parseFooter(lg.getLinesAsString(), config.getFooterDescription(), new Metadata(), config);
//				}
//				if (isFooter) {
//					lg.setTextType(TextType.FOOTER);
//					System.out.println("assignTextType FOOTER to : "+lg.getLinesAsString());
//					footerFound=true;
//					
//					
//					// correct wrong title
//					// title is set but author has not been set => found title is incorrect, maybe footer prefix
//					if (titleFound && !authorFound) {
//						System.out.println("Correcting wrong title : "+lastTitle);
//						titleFound = false;}
//					continue;
//				}
				
			}
			
			
			// get POS
			List<TaggedWord> tagged = OpenNlpTools.stanfordTagger(lg.getLinesAsString());
			//List<String> posTags = OpenNlpTools.detectPartsOfSpeech(lg.getLinesAsString());
			
			HashMap<String,Integer> distribution = new HashMap<String, Integer>();
			for (TaggedWord tw : tagged) {
				if (!distribution.containsKey(tw.tag())) {
					distribution.put(tw.tag(), 1);
				} else {
					distribution.put(tw.tag(), distribution.get(tw.tag())+1);
				}
			}
			int diffTags = distribution.keySet().size();
			int nnpCount = 0;
			if (distribution.containsKey("NNP")) nnpCount = distribution.get("NNP");
			float nnpPart = nnpCount / (float) tagged.size();
			
			int nameCount = OpenNlpTools.findNames(lg.getLinesAsString());
			float namePart = nameCount / (float) tagged.size();
			
			int maxTokenLength = 0;
			
			System.out.println();
			System.out.println("nnpCount "+nnpCount);
			System.out.println("tagCount "+tagged.size());
			System.out.println("nnpPart "+nnpPart);
			System.out.println("nameCount "+nameCount);
			System.out.println("namePart "+namePart);
			for (TaggedWord tw : tagged) {
				if (tw.word().trim().length() > maxTokenLength) {
					maxTokenLength = tw.word().trim().length();
				}
			}
			System.out.println("maxTokenLength "+maxTokenLength);
			
			
			// Evaluation
			if (!titleFound && (nnpPart <.4 || lg.getFontSize() == maxFontSize)) {
				lg.setTextType(TextType.TITLE);
				titleFound=true;
				System.out.println("assignTextType TITLE : "+lg.getLinesAsString());
				lastTitle=lg.getLinesAsString();
				md.title = lg.getLinesAsString();
				continue;
			}
			
			if (maxTokenLength > 2 && (!authorFound && (nnpPart >= .4 || titleFound && nnpPart > .2))) {
				lg.setTextType(TextType.AUTHOR);
				authorFound=true;
				String authorsWithoutAffilation = OpenNlpTools.renoveAffilationEmailCopyright(lg.getLinesAsString());
				System.out.println("assignTextType AUTHOR to : "+authorsWithoutAffilation.trim());
				System.out.println("with affilation          : "+lg.getLinesAsString().trim());
				md.authorString = authorsWithoutAffilation.trim();
			}
	
		}
		
		return md;
		
		// Title is:
		// 50 % is max fontSize
		// 40 % not max nnp part
		// 10 % not shortest length
		
		// indicators for author :
		// allUppercase
		// regex for author list
		// order > title
		
		// indicators for title :
		// max fontSize
		// allUppercase
		// order < author
		// max detected word categories (by pos-tagger)

		// indicators for footer : footerDescription
		
	}

}
