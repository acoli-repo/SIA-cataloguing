package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.util.Metadata;
import org.acoli.glaser.metadata.pdf.read.XMLConverter;
import org.w3c.dom.Document;

import java.io.File;


public class Main {


    public Document readData() {
        String path = "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf";
        File file = new File(path);
        Document xml = null;
        return xml;
    }

    public Metadata extractData(){
        return null;
    }

    public void evaluateData(){
    }

    public void writeData(){
    }


    public void run(){
        readData();
        extractData();
        evaluateData();
        writeData();
    }

    public static void main(String[] args) throws Exception {
        XMLConverter xmlConverter = new XMLConverter();
        xmlConverter.convertToXML("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/backley.pdf");
    }
}
