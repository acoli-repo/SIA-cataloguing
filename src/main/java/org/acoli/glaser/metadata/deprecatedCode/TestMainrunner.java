package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.extract.PDFExtractionConfiguration;
import org.acoli.glaser.metadata.units.extract.PDFMetadataExtractor;
import org.acoli.glaser.metadata.units.util.Metadata;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class TestMainrunner {

    public PDFMetadataExtractor getExtractor(){
        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(2);
        config.setAuthorHeight(19);
        config.setTitleFont(0);
        config.setTitleHeight(32);
        config.setPageFont(5);
        config.setPageHeight(16);
        PDFExtractionConfiguration dummyConfig = config;
        PDFMetadataExtractor pme = new PDFMetadataExtractor(config);
        return pme;
    }

    public void testOne() throws Exception{ //Check
        File xml = new File("resultData/arad.xml");

        File tmpFile = new File(xml.getAbsolutePath()+".tmp");
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

        boolean success = tmpFile.renameTo(xml);
        if (success)
            System.out.println("Done.");
        else
            System.out.println("Couldn't move file.");



        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(5);
        config.setAuthorHeight(16);
        config.setTitleFont(4);
        config.setTitleHeight(19);
        config.setPageFont(3);
        config.setPageHeight(15);
        PDFExtractionConfiguration dummyConfig = config;
        PDFMetadataExtractor pme = new PDFMetadataExtractor(config);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        System.out.println(xml);
        Document xmlDocument = builder.parse(xml);
        assert pme.getMetadata(xmlDocument).authors.size() != 0;
    }

    public static void main(String[] args) throws Exception{
        TestMainrunner tm = new TestMainrunner();
        PDFMetadataExtractor pme = tm.getExtractor();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        File xml = new File("resultData/allott.xml.tmp");
        Document xmlDocument = builder.parse(xml);
        Metadata metadata = pme.getMetadata(xmlDocument);
        System.out.println(metadata);
    }

}
