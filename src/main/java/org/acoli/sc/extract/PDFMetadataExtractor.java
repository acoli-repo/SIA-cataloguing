package org.acoli.sc.extract;

import org.acoli.sc.start.Run;
import org.acoli.sc.util.LanguageMatch;
import org.acoli.sc.util.LanguageUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * changed :
 * buildTitleQuery
 * buildAuthorQuery
 * getAuthors
 * Contains everything concerned with extracting fields from the XML Document. Uses XPaths for this.
 */
public class PDFMetadataExtractor {

	private static Logger LOG = Logger.getLogger(PDFMetadataExtractor.class.getName());
	XPath xPath;
	static PDFExtractionConfiguration config;

	public PDFMetadataExtractor(PDFExtractionConfiguration config){
		if (config == null) {
			throw new RuntimeException("Configuration has to be set.");
		}
		if (!validate(config)) {
			throw new RuntimeException("Configuration "+config+" is invalid!");
		}
		this.xPath = XPathFactory.newInstance().newXPath();

		this.config = config;

	}

	boolean validate(PDFExtractionConfiguration config) {
		return true;
		//return (config.authorFontIsActive() || config.authorHeightIsActive())  && (config.titleFontIsActive() || config.titleHeightIsActive());
	}
	
	
	String buildTopQueryPart() {
	
		if (!config.authorTitleTopIsActive()) return "";
		else
		return "@top"+config.getAuthorTitleTopRelation()+config.getAuthorTitleTop();
	}
	
	
	String buildFooterTopQueryPart() {
		
		if (!config.footerTopIsActive()) return "";
		else
		return "@top"+config.getFooterTopRelation()+config.getFooterTop();
	}
	
	
	/**
	 * Query complex information (e.g. Working Papers in Scandinavian Syntax 87 (2011), 103-135.) sometimes found in headers or footers.
	 * @param numberOfFirstPage
	 * @return
	 */
	String buildFooterQuery(int numberOfFirstPage) {
		
		String FooterXP = "";
		if (config.footerHeightIsActive()) {
			FooterXP = "@height"+config.getFooterHeightRelation()+config.getFooterHeight();
		} else {
			// use min(max setting
			FooterXP = "@height>="+config.getFooterHeightMin()+" and @height<="+config.getFooterHeightMax();
		}
		
		String xPathForFooter = "pdf2xml/page[@number = "+numberOfFirstPage+"]/text[";
		if (config.footerFontIsActive()) {
			xPathForFooter+="@font"+config.getFooterFontRelation()+config.getFooterFont()+" and ";
		}
		xPathForFooter += FooterXP;
		
		String topQuery = buildFooterTopQueryPart();
		if (!topQuery.isEmpty()) {
			xPathForFooter+=" and "+topQuery;
		}
//		if (config.footerStartsWithTextIsActive()) {
//			xPathForFooter+=" and starts-with(.,'"+config.getFooterStartsWithText()+"')";
//		}
		
		xPathForFooter += "]/descendant::text()";
		LOG.info("Footer XPath: "+xPathForFooter);
		return xPathForFooter;
	}
	
	
	String buildTitleQuery(int numberOfFirstPage) {
		
		String titleXP = "";
		if (config.titleHeightIsActive()) {
			titleXP = "@height"+config.getTitleHeightRelation()+config.getTitleHeight();
		} else {
			// use min(max setting
			titleXP = "@height>="+config.getTitleHeightMin()+" and @height<="+config.getTitleHeightMax();
		}
		
		String xPathForTitle = "pdf2xml/page[@number = "+numberOfFirstPage+"]/text[";
		if (config.titleFontIsActive()) {
			xPathForTitle+="@font"+config.getTitleFontRelation()+config.getTitleFont()+" and ";
		}
		xPathForTitle += titleXP;
		
		String topQuery = buildTopQueryPart();
		if (!topQuery.isEmpty()) {
			xPathForTitle+=" and "+topQuery;
		}
		
		xPathForTitle += "]/descendant::text()";
		LOG.info("Title XPath: "+xPathForTitle);
		return xPathForTitle;
	}
	
	String getTitle(Document document) throws XPathExpressionException {
		
		int	numberOfFirstPage = findFirstPage(document);
		String xPathForTitle = buildTitleQuery(numberOfFirstPage);
		NodeList nodeList = (NodeList) xPath.compile(xPathForTitle).evaluate(document, XPathConstants.NODESET);
		LOG.fine(nodeList.getLength()+" Results for xPath: "+xPathForTitle);
		if (nodeList.getLength() == 1) {
			xPathForTitle = xPathForTitle.replaceFirst("/text\\(\\)$", "");
		}
		nodeList = (NodeList) xPath.compile(xPathForTitle).evaluate(document, XPathConstants.NODESET);
		LOG.fine(nodeList.getLength()+" Results for xPath: "+xPathForTitle);
		StringBuilder title = new StringBuilder();
		for (int i = 0; i < nodeList.getLength(); i++)
			title.append(nodeList.item(i).getTextContent());
		return String.join(" ", title);
	}

