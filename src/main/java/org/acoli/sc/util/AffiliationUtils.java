package org.acoli.sc.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.acoli.sc.config.Config;
import org.acoli.sc.extract.PDFExtractionConfiguration;
import org.acoli.sc.extract.ParseAffiliationResult;
import org.apache.commons.io.FileUtils;

import edu.stanford.nlp.ling.TaggedWord;

public class AffiliationUtils {
	
	
	private static HashMap<String, List<ArrayList<String>>> stopWords2TheLeft = new HashMap<String,List<ArrayList<String>>>(); // keys in lower-case
	private static HashMap<String, List<ArrayList<String>>> stopWords2TheRight = new HashMap<String,List<ArrayList<String>>>(); // keys in lower-case

	
	public AffiliationUtils(Config config) {
		createStopWords2AffiliationMap(config.getStopWordFiles(), config.getAffiliationFiles());
	}
	
	
	
	/**
	 * Find longest matching infix (common substring) in input text and list of affiliations.
	 * Example : for text="John Ford, The greatest college in the world" and affiliations=Florida College of Arts", 
	 * "The greatest College of all times" return "The greatest college"<p>
	 * @param text
	 * @return index in text where longest comment infix starts 
	 */
	public static ParseAffiliationResult matchTokenInAffiliation(List<TaggedWord> text) {
		
		// Find longest matching infix (e.g. for text="a b c d" and affiliation="d b c f" return "b c"
		int positionOfleftmostMatchingStopword=1000;
		String leftmostMatchingStopword = "";
		int positionOfleftmostMatchingAffiliation=1000;
		String leftmostMatchingAffiliation = "";
		String token="";
		int i = 0;
		while (i < text.size()) {
			
			if (text.get(i).tag().equals("PUNC")) {
				i++;
				continue;	// skip punctuation (,; etc.)
			}
			token = text.get(i).word().toLowerCase().trim();
			if (!stopWords2TheLeft.containsKey(token)) {
				i++;
				continue; // token not matched in known affiliations
			} else {
				if (i < positionOfleftmostMatchingStopword) {
					positionOfleftmostMatchingStopword=i; 	// remember leftmost found stop-word
					leftmostMatchingStopword=token;
				}
			}
			
			// Token found in text :
			System.out.println("Found token : "+token);
			// Find longest matching infix for token
			List<ArrayList<String>> prefixResults = stopWords2TheLeft.get(token); // all affiliations that contain the token
			for (List<String> pfx : prefixResults) {
				// go to the left and find max. prefix
				int l = pfx.size(); // prefix length
				int j = 1;
				while (j <= l && i-j >= 0) {
					
					// skip punctuation (,; etc.)
					if (text.get(i-j).tag().equals("PUNC")) {
						j++;continue;	
					}
					
					// Compare token in prefix with token in text
					if (pfx.get(l-j).toLowerCase().trim().equals(text.get(i-j).word().toLowerCase().trim())) {
						if (i-j < positionOfleftmostMatchingAffiliation) { 
							positionOfleftmostMatchingAffiliation = i-j; // position in text
							leftmostMatchingAffiliation = String.join(" ", pfx.subList(l-j, l));
						}
					} else {
						break;
					}
					j++;
				}
			}
			i++;
		}
		System.out.println("Text : ");
		for (TaggedWord tword : text) {
			System.out.print(tword.word()+" ");
		}
		System.out.println();
		System.out.println("leftmostMatchingAffiliation :"+leftmostMatchingAffiliation);
		System.out.println("positionOfleftmostMatchingAffiliation :"+positionOfleftmostMatchingAffiliation);
		System.out.println("leftmostMatchingStopword :"+leftmostMatchingStopword);
		System.out.println("positionOfleftmostFoundStopword :"+positionOfleftmostMatchingStopword);

		ParseAffiliationResult result = 
				new ParseAffiliationResult(
					leftmostMatchingStopword,
					positionOfleftmostMatchingStopword,
					leftmostMatchingAffiliation,
					positionOfleftmostMatchingAffiliation
					);
		
		return result;
	}
	
	
	
