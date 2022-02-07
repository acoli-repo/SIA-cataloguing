package org.acoli.sc.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.acoli.sc.extract.Author;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class NLPUtils {

/**
 * 
 * @param fileName
 * @param csvFormat
 * @param givenColumn column with given name info (first column is 1, not 0!)
 * @param familyColumn column with family name info (first column is 1, not 0!)
 * @return
 */
	private static List<Author> readAuthorNamesFromCSV (CSVFile csvFile) {
	
		List<Author> authors = new ArrayList<Author>();
	
		Iterable<CSVRecord> records;
		int readErrors = 0;
		int all = 0;
		try {
			records = CSVParser.parse(
					Utils.getResouceFile(csvFile.getFilePath()),
					StandardCharsets.UTF_8,
					csvFile.getCsvFormat()
					);
			
			for (CSVRecord record : records) {
				   all++;
				try {
					 String given = record.get(csvFile.getColumnDescription().get("given")-1).trim();
					 String family = record.get(csvFile.getColumnDescription().get("family")-1).trim();
					 authors.add(new Author(given, family));
				} catch (Exception e) {
					System.out.println(record);
					e.printStackTrace();
					readErrors++;
				}
			}
			System.out.println("Could not parse "+readErrors+"/"+all+" CSV records !");
			
			return authors;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static HashSet<String> getAuthorFamilyNames (CSVFile csvFile) {

		HashSet<String> familyNames = new HashSet<String>();
		
		for (Author a : readAuthorNamesFromCSV(csvFile)) {
			familyNames.add(a.getFamilyName());
		}
		
		return familyNames;
	}
	
	
	private static HashSet<String> getAuthorGivenNames (CSVFile csvFile) {

		HashSet<String> givenNames = new HashSet<String>();
		
		for (Author a : readAuthorNamesFromCSV(csvFile)) {
			givenNames.add(a.getGivenName());
		}
		
		return givenNames;
	}
	
	
	public static HashSet<String> getAuthorFamilyNamesFromCSVFiles(List<CSVFile> csvFiles) {
		
		HashSet<String> familyNames = new HashSet<String>();
		
		// read internal UB file
		Map<String, Integer> columnDescription = new HashMap<String, Integer>();
		columnDescription.put("family", 1);
		columnDescription.put("given", 2);
		CSVFile defaultCSVFile = new CSVFile("data/ub_creators_2021-07-02.csv", CSVFormat.DEFAULT, columnDescription);
		csvFiles.add(defaultCSVFile);
		
		for (CSVFile csvFile : csvFiles) {
			familyNames.addAll(getAuthorFamilyNames(csvFile));
		}
		
		return familyNames;
	}
	
	
	public static HashSet<String> getAuthorGivenNamesFromCSVFiles(List<CSVFile> csvFiles) {
		
		HashSet<String> familyNames = new HashSet<String>();
		
		// read internal UB file
		Map<String, Integer> columnDescription = new HashMap<String, Integer>();
		columnDescription.put("family", 1);
		columnDescription.put("given", 2);
		CSVFile defaultCSVFile = new CSVFile("data/ub_creators_2021-07-02.csv", CSVFormat.DEFAULT, columnDescription);
		csvFiles.add(defaultCSVFile);
		
		for (CSVFile csvFile : csvFiles) {
			familyNames.addAll(getAuthorGivenNames(csvFile));
		}
		
		return familyNames;
	}
	

	
}
