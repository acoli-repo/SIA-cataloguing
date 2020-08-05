package org.acoli.glaser.metadata.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetadataFromHTML {
    String url;
    boolean hasFailed = false;
    List<Metadata> partialMetadata = new ArrayList<>();
    Document page = null;
    FileHandler fh = null;

    public MetadataFromHTML(String url) {
        this.url = url;
        System.err.println("Created MetadataFromHTML object for URL"+this.url);
        // TODO: Make sure it's a valid url
    }
    public MetadataFromHTML(String url, FileHandler fh) {
        this.url = url;
        System.err.println("Created MetadataFromHTML object for URL"+this.url);
        // TODO: Make sure it's a valid url
        this.fh = fh;
    }

    public boolean getHTMLAndTellMeIfYouWereSuccessful() {
        if (page != null) { // Avoid spamming the server
            return true;
        }
        try {
            page = Jsoup.connect(this.url).get();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

        public void parseSummaryPage(){
        if (this.getHTMLAndTellMeIfYouWereSuccessful()) {
            System.err.println(page);
            Elements bibtex = page.select("td.bibtex_summaries");
            if (bibtex.size() > 0) {
                System.err.println("Found bibtex: " + bibtex.text());
                this.partialMetadata.add(Metadata.metadataFromBibtex(bibtex.text()));
            }
        }
    }
    public File findPDFofPublicationAndDownload() {
        System.err.println("Trying to find a pdf on page "+this.url+" ..");
        if (this.getHTMLAndTellMeIfYouWereSuccessful()) {
            String hrefToPDF = page.select("table.main_summaries a[href$=\".pdf\"]").attr("href");
            System.err.println("Found "+hrefToPDF);
            if (this.fh != null) {
                try {
                    return fh.downloadFileToTemp(hrefToPDF);
                } catch (IOException e) {
                    System.err.println("Couldn't download "+hrefToPDF);
                }
            } else {
                System.err.println("Can't download, no connection to a FileHandler.");
            }

        }
        return null;
    }
}
