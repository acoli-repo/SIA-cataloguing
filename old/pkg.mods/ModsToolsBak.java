package org.acoli.sc.mods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acoli.sc.extract.Metadata;
import org.acoli.sc.mods.ModsTools.DETAIL_TYPE;
import org.acoli.sc.mods.ModsTools.EXTENT_UNIT;
import org.acoli.sc.mods.ModsTools.GENRE;
import org.acoli.sc.mods.ModsTools.LANGUAGE_AUTHORITY;
import org.acoli.sc.mods.ModsTools.NAME_TYPE;
import org.acoli.sc.mods.ModsTools.ROLE_TYPE;
import org.acoli.sc.mods.classes.Detail;
import org.acoli.sc.mods.classes.Extent;
import org.acoli.sc.mods.classes.Language;
import org.acoli.sc.mods.classes.LanguageTerm;
import org.acoli.sc.mods.classes.Location;
import org.acoli.sc.mods.classes.Mods;
import org.acoli.sc.mods.classes.ModsCollection;
import org.acoli.sc.mods.classes.Name;
import org.acoli.sc.mods.classes.OriginInfo;
import org.acoli.sc.mods.classes.Part;
import org.acoli.sc.mods.classes.PhysicalLocation;
import org.acoli.sc.mods.classes.Place;
import org.acoli.sc.mods.classes.RecordInfo;
import org.acoli.sc.mods.classes.Role;
import org.acoli.sc.mods.classes.RoleTerm;
import org.acoli.sc.mods.classes.TitleInfo;
import org.acoli.sc.mods.classes.Url;
import org.apache.commons.io.FilenameUtils;

public class ModsToolsBak {
	
	
	public final static String lexvoLanguageUriPrefix = "http://lexvo.org/id/";

	
	/**
	 * Put extracted metadata into input MODS file and write to file
	 * @param modsCollection
	 * @param extractedMD 
	 * @param modsRecordIDs2PDFfilenames
	 * @param modsFilePath
	 */
	public static void exportMods(
			
			ModsCollection modsCollection,
			List<Metadata> extractedMD, 
			Map<String, String> modsRecordIDs2PDFfilenames,
			String modsFilePath) {
		
		ModsCollection result = insertExtractedMDintoMods(modsCollection, extractedMD, modsRecordIDs2PDFfilenames);
		String outfilePath = FilenameUtils.removeExtension(modsFilePath)+"processed.mods.xml";
		System.out.println("\nExporting "+result.getMods().size()+" records to MODS file : "+outfilePath);
		//exportMods(result, outfilePath);
		System.out.println("finished !");
	}
	
