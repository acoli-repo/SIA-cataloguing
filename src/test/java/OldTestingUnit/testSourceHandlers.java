package OldTestingUnit;

import org.acoli.glaser.metadata.deprecatedClasses.MetadataFromBibtex;
import org.acoli.glaser.metadata.deprecatedClasses.MetadataFromHTML;
import org.acoli.glaser.metadata.pdf.extract.MetadataFromPDF;
import org.acoli.glaser.metadata.deprecatedClasses.MetadataSourceHandler;
import org.acoli.glaser.metadata.pdf.util.Metadata;
import org.acoli.glaser.metadata.deprecatedClasses.Metadata2TTL;
import org.acoli.glaser.metadata.pdf.extract.PDFExtractionConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class testSourceHandlers {
    String resourcesDir;

    @Before
    public void setUp() {
        resourcesDir = System.getProperty("user.dir")+"/src/test/resources/";
    }



    public void testIfHandlerAdheresToBusinessLogic(MetadataSourceHandler msh) {
        Assert.assertFalse(msh.finished());
        msh.run();
        if (msh.finished()) {
            Assert.assertNotNull(msh.getMetadata());
        }
    }
    @Test
    public void testMetadataFromPDFWithSplit() throws MalformedURLException {
        // Workshop proceedings with 13 papers
        MetadataFromPDF mfp = new MetadataFromPDF(new URL("http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf"), true, PDFExtractionConfiguration.emptyConfig());

        mfp.run();
        Metadata2TTL m2t = new Metadata2TTL();

        Assert.assertEquals(mfp.getMetadata().size(), 13);


    }
    @Test
    public void testPDFExtractionWithSourceHandlerWithExtractionConfig() throws MalformedURLException {
        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(5);
        config.setAuthorHeight(16);
        config.setTitleFont(4);
        config.setTitleHeight(19);
        config.setPageFont(3);
        config.setPageHeight(15);
        MetadataFromPDF mfp = new MetadataFromPDF(new URL("file://"+resourcesDir+"/testPaper.pdf"), false, config);
        mfp.run();
        assert mfp.getMetadata().size() == 1;
        Metadata result = mfp.getMetadata().get(0);
        System.err.println(result);
    }
    @Test
    public void testMetadataFromHTMLaddingCorrectMetadataFromSourcesToPool() throws MalformedURLException {
        MetadataFromHTML mfh = new MetadataFromHTML(new URL("http://lrec-conf.org/workshops/lrec2018/W29/summaries/6_W29.html"));
        mfh.run();
        Assert.assertTrue(mfh.foundOtherSourcesThatRequireHandling());
        Assert.assertEquals(MetadataFromPDF.class, mfh.createPDFHandlersFromFoundURLS().get(0).getClass());
        Assert.assertEquals(MetadataFromBibtex.class, mfh.createPDFHandlersFromFoundURLS().get(1).getClass());
    }
}
