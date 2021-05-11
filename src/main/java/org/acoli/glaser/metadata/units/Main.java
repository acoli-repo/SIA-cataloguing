package org.acoli.glaser.metadata.units;

import org.acoli.glaser.metadata.units.extract.PDFToXML;

import java.io.File;


public class Main {



    public static void extractData() throws Exception {
        PDFToXML xmlConverter = new PDFToXML();
        String xmlPath = xmlConverter.convertToXml("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf");
        File xml = new File(xmlPath);
        xmlConverter.formatXmlFile(xml);
    }

    public static void writeData(){

    }

    public static void evaluateData(){
    }

    public void run() throws Exception{
        extractData();
        writeData();
        evaluateData();
    }

    public static void main(String[] args) throws Exception {
        extractData();
    }
}