	/**
	 * Insert extracted metadata into MODS input
	 * @param modsCollection
	 * @param mapRecords2PDFfilenames 
	 * @param resultMap Maps a filename to the extracted metadata
	 * @return new ModsCollection with metadata
	 */
	public static ModsCollection insertExtractedMDintoMods(
			
			ModsCollection modsCollection,
			List<Metadata> extractedMD,
			Map<String, String> modsRecordIDs2PDFfilename) {
		
		ModsCollection result = new ModsCollection();
		
		// build table for quick lookup
		HashMap<String, ArrayList<Metadata>> pdfFilenames2ExtractedMD = new HashMap<String, ArrayList<Metadata>>();
		for (Metadata md : extractedMD) {
			
			String key = FilenameUtils.getBaseName(md.getFileName());
			
			if (!pdfFilenames2ExtractedMD.containsKey(key)) {
				ArrayList<Metadata> mdList = new ArrayList<Metadata>();
				mdList.add(md);
				pdfFilenames2ExtractedMD.put(key, mdList);
			} else {
				ArrayList<Metadata> mdList = pdfFilenames2ExtractedMD.get(key);
				mdList.add(md);
				pdfFilenames2ExtractedMD.put(key, mdList);
			}
		}
		
//		System.out.println("hier "+extractedMD.size());
//		for (String x : pdfFilenames2ExtractedMD.keySet()) {
//			System.out.println(x);
//			System.out.println(pdfFilenames2ExtractedMD.get(x).size());
//		}
		
		// main loop
		int ok = 0;
		Set<String> unmatchedMDFilenames = new HashSet<String>();
		for (Metadata mdx : extractedMD) {
			unmatchedMDFilenames.add(mdx.fileName);
			//System.out.println(mdx.fileName);
		}
		List<String> noMetadataFound = new ArrayList<String>();
		
		for (Mods mo : modsCollection.getMods()) {
			
			String filename = FilenameUtils.getBaseName(modsRecordIDs2PDFfilename.get(mo.getRecordInfo().getRecordIdentifier()));
			if (!pdfFilenames2ExtractedMD.containsKey(filename)) {
				noMetadataFound.add(filename+"#"+mo.getRecordInfo().getRecordIdentifier());
				continue; // file not found
			}
			
			// now insert extracted MD into MODS
			List<Metadata> moMDList = pdfFilenames2ExtractedMD.get(filename);
			for (Metadata moMD : moMDList) {
			
				unmatchedMDFilenames.remove(moMD.getFileName());
				
				// insert values
				Mods x = new Mods();
				// copy record-identifier
				RecordInfo ri = new RecordInfo(); 
				ri.setRecordIdentifier(mo.getRecordInfo().getRecordIdentifier());
				x.setRecordInfo(ri);
				moMD.setID(filename);
				
				// copy location information
				for (Location lo : mo.getLocation()) {
					Location l = new Location();
					if (lo.getUrl() != null) {
						Url url = new Url();
						url.setNote(lo.getUrl().getNote());
						url.setValue(lo.getUrl().getValue());
						l.setUrl(url);
					}
					if (lo.getPhysicalLocation() != null) {
						PhysicalLocation ph = new PhysicalLocation();
						ph.setType(lo.getPhysicalLocation().getType());
						ph.setValue(lo.getPhysicalLocation().getValue());
						l.setPhysicalLocation(ph);
					}
					x.getLocation().add(l);
				}
				// copy type of resource
				if (mo.getTypeOfResource() != null) {
					x.setTypeOfResource(mo.getTypeOfResource());
				} else {
					// if not present insert detected type of resource
				}
				
				// INSERT extracted metadata
				
				// insert extracted title
				TitleInfo ti = new TitleInfo(); ti.setTitle(moMD.getTitle());
				x.setTitleInfo(ti);
				

				// name
//				 	<name type="personal">
//			  	  	<namePart>Raschke, Gregory K.</namePart>
//			  	  	<displayForm>Gregory K. Raschke</displayForm>
//				  	<affiliation>University of Łódź</affiliation>
//				    <role>
//				        <roleTerm type="text" authority="dcterms" authorityURI="http://purl.org/dc/terms/" valueURI="http://purl.org/dc/terms/creator">creator</roleTerm>
//				    </role> 
//				  	</name>
//				  	
//				  	<name type="personal">
//				    <namePart>Piotr Pęzik</namePart>
//				    <affiliation>University of Łódź</affiliation>
//				    <role>
//				        <roleTerm type="text" authority="dcterms" authorityURI="http://purl.org/dc/terms/" valueURI="http://purl.org/dc/terms/creator">contributor</roleTerm>
//				    </role>           
//					</name>
				
				for (String name : moMD.getAuthors()) {
					Name na = new Name();
					na.setType(NAME_TYPE.PERSONAL.name().toLowerCase());
					na.setNamePart(name);
					Role role = new Role();
					RoleTerm rt = new RoleTerm();
					rt.setType("text");
					rt.setAuthority("dcterms");
					rt.setAuthorityURI("http://purl.org/dc/terms/");
					rt.setValueURI("http://purl.org/dc/terms/creator");
					rt.setValue(ROLE_TYPE.CREATOR.name().toLowerCase());
					role.setRoleTerm(rt);
					na.setRole(role);
					x.getName().add(na);
				}
				
				
				// part
//					<part>
//			  	  	<detail type="volume">
//			  	  	    <caption>Endangered languages and the land: Mapping landscapes of multilingualism</caption>
//			  	  	  	<number>3</number>
//		  		  	</detail>
//		  	  		<detail type="number">
//		  	  	  		<number>1</number>
//			  	  	  	<caption>no.</caption>
//			  	  	</detail>
//			  	  	<extent unit="page">
//		  		  	  	<start>53</start>
//		  	  		  	<end>57</end>
//			  	  	</extent>
//			  	  	<date>Jan. 2003</date>
//			  	  	</part>
				
				boolean hasExtent = false;
				boolean hasJournalTitle = false;
				boolean hasIssue = false;
				Extent extent=null;
				List<Detail> details = new ArrayList<Detail>();

				// start + end page
				if (moMD.beginPageIsActive() && moMD.endPageIsActive()) {
					extent = new Extent();
					extent.setStart(BigInteger.valueOf(moMD.getBeginPage()));
					extent.setEnd(BigInteger.valueOf(moMD.getEndPage()));
					extent.setUnit(EXTENT_UNIT.PAGE.name().toLowerCase());
					hasExtent = true;
				} else {
					// only start page
					if (moMD.beginPageIsActive()) {
						extent = new Extent();
						extent.setStart(BigInteger.valueOf(moMD.getBeginPage()));
						extent.setUnit(EXTENT_UNIT.PAGE.name().toLowerCase());
						hasExtent = true;
					}
					
				}
				
				if (moMD.journalTitleIsActive()) {
					//System.out.println(moMD.getJournalTitle());
					Detail detail1 = new Detail();
					detail1.setType(DETAIL_TYPE.VOLUME.name().toLowerCase());
					detail1.setCaption(moMD.getJournalTitle());
					
					if (moMD.volumeIsActive()) {
						detail1.setNumber(BigInteger.valueOf(moMD.getVolume()));
					}
					details.add(detail1);
					hasJournalTitle = true;
				}
				
				if (moMD.issueIsActive()) {
					Detail detail2 = new Detail();
					detail2.setType(DETAIL_TYPE.NUMBER.name().toLowerCase());
					detail2.setNumber(BigInteger.valueOf(moMD.getIssue()));
					details.add(detail2);
					hasIssue = true;
				}
				
				if (hasExtent || hasJournalTitle || hasIssue) {
					
					Part part = new Part();
					part.setDetail(details);
					part.setExtent(extent);
					
					if (moMD.yearIsActive()) {
						part.setDate(moMD.getYear().toString());
					}
					x.setPart(part);
				}
				
			
				// language
//				  	<language>
//				    <languageTerm authority="iso639-3" authorityURI="http://lexvo.org/id/iso639-3/" type="code" valueURI="http://lexvo.org/id/iso639-3/swe">swe</languageTerm>
//					</language>
//				  	<language authority="iso639-2b">eng</language>
				
				for (Object lang : moMD.getLanguagesAsISO6393Codes()) {
					Language la = new Language();
					LanguageTerm lt = new LanguageTerm();
					lt.setAuthority(LANGUAGE_AUTHORITY.ISO639_3.name().toLowerCase());
					lt.setType("code");
					lt.setValueURI(lexvoLanguageUriPrefix+lang);
					lt.setValue((String) lang);
					x.getLanguage().add(la);
				}
				
				
				// origin info
//					<originInfo>
//			  	  	<place>
//			  	  	  	<text>Baltimore, Md.</text>
//		  		  	</place>
//			  	  	<publisher>Johns Hopkins University Press</publisher>
//			  	  	<dateIssued>2003</dateIssued>
//			  	  	<issuance>monographic</issuance>
//		  		  	</originInfo>
				
				if (moMD.locationIsActive()) {
					
					OriginInfo originInfo = new OriginInfo();
					Place place = new Place();
					place.setText(moMD.getLocation());
					originInfo.setPlace(place);
					
					if (moMD.yearIsActive()) {
						originInfo.setDateIssued(BigInteger.valueOf(moMD.getYear()));
					}
					if (moMD.publisherIsActive()) {
						originInfo.setPublisher(moMD.getPublisher());
					}
					x.setOriginInfo(originInfo);
				}
				
				// genre
//		  			<genre>journal article</genre>
				
				x.setGenre(GENRE.JOURNAL_ARTICLE.name().toLowerCase());
				
				// subject
//					<subject>
//			        <topic>Schlagwort 1</topic>
//				    </subject>
//				    <subject>
//				        <topic>Schlagwort 2</topic>
//				    </subject>
//				    <subject>
//				        <topic>Schlagwort N</topic>
//				    </subject>  
				
				result.getMods().add(x);
				ok++;
			}
		}
		if(!unmatchedMDFilenames.isEmpty()) {
			System.out.println("Skipping "+unmatchedMDFilenames.size()+ " results of Pdf files that could not be found in the input MODS:");
		}
		for (String mdx : unmatchedMDFilenames) {
			System.out.println(mdx);
		}
//		System.out.println("Entries without extracted MD :");
//		Collections.sort(noMetadataFound);
//		for (String y : noMetadataFound) {
//			System.out.println(y);
//		}
		
		return result;
	}

}
