package org.acoli.sc.start;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.acoli.sc.config.AuthorNameFile;
import org.acoli.sc.config.Config;
import org.acoli.sc.config.SourceDescription;
import org.acoli.sc.extract.KeywordDefinition;
import org.acoli.sc.extract.Metadata;
import org.acoli.sc.extract.PDF2XML;
import org.acoli.sc.extract.PDFExtractionConfiguration;
import org.acoli.sc.extract.PDFMetadataExtractor;
import org.acoli.sc.mods.MetadataDB;
import org.acoli.sc.mods.ModsTools;
import org.acoli.sc.mods.classes.Location;
import org.acoli.sc.mods.classes.Mods;
import org.acoli.sc.mods.classes.ModsCollection;
import org.acoli.sc.mods.classes.PhysicalLocation;
import org.acoli.sc.util.CSVFile;
import org.acoli.sc.util.ClusterUtils;
import org.acoli.sc.util.NLPUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.FilenameUtils;

/**
 * XPATH documentation
 * https://www.w3schools.com/xml/xpath_syntax.asp
 *
 * - read parse parameter from config file
 */

public class Run {
	
	private static Logger LOG = Logger.getLogger(PDFMetadataExtractor.class.getName());
    //public static String base = "/home/demo/Schreibtisch/ide/github-master/SIA-cataloguing/documentation/samples/input-examples/047006471";
    //public static String itemsSample = "documentation/samples/input-examples/047006471/items.jsonl";
	
	public static String dataFolder;
	public static Map<String, KeywordDefinition> keywordDefinitionMap;
	public static KeywordDefinition activeKeywordDefinition;
	public static String stanfordTaggerPath;
	public static String stanfordNerClassifierPath;
	public static String openNlpPosModelPath;
	public static String openNlpNerModelPath;
	public static String subTitleSplitRegexRemove;
	public static String subTitleSplitRegexMaintain;
	public static String familyNamePrefixes;
	public static HashSet<String> knownFamilyNames;
	public static HashSet<String> knownGivenNames;
	public static int languageDetectionSampleChars;
	public static Float ngramDetectorMinConfidence;
    
	
    public static void main(String[] args) throws Exception{
    	
    	
    	Option optionConfig = Option.builder("config").required(false).numberOfArgs(1).argName("configFile").desc("Configuration JSON file").build();
	    Option optionMode = Option.builder("mode").required(false).numberOfArgs(1).argName("runMode").desc("Set run mode : GET_STATS | EXPORT_DB | GET_NEW/CHANDED/MISSING|COMMIT_NEW/CHANDED/MISSING |").build();

	    Option optionSort = Option.builder("sort").required(false).numberOfArgs(1).argName("directory").desc("Cluster files in directory according to their name").build();
	    Option optionSortRecursions = Option.builder("recursions").required(false).numberOfArgs(1).argName("n").desc("Recursion count").build();
	    Option optionSortMinClusterNameLength = Option.builder("minClusterNameLength").required(false).numberOfArgs(1).argName("n").desc("Minimal cluster name length").build();
	    Option optionSortClusterJoinMaxSuffixMissmatch = Option.builder("clusterJoinMaxSuffixMissmatch").required(false).numberOfArgs(1).argName("n").desc("Join cluster names that differ at most in k chars at the end").build();
	    Option optionSortExtension = Option.builder("extension").required(false).numberOfArgs(1).argName("Sort files with extension").desc("Sort only filenames with extension").build();
	    Option optionSortExecute = Option.builder("execute").required(false).numberOfArgs(0).desc("Create subfolders for clusters and move files into subdirs").build();
	    Option optionHelp = Option.builder("h").desc("Show this help").build();

	    
	    Options options = new Options();
	    options.addOption(optionConfig);
	    options.addOption(optionMode);
	    options.addOption(optionSort);
	    options.addOption(optionSortRecursions);
	    options.addOption(optionSortMinClusterNameLength);
	    options.addOption(optionSortClusterJoinMaxSuffixMissmatch);
	    options.addOption(optionSortExtension);
	    options.addOption(optionSortExecute);
	    options.addOption(optionHelp);

	    
	    
	    CommandLineParser parser = new DefaultParser();
	    
	    
	    String configFile = "config.json";
	    String runModeString = "";
	    String sortDirectory = "";
	    int sortRecursions = -1;
	    String sortExtension = "pdf";
	    int sortMinClusterNameLength = 3;
	    int sortClusterJoinMaxSuffixMissmatch = 6;
	    boolean sortExecute = false;
	    
	    MetadataDB.dbOperation runMode=null;
	    
	    
	    String header = "MDE (Metadata-Extraction tool)";
	    String footer = "";
	    String [] defaultArgs = {"-h"};
	    HelpFormatter formatter = new HelpFormatter();
	    if (args.length > 0) {
	    	defaultArgs = args;
	    }
	    
	    
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse(options, args);
	        
	        if (line.hasOption("h"))
	        {
	        	System.out.println(header);
	        	formatter.printHelp("program", " ", options, footer, true);
	        	return;
	        }
	        
	        
	        if(line.hasOption("config")) {
	        	configFile = line.getOptionValue("config");
	        	
	        	if (!new File(configFile).exists()) {
	        		System.out.println("Error : Could not find configuration file : "+configFile+" !");
	        		return;
	        	}
	        
	        
		        if(line.hasOption("mode")) {
		        	runModeString = line.getOptionValue("mode");
		        	try {
		        	runMode = MetadataDB.dbOperation.valueOf(runModeString);
		        	} catch (Exception e) {
		        		e.printStackTrace();
		        		return;
		        	}
		        }
		        
	        } else {
	        
		        if(line.hasOption("sort")) {
		        	
	                sortDirectory = line.getOptionValue("sort");
	                
	                if (!new File(sortDirectory).exists() && !new File(sortDirectory).isDirectory()) {
		        		System.out.println("Error : Could not find configuration file : "+sortDirectory+" !");
		        		return;
		        	}
		        
			        if(line.hasOption("recursions")) {
		                sortRecursions = Integer.parseInt(line.getOptionValue("recursions"));
			        }
			        
			        if(line.hasOption("extension")) {
		                sortExtension = line.getOptionValue("extension");
			        }
			        
			        if(line.hasOption("minClusterNameLength")) {
		                sortMinClusterNameLength = Integer.parseInt(line.getOptionValue("minClusterNameLength"));
			        }
			        
			        if(line.hasOption("clusterJoinMaxSuffixMissmatch")) {
		                sortClusterJoinMaxSuffixMissmatch = Integer.parseInt(line.getOptionValue("clusterJoinMaxSuffixMissmatch"));
			        }
			        
			        if(line.hasOption("execute")) {
			        	sortExecute = true;
			        }
			        
			    	HashMap<String, List<String>> map = ClusterUtils.clusterFilesInDir(
			    			sortDirectory,
			    			sortMinClusterNameLength, 
			    			sortClusterJoinMaxSuffixMissmatch, 
			    			sortRecursions, 
			    			sortExtension);
			    	
			    	// show clustering result
			    	ClusterUtils.printClusters(map);
			    	
			    	// apply clustering to fs
			    	if (sortExecute) {
			    		ClusterUtils.moveFilesToClusters(map, sortDirectory);
			    	}
			    	
			    	return;
		        }
	        }
	           
	    }
	    
