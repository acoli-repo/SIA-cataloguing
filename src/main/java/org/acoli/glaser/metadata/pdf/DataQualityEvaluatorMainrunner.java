package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.crawl.DataReader;

import java.util.List;

public class DataQualityEvaluatorMainrunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        dataReader.parseItemsTitle("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
    }

}