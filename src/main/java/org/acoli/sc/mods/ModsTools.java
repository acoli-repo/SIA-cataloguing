package org.acoli.sc.mods;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.acoli.sc.extract.Author;
import org.acoli.sc.extract.Metadata;
import org.acoli.sc.mods.classes.Detail;
import org.acoli.sc.mods.classes.Extent;
import org.acoli.sc.mods.classes.Language;
import org.acoli.sc.mods.classes.LanguageTerm;
import org.acoli.sc.mods.classes.Location;
import org.acoli.sc.mods.classes.Mods;
import org.acoli.sc.mods.classes.ModsCollection;
import org.acoli.sc.mods.classes.Name;
import org.acoli.sc.mods.classes.NamePart;
import org.acoli.sc.mods.classes.OriginInfo;
import org.acoli.sc.mods.classes.Part;
import org.acoli.sc.mods.classes.PhysicalLocation;
import org.acoli.sc.mods.classes.Place;
import org.acoli.sc.mods.classes.RecordInfo;
import org.acoli.sc.mods.classes.Role;
import org.acoli.sc.mods.classes.RoleTerm;
import org.acoli.sc.mods.classes.TitleInfo;
import org.acoli.sc.mods.classes.Url;
import org.acoli.sc.start.Run;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationUtils;


	/**
	 * Implementation of an XML Parser for mods xml exports<p> 
	 * <p>
	 * Get java classes from an xsd schema representing a mods file by using the xjc tool included
	 * in the java jdk/bin folder. The xsd schema needed by xjc can be generated e.g. with the trang tool <p>
	 * see <a href="http://www.thaiopensource.com/relaxng/trang.html">http://www.thaiopensource.com/relaxng/trang.html</a>
	 * <p>
	 * Example for xml-schema and java class generation.
	 * <p><p>
	 * # XSD schema generation from xml (using the trang tool)<p>
	 * java -jar trang.jar filename.xml filename.xsd
	 * <p><p>
	 * # JAXB class generation (-p target package path, -d target directory)<p>
	 * xjc turcet.xsd -p org.acoli.sc.mods -d path where java classes are written
	 * 
	 * @author frank
	 *
	 */
	
	public class ModsTools {
		
		public static enum LOCATION_TYPE {PDF_NAME, HTML_NAME, PDF_LINK};
		
		public static enum NAME_TYPE {PERSONAL};
		
		public static enum ROLE_TYPE {CREATOR, CONTRIBUTOR};
		
		public static enum LANGUAGE_AUTHORITY_6393 {ISO639_3};
		
		public static enum LANGUAGE_AUTHORITY_6392B {ISO639_2B};
		
		public static enum DETAIL_TYPE {VOLUME, NUMBER};
		
		public static enum EXTENT_UNIT {PAGE};
		
		public static enum GENRE {JOURNAL_ARTICLE};
		
		public final static String lexvoLanguageUriPrefix = "http://lexvo.org/id/";
		
		public final static String jaxbClassFolder = "org.acoli.sc.mods.classes";


		
		/**
		 * Constructor for JAXB reader (which uses default jaxb classes)
		 * @param xmlFilePath Mods file path
		 */
		public static ModsCollection unmarshall(String xmlFilePath) {
			
			return unmarshall (jaxbClassFolder, xmlFilePath);
		}

		
		/**
		 * Read MODS-XML into java Mods objects
		 * @param classesFolderPath Path to already generated java classes with xjc
		 * @param modsXmlFilePath Mods file path
		 */
		public static ModsCollection unmarshall (String classesFolderPath, String modsXmlFilePath) {
				
		try {
				JAXBContext jaxbContext = JAXBContext.newInstance (classesFolderPath);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				System.out.println(modsXmlFilePath);
				// Read xml file into java classes
				
				Reader is = null;
				try {
					is = new InputStreamReader(ModsTools.class.getResourceAsStream(modsXmlFilePath));
				} catch (Exception e) {
					try {
						is = new InputStreamReader(new FileInputStream(modsXmlFilePath));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
				Object o = unmarshaller.unmarshal(is);
				//Object o = unmarshaller.unmarshal(new File(xmlFilePath));
				
				// Root class := root element of xml document
				ModsCollection root = (ModsCollection) o;
				
				return root;
				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		
		public static void marshall(ModsCollection modsCollection, String exportXmlFilePath) {
			
			marshall(jaxbClassFolder, modsCollection, exportXmlFilePath);
			
		}
		
	
		private static void marshall(
				String classesFolderPath, ModsCollection modsCollection, String exportXmlFilePath) {
			
			System.out.println(exportXmlFilePath);
			
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance (classesFolderPath);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.marshal(modsCollection, new File(exportXmlFilePath));

			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		
		public static void addMods2GlobalMods(HashMap<String, Mods> allMods, ModsCollection mods) {
			
			for (Mods mo : mods.getMods()) {
				allMods.put(mo.getRecordInfo().getRecordIdentifier(), mo);
			}
		}
		
		
		/**
		 * Put extracted metadata into input MODS file and write to file
		 * @param modsCollection
		 * @param processedModsFilePath TODO
		 * @param extractedMD 
		 * @param modsRecordIDs2PDFfilenames
		 */
		public static void exportMods(
				
				ModsCollection modsCollection,
				HashMap<String, List<Metadata>> map,
				String outputModsFilePath) {
			
			System.out.println("\nExtracted "+map.keySet().size()+" metadata objects");			
			ModsCollection result = insertExtractedMDintoMods(modsCollection, map);
			System.out.println("\nExporting "+result.getMods().size()+" records to MODS file : "+outputModsFilePath);
			exportMods(result, outputModsFilePath);
			System.out.println("finished !");
		}
		
		
		
		
		/**
		 * Map extracted Metadata objects to MODS record identifiers
		 * @param extractedMD
		 * @param modsRecordIDs2PDFfilenames
		 * @return Map
		 */
		public static HashMap<String, List<Metadata>> mapMetadata2ModsRecords(
				List<Metadata> extractedMD,
				Map<String, String> modsRecordIDs2PDFfilenames,
				ModsCollection modsCollection) {
			
			HashMap<String, List<Metadata>> map = new HashMap<String, List<Metadata>>();
			
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
			
			for (Mods mo : modsCollection.getMods()) {
				
				String filename = FilenameUtils.getBaseName(modsRecordIDs2PDFfilenames.get(mo.getRecordInfo().getRecordIdentifier()));
				if (!pdfFilenames2ExtractedMD.containsKey(filename)) {
					continue; // file not found
				}
				List<Metadata> moMDList = pdfFilenames2ExtractedMD.get(filename);
				map.put(mo.getRecordInfo().getRecordIdentifier(), moMDList);
			}
			
			return map;
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
				HashMap<String, List<Metadata>> map
				) {
			
			ModsCollection result = new ModsCollection();
		
//			System.out.println("hier "+extractedMD.size());
//			for (String x : pdfFilenames2ExtractedMD.keySet()) {
//				System.out.println(x);
//				System.out.println(pdfFilenames2ExtractedMD.get(x).size());
//			}
			
			int metadataCount=0;
			for (String y : map.keySet()) {
				metadataCount+= map.get(y).size();
			}
			System.out.println("Map contains "+map.keySet().size() +" MODS IDs and "+metadataCount+" metadata objects in total");
			System.out.println("ModsCollection contains "+modsCollection.getMods().size()+" records");

			// main loop
			int ok = 0;
			Set<String> unmatchedMDFilenames = new HashSet<String>();
			for (String y : map.keySet()) {
				for (Metadata mdx : map.get(y)) {
					unmatchedMDFilenames.add(mdx.fileName);
					//System.out.println(mdx.fileName);
				}
			}
			//List<String> noMetadataFound = new ArrayList<String>();
			
			for (Mods mo : modsCollection.getMods()) {
				
				String modsRecordIdentifier = mo.getRecordInfo().getRecordIdentifier();
				if (!map.containsKey(modsRecordIdentifier)) continue;
				
				for (Metadata moMD : map.get(modsRecordIdentifier)) {
				
					unmatchedMDFilenames.remove(moMD.getFileName());
					
					// insert values
					Mods x = new Mods();
					// copy record-identifier
					RecordInfo ri = new RecordInfo(); 
					ri.setRecordIdentifier(mo.getRecordInfo().getRecordIdentifier());
					x.setRecordInfo(ri);
					
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
					if (moMD.getSubTitle() != null && !moMD.getSubTitle().isEmpty()) {
						ti.setSubTitle(moMD.getSubTitle());
					}
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
					
					for (Author author : moMD.getAuthors()) {
						Name na = new Name();
						na.setType(NAME_TYPE.PERSONAL.name().toLowerCase());
						NamePart given = new NamePart();
						given.setType("given");
						given.getContent().add(author.getGivenName());
						na.getNamePart().add(given);
						NamePart family = new NamePart();
						family.setType("family");
						family.getContent().add(author.getFamilyName());
						na.getNamePart().add(family);
						
						//na.setNamePart(name);
						Role role = new Role();
						RoleTerm rt = new RoleTerm();
						rt.setType("code");
						if (author.isPrimaryAuthor()) {
							rt.setValue("aut");
						} else {
							rt.setValue("ctb");
						}
//						rt.setType("text");
//						rt.setAuthority("dcterms");
//						rt.setAuthorityURI("http://purl.org/dc/terms/");
//						rt.setValueURI("http://purl.org/dc/terms/creator");
//						rt.setValue(ROLE_TYPE.CREATOR.name().toLowerCase());
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
					
					for (String lang : moMD.getLanguagesISO639Codes()) {
						Language la = new Language();
						la.getContent().add(lang);
						la.setAuthority(LANGUAGE_AUTHORITY_6392B.ISO639_2B.name().toLowerCase());
						LanguageTerm lt = new LanguageTerm();
						lt.setAuthority(LANGUAGE_AUTHORITY_6392B.ISO639_2B.name().toLowerCase());
						lt.setType("code");
						//lt.setValueURI(lexvoLanguageUriPrefix+lang);
						lt.setValue(lang);
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
//			System.out.println("Entries without extracted MD :");
//			Collections.sort(noMetadataFound);
//			for (String y : noMetadataFound) {
//				System.out.println(y);
//			}
			
			return result;
		}
		
		
		public static void exportMods(ModsCollection modsCollection, String filePath) {
			
			// sort MODS objects by ID
			HashMap<String, List<Mods>> map = new HashMap<String, List<Mods>>();
			for (Mods mo : modsCollection.getMods()) {
				if(map.containsKey(mo.getRecordInfo().getRecordIdentifier())) {
					List<Mods> moList = map.get(mo.getRecordInfo().getRecordIdentifier());
					moList.add(mo);
					map.put(mo.getRecordInfo().getRecordIdentifier(), moList);
				} else {
					List<Mods> moList = new ArrayList<Mods>();
					moList.add(mo);
					map.put(mo.getRecordInfo().getRecordIdentifier(), moList);
				}
			}
			
			HashMap<String, List<Integer>> subIdMap = new HashMap<String, List<Integer>>();
			
			for (String id : map.keySet()) {
				String[] parts = id.split("_");
				String recordId = parts[0];
				int subId = Integer.parseInt(parts[1]);
				
				if (!subIdMap.containsKey(recordId)) {
					List<Integer> list = new ArrayList<Integer>();
					list.add(subId);
					subIdMap.put(recordId, list);
				} else {
					List<Integer> list = subIdMap.get(recordId);
					list.add(subId);
					subIdMap.put(recordId, list);
				}
			}
			
			List<String> modsIDs = new ArrayList<String>(subIdMap.keySet());			
			Collections.sort(modsIDs);
			List<Mods> sortedMods = new ArrayList<Mods>();
			
			for (String id : modsIDs) {
				
				List<Integer> sortedSubIDs = subIdMap.get(id);
				Collections.sort(sortedSubIDs);
				
				for (int subId : sortedSubIDs) {
					for(Mods mo : map.get(id+"_"+subId)) {
						sortedMods.add(mo);
					}
				}
				
			}
			
			ModsCollection sortedModsCollection = new ModsCollection();
			sortedModsCollection.getMods().addAll(sortedMods);
			marshall(jaxbClassFolder, sortedModsCollection, filePath);

		}
		
		
		/**
		 * Returns the map with keys (MODS record identifier) and values PDF filenames
		 * @param Mods collection
		 * @return map
		 */
		public static HashMap<String, String> getPdfFileNames(ModsCollection mods) {
			
			HashMap<String, String> fileMap = new HashMap<String, String>();
			for (Mods m : mods.getMods()) {
				for (Location l : m.getLocation()) {
					if (l.getPhysicalLocation() != null) {
					if (l.getPhysicalLocation().getType().equalsIgnoreCase(LOCATION_TYPE.PDF_NAME.name())) {
						fileMap.put(m.getRecordInfo().getRecordIdentifier(), l.getPhysicalLocation().getValue());
					}
				}
			}
			}
			return fileMap;
			
			//JAXBModsTools.marshall(test, "/home/demo/Schreibtisch/test.xml");

		}


		public static void readModsTest() {

			// String inputXML = "/mods/048593834.xml";#
			String [] files = {
					"047502614_items.mods",
					"047080795_items.mods",
					"124671918_items.mods",
					"048593834_items.mods",
					"048421049_items.mods",
					"048134759_items.mods",
					"048020990_items.mods",
					"047574216_items.mods",
					"047505494_items.mods",
					"047222476_items.mods"
					};
			String dir = "/home/demo/Schreibtisch/items.mods/";
			String inputXML = "";
			
			for (String file : files) {
				inputXML = dir+file;
				ModsCollection test = ModsTools.unmarshall(inputXML);
				System.out.println(test.getMods().size());
				for (Mods mods : test.getMods()) {
					TitleInfo x = mods.getTitleInfo();
					for (Location l : mods.getLocation()) {
						if (l.getPhysicalLocation() != null) {
							String y = l.getPhysicalLocation().getType();
							if(l.getPhysicalLocation().getType().equals("pdf_name")) {
							System.out.println(l.getPhysicalLocation().getValue());
							}
						}
						if (l.getUrl() != null) {
							 Url z = l.getUrl();
						}
					}
				}
				System.out.println("o.k.");
			}
			
		}

		public static void main(String[] args) {
			
			ModsCollection result = new ModsCollection();
			Mods x = new Mods();
			
			TitleInfo ti = new TitleInfo(); ti.setTitle("title");
			x.setTitleInfo(ti);

			Name na = new Name();
			na.setType(NAME_TYPE.PERSONAL.name().toLowerCase());
			//na.setNamePart("author name");
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
			
			List<Detail> details=new ArrayList<Detail>();
			Part part = new Part();
			
			Extent extent = new Extent();
			extent.setStart(BigInteger.valueOf(1));
			extent.setEnd(BigInteger.valueOf(20));
			extent.setUnit(EXTENT_UNIT.PAGE.name().toLowerCase());
			part.setExtent(extent);

			Detail detail = new Detail();
			detail.setType(DETAIL_TYPE.VOLUME.name().toLowerCase());
			detail.setCaption("journal title");
			detail.setNumber(BigInteger.valueOf(1));
			details.add(detail);
			
			Detail detail2 = new Detail();
			detail2.setType(DETAIL_TYPE.NUMBER.name().toLowerCase());
			detail2.setNumber(BigInteger.valueOf(99));
			details.add(detail2);
			
			part.setDate("2000");
			part.setDetail(details);
			x.setPart(part);
			result.getMods().add(x);
			
			exportMods(result,"/home/demo/Schreibtisch/test.xml");
		}

	}
	