	    catch (ParseException exp) {
	        // oops, something went wrong
	        System.err.println("Parsing failed.  Reason: " + exp.getMessage());
	        return;
	    }

    	
	    // catch missing config file
    	if (!new File(configFile).exists()) {
    		System.out.println("Error : configuration file '"+configFile+"' was not found !");
    		return;
    	}
    	
    	Config config = PDFExtractionConfiguration.readConfigs(configFile);
    	keywordDefinitionMap = new HashMap<String, KeywordDefinition>();
    	boolean hasDefaultKeywords = false;
    	if (config.getKeywordDefinitions() != null) {
    		for (KeywordDefinition kw : config.getKeywordDefinitions()) {
    			keywordDefinitionMap.put(kw.getId(), kw);
    			if (kw.getId().toLowerCase().equals("default")) {
    				hasDefaultKeywords = true;
    			}
    		}
    	}
    	if (!hasDefaultKeywords) {
    		keywordDefinitionMap.put("default", KeywordDefinition.getDefaults());
    	}
    	
    	if (config.getSubTitleSplitRegexRemove() != null) {
    		subTitleSplitRegexRemove = config.getSubTitleSplitRegexRemove();
    	} else {
    		subTitleSplitRegexRemove = "";
    	}
    	
    	if (config.getSubTitleSplitRegexMaintain() != null) {
    		subTitleSplitRegexMaintain = config.getSubTitleSplitRegexMaintain();
    	} else {
    		subTitleSplitRegexMaintain = "";
    	}
    	
    	if (config.getFamilyNamePrefixes() != null) {
    		familyNamePrefixes = config.getFamilyNamePrefixes();
    	} else {
    		familyNamePrefixes = "";
    	}
    	
