package org.acoli.sc.extract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.acoli.sc.start.Run;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Metadata implements Serializable{
	
	private static final long serialVersionUID = -5375747454403230156L;
	
	private String ID;
	public String fileName="";
	public String title="";
	public String subTitle="";
	public String journalTitle="";
	public Integer year=-1;
	public String authorString = "";
	//public List<String> authors=new ArrayList<String>();
	public List<Author> authors =new ArrayList<Author>();
	public String location="";
	public String booktitle="";
	public int beginPage=-1;
	public int endPage=-1;
	public int volume=-1;
	public int issue=-1;
	public String journalNote="";
	private String url="";
	private String affiliation="";
	private List<String> languagesISO639Codes = new ArrayList<String>();; // TODO set appropriate type (e.g. String)
	private String publisher = "";
	private String date = "";
	private String place = "";


	
	public boolean hasTitle() {
		return  title != null;
	}
	public boolean hasAuthors() {
		return (!authorString.trim().isEmpty());
		//return authors != null;
	}

	public int getExtent(){
		if (beginPage == -1 || endPage == -1) {
			return -1;
		} else {
			return endPage- beginPage;
		}
	}

	public String getPages(){
		int pages = getExtent();
		return String.valueOf(pages);
	}
	
	public void setPageNumbers(List<Integer> pageNumbers) {
		if (pageNumbers.size()>1) {
			this.beginPage = pageNumbers.get(0);
			this.endPage = pageNumbers.get(pageNumbers.size() - 1);
		}
	}

	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	
	public String getSubTitle() {
		return this.subTitle;
	}


	public String getJournalTitle() {
		return journalTitle;
	}


	public void setJournalTitle(String journalTitle) {
		this.journalTitle = journalTitle;
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}

	
	public String getAuthorString() {
		return authorString;
	}
	
	
	public void setAuthorString(String authorString) {
		this.authorString = authorString;
	}
	

	public List<Author> getAuthors() {
		return authors;
	}


	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getBooktitle() {
		return booktitle;
	}


	public void setBooktitle(String booktitle) {
		this.booktitle = booktitle;
	}


	public int getBeginPage() {
		return beginPage;
	}


	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}


	public int getEndPage() {
		return endPage;
	}


	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}


	public int getVolume() {
		return volume;
	}


	public void setVolume(int volume) {
		this.volume = volume;
	}


	public int getIssue() {
		return issue;
	}


	public void setIssue(int issue) {
		this.issue = issue;
	}


	public String getJournalNote() {
		return journalNote;
	}


	public void setJournalNote(String journalNote) {
		this.journalNote = journalNote;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		// TODO use setUrl (use real URL if present)
		this.url = "file://"+Run.dataFolder+"/"+FilenameUtils.getBaseName(fileName)+".pdf";
	}

	
	@Override
	public String toString() {
		
		String output = "";
		output+="METADATA :\n";
		output+="fileName "+fileName+"\n";
		output+="title :"+title+"\n";
		output+="subTitle :"+subTitle+"\n";
		output+="authors :"+authorString+"\n";
//		for (Author a : getAuthors()) {
//			output+="given :"+a.getGivenName()+"\n";
//			output+="family :"+a.getFamilyName()+"\n";
//		}
		output+="journal :"+journalTitle+"\n";
		output+="volume :"+volume+"\n";
		output+="issue :"+issue+"\n";
		output+="year :"+year+"\n";
		output+="beginPage :"+beginPage+"\n";
		output+="endPage :"+endPage+"\n";
		output+="date :"+date+"\n";
		output+="place :"+place+"\n";
		if (languagesISO639Codes ==null || languagesISO639Codes.isEmpty()) {
			output+="language :\n";
		} else {
			output+="language :"+languagesISO639Codes.get(0)+"\n";
		}
		
		return output;
	}
	
