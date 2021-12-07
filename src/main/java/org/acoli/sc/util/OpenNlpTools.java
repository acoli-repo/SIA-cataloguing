package org.acoli.sc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.acoli.sc.config.Config;
import org.acoli.sc.extract.ParseAffiliationResult;
import org.acoli.sc.start.Run;

import edu.stanford.nlp.coref.data.Dictionaries.Person;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

/*
 * OpenNLP Tools and OpenNLP UIMA Annotators
 * https://opennlp.apache.org/maven-dependency.html
 * 
 * https://www.devglan.com/artificial-intelligence/stanford-nlp-pos-tagger-example
 * 
 * Examples (2012)
 * http://www.programcreek.com/2012/05/opennlp-tutorial/
 * also
 * https://www.programcreek.com/2012/07/java-example-for-using-stanford-postagger/
 * 
 * Trained models for different applications and languages
 * http://opennlp.sourceforge.net/models-1.5/
 * 
 * http://docs.oracle.com/javase/tutorial/i18n/text/convertintro.html
 * 
 * https://www.informatik.hu-berlin.de/de/forschung/gebiete/wbi/teaching/archive/ws1213/vl_textanalytics/studis/04_opennlp.pdf
 * 
 */

public class OpenNlpTools {
	
	
	static POSModel model;
	static POSTaggerME tagger;
	static TokenNameFinderModel nameFinderModel=null;
	static NameFinderME nameFinder = null;
	static String stanfordTagger = Run.stanfordTaggerPath;
	//static String stanfordModelPath = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/stanford-postagger-full-2020-11-17/models/english-caseless-left3words-distsim.tagger";
	//static String stanfordModelPath = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/stanford-postagger-full-2020-11-17/models/english-left3words-distsim.tagger";
	
	static String stanfordNERclassifier = Run.stanfordNerClassifierPath;
	//static String serializedClassifier = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/stanford-postagger-full-2020-11-17/NER-classifiers/english.all.3class.distsim.crf.ser.gz";
	
	static String openNLP_NER_Model = Run.openNlpNerModelPath;
	static String openNLP_POS_Model = Run.openNlpPosModelPath;
	
	static CRFClassifier<CoreLabel> classifier;
	static MaxentTagger taggerMax;
	
	static AffiliationUtils affiliationUtils;
	
