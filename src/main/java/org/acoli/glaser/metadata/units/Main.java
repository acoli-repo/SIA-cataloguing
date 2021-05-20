package org.acoli.glaser.metadata.units;

import org.acoli.glaser.metadata.units.extract.PDFExtractionConfiguration;
import org.acoli.glaser.metadata.units.extract.PDFMetadataExtractor;
import org.acoli.glaser.metadata.units.extract.PDFToXML;
import org.acoli.glaser.metadata.units.util.Config;
import org.acoli.glaser.metadata.units.util.Metadata;
import org.acoli.glaser.metadata.units.util.Util;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;


public class Main {

    private static Config jsonConfig = Util.readConfigs("configs.json");
    private static PDFExtractionConfiguration extractorConfig = jsonConfig.sources.get(0).getExtractorConfig();


    public static String convertPdfToXml() throws Exception {
        PDFToXML xmlConverter = new PDFToXML();
        String xmlFilePath = xmlConverter.convertToXml("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf");
        File xmlFile = new File(xmlFilePath);
        String tempFilePath = xmlConverter.formatXmlFile(xmlFile); //TODO replace the xml file instead of creating xml.temp file
        return tempFilePath;
    }

    public static Metadata extractMetadata(String path) throws Exception {

        PDFMetadataExtractor pme = new PDFMetadataExtractor(extractorConfig);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        File xml = new File(path);
        Document xmlDocument = builder.parse(xml);
        Metadata metadata = pme.getMetadata(xmlDocument);
        return metadata;
    }

    public static void writeData(){

    }

    public static void evaluateData(){
    }

    public void run() throws Exception{
        convertPdfToXml();
        writeData();
        evaluateData();
    }

    public static void main(String[] args) throws Exception {
        String filePath = convertPdfToXml(); // Step 1
        Metadata metadata = extractMetadata(filePath); //Step 2
        writeData();
    }
}
