package org.acoli.sc.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.acoli.informatik.uni.frankfurt.classifier.BibAnalyzer;

public class BibAnalyzerTools {
	
	
	
	public static void main(String[] args) {
		
		
		String [] x = {"/home/demo/Schreibtisch/se_resources/bibanalyzer-master/test-examples/cite.txt"};
		String [] y = {"/home/demo/Schreibtisch/se_resources/bibanalyzer-master/test-examples/footer.txt"};

		try {
			BibAnalyzer.analyzeBibliography(
					x,
					"/home/demo/Schreibtisch/se_resources/bibanalyzer-master/bibanalyzer/");
			BibAnalyzer.analyzeBibliography(
					x,
					"/home/demo/Schreibtisch/se_resources/bibanalyzer-master/bibanalyzer/");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
