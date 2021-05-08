package org.acoli.glaser.metadata.deprecatedCode;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class DocumentProperties{
    public static void main(String[] args)throws IOException {
        PDDocumentInformation pddi = new PDDocumentInformation();
        pddi.getAuthor();
        System.out.println(pddi.getAuthor());
    }
}