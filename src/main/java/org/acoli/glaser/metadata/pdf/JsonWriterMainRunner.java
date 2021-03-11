package org.acoli.glaser.metadata.pdf;

import org.acoli.glaser.metadata.pdf.extract.DataReader;
import org.acoli.glaser.metadata.pdf.extract.XMLExtractor;
import org.acoli.glaser.metadata.pdf.util.JsonWriter;
import org.json.simple.JSONObject;

import java.util.List;

public class JsonWriterMainRunner {

    public static void main(String[] args) throws Exception{
        JsonWriter js = new JsonWriter();
        JSONObject obj = js.writeJSONFromMeta();
        System.out.println(obj);
    }

}