	/**
	 * We often use the position of the abstract to figure out if something is a title / author annotation and not
	 * a heading etc. This function finds the position of a node that says "Abstract"
	 * @param document
	 * @return
	 * @throws XPathExpressionException
	 */
	static double findPositionOfAbstract(Document document) throws XPathExpressionException {
//		<text top="293" left="416" width="56" height="13" font="3">Abstract</text>
		XPath xPath = XPathFactory.newInstance().newXPath();
		// below ugly XPath 1.0 way to say "position of Element that matches x"
		Integer indexOfFirstPage = findFirstPage(document);
		String xPathForAbstractPosition = "count(pdf2xml/page[@number="+indexOfFirstPage+"]/text[text() = 'Abstract']/preceding-sibling::text)+1";
		String stupidXPathForAbstractPosition = "count(pdf2xml/page[@number="+indexOfFirstPage+"]/text/b[text() = 'Abstract']/preceding::text)+1";
		// TODO: sometimes it's <b>Abstract </b>
		double abstractPosition = 0;
		try {
			abstractPosition = (double) xPath.compile(xPathForAbstractPosition).evaluate(document, XPathConstants.NUMBER);
			if (abstractPosition == 1.0) {
				LOG.fine("Didnt find");
				LOG.fine(xPath.compile(stupidXPathForAbstractPosition).evaluate(document));
				abstractPosition = (double) xPath.compile(stupidXPathForAbstractPosition).evaluate(document, XPathConstants.NUMBER);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		LOG.info("Found abstract position "+abstractPosition);
		return abstractPosition;
	}
	static Integer findFirstPage(Document document) throws XPathExpressionException {

		// return default page
		if (config.firstPageIsActive()) {
			return config.getFirstPage();
		}
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xPathForFirstPageNumber = "(pdf2xml/page/@number[not(. > pdf2xml/page/@number)])[1]";
		Double result = (double) xPath.compile(xPathForFirstPageNumber).evaluate(document, XPathConstants.NUMBER);
		LOG.info("Found first page: "+result);
		return result.intValue();
	}

	String buildAuthorQuery(int numberOfFirstPage, int indexOfAbstract) {
		/*
		 * String xPathForAuthors = "pdf2xml/page[@number = " + numberOfFirstPage +
		 * "]/text[position() < " + indexOfAbstract; if (config.authorFont >= 0)
		 * xPathForAuthors += " and @font>=" + config.authorFont; if
		 * (config.authorHeight >= 0) xPathForAuthors += " and @height>=" +
		 * config.authorHeight;
		 */
		
		String authorXP="";
		if (config.authorHeightIsActive()) {
			authorXP = " and @height"+config.getAuthorHeightRelation()+ config.getAuthorHeight();		
		} else {
			authorXP = " and @height>="+config.getAuthorHeightMin()+" and @height<="+config.getAuthorHeightMax();
		}
		String xPathForAuthors = "pdf2xml/page[@number = " + numberOfFirstPage + ""
				+ "]/text[@font"+config.getAuthorFontRelation()+config.getAuthorFont() + authorXP;
		
		String topQuery = buildTopQueryPart();
		if (!topQuery.isEmpty()) {
			xPathForAuthors+=" and "+topQuery;
		}
		/*
		 * if (config.authorFont >= 0) xPathForAuthors += " and @font>=" +
		 * config.authorFont; if (config.authorHeight >= 0) xPathForAuthors +=
		 * " and @height>=" + config.authorHeight;
		 */
		xPathForAuthors += "]/descendant::text()";
		LOG.finer("Constructed XPath: " + xPathForAuthors);
		return xPathForAuthors;
	}

	/**
	 * @param document
	 * @return
	 */
	List<String> getAuthors(Document document) throws XPathExpressionException {
		int numberOfFirstPage = findFirstPage(document);
		double indexOfAbstract = findPositionOfAbstract(document);
		String xPathForAuthors = buildAuthorQuery(numberOfFirstPage, (int) indexOfAbstract);
		System.out.println(xPathForAuthors);
		NodeList nodeList = (NodeList) xPath.compile(xPathForAuthors).evaluate(document, XPathConstants.NODESET);
		List<String> authors = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			LOG.fine(nodeList.item(i).toString());
			String candidate = nodeList.item(i).getTextContent();
			/* optional deletion
			 * if (candidate.contains("and")) { System.out.println("+++ "+candidate); String
			 * tmp = candidate.replace("and", ""); }
			 */
			String tmp = candidate;//candidate.replace("and", "");
			
			//tmp.replace(" ", "");
			if (config.isAuthorAllUppercase()) {
				boolean error = false;
				for (String token : tmp.split(" ")) {
					if (token.toLowerCase().equals("and")) continue;
					if (!token.toUpperCase().equals(token)) {
						error = true;
					};
				}
				if (error) continue;
				//if (!tmp.toUpperCase().equals(tmp)) continue;
			}
			if (config.isAuthorNoDigits()) {
				if (tmp.matches(".*\\d.*")) continue;
			}
			//if (tmp.isEmpty() || tmp.equalsIgnoreCase("and")) continue;
			if (config.authorMinLengthIsActive()) {
				if (tmp.length() < config.authorMinLength) continue;
			}
			if (candidate.contains(" ")) {
				candidate = candidate.replace(",$", "")
						.replaceAll("^,", "")
						.trim();
				authors.add(candidate);
			}
		}
		if (authors.size() == 1) {
			authors = Arrays.asList(authors.get(0).split(", "));
		}
		return authors;
	}


	static List<Integer> getPageNumberCandidatesWithHeight(Document document, PDFExtractionConfiguration config) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		String xPathForPageNumbers="";
		if (config.pageHeightIsActive()) {
			xPathForPageNumbers = "//page/text[@height"+config.getPageHeightRelation()+config.getPageHeight()+"]";
		} else {
			// use min/max setting
			xPathForPageNumbers = "//page/text[@height>="+config.getPageHeightMin()+" and @height<="+config.getPageHeightMax()+"]";
		}
		NodeList pageNumbersNodeList = (NodeList) xPath.compile(xPathForPageNumbers).evaluate(document, XPathConstants.NODESET);
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = 0; i < pageNumbersNodeList.getLength(); i++) {
			String candidatePageNumber = pageNumbersNodeList.item(i).getTextContent();
			if (candidatePageNumber.matches("-?[0-9]+")) { // TODO changing regex may improve may pagenr recognition 
				pageNumbers.add(Integer.parseInt(pageNumbersNodeList.item(i).getTextContent()));
			}
		}
		LOG.info("Found "+pageNumbersNodeList.getLength()+" candidates for pageNumbers.");
		return pageNumbers;
	}
	List<Integer> getPageNumbers(Document document) throws XPathExpressionException {
		List<Integer> pageNumbers = getPageNumberCandidatesWithHeight(document, config);

		Collections.sort(pageNumbers);
		return pageNumbers;
	}

	
	public List<Metadata> getMetadata(Document document) {
		
		List<Metadata> md = new ArrayList<Metadata>();
		
		int textSamplePages = 10;
		
		try {
			
			// parse PDF that contains a single article
			if (!config.hasToc() && !config.hasCitation()) {
				
				md.add(LineGroup.extractMDfromLineGroups(getLineGroups(document, true, false, 10), config));
				
				// add language info
				assignLanguage(md, document, "single", textSamplePages);				

			// parse PDF that contains a collection of articles (must contain table of content)
			} else {
				// a. get raw linegroups for text that contains toc
				List<LineGroup> lineGroups = getLineGroups(document, true, true, 200);
				
				System.out.println("line groups:"+lineGroups.size());
				
				if(config.hasToc()) {
					
					// b. remove text before and after toc
					List<TOC> tds = config.getTocDescriptions();
					List<Metadata> results = getTOCMetadataWithBacktrack(lineGroups, tds);
					if (config.hasFooter()) {
						addFooterInfoToTocOrCitation(results, document, config);
					}
					
					// add language info
					assignLanguage(results, document, "toc", textSamplePages);
					
					return results;
					//return getTOCMetadataByLinear(lineGroups, tds);
					
				} else { // (hasCitation)
					
					List<Citation> cts = config.getCitationDescriptions();
					List<TOC> tds = new ArrayList<TOC>();
					for (Citation cti : cts) {
						TOC t = new TOC(
								cti.getCitationStartsAfterText(),
								cti.getCitationEndsBeforeText(),
								cti.getCitationVariants());
						t.setJoinTocEntries(true);
						tds.add(t);
					}
					List<Metadata> results = getTOCMetadataWithBacktrack(lineGroups, tds);
					if (config.hasFooter()) {
						addFooterInfoToTocOrCitation(results, document, config);
					}
					
					// add language info
					assignLanguage(results, document, "cite", textSamplePages);					
					return results;
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return md;
	}
	
	
	private void assignLanguage(List<Metadata> md, Document document, String type, int textSamplePages) {
		
		int sampleChars = Run.languageDetectionSampleChars;// 80
		// add language info
		String textSample = getTextSample(document, 1, textSamplePages);
		try {
			int startOffset = getStartOffset(textSample.length(), type);
			// n-gram language detector 
			LanguageMatch foundLanguage = 
					LanguageUtils.detectIsoCode639_2Optimaize(textSample.substring(startOffset,Math.min(startOffset+sampleChars, textSample.length())));
			if (foundLanguage != null && foundLanguage.getAverageProb() > Run.ngramDetectorMinConfidence) {
				List<String> languageCodes = new ArrayList<String>();
				languageCodes.add(foundLanguage.getLanguageISO639Identifier());
				for (Metadata x : md) {
					x.setLanguagesISO639Codes(languageCodes);
				}
			} else {
				// try alternative language detector
				String iso639_2b = 
						LanguageUtils.detectIsoCode639_2bLingua(textSample.substring(startOffset,Math.min(startOffset+sampleChars, textSample.length())));
				if (iso639_2b != null) {
					System.out.println("Lingua : "+iso639_2b);
					
					List<String> languageCodes = new ArrayList<String>();
					languageCodes.add(iso639_2b);
					for (Metadata x : md) {
						x.setLanguagesISO639Codes(languageCodes);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private int getStartOffset(int length, String type) {
		
		switch (type) {
		
		case "single":
			return Math.max(400, Math.round(length/2));
			
		case "toc":
			return Math.max(3000, Math.round(length/2));
		
		case "cite":
			return Math.max(3000, Math.round(length/2));
		
		default:
			return 2000; 
		}
	}

	
	private void addFooterInfoToTocOrCitation(List<Metadata> mdList, Document document, PDFExtractionConfiguration config) {
		
		Metadata footerMD;
		try {
			footerMD = LineGroup.extractMDfromLineGroups(getLineGroups(document, true, true, 200), config);
			System.out.println(footerMD);

		if (footerMD.journalTitleIsActive()) {
			for (Metadata result : mdList) {
				if(!result.journalTitleIsActive()) {
					result.setJournalTitle(footerMD.getJournalTitle());
				}
				
				if(!result.journalNoteIsActive()) {
					result.setJournalNote(footerMD.getJournalNote());
				}
				
				if(!result.volumeIsActive()) {
					result.setVolume(footerMD.getVolume());
				}
				
				if(!result.issueIsActive()) {
					result.setIssue(footerMD.getIssue());
				}
				
				if(!result.yearIsActive()) {
					result.setYear(footerMD.getYear());
				}
				
				if(!result.placeIsActive()) {
					result.setPlace(footerMD.getPlace());
				}
				
				if(!result.dateIsActive()) {
					result.setDate(footerMD.getDate());
				}
				
			}
		}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	
	private List<Metadata> getTOCMetadataWithBacktrack(List <LineGroup> lineGroups, List<TOC> tds) {
		
		// for all given toc descriptions
		LineGroup lgPrefix=null;
		String lgPrefixRemainder = null;
		int tdsCount = 1;
		boolean joinTocLines = false;
		List<TOCDelimiterInfo> delimiterInfos = new ArrayList<TOCDelimiterInfo>();
		for (TOC toc : tds) {
			System.out.println("Trying TOC description : "+tdsCount++);
			
			boolean foundStart = false;
			boolean foundEnd = false;
			joinTocLines = toc.isJoinTocEntries();
			if (toc.getTocStartsAfterText().trim().isEmpty()) foundStart=true;
			
			int lineGroupCount = lineGroups.size();
			int j = 0;
			int lineGroupContainingTocPrefix=-1;
			int lineGroupContainingTocPostfix=100000;
			String prefixRegex = "(.*"+toc.getTocStartsAfterText()+")(.*)";
			Pattern pre = Pattern.compile(prefixRegex);
			//String postfixRegex = "("+toc.getTocEndsBeforeText()+".*)";
			String postfixRegex = "(.*)("+toc.getTocEndsBeforeText()+".*)";
			Pattern post = Pattern.compile(postfixRegex);
			
			while (j < lineGroupCount) {
				
				LineGroup lg = lineGroups.get(j);
				String lines = lg.getLinesAsString();
				System.out.println("line group : "+j);
				System.out.println(lines);
				
				if (!foundStart) {
					
					System.out.println("searching for TOC start");
					System.out.println("searching regex : "+prefixRegex);
					Matcher m = pre.matcher(lines);
					int i = 1;
					m.find();
					System.out.println("Found groups : "+m.groupCount());
					
					if (lines.matches(prefixRegex)) {
						try {
							while (i <= m.groupCount()) {
								System.out.println(i+":"+m.group(i));
								i++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// save delete prefix until TOC end was found (below)
						lgPrefix = lg;
						lgPrefixRemainder = m.group(2).trim();
//						lg.getLines().clear();
//						lg.addLine(m.group(2).trim());
						foundStart = true;
						lineGroupContainingTocPrefix = j;
						System.out.println("success");
						continue; // allow TOC end in same lineGroup
					}
				
				} else {
					
					System.out.println("searching for TOC end");
					System.out.println("searching regex : "+postfixRegex);
					Matcher m = post.matcher(lines);
					int i = 1;
					m.find();
					System.out.println("Found groups : "+m.groupCount());
					
					if (lines.matches(postfixRegex)) {
						try {
							while (i <= m.groupCount()) {
								System.out.println(i+":"+m.group(i));
								i++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						TOCDelimiterInfo tdi = 
								new TOCDelimiterInfo(
										lineGroupContainingTocPrefix,
										lgPrefixRemainder,
										j,
										m.group(1).trim(),
										toc
										);
						delimiterInfos.add(tdi);
						foundEnd = true;
						
						// remove TOC prefix
//						lgPrefix.getLines().clear();
//						lgPrefix.addLine(lgPrefixRemainder);
//						// remove TOC postfix
//						lg.getLines().clear();
//						lg.addLine(m.group(1).trim());
//						foundEnd = true;
//						lineGroupContainingTocPostfix = j;
						System.out.println("success");
					}
					
				}
				if(foundEnd) break;
				j++;
			}
			}

			
//			if (!foundEnd) {
//				continue;
//			}
			
			if (!delimiterInfos.isEmpty()) {
				// Determine best matching delimiterInfos
				TOCDelimiterInfo best = TOCDelimiterInfo.getBestTocDelimiterInfo(delimiterInfos);
				int lineGroupContainingTocPrefix = best.getLineGroupContainingTocPrefix();
				int lineGroupContainingTocPostfix = best.getLineGroupContainingTocPostfix();
				
				if (lineGroupContainingTocPrefix != lineGroupContainingTocPostfix) {
				
					lineGroups.get(best.getLineGroupContainingTocPrefix()).getLines().clear();
					lineGroups.get(best.getLineGroupContainingTocPrefix()).addLine(best.getFirstLineGroupRemainderText());
					lineGroups.get(best.getLineGroupContainingTocPostfix()).getLines().clear();
					lineGroups.get(best.getLineGroupContainingTocPostfix()).addLine(best.getLastLineGroupRemainderText());
				
				} else {
					
					String prefix = best.getFirstLineGroupRemainderText();
					String postfix = best.getLastLineGroupRemainderText();
					int maxCommonPrefixLength = 0;
					int maxPrefixLengthAt = -1;
					int i = 0;
					while (i < postfix.length()) {
						int c = StringUtils.getCommonPrefix(prefix, postfix.substring(i)).length();
						if (c > maxCommonPrefixLength) {
							maxCommonPrefixLength = c;
							maxPrefixLengthAt = i;
						}
						i++;
					}
					
					if (maxCommonPrefixLength > 0) { 
						lineGroups.get(best.getLineGroupContainingTocPrefix()).getLines().clear();
						lineGroups.get(best.getLineGroupContainingTocPrefix()).addLine(postfix.substring(maxPrefixLengthAt));
						System.out.println("maxCommonInfix :"+postfix.substring(maxPrefixLengthAt));
					} else {
						System.out.println("Error in getTOCMetadataWithBacktrack : maxCommonPrefixLength not found !");
					}
				}
				
				// Remove lineGroups before and after TOC
				Iterator<LineGroup> iterator = lineGroups.iterator();
				int lineGroupCounter = 0;
				while (iterator.hasNext()) {
					LineGroup y = iterator.next();
					if (lineGroupCounter < lineGroupContainingTocPrefix) {
						iterator.remove();
					}
					if (lineGroupCounter > lineGroupContainingTocPostfix) {
						iterator.remove();
					}
					lineGroupCounter++;
				}
				
				
				int lgc = 1;
				System.out.println("Line groups in TOC:");
				for (LineGroup lg : lineGroups) {
					System.out.println(lgc++);
					System.out.println(lg.getLinesAsString());
				}
									
				// c. parse metadata from toc
				if (!joinTocLines) {
					return TOCTools.parseTocEntries(best.getToc(), lineGroups, config);
				} else {
					return TOCTools.parseCitation(best.getToc(), lineGroups, config);
				}
			}

	
			System.out.println("TOC could not be found !");
			return new ArrayList<Metadata>(); // return list with empty meta-data
	}
	
	
	/**
	 * 
	 * @param lineGroups
	 * @param tds
	 * @return
	 * @deprecated
	 */
	private List<Metadata> getTOCMetadataByLinear(List<LineGroup> lineGroups, List<TOC> tds) {
		
		// for all given toc descriptions
		LineGroup lgPrefix=null;
		String lgPrefixRemainder = null;
		int tdsCount = 1;
		for (TOC toc : tds) {
			System.out.println("Trying TOC description : "+tdsCount++);
			
			boolean foundStart = false;
			boolean foundEnd = false;
			if (toc.getTocStartsAfterText().trim().isEmpty()) foundStart=true;
			
			int lineGroupCount = lineGroups.size();
			int j = 0;
			int lineGroupContainingTocPrefix=-1;
			int lineGroupContainingTocPostfix=100000;
			String prefixRegex = "(.*"+toc.getTocStartsAfterText()+")(.*)";
			Pattern pre = Pattern.compile(prefixRegex);
			//String postfixRegex = "("+toc.getTocEndsBeforeText()+".*)";
			String postfixRegex = "(.*)("+toc.getTocEndsBeforeText()+".*)";
			Pattern post = Pattern.compile(postfixRegex);
			while (j < lineGroupCount) {
				
				LineGroup lg = lineGroups.get(j);
				String lines = lg.getLinesAsString();
				System.out.println("line group : "+j);
				System.out.println(lines);
				
				if (!foundStart) {
					
					System.out.println("searching for TOC start");
					System.out.println("searching regex : "+prefixRegex);
					Matcher m = pre.matcher(lines);
					int i = 1;
					m.find();
					System.out.println("Found groups : "+m.groupCount());
					
					if (lines.matches(prefixRegex)) {
						try {
							while (i <= m.groupCount()) {
								System.out.println(i+":"+m.group(i));
								i++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// save delete prefix until TOC end was found (below)
						lgPrefix = lg;
						lgPrefixRemainder = m.group(2).trim();
//						lg.getLines().clear();
//						lg.addLine(m.group(2).trim());
						foundStart = true;
						lineGroupContainingTocPrefix = j;
						System.out.println("success");
					}
				
				} else {
					
					System.out.println("searching for TOC end");
					System.out.println("searching regex : "+postfixRegex);
					Matcher m = post.matcher(lines);
					int i = 1;
					m.find();
					System.out.println("Found groups : "+m.groupCount());
					
					if (lines.matches(postfixRegex)) {
						try {
							while (i <= m.groupCount()) {
								System.out.println(i+":"+m.group(i));
								i++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						// remove TOC prefix
						lgPrefix.getLines().clear();
						lgPrefix.addLine(lgPrefixRemainder);
						// remove TOC postfix
						lg.getLines().clear();
						lg.addLine(m.group(1).trim());
						foundEnd = true;
						lineGroupContainingTocPostfix = j;
						System.out.println("success");
					}
					
				}
					if(foundEnd) break;
				j++;
			}

			if (!foundEnd) {
				continue;
			}
			
			// Remove lineGroups before and after TOC
			Iterator<LineGroup> iterator = lineGroups.iterator();
			int lineGroupCounter = 0;
			while (iterator.hasNext()) {
				LineGroup y = iterator.next();
				if (lineGroupCounter < lineGroupContainingTocPrefix) {
					iterator.remove();
				}
				if (lineGroupCounter > lineGroupContainingTocPostfix) {
					iterator.remove();
				}
				lineGroupCounter++;
			}
			
			int lgc = 1;
			System.out.println("Line groups in TOC:");
			for (LineGroup lg : lineGroups) {
				System.out.println(lgc++);
				System.out.println(lg.getLinesAsString());
			}
								
			// c. parse metadata from toc
			return TOCTools.parseTocEntries(toc, lineGroups, config);
	}	

	System.out.println("TOC could not be found !");
	return new ArrayList<Metadata>(); // return list with empty meta-data
	}
		
		

	

//	/**
//	 * Parse footer info, will overwrite previously parsed values in Metadata object.
//	 * @param document
//	 * @param metadata 
//	 * @deprecated
//	 */
//	private void getFooterInfo(Document document, Metadata metadata) {
//
//		if ((config.footerFontIsActive() || config.footerHeightIsActive()) && config.footerDescriptionIsActive()) {
//			
//			// 1. Get footer/header text from xml
//			
//			String xpathForFooter=null;
//			if (config.footerPageIsActive()) {
//				xpathForFooter = buildFooterQuery(config.getFooterPage());
//			} else {
//				try {
//					xpathForFooter = buildFooterQuery(findFirstPage(document));
//				} catch (XPathExpressionException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			LOG.info("Xpath to get footer: "+xpathForFooter);
//			
//			try {
//				NodeList nodeList = (NodeList) xPath.compile(xpathForFooter).evaluate(document, XPathConstants.NODESET);
//				
//				StringBuilder footer = new StringBuilder();
//				for (int i = 0; i < nodeList.getLength(); i++) {
//					footer.append(" "+nodeList.item(i).getTextContent());
//				}
//				
//				String footerText = footer.toString();
////				int x = -1;
////				if (config.footerStartsWithTextIsActive()) {
////					x = footerText.indexOf(config.getFooterStartsWithText());
////				}
////				if (x >= 0) {
////					footerText = StringUtils.substring(footerText, x, x+100);
////					System.out.println("cutting footet text @"+x+" "+footerText);
////				} else {
////					LOG.info("Footer text does not start with :"+config.getFooterStartsWithText()+ "skipping !");
////					return;
////				}
//				
//				// 2. Use footer description to parse parameters (journalTitle, etc.) from footer
//				//    and set parsed values in metadata object
//				LOG.info("Footer text is:"+footerText);
//				LOG.info("Using footer description: "+config.getFooterDescription());
//
//				Metadata.parseFooter(footerText, metadata, config);
//			} catch (XPathExpressionException e) {
//				e.printStackTrace();
//			}
//				
//		} else {
//			LOG.info("Footer parameter in config are not set");
//		}
//	}
	
	
	public static List<Author> splitAuthors(String authors) {
		
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(authors);
		return splitAuthors(tmp);
	}
	
	
	// text.split("\sand\s|\sA		System.out.println();\nND\s|&|,")
	public static List<Author> splitAuthors(List<String> authors) {
	
		
		List<String> authorsSplitted = new ArrayList<String>();
		List<Author> authorList = new ArrayList<Author>();
		for(String text : authors) {
			
			if (text.trim().toLowerCase().equals("and")) continue;
			System.out.println("before split: "+text);
			
			for (String y : text.split("\\sand\\s|\\sAND\\s|&")) {
				
			// for (String y : text.split("\sand\s|\sAND\s|&|,")) { changed due to compiler complains about text block
				y=y.trim();
				y=y.replace("\"", "");
				y=y.replace("„", "");
				y=StringUtils.normalizeSpace(y);
				if (!y.isEmpty()) {
					authorsSplitted.add(y);
				}
				System.out.println("author after split : "+y);
				
				String [] splitByColon = y.split(",");
				List<String> authorEntries = Arrays.asList(splitByColon);
				int i = 0;
				String nextToken = "";
				while (i < authorEntries.size()) {
					String token = authorEntries.get(i).trim();
					if (i+1 < authorEntries.size()) {
						nextToken = authorEntries.get(i+1).trim();
					}
					String[] splitSpace = token.split(" ");
					// multiple tokens
					if (splitSpace.length > 1) {
						// next token can not be given name
						if (nextToken.isEmpty() || nextToken.split(" ").length > 1) {
							Author a = new Author();
							String givenName = "";
							String familyName = "";
							int j = 0;
							while (j < splitSpace.length - 1) {
								// check for not a given name (e.g. de,Mc,O' etc.) 
								// or if a name is known to be a family name
								// but only for the second, third, ff. name parts
								if (j > 0 && (Run.familyNamePrefixes.contains(splitSpace[j]) ||
									(Run.knownFamilyNames.contains(splitSpace[j]) &&
									 !Run.knownGivenNames.contains(splitSpace[j])))) {
									System.out.println("hierhierhier "+splitSpace[j]);
									break;
								} else {
									givenName+= splitSpace[j]+" ";
								}
								j++;
							}
							while (j < splitSpace.length) {								
								familyName+= splitSpace[j]+" ";
								j++;
							}
							a.setGivenName(givenName.trim());
							a.setFamilyName(familyName.trim());
							authorList.add(a);
						} else {
							// next token is single token =>
							// token is family name, given name in next token
							Author a = new Author();
							a.setFamilyName(token);
							a.setGivenName(nextToken);
							authorList.add(a);
							i++;
						}
					} else {
						// single token => is family name, given name in next token
						if (!nextToken.isEmpty()) {
							Author a = new Author();
							a.setFamilyName(token);
							a.setGivenName(nextToken);
							authorList.add(a);
							i++;
						}
					} 
					
					nextToken = "";
					i++;
				}
				
		}
		}
		
		for (Author author : authorList) {
			System.out.println("hierhier "+author.getGivenName());
			System.out.println("hierhier "+author.getFamilyName());
			System.out.println();
		}

		

// Old		
//		// finally join a author with a single name with the subsequent author entry
//		int authorCount = authorsSplitted.size();
//		int j = 0;
//		while (j < authorCount) {
//			if (authorsSplitted.get(j).split(" ").length == 1) {
//				if (j+1 < authorCount) {
//					authorsSplitted.set(j+1, authorsSplitted.get(j+1)+" "+authorsSplitted.get(j));
//					authorsSplitted.set(j, "");
//				}
//			}
//			j++;
//		}
//		// Finally remove empty authors
//		Iterator<String> iterator = authorsSplitted.iterator();
//		while (iterator.hasNext()) {
//			String next = iterator.next();
//			if (next.trim().isEmpty()) iterator.remove();
//		}
//		return authorsSplitted;

		
		return authorList;
	}
	
	
	public static List<String> splitAuthorsOld(List<String> authors) {
	
		
		List<String> authorsSplitted = new ArrayList<String>();
		for(String text : authors) {
			
			if (text.trim().toLowerCase().equals("and")) continue;
			System.out.println("before split: "+text);
			
			for (String y : text.split("\\sand\\s|\\sAND\\s|&|,")) {
			// for (String y : text.split("\sand\s|\sAND\s|&|,")) { changed due to compiler complains about text block
				y=y.trim();
				y=y.replace("\"", "");
				y=y.replace("„", "");
				if (!y.isEmpty()) {
					authorsSplitted.add(StringUtils.normalizeSpace(y));
				}
		}
		}
		
//		if (authorsSplitted.size() == 2) {
//			if(authorsSplitted.get(0).split(" ").length == 1 && authorsSplitted.size() == 2) {
//				String joined = authorsSplitted.get(1)+" "+authorsSplitted.get(0);
//				authorsSplitted.clear();
//				authorsSplitted.add(joined);
//			}
//		}
		
		// finally join a author with a single name with the subsequent author entry
		int authorCount = authorsSplitted.size();
		int j = 0;
		while (j < authorCount) {
			if (authorsSplitted.get(j).split(" ").length == 1) {
				if (j+1 < authorCount) {
					authorsSplitted.set(j+1, authorsSplitted.get(j+1)+" "+authorsSplitted.get(j));
					authorsSplitted.set(j, "");
				}
			}
			j++;
		}
		// Finally remove empty authors
		Iterator<String> iterator = authorsSplitted.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (next.trim().isEmpty()) iterator.remove();
		}
		
		
		return authorsSplitted;
	}
	
	
	
	private List<LineGroup> getLineGroups(Document document, boolean cluster, boolean ffPages, Integer maxLinegroups) throws XPathExpressionException {
		
		int	numberOfFirstPage = findFirstPage(document);
		int numberOflastPage = numberOfFirstPage;
		if (ffPages) {
			numberOflastPage = numberOfFirstPage+10; // 10 pages should be long enough for any TOC
		}
		
		HashMap<Integer, Node> nmap = new HashMap<Integer, Node>();
		
		int pageNr = numberOfFirstPage;
		while (pageNr <= numberOflastPage) {
			
			String xPathForPageNodes = getPageNodeQuery(pageNr);
			System.out.println(xPathForPageNodes);
			NodeList nodeList = (NodeList) xPath.compile(xPathForPageNodes).evaluate(document, XPathConstants.NODESET);
			
			LOG.fine(nodeList.getLength()+" Results for xPath: "+xPathForPageNodes);
			System.out.println("Results for xPath: "+xPathForPageNodes);
			if (nodeList.getLength() == 1) {
				xPathForPageNodes = xPathForPageNodes.replaceFirst("/text\\(\\)$", "");
			}
			
			// get top values
			HashMap<Integer, List<Node>> nmap_1 = new HashMap<Integer, List<Node>>();
			for (int i = 0; i < nodeList.getLength(); i++) {
		
				Node y = nodeList.item(i);
				int topValue = Integer.parseInt(y.getAttributes().getNamedItem("top").getNodeValue());
				if (!nmap_1.containsKey(topValue)) {
					List<Node> x = new ArrayList<Node>();
					x.add(y);
					nmap_1.put(topValue, x);
				} else {
					List<Node> x = nmap_1.get(topValue);
					x.add(y);
					nmap_1.put(topValue, x);
				}
			}
			
			//HashMap<Integer, Node> nmap = new HashMap<Integer, Node>();
			HashMap<Integer, Node> left2Node = new HashMap<Integer, Node>();
			for (Integer tv : nmap_1.keySet()) {
				
				List<Node> nl = nmap_1.get(tv);
				if (nl.size() == 1) {
					nmap.put((pageNr-numberOfFirstPage)*100000+tv*100, nl.get(0));		
				} else {
					// sort nodes with same top-value by left-value
					left2Node.clear();
					for (Node y : nl) {
						int left = Integer.parseInt(y.getAttributes().getNamedItem("left").getNodeValue());
						left2Node.put(left, y);
					}
					List<Integer> leftValues = new ArrayList<Integer>(left2Node.keySet());
					Collections.sort(leftValues);
					int j=0;
					for (int lv : leftValues) {
						nmap.put((pageNr-numberOfFirstPage)*100000+(tv*100)+j, left2Node.get(lv));
						j++;
					}
				}
			}
		pageNr++;
		}
		
//		HashMap<Integer, Node> nmap = new HashMap<Integer, Node>();
//		for (int i = 0; i < nodeList.getLength(); i++) {
//			
//			Node y = nodeList.item(i);
//			
//			int topValue = Integer.parseInt(y.getAttributes().getNamedItem("top").getNodeValue());
//			//System.out.println(topValue+":"+y.getTextContent());
//			int top = 100 * topValue;
//			if (!nmap.containsKey(top)) { 
//				nmap.put(top, y);		
//			} else {
//				int j=0;
//				while (nmap.containsKey(top+j)) {
//					//System.out.println("top collision :"+(top+j)+"  "+y.getTextContent());
//				j++;
//				}
//				nmap.put(top+j, y);
//			}
//			//title.append(nodeList.item(i).getTextContent());
//		}
		
		ArrayList<Integer> tops = new ArrayList<Integer>(nmap.keySet());
		Collections.sort(tops);
		int j=0;
		String title="";
		ArrayList<LineGroup> lgs = new ArrayList<LineGroup>();
		int lastFont=-1;
		int lastSize=-1;
		int font;
		int fontSize;
		String text;
		LineGroup lg=null;
		int order=1;
		int boldItalic=0;
		Boolean isBold = false;
		Boolean lastBold = null;
		Boolean isItalic = false;
		Boolean lastItalic = null;
		boolean condition = false;
		while (j < tops.size()) {
			
			//parse at most maxLinegroups
			if (lgs.size() >= maxLinegroups) break;
			
			Node nextLine = nmap.get(tops.get(j));
			text = nextLine.getTextContent();
			if (text.trim().isEmpty()) {j++;continue;}; // skip empty line

			boldItalic = getTextNodeItalicBold(nextLine);
			font = Integer.parseInt(nextLine.getAttributes().getNamedItem("font").getNodeValue());
			fontSize = Integer.parseInt(nextLine.getAttributes().getNamedItem("height").getNodeValue());
			isBold = (boldItalic >= 10);
			isItalic = (boldItalic == 11 || boldItalic == 1);
			
			// ====================================================
			
			switch (config.getBindSequentialLinesOperator().toLowerCase().trim()) {
			
			case "or":
				
				condition = false; // init
				
				if (config.bindSequentialLinesByFontIsActive()) {
					condition = condition || (lastFont != font);
				}
				if (config.bindSequentialLinesByFontSizeIsActive()) {
					condition = condition || (lastSize != fontSize);
				}
				if (config.bindSequentialLinesByBoldFontIsActive()) {
					condition = condition || (lastBold != isBold);
				}
				if (config.bindSequentialLinesByItalicFontIsActive()) {
					condition = condition || (lastItalic != isItalic);
				}
				break;
				
			case "and":
				
				condition = true; // init
				
				if (config.bindSequentialLinesByFontIsActive()) {
					condition = condition && (lastFont != font);
				}
				if (config.bindSequentialLinesByFontSizeIsActive()) {
					condition = condition && (lastSize != fontSize);
				}
				if (config.bindSequentialLinesByBoldFontIsActive()) {
					condition = condition && (lastBold != isBold);
				}
				if (config.bindSequentialLinesByItalicFontIsActive()) {
					condition = condition && (lastItalic != isItalic);
				}
				break;
				
			default:
				System.out.println("Error, invalid value of config-parameter bindSequentialLinesOperator : "+config.getBindSequentialLinesOperator());
				System.exit(1);
			}
			
			

			if (condition) {
					lg = new LineGroup(text, font, fontSize, order, isBold, isItalic);
					lgs.add(lg);
					order++;
				} else {
					lg.addLine(text);
			}
			
//			if (config.bindSequentialLinesByFontIsActive() &&
//				config.bindSequentialLinesByFontSizeIsActive()
//					) {
//				if(lastFont != font || lastSize != fontSize) {
//					lg = new LineGroup(text, font, fontSize, order);
//					lgs.add(lg);
//					order++;
//				} else {
//					lg.addLine(text);
//				}
//			} else {
//			
//			
//				if (config.bindSequentialLinesByFontIsActive()) {
//					if(lastFont != font) {
//						lg = new LineGroup(text, font, fontSize, order);
//						lgs.add(lg);
//						order++;
//					} else {
//						lg.addLine(text);
//					}
//				}
//				
//				if (config.bindSequentialLinesByFontSizeIsActive()) {
//					if(lastSize != fontSize) {
//						lg = new LineGroup(text, font, fontSize, order);
//						lgs.add(lg);
//						order++;
//					} else {
//						lg.addLine(text);
//					}
//				}
//				
//			}
			
			// copy
			lastFont=font;
			lastSize=fontSize;
			lastBold=isBold;
			lastItalic=isItalic;
			
			j++;
		}
		
		// print groups
		System.out.println("Top Line Groups");
		for(LineGroup g : lgs) {
			System.out.println(g.print());
		}
		
		if (cluster) {
		List<LineGroup> clustered = LineGroup.clusterGroups(lgs, config);
		
		// group lines by font
		System.out.println("** Clustered Line Groups **");
		for(LineGroup g : clustered) {
			System.out.println(g.print());
		}
			return clustered;
		} else {
			return lgs;
		}
		
	}
	
	
	/**
	 * Check for bold/italic present as childs in node
	 * @param node
	 * @return 10=bold, 1=italic, 11=italic+bold otherwise 0
	 */
	private int getTextNodeItalicBold (Node node) {
		
		int bold = 0;
		int italic = 0;
		
		NodeList childs = node.getChildNodes();
		int yy = 0;
		String nodeName="";
		while (yy < childs.getLength()) {
			
			nodeName = childs.item(yy).getNodeName();
			
			switch (nodeName) {
			
			case "i":
				//System.out.println("italic");
				italic = 1;
				break;
				
			case "b":
				//System.out.println("bold");
				bold = 10;
				break;
			
			default :
				break;
			}
			yy++;
		}

		return bold+italic;
	}
	
//  split text; line1="also", line2="relevant",line3=". The concept of mediation as discussed here is in some ways"
//	<text top="472" left="80" width="473" height="20" font="14">
//	also
//	<i>relevant</i>
//	. The concept of mediation as discussed here is in some ways
//	</text>
	
	
	private String getPageNodeQuery(int pageNr) {
	
		return "pdf2xml/page[@number = "+pageNr+"]/text";
//		return "pdf2xml/page[@number = "+pageNr+"]/text[@height>= 0]/descendant::text()";

	}
	
	
	private String getTextSample(Document document, int firstPage, int lastPage) {
		
		String sampleText = "";
		
		try {
			int pageNr = firstPage;
			while (pageNr <= lastPage) {
				
				String xPathForPageNodes = getPageNodeQuery(pageNr);
				System.out.println(xPathForPageNodes);
				NodeList nodeList = (NodeList) xPath.compile(xPathForPageNodes).evaluate(document, XPathConstants.NODESET);
				
				LOG.fine(nodeList.getLength()+" Results for xPath: "+xPathForPageNodes);
				System.out.println("Results for xPath: "+xPathForPageNodes);
				if (nodeList.getLength() == 1) {
					xPathForPageNodes = xPathForPageNodes.replaceFirst("/text\\(\\)$", "");
				}
				
				// get text values of nodes
				for (int i = 0; i < nodeList.getLength(); i++) {
			
					Node y = nodeList.item(i);
					String line = y.getTextContent();
					if (line != null) {
						sampleText+=line+" ";
					}
				}
				
			pageNr++;
			}
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("sample text");
		//System.out.println(sampleText);
		return sampleText;
	}
	
	
	public static void main(String[] args) {
		
		String withSpace = "Points of convergence between functional and   formal approaches to syntactic analyis";
		//System.out.println(withSpace.replace("  ", " "));
		System.out.println(StringUtils.normalizeSpace(withSpace));
		
		
//		String text = "Table of Contents  A Corpus Study of Sakha (Yakut) Converbs: A Case of Baran………………………    1";
//		String regex = "(.*Table of Contents)(.*)";
//		
//		text = "Proceedings of the 24th NWLC";
//		regex = "(.*)(Proceedings of the.*)";
//		System.out.println(text.matches(regex));
//		
//		Pattern p = Pattern.compile(regex);
//		Matcher m = p.matcher(text);
//		int i = 1;
//		m.find();
//		System.out.println(m.groupCount());
//		
//		try {
//			while (i <= m.groupCount()) {
//				System.out.println(i+":"+m.group(i));
//				i++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
		
		
		
		//text ="Proceedings of the 24th NWLC, 3-4 May 2008, Seattle, WA";
		//System.out.println(text.matches(".*?Proceedings of the.*?"));
		
	}
}


//int lineCount = lg.getLines().size();
//int i = 0;
//while (i < lineCount) {
//
//	String line = lg.getLines().get(i);
//	
//	if (!foundStart) {
//		
//		System.out.println("searching start .. "+lineCount);
//		System.out.println("text "+line);
//		System.out.println("prefix "+tds.getTocStartsAfterText());
//		
//		int start = line.indexOf(tds.getTocStartsAfterText(), 0);
//		if (start > -1) {
//			// remove prefix text from line
//			lg.getLines().set(i, line.substring(start+tds.getTocStartsAfterText().length()));
//			foundStart=true;
//			lineGroupContainingTocPrefix = j;
//			System.out.println("foundStart:");
//			System.out.println(line);
//			System.out.println(line.substring(start+tds.getTocStartsAfterText().length()));
//			continue;
//		}
//	} else {
//		int end = line.indexOf(tds.getTocEndsBeforeText(), 0);
//		if (end > -1) {
//			// remove suffix text from line
//			lg.getLines().set(i, line.substring(0, end));
//			lineGroupContainingTocPostfix = j;
//			// finished
//			System.out.println("foundEnd:");
//			System.out.println(line);
//			System.out.println(line.substring(0, end));
//			foundEnd=true;
//			break;
//		}
//	}
//	
//	i++;
//}
//j++;
//if(foundEnd) break;
//}