    	// read tsv/csv files with author names
    	ArrayList<CSVFile> csvFiles = new ArrayList<CSVFile>();
    	for (AuthorNameFile af : config.getAuthorNameFiles()) {
    		
    		if (!af.getFilePath().trim().isEmpty() && af.getFamilyNameColumn() > 0 && af.getGivenNameColumn() > 0) {
    			String ext = FilenameUtils.getExtension(af.getFilePath()).toLowerCase();
    			HashMap<String, Integer> columnDescription = new HashMap<String, Integer>();
    			columnDescription.put("given", af.getGivenNameColumn());
    			columnDescription.put("family", af.getFamilyNameColumn());
    			CSVFormat csvFormat = null;
    			if (ext.equals("csv")) {
    				csvFormat = CSVFormat.DEFAULT;
    			}
    			if (ext.equals("tsv")) {
    				csvFormat = CSVFormat.TDF;
    			}
    			if (csvFormat == null) continue;
    			
    			CSVFile csvFile = new CSVFile(af.getFilePath().trim(), csvFormat, columnDescription);
    			csvFiles.add(csvFile);
    		} else {
    			System.out.println("Error in authorNameFile definition :");
    			System.out.println("filePath :"+af.getFilePath());
    			System.out.println("familyNameColumn :"+af.getFamilyNameColumn());
    			System.out.println("givenNameColumn  :"+af.getGivenNameColumn());

    		}
    	}
    	knownFamilyNames = NLPUtils.getAuthorFamilyNamesFromCSVFiles(csvFiles);
//    	for (String yy : knownFamilyNames) {
//    		System.out.println(yy);
//    	}
    	knownGivenNames = NLPUtils.getAuthorGivenNamesFromCSVFiles(csvFiles);
//    	for (String yy : knownGivenNames) {
//    		System.out.println(yy);
//    	}
    	
    	
    	if (config.getLanguageDetectionSampleChars() != null) {
    		languageDetectionSampleChars = config.getLanguageDetectionSampleChars();
    	} else {
        	languageDetectionSampleChars = 800;
    	}
    	
    	if (config.getNgramDetectorMinConfidence() != null) {
    		ngramDetectorMinConfidence = config.getNgramDetectorMinConfidence();
    	} else {
    		ngramDetectorMinConfidence = 0.8f;
    	}
    	
    	
    	
    	if (runMode == null) {
	    	try {
	    		runMode = MetadataDB.dbOperation.valueOf(config.getRunMode().toUpperCase());
	    	} catch (Exception e) {
	    		runMode = MetadataDB.dbOperation.GET_STATS;
	    	}
    	}
    	
    	java.util.logging.Level x = java.util.logging.Level.ALL;
		LOG.setLevel(x);

		System.out.println("Configurations :"+config.getSourceDescriptions().size());
	    System.out.println("documentRootDir :"+config.getDocumentRootDir());
	    System.out.println("tempDir :"+config.getXmlDir());
	    System.out.println("databaseDir :"+config.getDatabaseDir());
	    System.out.println("stanfordTaggerPath :"+config.getStanfordTaggerPath());
	    System.out.println("stanfordNerClassifierPath :"+config.getStanfordNerClassifierPath());
	    System.out.println("openNlpPosModelPath :"+config.getOpenNlpPosModelPath());
	    System.out.println("openNlpNerModelPath :"+config.getOpenNlpNerModelPath());
	    
	    MetadataDB database = new 
	    		MetadataDB(new File(config.getDatabaseDir()));

	    HashMap<String, List<Metadata>> allMetadata = new HashMap<String, List<Metadata>>();
	    HashMap <String, Mods> allMods = new HashMap<String, Mods>();
	    
		stanfordTaggerPath = config.getStanfordTaggerPath();
		stanfordNerClassifierPath = config.getStanfordNerClassifierPath();
		openNlpPosModelPath = config.getOpenNlpPosModelPath();
		openNlpNerModelPath = config.getOpenNlpNerModelPath();	    
		
	    
	    Init y = new Init(config);
	    
