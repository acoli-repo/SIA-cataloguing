package org.acoli.sc.start;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.acoli.sc.extract.Metadata;
import org.acoli.sc.extract.PDFMetadataExtractor;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;

public class RunExtract {
	
	
	
	public static List<Metadata> convertXMLtoXMLTMP(String path, PDFMetadataExtractor metadataExtractor) throws Exception {
        File testData = new File(path);

        List<Metadata> metadataList = new ArrayList<Metadata>();
        for(File file : testData.listFiles()){
        	if (!FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("xml")) continue;
            convertData(file);
            System.out.println("Get metadata from "+file.getName());
            List<Metadata> metadata = extractMetadata(file.getAbsolutePath() + ".tmp", metadataExtractor);
            for (Metadata md : metadata) {
            	md.setFileName(file.getName());
            }
            metadataList.addAll(metadata);
        }

        return metadataList;
    }
	
	
	public static void convertData(File xml) throws Exception{

        File tmpFile = new File(xml.getAbsolutePath()+".tmp");
        if (tmpFile.exists()) {
        	return;
        }
        BufferedReader bin = new BufferedReader(new FileReader(xml));
        BufferedWriter bout = new BufferedWriter(new FileWriter(tmpFile));
        System.out.println("Removing DTD in "+xml.getAbsolutePath());
        for(String line = ""; line!=null; line=bin.readLine()) {
            if (line.length() > 1 && !line.startsWith("<!DOCTYPE")) {
                bout.write(line+"\n");
            }
        }
        bout.flush();
        bout.close();
        bin.close();
    }
	
	
	 public static List<Metadata> extractMetadata(String path, PDFMetadataExtractor metadataExtractor) throws Exception{
	        //PDFMetadataExtractor pme = writeConfig();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = dbf.newDocumentBuilder();

	        File xml = new File(path);
	        Document xmlDocument = builder.parse(xml);
	        List<Metadata> metadata = metadataExtractor.getMetadata(xmlDocument);
	        return metadata;
	    }

}
