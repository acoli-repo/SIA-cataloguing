package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.crawl.DataReader;
import org.acoli.glaser.metadata.pdf.extract.XMLExtractor;

import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        XMLExtractor xmlExtractor = new XMLExtractor();

        List<String> itemsList = dataReader.parseItems("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");

        List<String> listOfFoundFiles = dataReader.readOutItems(itemsList);

        xmlExtractor.extractXML(listOfFoundFiles);
    }

}
