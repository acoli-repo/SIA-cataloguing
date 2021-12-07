package org.acoli.sc.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.acoli.sc.extract.KeywordDefinition;

/**
 * unchanged
 * @author frank
 *
 */
public class Config {
	
    private String documentRootDir;
    private String xmlDir;
    private String exportDir;
    private String databaseDir;
    private List<String> stopWordFiles;
    private List<String> affiliationFiles;
    private List<SourceDescription> sources;
    private List<KeywordDefinition> keywordDefinitions;
    private boolean runAllSources = false;
    private String runMode;
    private String stanfordTaggerPath;
    private String stanfordNerClassifierPath;
    private String openNlpPosModelPath;
    private String openNlpNerModelPath;
    

    public List<SourceDescription> getSourceDescriptions(){
        return sources;
    }

	public String getXmlDir() {
		return xmlDir;
	}

	
	public String getDocumentRootDir() {
		return documentRootDir;
	}

	public String getExportDir() {
		return exportDir;
	}

	public List<File> getStopWordFiles() {
		List<File> files = new ArrayList<File>();
		for (String x : stopWordFiles) {
			files.add(new File(x));
		}
		return files;
	}

	public List<File> getAffiliationFiles() {
		List<File> files = new ArrayList<File>();
		for (String x : affiliationFiles) {
			files.add(new File(x));
		}
		return files;
	}

	public boolean isRunAllSources() {
		return runAllSources;
	}

	public void setRunAllSources(boolean runAllSources) {
		this.runAllSources = runAllSources;
	}

	public String getDatabaseDir() {
		return databaseDir;
	}

	public void setDatabaseDir(String databaseDir) {
		this.databaseDir = databaseDir;
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	public List<KeywordDefinition> getKeywordDefinitions() {
		return keywordDefinitions;
	}

	public String getStanfordNerClassifierPath() {
		return stanfordNerClassifierPath;
	}

	public String getStanfordTaggerPath() {
		return stanfordTaggerPath;
	}

	public String getOpenNlpPosModelPath() {
		return openNlpPosModelPath;
	}

	public String getOpenNlpNerModelPath() {
		return openNlpNerModelPath;
	}


}