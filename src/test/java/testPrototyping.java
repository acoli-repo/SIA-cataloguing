import org.acoli.glaser.metadata.pdf.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.net.URL;
import java.util.List;

public class testPrototyping {

    //@Test
    public void readUrlSeed() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File("urlseed.csv")));
        for(String line = in.readLine(); line!=null; line=in.readLine()) {
            System.err.println(line);
            Source s = Source.sourceFromCSVRow(line);
            if (s.urlAsString.startsWith("http")) {
                System.err.println("Downloading "+s+"..");
                }
            }
        }

    @Test
    public void testExtractingLinksFromBasePageBasedOnCSSQuery() throws IOException {
        String urlAsString = "http://lrec-conf.org/workshops/lrec2018/W29/papers.html";
        Document doc = Jsoup.connect(urlAsString).get();
        List<URL> hrefs = new PageSpider().findHrefsByCSSQuery(doc, ".paper_papers > a");
        Assert.assertEquals(hrefs.size(), 12);

    }
    @Test
    public void testMetadataFromHTMLWithSingleLink() {
        String testURL = "http://lrec-conf.org/workshops/lrec2018/W29/summaries/6_W29.html";
        MetadataFromHTML mfh = new MetadataFromHTML(testURL, new FileHandler());
        mfh.run();
        mfh.findPDFofPublicationAndDownload();
    }
    @Test
    public void testParlaCLARIN() throws IOException {
        String urlAsString = "http://lrec-conf.org/workshops/lrec2018/W2/papers.html";
        Document doc = Jsoup.connect(urlAsString).get();
        List<URL> hrefs = new PageSpider().findHrefsByCSSQuery(doc, ".paper_papers > a");
        Assert.assertEquals(hrefs.size(), 16);
    }

    @Test
    public void testRun() {
        MainRunner mr = new MainRunner("urlseed.csv");
        mr.run();
    }

}
