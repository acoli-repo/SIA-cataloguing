package org.acoli.glaser.metadata.unit.extract;

import com.google.gson.Gson;
import org.acoli.glaser.metadata.deprecatedCode.MainRunner;
import org.acoli.glaser.metadata.deprecatedCode.SourceDescriptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PDFToXMLConverter {

    private Config config;
    private String tempDir;
    private String rootDir;
    private static Logger LOG = Logger.getLogger(MainRunner.class.getName());

    public PDFToXMLConverter(){
        this.config = readConfigs("configs.json");
        this.tempDir = config.PathToTempDir;
        this.rootDir = config.DocumentRootDir;
        File directory = new File(this.config.PathToTempDir);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    static public Config readConfigs(String pathToConfigFile) {
        Gson gson = new Gson();
        String json = readConfigJSONToString(pathToConfigFile);
        Config config = gson.fromJson(json, Config.class);
        return config;
    }

    static String readConfigJSONToString(String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            LOG.info("Couldn't read JSON config file.");
        }
        return null;
    }

    @Deprecated
    public boolean extractXML(List<String> listOfFoundFiles) throws Exception{
        for (String pdf : listOfFoundFiles) {
            String name = pdf.replace(".pdf", "");
            System.out.println(name);
            Runtime rt = Runtime.getRuntime();
            Process conversion = null;
            if(SystemUtils.IS_OS_LINUX){ //Operating System (OS)- Erkennung um den Shellcommand entsprechend anzupassen
                conversion = rt.exec("pdftohtml -xml -i -c -q -s documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/" + pdf + " resultData/" + name + ".xml");
            }else{
                conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/" + pdf + " resultData/" + name + ".xml");
            }
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
        return true;
    }

    public boolean convertListToXML(List<String> listOfFoundFiles) throws Exception{
        for (String file : listOfFoundFiles){
            convertToXML(file);
        }
        return true;
    }

    public void convertToXML(String path) throws Exception{
        File file = new File(path);
        String name = file.getName().replace(".pdf", "");
        Runtime rt = Runtime.getRuntime();
        Process conversion;
        if(SystemUtils.IS_OS_LINUX){ //Operating System (OS)- Erkennung um den Shellcommand entsprechend anzupassen
            conversion = rt.exec("pdftohtml -xml -i -c -q -s " + path + " resultData/" + name + ".xml");
        }else{
            conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s " + path + " resultData/test.xml");
        }
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
