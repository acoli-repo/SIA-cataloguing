package org.acoli.sc.etc;

import java.util.ArrayList;
import java.util.List;
import org.acoli.sc.extract.DataReader;

/**
 * NEW @tobias
 *
 */
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



        System.out.println(input.size());
        System.out.println(output.size());
        System.out.println(foundObjects.size());
    }

}