package org.acoli.glaser.metadata.units.util;

import com.google.gson.Gson;
import org.acoli.glaser.metadata.deprecatedCode.MainRunner;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Util {

    private static Logger LOG = Logger.getLogger(MainRunner.class.getName());

    public static Config readConfigs(String pathToConfigFile) {
        Gson gson = new Gson();
        String json = readConfigJSONToString(pathToConfigFile);
        Config config = gson.fromJson(json, Config.class);
        return config;
    }

    public static String readConfigJSONToString(String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            LOG.info("Couldn't read JSON config file.");
        }
        return null;
    }
}
