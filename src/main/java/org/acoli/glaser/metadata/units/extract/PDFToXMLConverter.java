package org.acoli.glaser.metadata.units.extract;

import org.acoli.glaser.metadata.units.util.Config;
import org.acoli.glaser.metadata.units.util.Util;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PDFToXMLConverter {

    private Config config;
    private String tempDir;
    private String rootDir;

    public PDFToXMLConverter(){
        this.config = Util.readConfigs("configs.json");
        this.tempDir = config.PathToTempDir;
        this.rootDir = config.DocumentRootDir;
        System.out.println(rootDir);
        File directory = new File(this.config.PathToTempDir);
        if(!directory.exists()){
            directory.mkdir();
        }
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
