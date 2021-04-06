package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.extract.DataReader;
import org.acoli.glaser.metadata.pdf.read.XMLConverter;

import java.util.List;


public class ItemParserMainRunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        XMLConverter xmlConverter = new XMLConverter();

        List<String> itemsList = dataReader.parseInputName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");

        List<String> listOfFoundFiles = dataReader.retrieveFilesFromList(itemsList, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        xmlConverter.convertListToXML(listOfFoundFiles);
    }

}
