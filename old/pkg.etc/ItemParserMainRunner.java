package org.acoli.sc.etc;

import java.util.List;
import org.acoli.sc.extract.DataReader;
import org.acoli.sc.extract.PDF2XML;

/**
 * NEW @tobias
 *
 */
public class ItemParserMainRunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();

        List<String> itemsList = dataReader.parseItemsName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");

        List<String> listOfFoundFiles = dataReader.readOutItems(itemsList, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        PDF2XML.extractXML(listOfFoundFiles, null);
    }

}
