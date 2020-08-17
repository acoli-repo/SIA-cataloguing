package org.acoli.glaser.metadata.pdf;

import java.util.ArrayList;
import java.util.List;

public class PageHandler {
    List<MetadataFromHTML> htmlSources;
    List<MetadataFromPDF> pdfSources;
    List<Metadata> mds;

    public PageHandler() {
        htmlSources = new ArrayList<>();
        pdfSources = new ArrayList<>();
        mds = new ArrayList<>();
    }
    public void run() {
        while (!htmlSources.isEmpty() || !pdfSources.isEmpty()) {
            System.err.println(htmlSources.size()+" html sources and "+pdfSources.size()+" pdf sources left to extract..");
            if (!htmlSources.isEmpty()) {
                MetadataFromHTML next = htmlSources.remove(0);
                next.addSummaryPageToPool(this.mds);
                if (next.pageContainsPDFs()) {
                    for (String urlToPDF : next.getPDFsOnPage()) {
                        System.err.println("WOULD ADD "+urlToPDF+" TO PDF SEED NOW.");
                    }
                }
            }
        }
    }
}
