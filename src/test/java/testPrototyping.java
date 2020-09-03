import org.acoli.glaser.metadata.pdf.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    public void testMetadataFromHTMLWithSingleLink() throws MalformedURLException {
        String testURL = "http://lrec-conf.org/workshops/lrec2018/W29/summaries/6_W29.html";
        MetadataFromHTML mfh = new MetadataFromHTML(new URL(testURL), new FileHandler());
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

    //@Test
    public void testRun() {
        MainRunner mr = new MainRunner("urlseed.csv");
        mr.run();
    }

    @Test
    public void testPageHandlerWithSingleInitialURL() throws MalformedURLException {
        MetadataFromHTML mfh = new MetadataFromHTML(new URL("http://lrec-conf.org/workshops/lrec2018/W29/summaries/4_W29.html"), new FileHandler());
        List<MetadataSourceHandler> list = new ArrayList<>();
        list.add(mfh);
        PageHandler ph = new PageHandler(list);
        ph.run();
    }

    @Test
    public void testRemovingDTD() throws IOException {
        PDF2XML pdf2XML = new PDF2XML("tempDir");
        File pdf = pdf2XML.pdfToXml(new File("tempDir/1848720702.xml"));
        Assert.assertTrue(pdf2XML.hasDoctypeAnnotation(pdf));
        pdf2XML.removeDtdFromFile(pdf);
        Assert.assertFalse(pdf2XML.hasDoctypeAnnotation(pdf));
        }

}
