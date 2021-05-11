package org.acoli.glaser.metadata.units.extract;

import org.acoli.glaser.metadata.units.util.Config;
import org.acoli.glaser.metadata.units.util.Util;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PDFToXMLConverter {

    private Config config;
    private String tempDir;
    private String rootDir;

    private static Logger LOG = Logger.getLogger(PDFToXMLConverter.class.getName());

    public PDFToXMLConverter(){
        this.config = Util.readConfigs("configs.json");
        this.tempDir = config.PathToTempDir;
        this.rootDir = config.DocumentRootDir;

        File directory = new File(tempDir);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public boolean extractXML(List<String> listOfFoundFiles) throws Exception{
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
            conversion = rt.exec("pdftohtml -xml -i -c -q -s " + path + " " + tempDir + name + ".xml");
        }else{
            conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s " + path + " " + tempDir  + "/" + name + ".xml");
        }
        try {
            LOG.info("Waiting for conversion to finish..");
            boolean didFinish = conversion.waitFor(10, TimeUnit.SECONDS);
            if (didFinish) {
                LOG.info("Conversion done.");
            } else {
                LOG.info("Conversion not done..?");
            }
        } catch (InterruptedException e) {
            System.exit(1); // TODO: Check how to handle this properly
        }
    }

    public boolean removeDtdFromFile(File xml) throws IOException {
        File tmpFile = new File(xml.getAbsolutePath()+".tmp");
        BufferedReader bin = new BufferedReader(new FileReader(xml));
        BufferedWriter bout = new BufferedWriter(new FileWriter(tmpFile));
        LOG.info("Removing DTD in "+xml.getAbsolutePath());
        for(String line = ""; line!=null; line=bin.readLine()) {
            if (line.length() > 1 && !line.startsWith("<!DOCTYPE")) {
                bout.write(line+"\n");
            }
        }
        bout.flush();
        bout.close();

        boolean success = tmpFile.renameTo(xml);
        if (success)
            LOG.info("Done.");
        else
            LOG.warning("Couldn't move file.");
        return success;
    }
}