	public OpenNlpTools () {
		
		model = new POSModelLoader()
	            .load(new File(openNLP_POS_Model));
		tagger = new POSTaggerME(model);
		try {
			InputStream is = new FileInputStream(openNLP_NER_Model);
			nameFinderModel = new TokenNameFinderModel(is);
			nameFinder = new NameFinderME(nameFinderModel);
			taggerMax = new MaxentTagger(stanfordTagger);
			is.close();
			classifier = CRFClassifier.getClassifier(stanfordNERclassifier);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String removeEmail(String text) {
		
		return text.replaceAll("[^ ]+@.+\\.[a-zA-Z]{2,6}", "");
	}
	
	
	public static String removeCopyright(String text) {
		
		return text.replaceAll("©.\\d+", "");
	}
	
	
	public static String renoveAffilationEmailCopyright(String text) {
		
		
		// remove Email, Copyright
		text = removeCopyright(removeEmail(text));
		
		List<TaggedWord>  result = new ArrayList<TaggedWord>();
		List<TaggedWord> sentence = stanfordTaggedWords(text);
		
		// erase NFP (*_) and mark ,:;!? with own tag PUNC
		Iterator<TaggedWord> iterator = sentence.iterator();
		while (iterator.hasNext()) {
			TaggedWord tw = iterator.next();
			if (tw.tag().equals("NFP")) {iterator.remove();continue;};
		    if (tw.word().length() == 1 && tw.word().matches("[^a-zA-Z0-9&]")) {
		    	tw.setTag("PUNC");
		    };
		}
		
		// Check affiliations file
		ParseAffiliationResult par = AffiliationUtils.matchTokenInAffiliation(sentence);
		int leftMostMatchedToken = par.getLeftmostMatch();
//		System.out.println("hier : "+par.getLeftmostMatch());
//		System.out.println("hier : "+par.getLeftmostMatchingAffiliation());
//		System.out.println("hier : "+par.getLeftmostMatchingStopword());
//		System.out.println("hier : "+par.getPositionOfleftmostMatchingAffiliation());
//		System.out.println("hier : "+par.getPositionOfleftmostMatchingStopword());


		if (leftMostMatchedToken > ParseAffiliationResult.NOT_FOUND) {
			
			// cut sentence before a matched affiliation stop-word
			if (par.affiliationWasMatched()) {
				return cutSentenceBeforeToken(sentence, leftMostMatchedToken);
			}
			
			// cut sentence before matched stop-word, if the stop-word is not university
			if (par.stopwordWasMatched()) { 
				if (!par.getLeftmostMatchingStopword().toLowerCase().equals("university")) {
					return cutSentenceBeforeToken(sentence, leftMostMatchedToken);
			}
			}
		} else {
			
			// do nothing if neither a stop-word or an affiliation was found
			return text;
		}
		
		boolean useNNP = false; // disable cutting after token with syntactic category that
		                        // indicates a non-author expression (e.g. the=DT)
		// Proceed with the generic approach (below) :
		// - if neither an affiliation or a stop-word was found
		// - if no affiliation, but a stop-word was found, and this stop-word equals to 'university'

    	int i = 0;
    	int tokenIndexOfFirstUniversity = -1;
    	int tokenIndexOfFirstUniversityOf = -1;
    	int tokenIndexOfFirstNonNNPCategory = -1;
    	String wtext = "";
    	String wtextLower = "";
    	
    	String wtag = "";
        while (i < sentence.size()) {
        	
        	wtext = sentence.get(i).word();
        	wtextLower = wtext.toLowerCase();
        	wtag = sentence.get(i).tag();
        	System.out.println(wtextLower);
        	System.out.println(wtag);
        	System.out.println();
        	
        	
        	if (tokenIndexOfFirstUniversity == -1 && wtextLower.equals("university")) {
    			tokenIndexOfFirstUniversity = i;
    		}
        	
        	// use tag = IN, DT... instead of words
        	/*if (tokenIndexOfFirstPrep == -1 && (
        		wtextLower.equals("of") ||
        		wtextLower.equals("the") ||
        		wtextLower.equals("in") ||
        		wtextLower.equals("for") ||
        		wtextLower.equals("at"))
        		) {
        			tokenIndexOfFirstPrep = i;
        	}*/
        	
        	if(useNNP) {
        	if (tokenIndexOfFirstNonNNPCategory == -1 && (
        			!wtag.equals("NNP") &&
        			!wtag.equals("NN") &&
        			!wtag.equals("NNS") &&
        			!wtag.equals("CC") &&
        			!wtag.equals(",") && // now PUNC
        			!wtag.equals(";") && // now PUNC
        			!wtag.equals(":") && // now PUNC
        			!wtag.equals("PUNC") &&
        			!wtag.equals("NFP") &&
        			!wtag.equals("HYPH")
	        		)) {
        			System.out.println("hello :"+wtag);
	        			tokenIndexOfFirstNonNNPCategory = i;
	        		if (wtag.equals("DT")) {
	        			tokenIndexOfFirstNonNNPCategory = i+1;
	        		}
	        	}
        	}

        	if (i+1 < sentence.size()) {
        		if (wtextLower.equals("university") &&
        			sentence.get(i+1).word().toLowerCase().equals("of")) {	
	        		tokenIndexOfFirstUniversityOf = i+1;
	        		break;
        		}
        	 }
        	i++;
        }
    
    	i = 0;
    	int seperators = 0;
        while (i < sentence.size()) {
        
          wtext = sentence.get(i).word();
          if (sentence.get(i).tag().equals("PUNC")) seperators++;
          
          if (tokenIndexOfFirstNonNNPCategory > -1     &&
              i >= tokenIndexOfFirstNonNNPCategory - 1 &&
    		  i - seperators > 1)                           // ensure that author has at least surname and name
    	      {
	          		System.out.println("skipping after tokenIndexOfFirstNonNNPCategory : "+(tokenIndexOfFirstNonNNPCategory-1));
	          		break;
	          	    //i++;continue;
	          }
    	  
        	
          
          // skip anything after 'university of'
          if (tokenIndexOfFirstUniversity > -1) {
        	  
        	  if (tokenIndexOfFirstUniversityOf == -1 || tokenIndexOfFirstUniversity != tokenIndexOfFirstUniversityOf - 1) {
        		  // skip anything after 'xuy university'
        		  if (i >= tokenIndexOfFirstUniversity-1) {
		        	  System.out.println("skipping after xyz 'university'");
        			  break;} 
        	  } else {
        		  if (i >= tokenIndexOfFirstUniversity) {
		        	  System.out.println("skipping after 'university of' ");
        			  break;}
        	  }
          }
	        		 
		          
		         
		          
		          result.add(sentence.get(i));
		          
//		          switch (wtag)) {
//		          
//		          case "PERSON" :
//		        	  result+=" "+wtext;
//		        	  break;
//		        	  
//		          case "LOCATION" :
//		        	  break;
//		          
//		          case "ORGANIZATION" :
//		        	  
//		        	  if (tokenIndexOfFirstUniversityOf > -1) {
//		        		 if (i < tokenIndexOfFirstUniversityOf) {
//		        			 result+=" "+label;
//		        		 }
//		        	  }
//		        	  
//		        	  break;
//		        	  
//		          default:
//		        	  if (label.matches("[,;]")) {
//		        			 result+=" "+label;
//
//		        	  }
//		          }
		         
		        i++;
		        }	
			
		System.out.println("in :"+text);
        System.out.println("out :"+result);
        
//        // finally check for missed location and remove if remaining author length >= 2
//        Iterator<TaggedWord> iterator2 = result.iterator();
//        i = 0;
//        int punctuations = 0;
//        while (iterator2.hasNext()) {
//        	TaggedWord tw = iterator2.next();
//        	if (tw.tag().equals("PUNC")) punctuations++;
//        	String cat = stanfordNerForSingleToken(tw.word());
//	    	if (cat != null && (cat.equals("LOCATION") || cat.equals("ORGANIZATION"))) {
//	    		System.out.println(tw.word()+" : "+cat+" ??? ");
//	    		if (i - punctuations > 1) {
//	    			iterator2.remove();
//	    		}
//	    	}
//	    	i++;
//	    }
        
        // build result string
        String r = "";
        for (TaggedWord tw : result) {
        	r+=" "+tw.word();
        }
        
		return r;
	}
	
	
	private static String cutSentenceBeforeToken(List<TaggedWord> sentence, int leftMostMatchedToken) {
		
		String result2 = "";
		for (TaggedWord tw : sentence.subList(0, leftMostMatchedToken)) {
			result2+= tw.word()+" ";
		}
		return result2.trim();
	}


	public static String stanfordNerForSingleToken(String token) {
		
		List<List<CoreLabel>> out = classifier.classify(token);
	    for (List<CoreLabel> y : out) {
	    	for (CoreLabel word : y) {
		    	System.out.println(word +" : "+word.get(CoreAnnotations.AnswerAnnotation.class));
	    		return word.get(CoreAnnotations.AnswerAnnotation.class);
	    	}
	    }
		return null;
	}
	
	
	
	public static String stanfordNer(String text) {
		
		String result="";
	
	    AbstractSequenceClassifier<CoreLabel> classifier;
		try {
			classifier = CRFClassifier.getClassifier(stanfordNERclassifier);
			List<List<CoreLabel>> out = classifier.classify(text);
		    for (List<CoreLabel> sentence : out) {
		    	
		    	int i = 0;
		    	int tokenIndexOfFirstUniversity = -1;
		    	int tokenIndexOfFirstUniversityOf = -1;
		    	int tokenIndexOfFirstPrep = -1;
		    	String wtext = "";
		        while (i < sentence.size()) {
		        	
		        	wtext = sentence.get(i).word().toLowerCase();
		        	
		        	if (tokenIndexOfFirstUniversity == -1 && wtext.equals("university")) {
	        			tokenIndexOfFirstUniversity = i;
	        		}
		        	
		        	if (tokenIndexOfFirstPrep == -1 && (
		        		wtext.equals("of") ||
		        		wtext.equals("in") ||
		        		wtext.equals("for") ||
		        		wtext.equals("at"))
		        		) {
		        			tokenIndexOfFirstPrep = i;
		        		}

		        	if (i+1 < sentence.size()) {
		        		if (wtext.equals("university") &&
		        			sentence.get(i).word().toLowerCase().equals("of")) {	
			        		 tokenIndexOfFirstUniversityOf = i;
			        		 break;
		        		}
		        	 }
		        	i++;
		        }
		    
		    	i = 0;
		        while (i < sentence.size()) {
		        	
		          // skip anything after 'university of'
		          if (tokenIndexOfFirstUniversityOf > -1 && i >= tokenIndexOfFirstUniversityOf) {
		        		  break;
		          }
		          if (tokenIndexOfFirstUniversity > -1 && i >= tokenIndexOfFirstUniversity) {
	        		  break;
	              }
		          
		          CoreLabel word = sentence.get(i);
		          String label = word.word();
		          System.out.println(label);
		          System.out.println(word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
		          
		          if (tokenIndexOfFirstPrep > -1) {
	        		  if (i >= tokenIndexOfFirstPrep - 1) {i++;continue;}
	        	  }
		          
		          switch (word.get(CoreAnnotations.AnswerAnnotation.class)) {
		          
		          case "PERSON" :
		        	  result+=" "+label;
		        	  break;
		        	  
		          case "LOCATION" :
		        	  break;
		          
		          case "ORGANIZATION" :
		        	  		        	  
		        	  if (tokenIndexOfFirstUniversityOf > -1) {
		        		 if (i < tokenIndexOfFirstUniversityOf) {
		        			 result+=" "+label;
		        		 }
		        	  }
		        	  
		        	  break;
		        	  
		          default:
		        	  if (label.matches("[,;]")) {
		        			 result+=" "+label;

		        	  }
		          }
		         
		        i++;
		        }
			}		
			} catch (Exception e) {
				e.printStackTrace();
			}
		System.out.println("stanfordNer");
		System.out.println("in :"+text);
        System.out.println("out :"+result);
		return result;
	}
	

	// open-nlp
	public static List<String> detectPartsOfSpeech(String text) {
		
		// Tag descriptions :
		// OpenNLP uses the same tags as defined in the Penn Tree Bank
		// https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
		// 
		// https://www.guru99.com/pos-tagging-chunking-nltk.html
		// https://www.baeldung.com/apache-open-nlp
		
		/*
		1. 	    CC 	Coordinating conjunction
		2. 	    CD 	Cardinal number
		3. 	    DT 	Determiner
		4. 	    EX 	Existential there
		5. 	    FW 	Foreign word
		6. 	    IN 	Preposition or subordinating conjunction
		7. 	    JJ 	Adjective
		8. 	    JJR 	Adjective, comparative
		9. 	    JJS 	Adjective, superlative
		10. 	LS 	List item marker
		11. 	MD 	Modal
		12. 	NN 	Noun, singular or mass
		13. 	NNS 	Noun, plural
		14. 	NNP 	Proper noun, singular
		15. 	NNPS 	Proper noun, plural
		16. 	PDT 	Predeterminer
		17. 	POS 	Possessive ending
		18. 	PRP 	Personal pronoun
		19. 	PRP$ 	Possessive pronoun
		20. 	RB 	Adverb
		21. 	RBR 	Adverb, comparative
		22. 	RBS 	Adverb, superlative
		23. 	RP 	Particle
		24. 	SYM 	Symbol
		25. 	TO 	to
		26. 	UH 	Interjection
		27. 	VB 	Verb, base form
		28. 	VBD 	Verb, past tense
		29. 	VBG 	Verb, gerund or present participle
		30. 	VBN 	Verb, past participle
		31. 	VBP 	Verb, non-3rd person singular present
		32. 	VBZ 	Verb, 3rd person singular present
		33. 	WDT 	Wh-determiner
		34. 	WP 	Wh-pronoun
		35. 	WP$ 	Possessive wh-pronoun
		36. 	WRB 	Wh-adverb 
		31.     HYPH     -
		*/
		

		
//	    String sentence = "POS processing is useful for enhancing the "
//	            + "quality of data sent to other elements of a pipeline.";
//	    
//	    sentence = "Recent approaches to bridging: Truth, coherence, relevance";

//	    POSModel model = new POSModelLoader()
//	            .load(new File("/home/demo/Schreibtisch/en-open-nlp-models/en-pos-maxent.bin"));
//	    POSTaggerME tagger = new POSTaggerME(model);

	    String tokens[] = WhitespaceTokenizer.INSTANCE
	            .tokenize(text);
	    String[] tags = tagger.tag(tokens);

	    POSSample sample = new POSSample(tokens, tags);
	    String posTokens[] = sample.getSentence();
	    String posTags[] = sample.getTags();
//	    for (int i = 0; i < posTokens.length; i++) {
//	        System.out.print(posTokens[i] + " - " + posTags[i]);
//	    }
	    System.out.println("checking POS");

	    for (int i = 0; i < tokens.length; i++) {
	        System.out.print(tokens[i] + "[" + tags[i] + "] ");
	    }
	    return Arrays.asList(tags);
	}
	
	
	
	public static int findNames(String text) {
	 
//		String [] sentence = new String[]{
//			    "Mike",
//			    "and",
//			    "John",
//			    "are",
//			    "good",
//			    "friends"
//			    };
		
		String [] sentence = tokenize(text);
		Span nameSpans[] = nameFinder.find(sentence);
 
//		for(Span s: nameSpans) {
//			System.out.println(s.getStart());
//			System.out.println(s.getEnd());
//		}
//		System.out.println(nameSpans.length);
		return nameSpans.length;
	}
	
	
	public static String[] tokenize (String text) {
		return WhitespaceTokenizer.INSTANCE.tokenize(text);
	}
	
	
	
	public static List<TaggedWord> stanfordTagger(String text) {
		
		List<String> tags = new ArrayList<String>();
		
	    TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
										   "untokenizable=noneKeep");
		Reader targetReader = new StringReader(text);
		
	    PrintWriter pw;
	    List<TaggedWord> tSentence = null;
		try {
			//pw = new PrintWriter(new OutputStreamWriter(System.out, "utf-8"));
		
	    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(targetReader);
	    documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
	    for (List<HasWord> sentence : documentPreprocessor) {
	      tSentence = taggerMax.tagSentence(sentence);
	      for (TaggedWord tw : tSentence) {
		      System.out.println(tw.toString());
		      tags.add(tw.tag());
		      }
	      	 return tSentence;
		  }
	    
	      //pw.println(SentenceUtils.listToString(tSentence, false));

	    // print the adjectives in one more sentence. This shows how to get at words and tags in a tagged sentence.
//	    List<HasWord> sent = SentenceUtils.toWordList("The", "slimy", "slug", "crawled", "over", "the", "long", ",", "green", "grass", ".");
//	    List<TaggedWord> taggedSent = tagger.tagSentence(sent);
//	    for (TaggedWord tw : taggedSent) {
//	      if (tw.tag().startsWith("JJ")) {
//	        pw.println(tw.word());
//	      }
//	    }

	    //pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tSentence;
	  }
	
	
	
	public static List<TaggedWord> stanfordTaggedWords(String text) {
		
	    TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
										   "untokenizable=noneKeep");
		Reader targetReader = new StringReader(text);
		
		try {
		
		    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(targetReader);
		    documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
		    for (List<HasWord> sentence : documentPreprocessor) {
		      return taggerMax.tagSentence(sentence);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	  }
	
	
	
	public static void stanfordTaggerShort(String text) {
		
		MaxentTagger tagger = new MaxentTagger(stanfordTagger);
		Reader targetReader = new StringReader(text);
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(targetReader);
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			System.out.println(SentenceUtils.listToString(tSentence, false));
			
    }
  }

	
	public static List<String> detectPartsOfSpeechStanford17(String text) {

        MaxentTagger maxentTagger = new MaxentTagger(stanfordTagger);;
        String tag = maxentTagger.tagString(text);
        String[] eachTag = tag.split("\\s+");
        System.out.println("Word      " + "Standford tag");
        System.out.println("----------------------------------");
        for(int i = 0; i< eachTag.length; i++) {
        	System.out.println(eachTag[i]);
            //System.out.println(eachTag[i].split("_")[0] +"           "+ eachTag[i].split("_")[1]);
        }
        
        return Arrays.asList(eachTag);

    }
	


	
	public static void main(String[] args) {
		
		OpenNlpTools tools = new OpenNlpTools();
    	AffiliationUtils.main(args);
		
	    String sentence = "James Stewart";
	    sentence = "POS processing is useful for enhancing the quality of data sent to other elements of a pipeline.";
	    //OpenNlpTools.detectPartsOfSpeech(sentence);
	    
	    //OpenNlpTools.findNames("Deidre Wilson AND Tomoko Matsui");
	    sentence="© 2006 Camilla Thurén, Malmö University, cam@m-uni.se";
	    //sentence="with affilation          : Hye-Min Kang & Ellen Thompson  Florida International University  hkang007@fiu.edu, thompson@fiu.edu";
	    //sentence="Jeff Stevens jps@u.washington.edu";
	    //stanfordTagger(sentence);
	  
	    sentence = "YOUNG KOOK KIM";
	    sentence = "Tam Blaxter & David Willis Cambridge University and University of Oxford";
	    sentence = "Hye-Min Kang & Ellen Thompson  Florida International University";
//	    for (String x : sentence.split(" ")) {
//	    	stanfordNerForSingleToken(x);
//	    }
	    String text = renoveAffilationEmailCopyright(sentence);
	    System.out.println("result :"+text);
	}

}


// old
//public static void posTag(POSTaggerME tagger, String input, HashSet <String> set) throws IOException {
//	POSModel model = new POSModelLoader().load(new File("open-nlp-models/en-pos-maxent.bin"));
//	//PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
//	//POSTaggerME tagger = new POSTaggerME(model);
//	//perfMon.start();
//	
//	String tokenized[] = tokenize(input);
//	//String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(input);
//	String[] tags = tagger.tag(tokenized);
//	
//	int i = 0;
//	int n = tags.length;
//	while (i < n) {
//		
//		// Filter [ADJA;NN]
//		if (tags[i].equals("ADJA") && (i+1 < n && tags[i+1].equals("NN"))) {
//			set.add((tokenized[i]+","+tokenized[i+1]).replaceAll("[.;\"\'‘’“”]",""));
//			i = i + 2;continue;
//		}
//		
//		// Filter [NN]
//		if (tags[i].equals("NN")) {
//			set.add(tokenized[i].replaceAll("[.;\"\'‘’“”]",""));
//			i = i + 1;continue;
//		}
//		
//		//System.out.println(tokenized[i]+" : "+tags[i]);
//		i++;
//	}
//POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
//System.out.println(sample.toString());
//perfMon.incrementCounter();
//perfMon.stopAndPrintFinalResult();
//}


//public static void POSTag() throws IOException {
//POSModel model = new POSModelLoader()	
//	.load(new File("en-pos-maxent.bin"));
//PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
//POSTaggerME tagger = new POSTaggerME(model);
//
//String input = "Hi. How are you? This is Mike.";
//InputStreamFactory y = new MarkableFileInputStreamFactory(null);
//ObjectStream<String> lineStream = new PlainTextByLineStream(y, input);
//
//perfMon.start();
//String line;
//while ((line = lineStream.read()) != null) {
//
//	String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
//			.tokenize(line);
//	String[] tags = tagger.tag(whitespaceTokenizerLine);
//
//	POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
//	System.out.println(sample.toString());
//
//	perfMon.incrementCounter();
//}
//perfMon.stopAndPrintFinalResult();
//}


///*
// * Sentence detector
// * 
// */
//	public static String [] sentenceDetect(String string) throws InvalidFormatException,IOException {
//		
//		// always start with a model, a model is learned from training data
//		InputStream is = new FileInputStream("open-nlp-models/en-sent.bin");
//		SentenceModel model = new SentenceModel(is);
//		SentenceDetectorME sdetector = new SentenceDetectorME(model);
//		
//		String sentences[] = sdetector.sentDetect(string);
//		
//		is.close();
//		return sentences;
//	}
//	
//	
///*
// * Tokenizer
// * http://opennlp.sourceforge.net/api/opennlp/tools/tokenize/Tokenizer.html
// */
//	public static String [] tokenize(String string) throws InvalidFormatException, IOException {
//		
//		ArrayList <String> result = new ArrayList <String> ();
//		String [] camcSplit;
//		
//		InputStream is = new FileInputStream("open-nlp-models/en-token.bin");
//		TokenizerModel model = new TokenizerModel(is);
//		Tokenizer tokenizer = new TokenizerME(model);
//	 
//		// Tokenize string and split tokens which are concatenated words xYz or XyZ in seperate words
//		for (String a : tokenizer.tokenize(string)) {
//			
//			// Split camelcase words 
//			camcSplit = StringUtils.splitByCharacterTypeCamelCase(a);
//			
//			for (String b : camcSplit)
//				result.add(b);
//		}
//	 
//		is.close();
//		
//		String tokens[] = new String[result.size()];
//		return result.toArray(tokens);
//	}