	    // loop over configurations
	    int configCounter=1;
	    int errorConfigs=0;
		for (SourceDescription sdc : config.getSourceDescriptions()) {
			
//			if(!sdc.isActive()) {
//				if (config.isRunAllSources()) {
//					System.out.println("Running all configurations !");
//				} else {
//					System.out.println("Skipping inactive configuration : "+configCounter++);
//					continue;
//				}
//			}
			
			if (sdc.isActive()) {
				System.out.println("Using configuration : "+configCounter);
			}
			
			dataFolder = new File(config.getDocumentRootDir(),sdc.getId()).getAbsolutePath();
	        if (sdc.getSubId() != null) dataFolder = new File(dataFolder, sdc.getSubId()).getAbsolutePath();
	        File resultFolder = new File(dataFolder, "resultData");
	        String itemsFilePath = new File(dataFolder, "items.jsonl").getAbsolutePath();
	        String modsFilePath = new File(dataFolder, "items.mods").getAbsolutePath();
	        
	        if (!(new File(modsFilePath)).exists()) {
	        	System.out.println("IO-Error : MODS input file "+modsFilePath+" was not found !");
	        	configCounter++;
	        	errorConfigs++;
	        	continue;
	        }

	        //Create resultDirectory
	    	//FileUtils.deleteDirectory(resultFolder);
	        if (!resultFolder.exists()){
	            resultFolder.mkdirs();
	        }
	        
			// usage of alternative keyword definitions which have to be defined in keywordDefinitions 
	        // otherwise default keyword definitions are used
	        activeKeywordDefinition = keywordDefinitionMap.get("default");
			String keywordDefinitionsId = sdc.getKeywordDefinitionId();
			if (keywordDefinitionsId != null && !keywordDefinitionsId.isEmpty()) {
				if (Run.keywordDefinitionMap.keySet().contains(keywordDefinitionsId)) {
					activeKeywordDefinition = keywordDefinitionMap.get(keywordDefinitionsId);
					System.out.println("567");
				} else {
					System.out.println("Error : keywordDefinitionId '"+keywordDefinitionsId+"' is undefined - using defaults !");
				}
			}
			
    
	        // Read MODS-XML into list of Mods objects
	        ModsCollection modsCollection = ModsTools.unmarshall(modsFilePath);
	        ModsTools.addMods2GlobalMods(allMods, modsCollection);
	        
	        HashMap<String, List<String>> fileName2RecordIdentifier = new HashMap<String, List<String>>();
	        int ix = 1;
	        for (Mods mo : modsCollection.getMods()) {
//	        	System.out.println(ix++);
//	        	System.out.println(mo.getRecordInfo().getRecordIdentifier());
//	        	System.out.println(mo.getTitleInfo().getTitle());
	        	
	        	String xa = "";
	        	for (Location loc : mo.getLocation()) {
	        		if (loc.getPhysicalLocation() != null) {
	        			PhysicalLocation pl = loc.getPhysicalLocation();
	        			if (pl.getType().equals(ModsTools.LOCATION_TYPE.PDF_NAME.name().toLowerCase())) {
	        				xa = pl.getValue();
	        			}
	        		}
	        	}
	        	
	        	if (xa.trim().isEmpty()) continue;
	        	
	        	if (!fileName2RecordIdentifier.containsKey(xa)) {
	        		ArrayList<String> idList = new ArrayList<String>();
	        		idList.add(mo.getRecordInfo().getRecordIdentifier());
	        		fileName2RecordIdentifier.put(xa, idList);
	        	} else {
	        		List<String> idList = fileName2RecordIdentifier.get(xa);
	        		idList.add(mo.getRecordInfo().getRecordIdentifier());
	        		fileName2RecordIdentifier.put(xa, idList);
	        	}
	        }
	        
	        List<String> errorList = new ArrayList<String>();
	        for (String fileName : fileName2RecordIdentifier.keySet()) {
	        
	        	if (fileName2RecordIdentifier.get(fileName).size() != 1) {
	        		//System.out.println(fileName+" : "+fileName2RecordIdentifier.get(fileName).size());
	        		errorList.add(fileName);
	        	} 
	        }
	        
	        
	        if (sdc.isActive() || runMode == MetadataDB.dbOperation.EXPORT_DB) {
	        	
	        	if (runMode == MetadataDB.dbOperation.EXPORT_DB) {
	        		continue;
	        	}
	        	
		        System.out.println("Filenames with multiple record identifiers : "+errorList.size());
		        for (String fileName : errorList) {
		        	System.out.println("file "+fileName);
		        	for (String yy : fileName2RecordIdentifier.get(fileName)) {
		        		System.out.println(yy);
		        	}
		        }
	
		        if (errorList.size() > 0) {
		        	System.exit(0);
		        }
	        }
	        
	        
//	        // Build map with keys recordIdentifers and values PDF filenames
	        Map<String, String> modsRecordIDs2PDFfilenames = ModsTools.getPdfFileNames(modsCollection);
	        
//	        for (String mo : modsRecordIDs2PDFfilenames.keySet()) {
//	        	System.out.println(ix++);
//	        	System.out.println(mo);
//	        	System.out.println(modsRecordIDs2PDFfilenames.get(mo));
//	        }
//	        
//	        System.exit(0);
	        
	        
	        if(!sdc.isActive()) {
				if (config.isRunAllSources()) {
					System.out.println("Running all configurations !");
				} else {
					System.out.println("Skipping inactive configuration : "+configCounter++);
					continue;
				}
			}
	        
	           
	        // Run PDF-XML conversion
	        int failedPdfConversions = PDF2XML.extractXML(new ArrayList<String>(modsRecordIDs2PDFfilenames.values()), new File(dataFolder));
	        
	        // Check count of converted XML documents
	        int convertedPdfs = 0;
	        for(File file : resultFolder.listFiles()){
	        	if (FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("xml")) {
	        		convertedPdfs++;
	        	}
	        }

	        //Extract Metadata from converted PDF files
	        List<Metadata> extractedMD = 
	        		RunExtract.convertXMLtoXMLTMP(
	        				resultFolder.getAbsolutePath(), 
	        				new PDFMetadataExtractor(sdc.getExtractorConfig()));
	        
	        ResultEvaluator re = new ResultEvaluator(sdc, extractedMD);
	        re.setRecordsInInputMods(modsCollection.getMods().size());
	        re.setConvertedPdfs(convertedPdfs);
	        re.setFailedPdfConversions(failedPdfConversions);
	        re.doPostProcessing();
	        re.printResults();
	        re.evalResults();

	        
	        // export mods
	        HashMap<String, List<Metadata>> map = ModsTools.mapMetadata2ModsRecords(extractedMD, modsRecordIDs2PDFfilenames, modsCollection);
	        allMetadata.putAll(map);
	        
	        ModsTools.exportMods(modsCollection, map, modsFilePath);
	        //ModsTools.exportMods(modsCollection, extractedMD, modsRecordIDs2PDFfilenames, modsFilePath);

	        
			// export bibtex
//	        String id = sdc.getId();
//			if (sdc.getSubId() != null) id=id+"-"+sdc.getSubId();
//			 // parse items.jsonl (only used for bibtex export)
//	        List<Metadata> mdjson = DataReader.parseMetadata(itemsFilePath);
//	        BibtexTools.export(MetadataMerger.mergeMetadataNew(extractedMD, mdjson), new File(config.getExportDir(),id+".bib"));
//	        //BibtexTools.export(mdextracted, new File("/home/demo/Schreibtisch/demo.bib"));
	        
	        
	        // cleanup
	        resultFolder.delete();
	        configCounter++;
		}
		
		
		// Update database
		database.runMetadataDBOperation(runMode, allMetadata, allMods);
		
