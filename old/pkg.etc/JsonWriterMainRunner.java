package org.acoli.sc.etc;

import org.json.simple.JSONObject;

/**
 * NEw @tobias
 *
 */
public class JsonWriterMainRunner {

    public static void main(String[] args) throws Exception{
        JsonWriter js = new JsonWriter();
        JSONObject obj = js.writeJSONFromMeta();
        System.out.println(obj);
    }

}
