package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.util.JsonWriter;
import org.json.simple.JSONObject;

public class JsonWriterMainRunner {

    public static void main(String[] args) throws Exception{
        JsonWriter js = new JsonWriter();
        JSONObject obj = js.writeJSONFromMetadata(null);
        System.out.println(obj);
    }

}
