package DeprecatedTestingUnit;

import org.acoli.glaser.metadata.deprecatedClasses.*;
import org.acoli.glaser.metadata.pdf.read.PDF2XML;
import org.acoli.glaser.metadata.pdf.extract.MetadataFromPDF;
import org.acoli.glaser.metadata.deprecatedClasses.MetadataSourceHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class testPrototyping {
    String resourcesDir;

    @Before
    public void setUp() {
        resourcesDir = System.getProperty("user.dir")+"/src/test/resources/";
    }

    @Test
    public void testExtractingLinksFromBasePageBasedOnCSSQuery() throws IOException {
        String urlAsString = "http://lrec-conf.org/workshops/lrec2018/W29/papers.html";
        Document doc = Jsoup.connect(urlAsString).get();
        List<URL> hrefs = new PageSpider().findHrefsByCSSQuery(doc, ".paper_papers > a");
        Assert.assertEquals(hrefs.size(), 12);

    }
    @Test
    public void testParlaCLARIN() throws IOException {
        String urlAsString = "http://lrec-conf.org/workshops/lrec2018/W2/papers.html";
        Document doc = Jsoup.connect(urlAsString).get();
        List<URL> hrefs = new PageSpider().findHrefsByCSSQuery(doc, ".paper_papers > a");
        Assert.assertEquals(hrefs.size(), 16);
    }


    @Test
    public void testPageHandlerWithSingleInitialURL() throws IOException {
        MetadataFromPDF mfp = new MetadataFromPDF(new URL("http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf"), true);
        List<MetadataSourceHandler> list = new ArrayList<>();
        list.addAll(MetadataFromHTML.spawnHandlersWithInitialPageAndSelector(new URL("http://lrec-conf.org/workshops/lrec2018/W29/papers.html"), ".paper_papers > a", new FileHandler()));
        //list.add(mfp);
        PageHandler ph = new PageHandler(list);
        ph.run();
    }

    @Test
    public void testRemovingDTD() throws IOException {
        PDF2XML pdf2XML = new PDF2XML(resourcesDir+"tmpDir/");
        File pdf = new File(resourcesDir+"testPaper.pdf");
        File xml = pdf2XML.pdfToXml(pdf);
        Assert.assertTrue(pdf2XML.hasDoctypeAnnotation(xml));
        pdf2XML.removeDtdFromFile(xml);
        Assert.assertFalse(pdf2XML.hasDoctypeAnnotation(xml));
        }

}
