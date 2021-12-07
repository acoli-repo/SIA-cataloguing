package org.acoli.sc.extract;

import java.util.ArrayList;
import java.util.List;

/**
 * Table of contents
 * @author frank
 *
 */
public class TOCTools {

	
	/**
	 * Get metadata from a single citation. Tries one or several citation descriptions.
	 * @param toc
	 * @param lineGroups
	 * @param config
	 * @return
	 */
	public static List<Metadata> parseCitation(
			TOC toc,
			List<LineGroup> lineGroups,
			PDFExtractionConfiguration config
			) {
		
		LineGroup lineGroup = LineGroup.joinGroups(lineGroups);
		
		List<Metadata> result = new ArrayList<Metadata>();
		Metadata md=new Metadata();

		int citationDescriptions = toc.getTocEntryDescription().size();
		int i = 0;
		while (i < citationDescriptions) {
			
			// parse metadata from citation
			if(Metadata.parseTocEntryPart (md, lineGroup.getLinesAsString(), toc.getTocEntryDescription().get(i))) {
				result.add(md);
				break;
			};
			
			i++;
		}
		
		// return parsed metadata
		return result;
	}
	
	
	
	public static List<Metadata> parseTocEntries(
			TOC toc,
			List<LineGroup> lineGroups,
			PDFExtractionConfiguration config
			) {
		
		List<Metadata> result = new ArrayList<Metadata>();
		// group lineGroups to TOC entries according to TOC entry description in the config file
		Metadata md=new Metadata();
		int counter=0;
		int parts = toc.getTocEntryDescription().size();
		int i = 0;
		int lineGroupCount = lineGroups.size();
		int entryStart = 0;
		while (i < lineGroupCount) {
		//for (LineGroup lg : lineGroups) {
			
			LineGroup lg = lineGroups.get(i);
			
			// filter lines that obviously do not belong to a toc entry, e.g. page numbers, etc.
			if (lg.getLinesAsString().trim().length() < 5) {
				i++;
				continue;
			}
			
			//System.out.println("counter :"+counter+" parts :"+parts+ " -> "+counter % parts);
			// parse metadata from toc entry part
			if(!Metadata.parseTocEntryPart (
					md,
					lg.getLinesAsString(),
					toc.getTocEntryDescription().get(counter % parts))) {
				// restart entry parsing
				// reset description counter
				counter=0;
				entryStart++;
				i = entryStart;
				continue;
			};
			
			counter++;
			
			// next entry
			if (counter % parts == 0) {
				result.add(md);
				md = new Metadata();
				entryStart = i + 1;
			}
			
			i++;
		}
		
		// for each TOC entry
		// for each TOC entry part
		// get the metadata from the individual parts
		// Metadata.matchMetadataDescriptionRegex
		
		// 
		
		// return parsed metadata
		return result;
	}
	
}

//for (LineGroup lg : lineGroups) {
//	
//	// filter lines that obviously do not belong to a toc entry, e.g. page numbers, etc.
//	if (lg.getLinesAsString().trim().length() < 5) {
//		continue;
//	}
//	
//	//System.out.println("counter :"+counter+" parts :"+parts+ " -> "+counter % parts);
//	// parse metadata from toc entry part
//	Metadata.parseTocEntryPart(
//			md,
//			lg.getLinesAsString(),
//			toc.getTocEntryDescription().get(counter % parts));
//	
//	counter++;
//	
//	// next entry
//	if (counter % parts == 0) {
//		result.add(md);
//		md = new Metadata();
//	}
//}
