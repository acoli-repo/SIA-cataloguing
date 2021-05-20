package org.acoli.glaser.metadata.units.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {

    public JSONObject writeJSONFromMeta() throws IOException {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("source", "pdf");
        obj1.put("title", "Representing and processing idioms");
        list.add(obj1);
        obj.put("title", list);
        obj.put("ppn", "17465734X");


        try(FileWriter file = new FileWriter("myJson.json")){
            file.write(obj.toString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return obj;
    }
}
