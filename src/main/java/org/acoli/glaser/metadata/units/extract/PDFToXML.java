package org.acoli.glaser.metadata.units.extract;

import org.acoli.glaser.metadata.units.util.Config;
import org.acoli.glaser.metadata.units.util.Util;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PDFToXML {

    private Config config;
    private String tempDir;
    private String rootDir;

    private static Logger LOG = Logger.getLogger(PDFToXML.class.getName());

    public PDFToXML(){
        this.config = Util.readConfigs("configs.json");
        this.tempDir = config.PathToTempDir;
        this.rootDir = config.DocumentRootDir;

        File directory = new File(tempDir);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public boolean extractXmlFromPdf(List<String> listOfFoundFiles) throws Exception{
        for (String file : listOfFoundFiles){
            String xmlPath = convertToXml(file);
            File xmlFile = new File(xmlPath);
            formatXmlFile(xmlFile);
        }
        return true;
    }

    public String convertToXml(String pdfFilePath) throws Exception {
        File pdfFile = new File(pdfFilePath);
        String nameOfPaper = pdfFile.getName().replace(".pdf", "");
        Runtime executionRuntime = Runtime.getRuntime();
        String xmlFilePath = tempDir + "/" + nameOfPaper + ".xml";
        Process pdfToXmlConversion;
        if (SystemUtils.IS_OS_LINUX) { //Linus Process
            pdfToXmlConversion = executionRuntime.exec("pdftohtml -xml -i -c -q -s " + pdfFilePath + " " + xmlFilePath);
        } else {//Windows Process. Ubuntu Virtual Machine needs to be installed
            pdfToXmlConversion = executionRuntime.exec("wsl \n  pdftohtml -xml -i -c -q -s " + pdfFilePath + " " + xmlFilePath);
        }
        try {
            LOG.info("Waiting for conversion to finish..");
            boolean didFinish = pdfToXmlConversion.waitFor(10, TimeUnit.SECONDS);
            if (didFinish) {
                LOG.info("Conversion done.");
            } else {
                LOG.info("Conversion not done..?");
            }
        } catch (InterruptedException e) {
            System.exit(1); // TODO: Check how to handle this properly
        }
        return xmlFilePath;
    }

    public boolean formatXmlFile(File xml) throws IOException {
        System.out.println("check");
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
