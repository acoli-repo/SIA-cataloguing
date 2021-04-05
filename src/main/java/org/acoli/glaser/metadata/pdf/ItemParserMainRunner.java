package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.extract.DataReader;
import org.acoli.glaser.metadata.pdf.extract.XMLExtractor;

import java.util.List;


public class ItemParserMainRunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        XMLExtractor xmlExtractor = new XMLExtractor();

        List<String> itemsList = dataReader.parseInputName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");

        List<String> listOfFoundFiles = dataReader.retrieveFilesFromList(itemsList, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        xmlExtractor.extractXML(listOfFoundFiles);
    }

}
