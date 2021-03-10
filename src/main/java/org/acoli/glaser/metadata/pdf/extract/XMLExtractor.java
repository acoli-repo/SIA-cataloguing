package org.acoli.glaser.metadata.pdf.extract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class XMLExtractor {

    public boolean extractXML(List<String> listOfFoundFiles) throws Exception{

        for (String pdf : listOfFoundFiles) {
            String name = pdf.replace(".pdf", "");
            System.out.println(name);
            Runtime rt = Runtime.getRuntime();
            Process conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/" + pdf + " resultData/" + name + ".xml");
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
}
