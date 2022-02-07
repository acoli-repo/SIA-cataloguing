package org.acoli.sc.start;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.acoli.sc.config.SourceDescription;
import org.acoli.sc.extract.Author;
import org.acoli.sc.extract.Metadata;
import org.acoli.sc.extract.PDFMetadataExtractor;
import org.apache.commons.lang3.StringUtils;

public class ResultEvaluator {
	
	private List<Metadata> mdExtracted;
	private SourceDescription sdc;
	private int mdWithoutTitleAndAuthor;
	private int recordsInInputMods;
	private int convertedPdfs;
	private int failedPdfConversions;

	ResultEvaluator (SourceDescription sdc, List<Metadata> mdextracted) {
		
		this.mdExtracted = mdextracted;
		this.sdc = sdc;
		this.setMdWithoutTitleAndAuthor(0);
		this.setRecordsInInputMods(0);
		this.setConvertedPdfs(0);
	}
	
	
	public void printResults() {
		
		System.out.println("##########################");
		System.out.println("##  Extraction results  ##");
		System.out.println("##########################");
		System.out.println();
		System.out.println("for "+sdc.toString());
		
		
		
		int index = 1;
		// use meta.toString() !
		for(Metadata meta : mdExtracted){
			
			System.out.println("file "+index++);
			System.out.println("FILE:"+meta.fileName);
			System.out.println("JOURNAL:"+meta.getJournalTitle());
			System.out.println("VOLUME:"+meta.getVolume());
			System.out.println("ISSUE:"+meta.getIssue());
			System.out.println("YEAR:"+meta.getYear());
			System.out.println("TITLE:"+meta.title);
			System.out.println("SUBTITLE:"+meta.subTitle);
			System.out.println("AUTHOR string:"+meta.authorString);
			for (Author a : meta.getAuthors()) {
				System.out.println("AUTHOR: \n");
				System.out.println(a);
			}
			System.out.println("FROM page:"+meta.beginPage);
			System.out.println("TO page:"+meta.endPage);
			System.out.println("PAGES:"+meta.getExtent());
			System.out.println("URL:"+meta.getUrl());
			if (meta.getLanguagesISO639Codes().isEmpty()) {
				System.out.println("Language:\n");
			} else {
				System.out.println("Language:"+meta.getLanguagesISO639Codes().get(0));
			}
			System.out.println("");
			  
		}
	}
	
	
	/**
	 * Cleanup and further format extracted metadata 
	 */
	public void doPostProcessing() {
		
		
		System.out.println("#########################");
		System.out.println("##   Post-processing   ##");
		System.out.println("#########################");

		
		List<Metadata> withPostProcessing = new ArrayList<Metadata>();
		
		System.out.println("###########################");
		System.out.println("## a. Split author names ##");
		System.out.println("###########################");
		
		
		for (Metadata md : mdExtracted) {
			
			// Split author string into individual authors
			List<Author> authors = PDFMetadataExtractor.splitAuthors(md.getAuthorString());
			
			// Fix missing dots in 'J F Kennedy' => 'J. F. Kennedy'
			int i = 1;
			for (Author a : authors) {
				String newGivenName = "";
				for (String t : a.getGivenName().split(" ")) {
					if (t.length() == 1) {
						newGivenName+=t+"."+" ";
					} else {
						newGivenName+=t+" ";
					}
				}
				a.setGivenName(newGivenName.trim());
				
				String newFamilyName = ""; 
				for (String t : a.getFamilyName().split(" ")) {
					if (t.length() == 1) {
						newFamilyName+=t+"."+" ";
					} else {
						newFamilyName+=t+" ";
					}
				}
				a.setFamilyName(newFamilyName.trim());
				if (i == 1) { // first author in list is primary author
					a.setPrimaryAuthor(true);
				}
				
				i++;
			}
			
			md.setAuthors(authors);
			
//			for (String author : PDFMetadataExtractor.splitAuthors(md.getAuthorString())) {
//				
//				// remove extra characters
//				author = removeLeadingTrailingPunctuation(author);
//				
//				// TODO move name split by given, family name into splitAuthors method 
//				// Split author string into given name and family name
//				String[] split = author.split(" ");
//				Author a = new Author();
//				a.setGivenName(split[0]);
//				System.out.println("given : "+split[0]);
//				String familyName = "";
//				if(split.length > 1) {
//					int sx = 1;
//					while (sx < split.length) {
//						familyName+=split[sx++]+" ";
//					}
//					System.out.println("family  : "+familyName);
//					a.setFamilyName(familyName.trim());
//				}
//				authors.add(a);
//			}
//			md.setAuthors(authors);
		}
		
		
		System.out.println("###############################################################");
		System.out.println("## b. Remove results were title and author info are missing  ##");
		System.out.println("###############################################################");
		
		int removed = 0;
		// only include results that have a title and an author
		Iterator<Metadata> iterator = mdExtracted.iterator();
		while(iterator.hasNext()) {
			Metadata y = iterator.next();
			if (!y.authorsIsActive() && !y.titleIsActive()) {
				iterator.remove();
				removed++;
				System.out.println(y.getFileName());
			} else {
				// also discard metadata, were title or author are at most 2 characters long
				if (y.getTitle().trim().length() < 3 || y.getAuthorString().trim().length() < 3) {
					// TODO use author.givenName or author.familyName empty instead 
					iterator.remove();
					removed++;
					System.out.println(y.getFileName());
				}
			}
		}
		
		System.out.println("##########################################");
		System.out.println("## c. Remove redundant space characters ##");
		System.out.println("##########################################");
		iterator = mdExtracted.iterator();
		while(iterator.hasNext()) {
			Metadata y = iterator.next();
			y.setTitle(StringUtils.normalizeSpace(y.getTitle()));
			y.setJournalTitle(StringUtils.normalizeSpace(y.getJournalTitle()));
			y.setPublisher(StringUtils.normalizeSpace(y.getPublisher()));
			y.setAffiliation(StringUtils.normalizeSpace(y.getAffiliation()));
			// authors already normalized in split authors (see above)
		}
		
		
		System.out.println("############################################");
		System.out.println("## d. Remove leading/trailing punctuation ##");
		System.out.println("############################################");
		iterator = mdExtracted.iterator();
		while(iterator.hasNext()) {
			Metadata y = iterator.next();
			y.setTitle(removeLeadingTrailingPunctuationFromTitle(y.getTitle()));
			y.setJournalTitle(removeLeadingTrailingPunctuationFromJournal(y.getJournalTitle()));
			y.setPublisher(removeLeadingTrailingPunctuation(y.getPublisher()));
			y.setAffiliation(removeLeadingTrailingPunctuation(y.getAffiliation()));
			// authors already normalized in split authors (see above)
//			List<String> authors2 = new ArrayList<String>();
//			for (String author : y.getAuthors()) {
//				authors2.add(removeLeadingTrailingPunctuation(author));
//			}
//			y.setAuthors(authors2);
		}
		
		
		System.out.println("##########################################");
		System.out.println("## e.     Split title / subtitle        ##");
		System.out.println("##########################################");
		
		// error: John Van Way: Effect of Creaky Voice Simulation on Third-Tone Perception in Mandarin Chinese
		// error: Dong Bu Dong? – An Comprehension Check Question In CSL Classroom Discourse 
		
		iterator = mdExtracted.iterator();
		while(iterator.hasNext()) {
			Metadata y = iterator.next();
			String titleText = y.getTitle().trim();
			if (titleText.trim().isEmpty()) continue;
			
			// Split 1 : with delimiters to be removed
			// ok: Object shift and optionality. An intricate interplay between syntax, prosody and information structure
			// error: LANGUAGE VS. DIALECT IN LANGUAGE CATALOGUING: THE VEXED CASE OF OTOMANGUEAN DIALECT CONTINUA
			String[] split = titleText.split(":|\s–\s|\\s-\\s");
			String title1 = split[0].trim();
			String subTitle = "";
			int i=1;
			while (i < split.length) {
				subTitle+=split[i++];
			}
			subTitle = subTitle.trim();
			
			// Split 2 : with delimiters to be maintained
			// error for: The ‘How are you?’ sequence in telephone openings in Arabic
			StringTokenizer st = new StringTokenizer(titleText,"\\!\\?",true);
			String title2 = "";
			String subTitle2 = "";
			int j = 0;
			while (st.hasMoreTokens()) {
				if (j++ == 0) {
					title2=st.nextToken();
					if(!title2.equals(titleText) && st.hasMoreTokens()) {
						title2+=st.nextToken();
					}
				} else {
					subTitle2+=st.nextToken();
				}
			}
			subTitle2 = subTitle2.trim();
			
			if (title1.length() < title2.length()) {
				y.setTitle(title1);
				if (!subTitle.isEmpty()) {
					y.setSubTitle(subTitle);
				}
			} else {
				y.setTitle(title2);
				if (!subTitle2.isEmpty()) {
					// error: Dong Bu Dong? – An Comprehension Check Question In CSL Classroom Discourse
					// remove delimiter at the beginning of subtitle
					y.setSubTitle(subTitle2);
				}
			}
		}
		
		System.out.println("##########################################");
		System.out.println("## e.        Case formatter             ##");
		System.out.println("##########################################");
		// title, subTitle starts with uppercase letter
		// normalize capitalized title, subtitle, authors, etc. 
		// <title>THE ACQUISITION OF ERGATIVITY IN SAMOAN</title>
		
		System.out.println("\nResults without title and author info :"+removed+"\n");
		this.setMdWithoutTitleAndAuthor(removed);
	}
	
	
	public String removeLeadingTrailingPunctuation(String text) {
		
		String tbd = "[^a-zA-Z]";
		String rel = "^"+tbd+"+";
		String rer = tbd+"+$";
		return (text.replaceAll(rel, "").replaceAll(rer, ""));
	}
	
	
	public String removeLeadingTrailingPunctuationFromTitle(String text) {
		
		String tbdl = "[.,;:\\-!?'\\]\\s\\)\\}]";
		String tbdr = "[.,;:\\-'\\[\\s\\(\\{]";

		//tbd = "[^a-zA-Z]";
		String rel = "^"+tbdl+"+";
		String rer = tbdr+"+$";
		return (text.replaceAll(rel, "").replaceAll(rer, ""));
	}
	
	
	public String removeLeadingTrailingPunctuationFromJournal(String text) {
		
		String tbdl = "[.,;:\\-!?'\\]\\s\\)\\}]";
		String tbdr = "[.,;:\\-!?'\\[\\s\\(\\{]";

		//tbd = "[^a-zA-Z]";
		String rel = "^"+tbdl+"+";
		String rer = tbdr+"+$";
		return (text.replaceAll(rel, "").replaceAll(rer, ""));
	}

	
	
	
	public void evalResults() {
		
		int withTitle = 0;
		int withAuthor = 0;
		int withPage = 0;
		int withJournal = 0;
		int withYear = 0;
		int authorAndTtile = 0;
		
		for (Metadata md : mdExtracted) {
			
			if (md.titleIsActive()) withTitle++;
			if (md.authorsIsActive()) withAuthor++;
			if (md.yearIsActive()) withYear++;
			if (md.journalTitleIsActive()) withJournal++;
			if (md.beginPageIsActive() || md.endPageIsActive()) withPage++;
			if (md.titleIsActive() && md.authorsIsActive()) authorAndTtile++;
		}
		
		
		System.out.println("Records in input MODS :"+recordsInInputMods);
		System.out.println("Pdf files converted to XML :"+convertedPdfs);
		System.out.println("Failed Pdf conversions :"+this.failedPdfConversions);
		System.out.println("Extraction results");
		System.out.println("for "+mdExtracted.size()+" files");
		System.out.println("extracted title :"+withTitle);
		System.out.println("extracted author :"+withAuthor);
		System.out.println("extracted author & title :"+authorAndTtile);
		System.out.println("extracted page :"+withPage);
		System.out.println("extracted journal :"+withJournal);
		System.out.println("extracted year :"+withYear);
		System.out.println("Errors");
		if (this.getMdWithoutTitleAndAuthor() > 0) {
			System.out.println("Discarded "+this.getMdWithoutTitleAndAuthor()+" results were title & author info could not be extracted");
		}
	
		 //Compare received Metadata with Output Data
//        List<ArrayList<String>> output = dataReader.parseOutputAuthors("documentation/output-data-format/047006471-output.jsonl");
//        ArrayList<ArrayList<String>> foundObjects = new ArrayList<ArrayList<String>>();
//
//        for(ArrayList<String> i : output){
//            for(List<String> o : authorsList){
//                if(i.equals(o)){
//                    foundObjects.add(i);
//                }
//            }
//        }
		
	}


	public int getMdWithoutTitleAndAuthor() {
		return mdWithoutTitleAndAuthor;
	}


	public void setMdWithoutTitleAndAuthor(int mdWithoutTitleAndAuthor) {
		this.mdWithoutTitleAndAuthor = mdWithoutTitleAndAuthor;
	}


	public int getRecordsInInputMods() {
		return recordsInInputMods;
	}


	public void setRecordsInInputMods(int recordsInInputMods) {
		this.recordsInInputMods = recordsInInputMods;
	}


	public int getConvertedPdfs() {
		return convertedPdfs;
	}


	public void setConvertedPdfs(int convertedPdfs) {
		this.convertedPdfs = convertedPdfs;
	}


	public int getFailedPdfConversions() {
		return failedPdfConversions;
	}


	public void setFailedPdfConversions(int failedPdfConversions) {
		this.failedPdfConversions = failedPdfConversions;
	}
	
	public static void main(String[] args) {
		
		String text = "; !Language Documentation? and Description, ;!";
		//System.out.println(removeLeadingTrailingPunctuation(text));
		StringTokenizer st = new StringTokenizer(text,"\\?",true);
		while (st.hasMoreTokens()) {
				System.out.println(st.nextToken());
		}

	}
}
