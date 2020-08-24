
import org.acoli.glaser.metadata.pdf.Metadata;
import org.acoli.glaser.metadata.pdf.Metadata2TTL;
import org.acoli.glaser.metadata.pdf.MetadataFromPDF;
import org.acoli.glaser.metadata.pdf.MetadataSourceHandler;
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
        MetadataFromPDF mfp = new MetadataFromPDF(new URL("http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf"), true);
        mfp.run();
        Metadata2TTL m2t = new Metadata2TTL();

        Assert.assertEquals(mfp.getMetadata().size(), 13);
        for (Metadata md : mfp.getMetadata()) {
            System.err.println(m2t.metadataToTTL(md));
        }

    }
}
