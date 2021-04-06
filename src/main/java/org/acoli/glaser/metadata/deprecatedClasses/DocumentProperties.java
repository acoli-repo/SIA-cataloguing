package org.acoli.glaser.metadata.deprecatedClasses;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.w3c.dom.Document;

public class DocumentProperties{
    public static void main(String[] args)throws IOException {
        PDDocumentInformation pddi = new PDDocumentInformation();
        pddi.getAuthor();
        System.out.println(pddi.getAuthor());
    }
}