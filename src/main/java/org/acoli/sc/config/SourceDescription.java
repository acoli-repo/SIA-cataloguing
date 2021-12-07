package org.acoli.sc.config;

import java.net.URL;

import org.acoli.sc.extract.PDFExtractionConfiguration;

/**
 * unchanged
 *
 */
public class SourceDescription {
	
	private String id;
	private String subId;
    public URL url;
    public SourceType type;
    public boolean split;
   	private boolean active;
    private PDFExtractionConfiguration extractorConfig;
	private String keywordDefinitionId;  // id of used definition for keywords

	
	public PDFExtractionConfiguration getExtractorConfig() {
        return extractorConfig;
    }

    public URL getUrl() {
        return url;
    }

    public SourceType getType() {
        return type;
    }

	public String getId() {
		return id;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getSubId() {
		return subId;
	}
	
	public String getKeywordDefinitionId() {
		return keywordDefinitionId;
	}

	
	@Override
    public String toString() {
		
		String d = "SourceDescription\n"
				+ "id "+id+"\n"
				+ "subId "+subId+"\n"
				+ "url "+url+"\n"
				+ "type "+type+"\n"
				+ "split "+split+"\n";
		return d;
	}
}
