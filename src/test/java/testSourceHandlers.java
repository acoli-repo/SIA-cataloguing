
import org.acoli.glaser.metadata.pdf.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class testSourceHandlers {


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
        for (Metadata md : mfp.getMetadata()) {
            System.err.println(m2t.metadataToTTL(md));
        }

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
