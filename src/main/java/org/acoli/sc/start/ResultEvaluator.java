package org.acoli.sc.start;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.acoli.sc.config.SourceDescription;
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
		for(Metadata meta : mdExtracted){
			
			System.out.println("file "+index++);
			System.out.println("FILE:"+meta.fileName);
			System.out.println("JOURNAL:"+meta.getJournalTitle());
			System.out.println("VOLUME:"+meta.getVolume());
			System.out.println("ISSUE:"+meta.getIssue());
			System.out.println("YEAR:"+meta.getYear());
			System.out.println("TITLE:"+meta.title);
			System.out.println("AUTHOR string:"+meta.authorString);
			for (String a : meta.getAuthors()) {
			System.out.println("AUTHOR: "+a);
			}
			System.out.println("FROM page:"+meta.beginPage);
			System.out.println("TO page:"+meta.endPage);
			System.out.println("PAGES:"+meta.getExtent());
			System.out.println("URL:"+meta.getUrl());
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
		
		// Split author string into individual authors
		for (Metadata md : mdExtracted) {
			md.setAuthors(PDFMetadataExtractor.splitAuthors(md.getAuthorString()));
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
			List<String> authors2 = new ArrayList<String>();
			for (String author : y.getAuthors()) {
				authors2.add(removeLeadingTrailingPunctuation(author));
			}
			y.setAuthors(authors2);
		}
		
		
		
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
	
//	public static void main(String[] args) {
//		
//		String text = "; !Language Documentation and Description, ;!";
//		System.out.println(removeLeadingTrailingPunctuation(text));
//		
//	}
}