//	@Override
//	public String toString() {
//		StringBuilder repr = new StringBuilder();
//		for (Object obj : Arrays.asList(title, String.join(", ", authors),booktitle,location, journalTitle, year, beginPage + "-" + endPage)) {
//			repr.append(obj);
//			repr.append("\n\t");
//		}
//		return repr.toString();
//	}

	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setDate(String date) {
		this.date=date;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getFilenameWithoutExtension() {
		return FilenameUtils.getBaseName(fileName);
	}
	
	
	public boolean fileNameIsActive() {
		return sia(fileName);
	}
	public boolean titleIsActive() {
		return sia(title);
	}
	public boolean journalTitleIsActive() {
		return sia(journalTitle);
	}
	public boolean locationIsActive() {
		return sia(location);
	}
	public boolean booktitleIsActive() {
		return sia(booktitle);
	}
	public boolean volumeIsActive() {
		return iia(volume);
	}
	public boolean journalNoteIsActive() {
		return sia(journalNote);
	}
	public boolean issueIsActive() {
		return iia(issue);
	}
	public boolean urlIsActive() {
		return sia(url);
	}
	public boolean yearIsActive() {
		return iia(year);
	}
	public boolean beginPageIsActive() {
		return iia(beginPage);
	}
	public boolean endPageIsActive() {
		return iia(endPage);
	}
	public boolean authorsIsActive() {
		return !authors.isEmpty();
	}
	public boolean publisherIsActive() {
		return sia(publisher);
	}
	public boolean placeIsActive() {
		return sia(place);
	}
	public boolean dateIsActive() {
		return sia(date);
	}
	
	
	public boolean iia(int i) {
		return i>0;
	}
	
	public boolean sia(String y) {
		return !y.trim().isEmpty();
	}
	
	public boolean lsia(List<String> l) {
		return !l.isEmpty();
	}
	
	
	
	public static boolean parseTocEntryPart(Metadata md, String text, ContentDescription tp) {
		
		return matchMetadataDescriptionRegex(md, text, tp.getContentDescription());
	}
	
	
	/**
	 * Parse info from complex string 
	 * @param config 
	 * @param string
	 */
	public static boolean parseFooter(String footerText, Metadata md, PDFExtractionConfiguration config) {
		
		// parse variable names, delimiters from description
		// description must start with a variable name
		
		if (footerText.trim().isEmpty()) {
			System.out.println("Error : footer text empty, "+md.fileName);
			return false;
		}
		
		if (config == null) {
			System.out.println("Error : parserFooter argument PDFExtractionConfigurationö is null");
			return false;
		}
		
		boolean success = false;
		int count=1;
		for (Footer ft : config.getFooterDescriptions()) {
			
			System.out.println("checking footer : "+(count++));
			System.out.println("footerDescription : "+ft.getFooterDescription());
			System.out.println("footerStartsWithText : "+ft.getFooterStartsWithText());
		
			// apply parameter footerStartsWithText
			int x = -1;
			x = footerText.indexOf(ft.getFooterStartsWithText());
			if (x >= 0) {
				footerText = StringUtils.substring(footerText, x, x+300);
				System.out.println("cutting footer text @"+x+" "+footerText);
				success = true;
			} else {
				System.out.println("Footer text does not contain :"+ft.getFooterStartsWithText()+ " skipping !");
				success=false; continue;
				//return false;
			}
			
			
			success = matchMetadataDescriptionRegex(md, footerText, ft.getFooterDescription());
			// stop testing if footer description has been matched
			if (success) break;
			
//			String regex="";
//			String token;
//			HashMap <Integer, String> variableOrdering = new HashMap <Integer, String>();
//			int variableID = 0;
//			StringTokenizer st = new StringTokenizer(ft.getFooterDescription()," ,;-()[:].!@",true);
//			
//			System.out.println(ft.getFooterDescription());
//	
//			while (st.hasMoreTokens()) {
//				
//				token = st.nextToken();
//				//System.out.println("token:"+token);
//				switch(token) {
//				
//				case "journalTitle":
//					variableOrdering.put(variableID++, "journalTitle");
//					regex+="(.+)";
//					break;
//					
//				case "volume":
//					variableOrdering.put(variableID++, "volume");
//					regex+="(\\d+)";
//					break;
//					
//				case "issue":
//					variableOrdering.put(variableID++, "issue");
//					regex+="(\\d+)";
//					break;
//					
//				case "year":
//					variableOrdering.put(variableID++, "year");
//					regex+="(\\d+)";
//					break;
//				
//				case "beginPage":
//					variableOrdering.put(variableID++, "beginPage");
//					regex+="(\\d+)";
//					break;
//				
//				case "endPage":
//					variableOrdering.put(variableID++, "endPage");
//					regex+="(\\d+)";
//					break;
//				
//				case "(":
//				case ")":
//				case "[":
//				case "]":
//				case ".":
//				case ":":
//					regex+="\\"+token;
//					break;
//					
//				case ",":
//					regex+="[,\\s]{1}";
//					break;
//				
//				case "-":
//				case "–":
//					regex+=".";
//					break;
//				
//				case " ":
//					break;
//					
//				case "@":
//					regex+="\\s+";
//					break;
//					
//				case "title":
//					variableOrdering.put(variableID++, "title");
//					regex+="(.+)";
//					break;
//					
//				case "authors":
//					variableOrdering.put(variableID++, "authors");
//					regex+="(.+)";
//					break;
//					
//				default:
//					regex+=token;
//					break;
//				}
//				
//				regex+="\\s*";
//			}
//			
//			//regex+=".*";
//			
//			System.out.println(regex);
//			System.out.println("checking footer text: "+footerText);
//				
//			Pattern p = Pattern.compile(regex);
//			Matcher m = p.matcher(footerText);
//			int i = 1;
//			m.find();
//			//System.out.println("matched groups:"+ m.groupCount());
//			
//			try {
//				while (i <= m.groupCount()) {
//					System.out.println(i+":"+m.group(i));
//					i++;
//				}
//			} catch (Exception e) {
//				//e.printStackTrace();
//				System.out.println("fail");
//				success=false;
//			}
//			
//			if (success) {
//				
//				// get results
//				for (int position : variableOrdering.keySet()) {
//					String varName = variableOrdering.get(position);
//					
//					//System.out.println(position+" "+variableOrdering.get(position));
//		
//					position+=1; // adjust group index
//					switch (varName) {
//					
//					case "journalTitle":
//						md.setJournalTitle(m.group(position));
//						System.out.println("nono "+m.group(position));
//						break;
//					
//					case "volume":
//						md.setVolume(Integer.parseInt(m.group(position)));
//						break;
//						
//					case "issue":
//						md.setIssue(Integer.parseInt(m.group(position)));
//						break;
//						
//					case "year":
//						md.setYear(Integer.parseInt(m.group(position)));
//						break;
//					
//					case "beginPage":
//						md.setBeginPage(Integer.parseInt(m.group(position)));
//						break;
//					
//					case "endPage":
//						md.setEndPage(Integer.parseInt(m.group(position)));
//						break;
//						
//					case "title":
//						md.setTitle(m.group(position));
//						break;
//						
//					case "authors":
//						md.getAuthors().add(m.group(position));
//						break;
//					}
//				}
//				
//				// stop testing if footer description has been matched
//				break;
//			}
		}
		if (success) {
			System.out.println("success");
			return true;
		} else {
			System.out.println("fail");
			return false;
		}
		
//		System.out.println(md.journalTitle);
//		System.out.println(md.issue);
//		System.out.println(md.year);
//		System.out.println(md.beginPage);
//		System.out.println(md.endPage);
		
	}
	
	/**
	 * Parse metadata from text string by using metadata description.
	 * @param md
	 * @param text Text to be analyzed
	 * @param mdDescription
	 * @param config TODO
	 * @return
	 */
	public static boolean matchMetadataDescriptionRegex(Metadata md, String text, String mdDescription) {
		
		boolean success=false;
		String regex="";
		String token;
		HashMap <Integer, String> variableOrdering = new HashMap <Integer, String>();
		int variableID = 0;
		StringTokenizer st = new StringTokenizer(mdDescription," ,;-()[:].!@'–",true);
		String notGreedy = "?";
		
		System.out.println(mdDescription);
		
		KeywordDefinition kwd = Run.activeKeywordDefinition;
		
		boolean literal=false;
		
		while (st.hasMoreTokens()) {
			
			token = st.nextToken();
			System.out.println("token:"+token);
			
			// start, end of literal regex expression, e.g. '\\.+'
			if (token.equals("'")) {
				if (literal) {
					System.out.println("end of literal regex");
					literal=false;
				} else {
					System.out.println("start of literal regex");
					literal=true;
				}
				continue;
			}
			
			if (literal) {
				regex+=token;
				continue;
			}
			
			if (token.endsWith("?")) {
				token = token.substring(0,token.length()-1);
				notGreedy="?";
			} else {
				notGreedy="";
			}
			
			switch(token) {
				
			case "title":
				//notGreedy="?"; // not greedy by default
				variableOrdering.put(variableID++, "title");
				regex+="("+kwd.getTitle()+notGreedy+")";
				break;
				
			case "author":
				variableOrdering.put(variableID++, "author");
				regex+="("+kwd.getAuthor()+notGreedy+")";
				break;
			
			case "journalTitle":
				variableOrdering.put(variableID++, "journalTitle");
				regex+="("+kwd.getJournalTitle()+notGreedy+")";
				break;
				
			case "volume":
				variableOrdering.put(variableID++, "volume");
				regex+="("+kwd.getVolume()+")";
				//regex+="(\\d+)";
				break;
				
			case "issue":
				variableOrdering.put(variableID++, "issue");
				regex+="("+kwd.getIssue()+")";
				break;
				
			case "year":
				variableOrdering.put(variableID++, "year");
				regex+="("+kwd.getYear()+")";
				break;
				
			case "beginPage":
				variableOrdering.put(variableID++, "beginPage");
				regex+="("+kwd.getBeginPage()+")";
				break;
			
			case "endPage":
				variableOrdering.put(variableID++, "endPage");
				regex+="("+kwd.getEndPage()+")";
				break;
				
			case "date":
				variableOrdering.put(variableID++, "date");
				regex+="("+kwd.getDate()+notGreedy+")";
				break;
				
			case "place":
				variableOrdering.put(variableID++, "place");
				regex+="("+kwd.getPlace()+notGreedy+")";
				break;
			
			case "(":
			case ")":
			case "[":
			case "]":
			case "{":
			case "}":
			case "?":
			case "!":
			case "*":
			case "^":
			case "$":
			case "+":
			case "=":
			case "\\":
			case ".":
			case ":":
			case ",":	
				regex+="\\"+token;
				break;
				
//			case ",":
//				regex+="[,]{1}";
//				break;
			
			case "-":
			case "–":
				regex+=".";
				break;
			
			case " ":
				break;
				
			case "@":
				regex+="\\s+";
				break;
				
			default:
				regex+=token;
				break;
			}
			
			regex+="\\s*";
		}
		
		if (literal) {
			System.out.println("Error : 'Missing closing quote in regex expression -> "+mdDescription);
			return false;
		}
		
		//regex+=".*";
		
		System.out.println(regex);
		System.out.println("checking text: "+text);
		System.out.println("with regex: "+regex);
			
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		int i = 1;
		m.find();
		//System.out.println("matched groups:"+ m.groupCount());
		
		try {
			while (i <= m.groupCount()) {
				System.out.println(i+":"+m.group(i));
				i++;
			}
			success = true;
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("fail");
			success=false;
		}
		
		if (success) {
			
			// get results
			for (int position : variableOrdering.keySet()) {
				String varName = variableOrdering.get(position);
				
				//System.out.println(position+" "+variableOrdering.get(position));
	
				position+=1; // adjust group index
				switch (varName) {
				
				case "journalTitle":
					md.setJournalTitle(m.group(position));
					break;
				
				case "volume":
					md.setVolume(Integer.parseInt(m.group(position)));
					break;
					
				case "issue":
					md.setIssue(Integer.parseInt(m.group(position)));
					break;
					
				case "year":
					md.setYear(Integer.parseInt(m.group(position)));
					break;
				
				case "beginPage":
					md.setBeginPage(Integer.parseInt(m.group(position)));
					break;
				
				case "endPage":
					md.setEndPage(Integer.parseInt(m.group(position)));
					break;
					
				case "title":
					md.setTitle(m.group(position));
					break;
					
				case "author":
					md.setAuthorString(m.group(position));
					break;
					
				case "date":
					md.setDate(m.group(position));
					break;
					
				case "place":
					md.setPlace(m.group(position));
					break;
				}
			}		
		}
		return success;
	}
	
	
	
	public static void main(String[] args) {
		
		String regex="(.+)\\s+(\\d+)\\s+\\((\\d+)\\)\\s*\\,\\s*(\\d+)\\s*.\\s*(\\d+)";
		String footerText="Working Papers in Scandinavian Syntax 96 (2016), 91–125";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(footerText);
		int i = 1;
		m.find();
		System.out.println(m.groupCount());
		
		try {
			while (i <= m.groupCount()) {
				System.out.println(i+":"+m.group(i));
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		/*
		 * StringTokenizer st = new
		 * StringTokenizer("journal issue (year),beginPage-endPage",",-()",true); while
		 * (st.hasMoreTokens()) { System.out.println(st.nextToken()); }
		 */
		 String footerDescription = "journalTitle @issue@ (year),beginPage-endPage";
		 String footerStartsWith = "Working Papers in Scandinavian";
		 String text = "Working Papers in Scandinavian Syntax 87 (2011), 103-135";
		 
		 footerDescription = "authors @year. title In";
		 footerStartsWith = "Cite this article:";
		 text = "Cite this article: Drude, Sebastian, Nicholas Ostler & Marielle Moser. 2018. Endangered "
		 		+ "languages and the land: Mapping landscapes of multilingualism (Preface)."
		 		+ "In S. Drude, N. Ostler & M. Moser (eds.), Endangered languages and the"
		 		+ "land: Mapping landscapes of multilingualism, Proceedings of FEL XXII/2018"
		 		+ "(Reykjavík, Iceland), 1–3. London: FEL & EL Publishing.";
		 
//		 text = "Cite this article: Tjeerd de Graaf, Hidetoshi Shiraishi (2004). Capacity"
//		 		+ "building for some endangered languages of Russia: Voices from"
//		 		+ "Tundra and Taiga. In Peter K. Austin (ed.) Language Documentation"
//		 		+ "and Description, vol 2. London: SOAS. pp. 59-70";
		 
		 footerDescription = "authors (year). title In";

//		 text = "Cite this article: Pam Macdonald (2016). Land cruising with Luise. In Language, land &"
//		 		+ "song: Studies in honour of Luise Hercus, edited by Peter K. Austin, Harold"
//		 		+ "Koch & Jane Simpson. London: EL Publishing. pp. 77-89";
		 
	     Metadata md = new Metadata();
	     PDFExtractionConfiguration z = new PDFExtractionConfiguration();
	     Footer footer = new Footer(footerDescription, footerStartsWith);
	     z.getFooterDescriptions().add(footer);
	     parseFooter(text ,md, z);
	     
	     
	}
	
	public String getAffiliation() {
		return affiliation;
	}
	
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public List<String> getLanguagesISO639Codes() {
		return languagesISO639Codes;
	}
	public void setLanguagesISO639Codes(List<String> languages) {
		this.languagesISO639Codes = languages;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(affiliation, authorString, authors, beginPage, booktitle, endPage, fileName, issue,
				journalNote, journalTitle, languagesISO639Codes, location, publisher, title, url, volume, year);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Metadata other = (Metadata) obj;
		boolean authorsEqual = new HashSet<Author>(authors).equals(new HashSet<Author>(other.authors));
		return Objects.equals(affiliation, other.affiliation) //&& Objects.equals(authorString, other.authorString)
				&& authorsEqual && beginPage == other.beginPage
				&& Objects.equals(booktitle, other.booktitle) && endPage == other.endPage
				&& Objects.equals(fileName, other.fileName) && issue == other.issue
				&& Objects.equals(journalNote, other.journalNote) && Objects.equals(journalTitle, other.journalTitle)
				&& Objects.equals(languagesISO639Codes, other.languagesISO639Codes)
				&& Objects.equals(location, other.location) && Objects.equals(publisher, other.publisher)
				&& Objects.equals(title, other.title) && Objects.equals(url, other.url) && volume == other.volume
				&& Objects.equals(year, other.year);
	}
	
	
	
//	@Override
//	public int hashCode() {
//		return getFilenameWithoutExtension().hashCode();
//	}
	

//	@Override
//	public boolean equals(Object obj) {
//		return EqualsBuilder.reflectionEquals(this, obj);
//	}
	
}


///**
// * Parse info from complex string 
// * @param config 
// * @param string
// */
//public static boolean parseFooter(String footerText, Metadata md, PDFExtractionConfiguration config) {
//	
//	// parse variable names, delimiters from description
//	// description must start with a variable name
//	
//	if (footerText.trim().isEmpty()) {
//		System.out.println("Error : footer text empty, "+md.fileName);
//		return false;
//	}
//	
//	if (config != null) {
//		// apply parameter footerStartsWithText
//		int x = -1;
//		if (config.footerStartsWithTextIsActive()) {
//			x = footerText.indexOf(config.getFooterStartsWithText());
//		}
//		if (x >= 0) {
//			footerText = StringUtils.substring(footerText, x, x+100);
//			System.out.println("cutting footer text @"+x+" "+footerText);
//		} else {
//			System.out.println("Footer text does not contain :"+config.getFooterStartsWithText()+ " skipping !");
//			return false;
//		}
//	}
//
//	
//	String regex="";
//	String token;
//	HashMap <Integer, String> variableOrdering = new HashMap <Integer, String>();
//	int variableID = 0;
//	StringTokenizer st = new StringTokenizer(config.getFooterDescription()," ,;-()[:].!@",true);
//	
//	System.out.println(config.getFooterDescription());
//
//	while (st.hasMoreTokens()) {
//		
//		token = st.nextToken();
//		//System.out.println("token:"+token);
//		switch(token) {
//		
//		case "journalTitle":
//			variableOrdering.put(variableID++, "journalTitle");
//			regex+="(.+)";
//			break;
//			
//		case "volume":
//			variableOrdering.put(variableID++, "volume");
//			regex+="(\\d+)";
//			break;
//			
//		case "issue":
//			variableOrdering.put(variableID++, "issue");
//			regex+="(\\d+)";
//			break;
//			
//		case "year":
//			variableOrdering.put(variableID++, "year");
//			regex+="(\\d+)";
//			break;
//		
//		case "beginPage":
//			variableOrdering.put(variableID++, "beginPage");
//			regex+="(\\d+)";
//			break;
//		
//		case "endPage":
//			variableOrdering.put(variableID++, "endPage");
//			regex+="(\\d+)";
//			break;
//		
//		case "(":
//		case ")":
//		case "[":
//		case "]":
//		case ".":
//		case ":":
//			regex+="\\"+token;
//			break;
//			
//		case ",":
//			regex+="[,\\s]{1}";
//			break;
//		
//		case "-":
//		case "–":
//			regex+=".";
//			break;
//			
//		case "@":
//			regex+="\\s+";
//			break;
//		}
//		
//		regex+="\\s*";
//	}
//	
//	//regex+=".*";
//	
//	System.out.println(regex);
//	System.out.println("checking for footer text: "+footerText);
//		
//	Pattern p = Pattern.compile(regex);
//	Matcher m = p.matcher(footerText);
//	int i = 1;
//	m.find();
//	//System.out.println("matched groups:"+ m.groupCount());
//	
//	try {
//		while (i <= m.groupCount()) {
//			System.out.println(i+":"+m.group(i));
//			i++;
//		}
//	} catch (Exception e) {
//		//e.printStackTrace();
//		System.out.println("fail");
//		return false;
//	}
//	
//	
//	for (int position : variableOrdering.keySet()) {
//		String varName = variableOrdering.get(position);
//		
//		//System.out.println(position+" "+variableOrdering.get(position));
//
//		position+=1; // adjust group index
//		switch (varName) {
//		
//		case "journalTitle":
//			md.setJournalTitle(m.group(position));
//			break;
//		
//		case "volume":
//			md.setVolume(Integer.parseInt(m.group(position)));
//			break;
//			
//		case "issue":
//			md.setIssue(Integer.parseInt(m.group(position)));
//			break;
//			
//		case "year":
//			md.setYear(Integer.parseInt(m.group(position)));
//			break;
//		
//		case "beginPage":
//			md.setBeginPage(Integer.parseInt(m.group(position)));
//			break;
//		
//		case "endPage":
//			md.setEndPage(Integer.parseInt(m.group(position)));
//			break;
//		}
//	}
//	System.out.println("success");
//	return true;
//	
////	System.out.println(md.journalTitle);
////	System.out.println(md.issue);
////	System.out.println(md.year);
////	System.out.println(md.beginPage);
////	System.out.println(md.endPage);
//	
//}
