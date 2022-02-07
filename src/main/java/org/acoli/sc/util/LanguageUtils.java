package org.acoli.sc.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pemistahl.lingua.api.*;
import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import org.apache.maven.shared.utils.io.FileUtils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class LanguageUtils {
	
	
	private static HashMap<String, String> iso639_3To2Map = new HashMap<String, String>();
	private static HashMap<String, String> iso639_2NamesDE = new HashMap<String, String>();
	private static HashMap<String, String> iso639_3NamesEN = new HashMap<String, String>();
	private static HashMap<String, String> iso639_1ToLexvo = new HashMap<String, String>();

	private static List<LanguageProfile>optimaizeInternalLanguageProfiles = null;
	private static LanguageProfileReader languageProfileReader;


	static LanguageDetector detector;
	
	/**
	 * Call constructor once before using static methods
	 */
	public LanguageUtils() {
		
		readISO639_1Codes();
		readISO639_2bCodes();
		readISO639_3Codes();
		try {
			languageProfileReader = new LanguageProfileReader();
			optimaizeInternalLanguageProfiles = languageProfileReader.readAllBuiltIn();
		} catch (IOException e) {
			e.printStackTrace();
		}
		detector = LanguageDetectorBuilder.fromAllLanguages().build();
	}
	
	

	/**
	 * Lingua language detector.
	 * Get ISO639-3 code, returns null if a ISO639_3 language could be detected
	 * @param text
	 * @return ISO639-3 code
	 */
	public static Language detectIsoCode639_3Lingua(String text) {
		
		return detector.detectLanguageOf(text);
		
//		if (detectedLanguage != Language.UNKNOWN) {
//			return detectedLanguage.name();
//			} else {
//			return null;
//		}
	}
	
	
	
	/**
	 * Lingua language detector.
	 * Get ISO639-2b code, returns null if a ISO639_2b language could not be detected
	 * @param text
	 * @return ISO639-2b code
	 */
	public static String detectIsoCode639_2bLingua(String text) {
		
		Language language = detectIsoCode639_3Lingua(text);
		
//		System.out.println("yyy");
//		System.out.println(language);
//		System.out.println(language.getIsoCode639_3());
//		System.out.println(language.getIsoCode639_3().name());

	
		if (language != Language.UNKNOWN) {
			String code639_2b = iso639_3To2Map.get(language.getIsoCode639_3().toString().toLowerCase());
			if (!code639_2b.isEmpty()) {
				return code639_2b;
			}
		}
		return null;
	}
	
	
	/**
     * Optimaize language detector. Splits text into sentences and returns the language which was
     * identified in the majority of all sentences.
	 * Get ISO639-2b code, returns null if a ISO639_2b language could not be detected
	 * @param text
	 * @return ISO639-2b code
	 */
	public static LanguageMatch detectIsoCode639_3Optimaize(String text) {
		
		ArrayList <LanguageMatch> found = detectLanguages(text);
		for (LanguageMatch lm : found) {
			if(lm.isSelected()) {
				String iso6393 = getISO639_3FromISO639_1(lm.getLanguageISO639Identifier());
				if (iso6393 != null) {
					lm.setLanguageISO639Identifier(iso6393);
				}
				return lm;
			}
		}
		return null;
	}
	
	
	
	/**
     * Optimaize language detector. Splits text into sentences and returns the language which was
     * identified in the majority of all sentences.
	 * Get ISO639-2b code, returns null if a ISO639_2b language could not be detected
	 * @param text
	 * @return ISO639-2b code
	 */
	public static LanguageMatch detectIsoCode639_2Optimaize(String text) {
		
		LanguageMatch languageMatch = detectIsoCode639_3Optimaize(text);
		
		if (languageMatch != null) {
			String code639_2b = iso639_3To2Map.get(languageMatch.getLanguageISO639Identifier());
			if (!code639_2b.isEmpty()) {
				languageMatch.setLanguageISO639Identifier(code639_2b);
				return languageMatch;
			}
		}
		return null;
	}
	
	

	private static ArrayList <LanguageMatch> detectLanguages(String text) {
		
		// Detect language for each sentence
	    return detectAllLanguages(splitText2Sentences(text));
	}
	
	
	private static ArrayList<String> splitText2Sentences(String text) {
		
		ArrayList <String> splitSentences = new ArrayList <String>();
		try {
			String filePath = "/tmp/tmp.txt";
			FileUtils.fileWrite(filePath, StandardCharsets.UTF_8.name(),text);
			DocumentPreprocessor dp = new DocumentPreprocessor(filePath);
			
			// Split text into sentences
			String sentence = "";
		    for (List<HasWord> tokenList : dp) {
		    	sentence = "";
		    	for (HasWord token : tokenList) {
		    		sentence += token.word()+" ";
		    	}
		    	//splitSentences.add(tokenList.toString());
		    	splitSentences.add(sentence);
		    }
		} catch (Exception e) {e.printStackTrace();}
		
		return splitSentences;
	}
	
	
	/**
	 * Detects languages in sentences
	 * @param sampleSentences
	 * @return list with all found languages, where the one with the highest probability is selected
	 */
	private static ArrayList <LanguageMatch> detectAllLanguages(ArrayList<String> sampleSentences) {
		
		List <DetectedLanguage> bestDetectedLanguages = new ArrayList<DetectedLanguage>();
		
		int i = 1;
		for (String sampleSentence : sampleSentences) {
			List<DetectedLanguage> detectedLanguages = getOptimaizeLanguageProbabilities(sampleSentence);
				if (detectedLanguages == null || detectedLanguages.isEmpty()) continue;
			DetectedLanguage bestLanguage = detectedLanguages.get(0);
			bestDetectedLanguages.add(detectedLanguages.get(0));
			
			System.out.println(i++ + " "+bestLanguage.getLocale().getLanguage()+" "+ sampleSentence);
		}
		
	    return computeBestLanguageMatchings(bestDetectedLanguages);
	}
	
	
	
	
	private Iterable<CSVRecord> readCodeTable(String fileName, CSVFormat csvFormat) {
		
		Iterable<CSVRecord> records;
		try {
			records = CSVParser.parse(
					Utils.getResouceFile("language-code-tables/"+fileName),
					StandardCharsets.UTF_8,
					csvFormat
					);
			return records;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		

	private HashMap<String, String> readISO639_2bCodes() {

		String file = "639-2b.csv";
		Iterable<CSVRecord> records = readCodeTable(file, CSVFormat.DEFAULT);
		
		if (records != null) {
			for (CSVRecord record : records) {
		
			   String languageName = record.get(0);
			   String iso639_2b = record.get(1);
			   iso639_2NamesDE.put(iso639_2b, languageName);
			}
		}
		return iso639_2NamesDE;
	}
	
	/**
	 * Mapping from ISO639-3 to 
	 * @return
	 */
	private void readISO639_3Codes() {

		String file = "iso-639-3.tab";
		
		Iterable<CSVRecord> records = readCodeTable(file, CSVFormat.TDF);
		
		if (records != null) {
			for (CSVRecord record : records) {
		
			   String code3 = record.get(0);
			   String code2b = record.get(1);
			   String nameEN = record.get(6);
			   iso639_3To2Map.put(code3, code2b);
			   iso639_3NamesEN.put(code3, nameEN);
			}
		}
	}
	
	
	private void readISO639_1Codes() {
		
		String file = "lexvo-iso639-1.tsv";
		
		Iterable<CSVRecord> records = readCodeTable(file, CSVFormat.TDF);
		
		if (records != null) {
			for (CSVRecord record : records) {
		
			   String code1 = record.get(0);
			   String lexvoURL = record.get(1);
			   iso639_1ToLexvo.put(code1, lexvoURL);
			}
		}
		
	}

	
	private static String getISO639_3FromISO639_1(String code1) {
		
		String lexvoUrl = iso639_1ToLexvo.get(code1);		
		if (lexvoUrl == null) return null;
		try {
			URL url = new URL(lexvoUrl);
			String code = new File(url.getPath()).getName();
			return code;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	private static ArrayList <LanguageMatch> computeBestLanguageMatchings(List<DetectedLanguage> detectedLanguages) {
		
		ArrayList<LanguageMatch> result = detectedLanguage2LanguageMatch(detectedLanguages);	
		
		// all entries in result are from the same column !
		LanguageMatch bestLanguage = null;
		float bestAverage = 0.0f;
		for (LanguageMatch x : result) {
			if (x.getAverageProb() > bestAverage) {
				bestAverage = x.getAverageProb();
				bestLanguage = x;
			}
		}
		
		ArrayList<LanguageMatch> bestLanguages = new ArrayList<LanguageMatch>();
		if (bestLanguage != null) {
			bestLanguage.setSelected(true);
			bestLanguages.add(bestLanguage);
		}
		
		return bestLanguages;
	}
	
	
	private static ArrayList<LanguageMatch> detectedLanguage2LanguageMatch(List<DetectedLanguage> detectedLanguages) {

		//Utils.debug("detectedLanguages : "+detectedLanguages.size());
		ArrayList<LanguageMatch> result = new ArrayList<LanguageMatch>();
		
		HashMap<String,Integer> count = new HashMap<String,Integer>();
		HashMap<String,Double> minProb = new HashMap<String,Double>();
		HashMap<String,Double> maxProb = new HashMap<String,Double>();
		HashMap<String,ArrayList<Double>> probs = new HashMap<String,ArrayList<Double>>();
		
		String lang;
		Double prob;
		
		for (DetectedLanguage dl : detectedLanguages) {
			
			lang = dl.getLocale().getLanguage();
			prob = dl.getProbability();
			
			//Utils.debug("lang :"+lang);
			//Utils.debug("prob :"+prob);
			if (!count.containsKey(lang)) {
				count.put(lang, 1);
				minProb.put(lang, prob);
				maxProb.put(lang, prob);
				ArrayList<Double> plist = new ArrayList<Double>();
				plist.add(prob);
				probs.put(lang, plist);
			} else {
				count.put(lang, count.get(lang)+1);
				if (minProb.get(lang) < prob) minProb.put(lang, prob);
				if (maxProb.get(lang) > prob) maxProb.put(lang, prob);
				ArrayList<Double> plist = probs.get(lang);
				plist.add(prob);
				probs.put(lang, plist);
			}
		}
		
		for (String lc : count.keySet()) {
			
			LanguageMatch lm;
			lm = new LanguageMatch(lc);
			ArrayList<Double> plist = probs.get(lc);
			Collections.sort(plist);
			lm.setSelected(false);
			
			if (!plist.isEmpty()) {
				lm.setMinProb(plist.get(0).floatValue());
				lm.setMaxProb(plist.get(plist.size()-1).floatValue());
				double sum = plist.stream().collect(Collectors.summingDouble(d -> d));
				lm.setAverageProb((float) (sum/detectedLanguages.size()));
			}
			result.add(lm);
		}
		return result;
	}
	
	
	/**
	 * Determine probabilities for languages in text
	 * @param text
	 * @return Sorted list of languages with best match on top
	 */
	private static List<DetectedLanguage> getOptimaizeLanguageProbabilities (String text) {
		
		com.optimaize.langdetect.LanguageDetector languageDetector = 
				com.optimaize.langdetect.LanguageDetectorBuilder.create(NgramExtractors.standard())
				.probabilityThreshold(0.1d)
				.withProfiles(optimaizeInternalLanguageProfiles)
		        .build();
		
		TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexingCleanText();

		try {
			TextObject textObject = textObjectFactory.forText(text);	
			List<DetectedLanguage> detectedLanguages = languageDetector.getProbabilities(textObject);
			Collections.sort(detectedLanguages);
			
			if (!detectedLanguages.isEmpty()) {
				for (DetectedLanguage xy : detectedLanguages) {
					System.out.println(xy.getLocale().getLanguage()+" "+xy.getProbability());
				}
			}
			
			return detectedLanguages;
		
		} catch (Exception e) {e.printStackTrace();}
		
		return null;
	}
	
}
