package org.acoli.glaser.metadata.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MetadataFromHTML extends MetadataSourceHandler {
    String url;
    List<Metadata> partialMetadata = new ArrayList<>();
    Document page = null;
    FileHandler fh = null;
    List<String> pdfUrlsOnPage = new ArrayList<>();
    boolean checkedForPDFs = false;
    boolean pokedTheServerAlready = false;

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

    private boolean getHTMLAndTellMeIfYouWereSuccessful() {
        if (pokedTheServerAlready)
            return page != null;
        else {
            try {
                pokedTheServerAlready = true;
                page = Jsoup.connect(this.url).get();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    // TODO: refactor this.
    @Deprecated
    public void addSummaryPageToPool(List<Metadata> pool) {
        run();
        pool.addAll(this.partialMetadata);
        this.partialMetadata.clear();
    }

    @Override
    public void run(){
        // TODO: Implement other sources of metadata
        if (this.getHTMLAndTellMeIfYouWereSuccessful()) {
            Elements bibtex = page.select("td.bibtex_summaries"); // TODO: Parameterize this
            if (bibtex.size() > 0) {
                System.err.println("Found bibtex: " + bibtex.text());
                this.partialMetadata.add(Metadata.metadataFromBibtex(bibtex.text()));
            }
            if (pageContainsPDFs()) {
                pdfUrlsOnPage = getPDFsOnPage();
            }
        }
    }
    public boolean pageContainsPDFs() {
        if (!this.checkedForPDFs) {
            this.pdfUrlsOnPage = this.getPDFsOnPage();
        }
        return !this.pdfUrlsOnPage.isEmpty();
    }

    List<String> getPDFsOnPage() {
        List<String> pdfUrls = new ArrayList<>();
        System.err.println("Trying to find a pdf on page "+this.url+" ..");
        if (this.getHTMLAndTellMeIfYouWereSuccessful()) {
            String hrefToPDF = page.select("table.main_summaries a[href$=\".pdf\"]").attr("href");
            pdfUrls.add(hrefToPDF);
        }
        return pdfUrls;
    }


    @Deprecated
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


    @Override
    public List<Metadata> getMetadata() {
        return this.partialMetadata;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public boolean success() {
        return this.partialMetadata != null && getHTMLAndTellMeIfYouWereSuccessful();
    }

    @Override
    public boolean foundOtherSourcesThatRequireHandling() {
        return checkedForPDFs && !pdfUrlsOnPage.isEmpty();
    }

    /**
     * TODO: Refactor this and rename
     * @param url
     * @return
     */
    public URL newURLICheckedForExceptionAlready(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            System.err.println("YOU TOLD ME YOU CHECKED THIS ALREADY!");
        }
        return null;
    }
    public List<MetadataFromPDF> createPDFHandlersFromFoundURLS() {
        List<MetadataFromPDF> mfp = new ArrayList<>();
        for (String url : pdfUrlsOnPage) {
            if (FileHandler.isURL(url)) {
                mfp.add(new MetadataFromPDF(newURLICheckedForExceptionAlready(url)));
            }
        }
        return mfp;
    }
    @Override
    public List<MetadataSourceHandler> getHandlersForOtherSources() {
        return null;
    }
}
