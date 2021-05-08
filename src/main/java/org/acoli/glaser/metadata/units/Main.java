package org.acoli.glaser.metadata.units;

import org.acoli.glaser.metadata.units.extract.PDFToXMLConverter;


public class Main {



    public static void extractData() throws Exception {
        PDFToXMLConverter xmlConverter = new PDFToXMLConverter();
        xmlConverter.convertToXML("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf");
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
