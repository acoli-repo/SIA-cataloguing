package org.acoli.sc.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;

public class CSVFile {

	private String filePath = ""; 
	private CSVFormat csvFormat = CSVFormat.DEFAULT;
	private Map<String, Integer> columnDescription = new HashMap<String, Integer>();
	
	/**
	 * Provide a description of a CSV resource. 
	 * @param filePath may be an absolute path or a resource path
	 * @param csvFormat
	 * @param columnDescription For author names use: 'given' for given name and 'family' for family name. Column indices start with 1!
	 */
	public CSVFile (String filePath, CSVFormat csvFormat, Map<String, Integer> columnDescription) {
		this.setFilePath(filePath);
		this.setCsvFormat(csvFormat);
		this.setColumnDescription(columnDescription);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public CSVFormat getCsvFormat() {
		return csvFormat;
	}

	public void setCsvFormat(CSVFormat csvFormat) {
		this.csvFormat = csvFormat;
	}

	public Map<String, Integer> getColumnDescription() {
		return columnDescription;
	}

	public void setColumnDescription(Map<String, Integer> columnDescription) {
		this.columnDescription = columnDescription;
	}

	
}
