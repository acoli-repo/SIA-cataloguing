package org.acoli.glaser.metadata.pdf.util;

import org.acoli.glaser.metadata.pdf.crawl.DataReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataQualityEvaluator {

    public static void main(String[] argv) throws Exception {
        DataQualityEvaluator datavaluator = new DataQualityEvaluator();
        List<String> names = datavaluator.nameParser();

        System.out.println(names);
    }

    public List<String> nameParser() throws Exception{
        String filename = "documentation/input-data-format/047006471-input.jsonl";
        List<String> items = new ArrayList<>();

        //Input Stream
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        //Reading JsonL Lines and adding to items-list
        String line;
        while ((line = br.readLine()) != null){
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj;
            String name = (String) jo.get("pdf_name");
            items.add(name);
        }

        return items;
    }


    public List<String> getDummyData() throws Exception {
        DataReader dataReader = new DataReader();


        List<String> itemsList = dataReader.parseItems("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
        File testData = new File("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");

        List<String> listOfFoundFiles = new ArrayList<>();

        for(String item : itemsList){
            for(File file : testData.listFiles()){
                if(file.getName().equalsIgnoreCase(item)){
                    System.out.println(item + " was found");
                    listOfFoundFiles.add(item);
                }
            }
        }
        return listOfFoundFiles;
    }
}
