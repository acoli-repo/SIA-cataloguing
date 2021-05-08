package org.acoli.glaser.metadata.unit;

import org.acoli.glaser.metadata.unit.extract.XMLConverter;


public class Main {



    public static void extractData() throws Exception {
        XMLConverter xmlConverter = new XMLConverter();
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
