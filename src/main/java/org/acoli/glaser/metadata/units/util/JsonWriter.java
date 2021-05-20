package org.acoli.glaser.metadata.units.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {

    public JSONObject writeJSONFromMetadata(Metadata metadata) throws IOException {
        JSONObject jsonObj = new JSONObject();
        JSONArray list = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("source", "pdf");
        obj1.put("title", "Representing and processing idioms");
        list.add(obj1);
        jsonObj.put("title", list);
        jsonObj.put("ppn", "17465734X");


        try(FileWriter file = new FileWriter("myJson.json")){
            file.write(jsonObj.toString());
            file.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return jsonObj;
    }
}