	public static boolean createStopWords2AffiliationMap(List<File> stopWordFiles, List<File> affiliationFiles) {
		
		boolean printLog = false;
		HashSet<String> affiliations = new HashSet<String>();
		HashSet<String> stopWords = new HashSet<String>();
		
		try {
			
			// Read stop word files
			for (File swf : stopWordFiles) {
				HashSet<String> tmp = new HashSet<String>(FileUtils.readLines(swf));
				stopWords.addAll(tmp);
			}
			
			System.out.println("stop words ."+stopWords.size());
	
			
			// Read affiliation files
			for (File af : affiliationFiles) {
				HashSet<String> tmp = new HashSet<String>(FileUtils.readLines(af));
				affiliations.addAll(tmp);
			}
			
			System.out.println("affiliations : "+affiliations.size());
		
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		
		// read stopWordsFile, affiliationsFile
		int ix = -1;
		int good = 0;
		for (String afl : affiliations) {
			
			afl = afl.toLowerCase().trim().replace(",","");
			boolean found = false;
			for (String sw : stopWords) {
				
				sw = sw.toLowerCase().trim();
				
				// check if affiliation contains stop word
				ix = afl.indexOf(sw); // use regex with \b
				
				if (ix > -1) {
					good++;			
					ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(afl.split(" ")));
					
					if(printLog) {
						System.out.println();
						for (String tok : tokens) {
							System.out.println(tok);
						}
					}
					
					// remove non-words
					int i = 0;
					while (i < tokens.size()) {
						if (sw.equals(tokens.get(i).toLowerCase().trim())) {
							
							if(printLog)
								System.out.println(tokens.get(i)+" == "+sw);

							if (!stopWords2TheLeft.containsKey(sw)) {
								List<ArrayList<String>> afListLeft = new ArrayList<ArrayList<String>>();
								afListLeft.add(new ArrayList<String>(tokens.subList(0, i)));
								stopWords2TheLeft.put(sw, afListLeft);
								
								List<ArrayList<String>> afListRight = new ArrayList<ArrayList<String>>();
								afListRight.add(new ArrayList<String>(tokens.subList(i, tokens.size())));
								stopWords2TheRight.put(sw, afListRight);
							} else {
								List<ArrayList<String>> afListLeft = stopWords2TheLeft.get(sw);
								afListLeft.add(new ArrayList<String>(tokens.subList(0, i)));
								stopWords2TheLeft.put(sw, afListLeft);
								
								List<ArrayList<String>> afListRight = stopWords2TheRight.get(sw);;
								afListRight.add(new ArrayList<String>(tokens.subList(i, tokens.size())));
								stopWords2TheRight.put(sw, afListRight);
							}
							break;
						}
						i++;
					}
					
				}
			}
		}
		
		System.out.println(good);
		return true;	
	}
	
	
	public static Set<String> createTranslations(File germanStopWordsFile) {
		
		List<String> stopWordsGerman = new ArrayList<String>();
		Set<String> stopWordTranslations = new HashSet<String>();
		try {
			stopWordsGerman = FileUtils.readLines(germanStopWordsFile);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return stopWordTranslations;
	}


	public static HashMap<String, List<ArrayList<String>>> getStopWords2TheLeft() {
		return stopWords2TheLeft;
	}


	public void setStopWords2TheLeft(HashMap<String, List<ArrayList<String>>> stopWords2TheLeft) {
		AffiliationUtils.stopWords2TheLeft = stopWords2TheLeft;
	}


	public static HashMap<String, List<ArrayList<String>>> getStopWords2TheRight() {
		return stopWords2TheRight;
	}


	public static void setStopWords2TheRight(HashMap<String, List<ArrayList<String>>> stopWords2TheRight) {
		AffiliationUtils.stopWords2TheRight = stopWords2TheRight;
	}
	
	public static Set<String> getStopWords() {
		return stopWords2TheLeft.keySet();
	}
	
	
	public static void main (String[] args) {
		
//		List<String> result = new ArrayList<String>();
//		File outputFile = new File("/home/demo/Schreibtisch/se_resources/name_lists/lexvo_university_multiling2");
//		try {
//			List<String> lines = FileUtils.readLines(new File("/home/demo/Schreibtisch/se_resources/name_lists/lexvo_university_multiling"));
//			for (String line : lines) {
//				String url = URLDecoder.decode(line, StandardCharsets.UTF_8.name());
//				System.out.println(line);
//				System.out.println(url);
//				System.out.println();
//				result.add(url);
//			}
//			FileUtils.writeLines(outputFile, null, result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (true) return;
		
		String folder = "/home/demo/Schreibtisch/se_resources/name_lists";
		File stopWords = new File(folder, "ub_stopwords_eng_short");
		File affiliations = new File(folder, "ub_affiliations_2021-07-02.txt");
		affiliations = new File(folder, "wikidata_q3918_affiliations");
		File lexvo_stopword_university_multiling = new File(folder,"lexvo_stopword_university_multiling");

		List<File> fls = new ArrayList<File>();
		fls.add(stopWords);
		fls.add(lexvo_stopword_university_multiling);
		List<File> fla = new ArrayList<File>();
		fla.add(affiliations);

		createStopWords2AffiliationMap(fls, fla);
		
//		System.out.println("stopWords:");
//		for (String sw : getStopWords()) {
//			System.out.println(sw+"\t\t\t"+getStopWords2TheLeft().get(sw).size());
//			int i = 0;
//			for (List<String> pfxl : getStopWords2TheLeft().get(sw)) {
//				System.out.println(i);
//				for (String pfx : pfxl) {
//					System.out.print(pfx+" ");
//				}
//				System.out.println("("+sw+")");
//				i++;
//			}
//		}
		
//		OpenNlpTools nlp = new OpenNlpTools();
//		String line = "Tam Blaxter & David Willis University of Cambridge and University of Oxford";
//		//line="© 2006 Camilla Thurén, Malmö University, cam@m-uni.se";
//		line = "Hye-Min Kang & Ellen Thompson  Florida International University";
//
//		List<TaggedWord> text = OpenNlpTools.stanfordTaggedWords(line);
//		System.out.println(matchTokenInAffiliation(text));
		
	}
}



//if (positionOfleftmostMatchingAffiliation == 1000) {
//if (positionOfleftmostMatchingStopword < 1000) {
//	result.setLeftmostMatchingStopWord(leftmostMatchingStopword);
//	result.setPositionOfleftmostMatchingStopword(positionOfleftmostMatchingStopword);
//	return result; //positionOfleftmostMatchingStopword;
//} else {
//	return result; //-1;
//}
//} else {
//if (positionOfleftmostMatchingAffiliation < positionOfleftmostMatchingStopword) {
//	result.setLeftmostMatchingAffiliation(leftmostMatchingStopword);
//	result.setPositionOfleftmostMatchingStopword(positionOfleftmostMatchingStopword);
//	return result; // positionOfleftmostMatchingAffiliation;
//} else {
//	result.setLeftmostMatchingStopWord(leftmostMatchingStopword);
//	result.setPositionOfleftmostMatchingStopword(positionOfleftmostMatchingStopword);
//	return result; //positionOfleftmostMatchingStopword;
//}
//}
