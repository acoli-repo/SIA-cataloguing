package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.unit.util.Metadata;
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
    List<Metadata> partialMetadata = new ArrayList<>();
    Document page = null;
    FileHandler fh = null;
    List<String> pdfUrlsOnPage = new ArrayList<>();
    List<String> rawBibtexOnPage = new ArrayList<>();
    boolean containsBibtex = false;
    boolean checkedForPDFs = false;
    boolean pokedTheServerAlready = false;

    public MetadataFromHTML(URL url) {
        this.source = url;
        System.err.println("Created MetadataFromHTML object for URL"+this.source);
    }
    public MetadataFromHTML(URL url, FileHandler fh) {
        this.source = url;
        System.err.println("Created MetadataFromHTML object for URL"+this.source);
        this.fh = fh;
    }


    /**
     * Collects and parses the webpage in Jsoup
     * Can be used to avoid spamming the endpoint.
     * @return false if something went wrong during collection.
     */
    private boolean getHTMLAndTellMeIfYouWereSuccessful() {
        if (pokedTheServerAlready)
            return page != null;
        else {
            try {
                pokedTheServerAlready = true;
                page = Jsoup.connect(this.source.toString()).get();
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
                rawBibtexOnPage.add(bibtex.text());
                containsBibtex = true;
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
        System.err.println("Trying to find a pdf on page "+this.source+" ..");
        if (this.getHTMLAndTellMeIfYouWereSuccessful()) {
            for (URL hrefToPDF : PageSpider.findHrefsByCSSQuery(page, "table.main_summaries a[href$=\".pdf\"]")) {
                pdfUrls.add(hrefToPDF.toString()); // TODO: fix this back-and-forth between type URL and String
            }
        }
        checkedForPDFs = true;
        return pdfUrls;
    }


    @Deprecated
    public File findPDFofPublicationAndDownload() {
        System.err.println("Trying to find a pdf on page "+this.source+" ..");
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
        return checkedForPDFs && pokedTheServerAlready;
    }

    @Override
    public boolean success() {
        return this.partialMetadata.size() > 0 && getHTMLAndTellMeIfYouWereSuccessful() || foundOtherSourcesThatRequireHandling();
    }

    @Override
    public boolean foundOtherSourcesThatRequireHandling() {
        return (checkedForPDFs && !pdfUrlsOnPage.isEmpty()) || containsBibtex;
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
    public List<MetadataSourceHandler> createPDFHandlersFromFoundURLS() {
        List<MetadataSourceHandler> mfp = new ArrayList<>();
        for (String url : pdfUrlsOnPage) {
            if (FileHandler.isURL(url)) {
               // mfp.add(new MetadataFromPDF(newURLICheckedForExceptionAlready(url)));
            }
        }
        for (String bibtex : rawBibtexOnPage) {
            mfp.add(new MetadataFromBibtex(bibtex));
        }
        return mfp;
    }
    @Override
    public List<MetadataSourceHandler> getHandlersForOtherSources() {
        return createPDFHandlersFromFoundURLS();
    }

    static public List<MetadataFromHTML> spawnHandlersWithInitialPageAndSelector(URL initialURL, String cssQuery, FileHandler fh) throws IOException {
        Document doc = Jsoup.connect(initialURL.toString()).get();
        List<MetadataFromHTML> mfh = new ArrayList<>();
        for (URL url : PageSpider.findHrefsByCSSQuery(doc, cssQuery)) {
            mfh.add(new MetadataFromHTML(url, fh));
        }
        return mfh;
    }
}
