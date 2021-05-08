package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.testing.DataReader;
import org.acoli.glaser.metadata.units.extract.PDFToXMLConverter;

import java.util.List;


public class ItemParserMainRunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        PDFToXMLConverter xmlConverter = new PDFToXMLConverter();

        List<String> itemsList = dataReader.parseInputName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");

        List<String> listOfFoundFiles = dataReader.retrieveFilesFromList(itemsList, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        xmlConverter.convertListToXML(listOfFoundFiles);
    }

}
