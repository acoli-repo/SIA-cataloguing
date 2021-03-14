package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.extract.DataReader;
import org.apache.commons.lang3.SystemUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataQualityEvaluatorMainrunner {

    public static void main(String[] args) throws Exception{
        DataReader dataReader = new DataReader();
        List<ArrayList<String>> input = dataReader.parseItemsTitle("documentation/input-data-format/047006471-input.jsonl");
        List<ArrayList<String>> output = dataReader.parseOutputTitles("documentation/output-data-format/047006471-output.jsonl");

        ArrayList<ArrayList<String>> foundObjects = new ArrayList<ArrayList<String>>();

        for(ArrayList<String> i : input){
            for(ArrayList<String> o : output){
                if(i.equals(o)){
                    foundObjects.add(i);
                }
            }
        }
        String s = System.getProperty("os.name");
        System.out.println(SystemUtils.IS_OS_LINUX);
        System.out.println(input.size());
        System.out.println(output.size());
        System.out.println(foundObjects.size());
    }

}