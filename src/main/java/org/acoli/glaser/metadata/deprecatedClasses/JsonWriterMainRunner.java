package org.acoli.glaser.metadata.deprecatedClasses;

import org.acoli.glaser.metadata.pdf.write.JsonWriter;
import org.json.simple.JSONObject;

public class JsonWriterMainRunner {

    public static void main(String[] args) throws Exception{
        JsonWriter js = new JsonWriter();
        JSONObject obj = js.writeJSONFromMeta();
        System.out.println(obj);
    }

}