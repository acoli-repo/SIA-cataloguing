package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.util.DataReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception{
        DataReader dr = new DataReader();


        List<String> itemsList = dr.parseItems("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
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
    }

}