		System.out.println();
	    System.out.println("Finished processing of "+config.getSourceDescriptions().size()+" configurations");
	    if (config.isRunAllSources()) {
	    	System.out.println(errorConfigs+"/"+config.getSourceDescriptions().size()+" configurations had errors !");
	    }
	    
	    if (runMode != MetadataDB.dbOperation.GET_STATS) {
	    	database.runMetadataDBOperation(MetadataDB.dbOperation.GET_STATS, allMetadata, allMods);
	    }
    }

}

// Build PDF file list from json file
//DataReader dataReader = new DataReader();
//List<String> pdfFileListJson = dataReader.parseItemsName(itemsFilePath);
//List<String> pdfFileList = dataReader.readOutItems(new ArrayList<String>(mapRecords2PDFfilenames.values()), dataFolder);	



//public PDFMetadataExtractor writeConfig(){
//PDFExtractionConfiguration config = new PDFExtractionConfiguration();
////config.setAuthorFont(2);
////config.setAuthorHeight(18);
//config.setAuthorFont(0);
//config.setAuthorHeight(0);
//config.setTitleFont(0);
////config.setTitleHeight(32);
//config.setTitleHeight(30);
//config.setPageFont(5);
//config.setPageHeight(16);
//
//config.setAuthorHeightRelation(">=");
//config.setAuthorFontRelation(">=");
//config.setTitleHeightRelation(">=");
//config.setAuthorAllUppercase(true);
//config.setAuthorMinLength(4);
//config.setTitleHeightMinMax(30, 35);
//config.setAuthorHeightMinMax(0, 100);
//config.setActive(true);
//
//
//PDFExtractionConfiguration dummyConfig = config;
//PDFMetadataExtractor pme = new PDFMetadataExtractor(config);
//return pme;
//}
