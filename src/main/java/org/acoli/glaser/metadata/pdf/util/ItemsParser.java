package org.acoli.glaser.metadata.pdf.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ItemsParser {

    public Object testParser(String filename) throws Exception{
        FileReader fr = new FileReader(filename);
        System.out.println(fr.toString());
        Object obj = new JSONParser().parse(fr);
        System.out.println(obj.toString());
        System.out.println("test");
        Object test = null;
        return obj;
    }

    public List<String> itemsParser(String filename) throws Exception{
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

    public static void main(String[] argv) throws Exception {
        ItemsParser ip = new ItemsParser();


        List<String> itemsList = ip.itemsParser("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
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

        for (String pdf : listOfFoundFiles) {
            String name = pdf.replace(".pdf", "");
            System.out.println(name);
            Runtime rt = Runtime.getRuntime();
            Process conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/"+ pdf +" resultData/"+name+".xml");
            try {
                System.out.println("Waiting for conversion to finish..");
                boolean didFinish = conversion.waitFor(10, TimeUnit.SECONDS);
                if (didFinish) {
                    System.out.println("Conversion done.");
                } else {
                    System.out.println("Conversion not done..?");
                }
            } catch (InterruptedException e) {
                System.exit(1); // TODO: Check how to handle this properly
            }
        }

        /*
        XML's now need to be read out in order to get the title, author and Pagenumber
        current issue: find a good way to read out the Data
        */


    }

}
