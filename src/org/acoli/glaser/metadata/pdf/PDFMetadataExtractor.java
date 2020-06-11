package org.acoli.glaser.metadata.pdf;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PDFMetadataExtractor {

	// TODO: Make these into lists. e.g. LREC 2018 needs +1 title heigth
	int authorHeight = -1;
	int authorFont = -1;
	int titleHeight = -1;
	int titleFont = -1;
	int pageHeight = -1;
	int pageFont = -1;
	XPath xPath;

	public PDFMetadataExtractor() {
		this.xPath = XPathFactory.newInstance().newXPath();
	}


	String buildTitleQuery(int numberOfFirstPage) {
		String xPathForTitle = "pdf2xml/page[@number = "+numberOfFirstPage+"]/text[";
		List<String> conditions = new ArrayList<>();
		if (this.titleFont >= 0)
			conditions.add("@font="+this.titleFont);
		if (this.titleHeight >= 0)
			conditions.add("@height="+this.titleHeight);
		xPathForTitle += String.join(" and ", conditions);
		xPathForTitle += "]/text()";
		System.err.println("Constructed XPath: "+xPathForTitle);
		return xPathForTitle;

	}
	String getTitle(Document document) throws XPathExpressionException {
		int numberOfFirstPage = findFirstPage(document);
		String xPathForTitle = buildTitleQuery(numberOfFirstPage);
		NodeList nodeList = (NodeList) xPath.compile(xPathForTitle).evaluate(document, XPathConstants.NODESET);
		System.err.println(nodeList.getLength()+" Results for xPath: "+xPathForTitle);
		if (nodeList.getLength() == 1) {
			xPathForTitle = xPathForTitle.replaceFirst("/text\\(\\)$", "");
		}
		nodeList = (NodeList) xPath.compile(xPathForTitle).evaluate(document, XPathConstants.NODESET);
		System.err.println(nodeList.getLength()+" Results for xPath: "+xPathForTitle);
		StringBuilder title = new StringBuilder();
		for (int i = 0; i < nodeList.getLength(); i++)
			title.append(nodeList.item(i).getTextContent());
		return String.join(" ", title);
	}
//	@Deprecated
//	static String getTitle(Document document) throws XPathExpressionException {
//		// TODO: Fix missing spaces between line breaks
//		XPath xPath = XPathFactory.newInstance().newXPath();
//		Integer numberOfFirstPage = findFirstPage(document);
//		for (Integer fontID : new ArrayList<>(Arrays.asList(0, 21))) {
//			StringBuilder title = getTitleCandidatesWithFontID(document, numberOfFirstPage, fontID);
//			if (title.length() > 0)
//				return String.join(" ", title);
//		}
//		return "";
//	}

	static double findPositionOfAbstract(Document document) throws XPathExpressionException {
//		<text top="293" left="416" width="56" height="13" font="3">Abstract</text>
		XPath xPath = XPathFactory.newInstance().newXPath();
		// below ugly XPath 1.0 way to say "position of Element that matches x"
		Integer indexOfFirstPage = findFirstPage(document);
		String xPathForAbstractPosition = "count(pdf2xml/page[@number="+indexOfFirstPage+"]/text[text() = 'Abstract']/preceding-sibling::text)+1";
		String stupidXPathForAbstractPosition = "count(pdf2xml/page[@number="+indexOfFirstPage+"]/text/b[text() = 'Abstract']/preceding::text)+1";
		// TODO: sometimes it's <b>Abstract </b> -.-
		double abstractPosition = 0;
		try {
			abstractPosition = (double) xPath.compile(xPathForAbstractPosition).evaluate(document, XPathConstants.NUMBER);
			if (abstractPosition == 1.0) {
				System.out.println("Didnt find..");
				System.err.println(xPath.compile(stupidXPathForAbstractPosition).evaluate(document));
				abstractPosition = (double) xPath.compile(stupidXPathForAbstractPosition).evaluate(document, XPathConstants.NUMBER);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.err.println("Found abstract position "+abstractPosition);
		return abstractPosition;
	}
	static Integer findFirstPage(Document document) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xPathForFirstPageNumber = "(pdf2xml/page/@number[not(. > pdf2xml/page/@number)])[1]";
		Double result = (double) xPath.compile(xPathForFirstPageNumber).evaluate(document, XPathConstants.NUMBER);
		System.err.println("Found first page: "+result);
		return result.intValue();
	}
	static void evaluateTestPath(Document document) {
		XPath xPath = XPathFactory.newInstance().newXPath();

	}
	static NodeList getAuthorCandidatesWithHeight(Document document, int height) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		double firstPage = findFirstPage(document);
		double indexOfAbstract = findPositionOfAbstract(document);
		String xPathForAuthors = "pdf2xml/page[@number="+firstPage+"]/text[@height='"+height+"' and position() < "+indexOfAbstract+"]"; // we want everything BEFORE the Abstract starts
				// so we check the position, note that the order is in reverse, thus > and not <.
//				+ " < 18.0]";
//		String xPathForAuthors = "pdf2xml/page[@number=1]/text[@height=16 and position()<7]";
//				+ "< count(pdf2xml/page[@number=1]/text[text() = 'Abstract']/preceding-sibling::text)+1]";
//        double authorNodes = (double) xPath.compile(xPathForAuthors).evaluate(document, XPathConstants.NUMBER);
		NodeList result = (NodeList) xPath.compile(xPathForAuthors).evaluate(document, XPathConstants.NODESET);
		System.err.println("Found "+result.getLength()+" candidates for author.");
		return result;
	}
	String buildAuthorQuery(int numberOfFirstPage, int indexOfAbstract) {
		String xPathForAuthors = "pdf2xml/page[@number = " + numberOfFirstPage + "]/text[position() < " + indexOfAbstract;
		if (this.authorFont >= 0)
			xPathForAuthors += " and @font=" + this.authorFont;
		if (this.authorHeight >= 0)
			xPathForAuthors += " and @height=" + this.authorHeight;
		xPathForAuthors += "]/text()"; // TODO: maybe without text?
		System.err.println("Constructed XPath: " + xPathForAuthors);
		return xPathForAuthors;
	}

	/**
	 * TODO: Make List of Authors
	 * @param document
	 * @return
	 */
	List<String> getAuthors(Document document) throws XPathExpressionException {
		// TODO: whats with multiple candidates for a given folder??
		int numberOfFirstPage = findFirstPage(document);
		double indexOfAbstract = findPositionOfAbstract(document);
		String xPathForAuthors = buildAuthorQuery(numberOfFirstPage, (int) indexOfAbstract);
		NodeList nodeList = (NodeList) xPath.compile(xPathForAuthors).evaluate(document, XPathConstants.NODESET);
		List<String> authors = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			System.err.println(nodeList.item(i));
			String candidate = nodeList.item(i).getTextContent();
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

	String buildPageQuery() {
		String xPathForPages = "//page/text[";
		List<String> conditions = new ArrayList<>();
		if (this.authorFont >= 0)
			conditions.add("@font=" + this.authorFont);
		if (this.authorHeight >= 0)
			conditions.add("@height=" + this.authorHeight);
		xPathForPages += String.join(" and ", conditions);
		xPathForPages += "]";
		System.err.println("Constructed XPath: " + xPathForPages);
		return xPathForPages;

	}

	static List<Integer> getPageNumberCandidatesWithHeight(Document document, int height) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xPathForPageNumbers = "//page/text[@height = "+height+"]"; // TODO: Maybe really check for postion, pdf 107 causes error with footnote
		NodeList pageNumbersNodeList = (NodeList) xPath.compile(xPathForPageNumbers).evaluate(document, XPathConstants.NODESET);
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = 0; i < pageNumbersNodeList.getLength(); i++) {
			String candidatePageNumber = pageNumbersNodeList.item(i).getTextContent();
			if (candidatePageNumber.matches("-?[0-9]+")) {
				pageNumbers.add(Integer.parseInt(pageNumbersNodeList.item(i).getTextContent()));
			}
		}
		System.err.println("Found "+pageNumbersNodeList.getLength()+" candidates for pageNumbers.");
		return pageNumbers;
	}
	List<Integer> getPageNumbers(Document document) throws XPathExpressionException {
		String xPathForPageNumbers = buildPageQuery();
		List<Integer> pageNumbers = new ArrayList<>();
		NodeList pageNumbersNodeList = (NodeList) xPath.compile(xPathForPageNumbers).evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < pageNumbersNodeList.getLength(); i++) {
			String candidatePageNumber = pageNumbersNodeList.item(i).getTextContent();
			if (candidatePageNumber.matches("-?[0-9]+")) {
				pageNumbers.add(Integer.parseInt(pageNumbersNodeList.item(i).getTextContent()));
			}
		}
		Collections.sort(pageNumbers);
		return pageNumbers;
	}

	public Metadata getMetadata(Document paper) {
		Metadata md = new Metadata();
		try {
			String title = getTitle(paper);
			System.err.println("Title: "+title);
			md.title = title;
			List<String> authorList = getAuthors(paper);
			md.authors = authorList;
			List<Integer> pageNumbersList = getPageNumbers(paper);
			md.setPageNumbers(pageNumbersList);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return md;
	}

	Metadata extract(Document paper) {
		return null;
	}
}
