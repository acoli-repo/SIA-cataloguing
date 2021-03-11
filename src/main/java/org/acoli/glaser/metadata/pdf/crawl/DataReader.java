package org.acoli.glaser.metadata.pdf.crawl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataReader {


    // this one reads the items.jsonl
    public List<String> parseItemsName(String filename) throws Exception{
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

    //this one reads the titles out of item.jsonl
    public List<ArrayList<String>> parseItemsTitle(String filename) throws Exception{
        List<ArrayList<String>> items = new ArrayList<>();

        //Input Stream
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        //Reading JsonL Lines and adding to items-list
        String line;
        while ((line = br.readLine()) != null){
            Object obj = new JSONParser().parse(line);
            JSONObject jo = (JSONObject) obj;
            JSONArray name = (JSONArray) jo.get("titles");
            items.add(name);
        }

        return items;
    }


    //this one reads out the pdf's found in the items.jsonl
    public List<String> readOutItems(List<String> itemsList, String path){
        File testData = new File(path);

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
