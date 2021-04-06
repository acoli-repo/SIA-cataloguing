package org.acoli.glaser.metadata.pdf.read;

import org.apache.commons.lang3.SystemUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class XMLConverter {

    public boolean convertListToXML(List<String> listOfFoundFiles) throws Exception{
        for (String file : listOfFoundFiles){
            convertToXML(file);
        }
        return true;
    }

    public boolean convertToXML(String file) throws Exception{
        String name = file.replace(".pdf", "");
        System.out.println(name);
        Runtime rt = Runtime.getRuntime();
        Process conversion = null;
        if(SystemUtils.IS_OS_LINUX){ //Operating System (OS)- Erkennung um den Shellcommand entsprechend anzupassen
            conversion = rt.exec("pdftohtml -xml -i -c -q -s " + file + " resultData/" + name + ".xml");
        }else{
               conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s " + file + " resultData/" + name + ".xml");
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
        return true;
    }
}
