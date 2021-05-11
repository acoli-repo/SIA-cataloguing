package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.extract.PDFExtractionConfiguration;
import org.acoli.glaser.metadata.units.extract.PDFMetadataExtractor;
import org.acoli.glaser.metadata.units.testing.DataReader;
import org.acoli.glaser.metadata.units.extract.PDFToXML;
import org.acoli.glaser.metadata.units.util.Metadata;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrototypeMainRunner {

    public List<Metadata> convertXMLtoXMLTMP(String path) throws Exception {
        File testData = new File(path);

        List<Metadata> metadataList = new ArrayList<Metadata>();
        for(File file : testData.listFiles()){
            convertData(file);
            Metadata metadata = extractMetadata(file.getAbsolutePath() + ".tmp");
            metadataList.add(metadata);
        }

        return metadataList;
    }

    public void convertData(File xml) throws Exception{

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
    }

    public Metadata extractMetadata(String path) throws Exception{
        PDFMetadataExtractor pme = writeConfig();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();

        File xml = new File(path);
        Document xmlDocument = builder.parse(xml);
        Metadata metadata = pme.getMetadata(xmlDocument);
        return metadata;
    }

    public PDFMetadataExtractor writeConfig(){
        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(2);
        config.setAuthorHeight(18);
        config.setTitleFont(0);
        config.setTitleHeight(32);
        config.setPageFont(5);
        config.setPageHeight(16);
        PDFExtractionConfiguration dummyConfig = config;
        PDFMetadataExtractor pme = new PDFMetadataExtractor(config);
        return pme;
    }

    public void setupData() throws Exception{
        File resultData = new File("resultData");
        if (!resultData.exists()){
            resultData.mkdirs();
        }

        //Find PDFs and Read them
        DataReader dataReader = new DataReader();
        List<String> itemsList = dataReader.parseInputName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
        List<String> listOfFoundFiles = dataReader.retrieveFilesFromList(itemsList, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        // Get XML out of PDF-Files
        PDFToXML xmlConverter = new PDFToXML();
        xmlConverter.extractXmlFromPdf(listOfFoundFiles);
        this.convertXMLtoXMLTMP("resultData");

        //Extract Metadata out of XML-Files
    }
    public static void main(String[] args) throws Exception{
        PrototypeMainRunner prototypeMainRunner = new PrototypeMainRunner();

        prototypeMainRunner.setupData();

        File testData = new File("resultData");

        List<Metadata> metadataList = new ArrayList<Metadata>();
        for(File file : testData.listFiles()){
            Metadata metadata = prototypeMainRunner.extractMetadata(file.getAbsolutePath() + ".tmp");
            metadataList.add(metadata);
        }


        List<List<String>> authorsList = new ArrayList<>();
        for(Metadata meta : metadataList) {
            System.out.println(meta);
            System.out.println(meta.title);
            System.out.println(meta.authors);
            if (meta.authors != null) {
                authorsList.add(meta.authors);
            }
        }

        DataReader dataReader = new DataReader();
        //Compare received Metadata with Output Data
        List<ArrayList<String>> output = dataReader.parseOutputAuthors("documentation/output-data-format/047006471-output.jsonl");
        ArrayList<ArrayList<String>> foundObjects = new ArrayList<ArrayList<String>>();

        for(ArrayList<String> i : output){
            for(List<String> o : authorsList){
                if(i.equals(o)){
                    foundObjects.add(i);
                }
            }
        }

        //Print Result and Accuracy
        System.out.println(foundObjects.size());
    }

}
