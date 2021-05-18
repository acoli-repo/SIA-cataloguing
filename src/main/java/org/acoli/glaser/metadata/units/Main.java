package org.acoli.glaser.metadata.units;

import org.acoli.glaser.metadata.units.extract.PDFExtractionConfiguration;
import org.acoli.glaser.metadata.units.extract.PDFMetadataExtractor;
import org.acoli.glaser.metadata.units.extract.PDFToXML;
import org.acoli.glaser.metadata.units.util.Metadata;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class Main {



    public static String convertPdf() throws Exception {
        PDFToXML xmlConverter = new PDFToXML();
        String xmlPath = xmlConverter.convertToXml("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf");
        File xml = new File(xmlPath);
        String tempFilePath = xmlConverter.formatXmlFile(xml);
        return tempFilePath;
    }

    public static Metadata extractMetadata(String path) throws Exception {
            PDFMetadataExtractor pme = writeConfig();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();

            File xml = new File(path);
            Document xmlDocument = builder.parse(xml);
            Metadata metadata = pme.getMetadata(xmlDocument);
            return metadata;
    }

    public static PDFMetadataExtractor writeConfig() {
        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(5);
        config.setAuthorHeight(18);
        config.setTitleFont(0);
        config.setTitleHeight(32);
        config.setPageFont(5);
        config.setPageHeight(16);
        PDFMetadataExtractor pme = new PDFMetadataExtractor(config);
        return pme;
    }

    public static void writeData(){

    }

    public static void evaluateData(){
    }

    public void run() throws Exception{
        convertPdf();
        writeData();
        evaluateData();
    }

    public static void main(String[] args) throws Exception {
        String filePath = convertPdf();
        Metadata metadata = extractMetadata(filePath);
        System.out.println(metadata);
    }
}
