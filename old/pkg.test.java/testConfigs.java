import java.io.IOException;
import java.util.List;
import org.acoli.sc.config.Config;
import org.acoli.sc.config.SourceDescription;
import org.acoli.sc.config.SourceType;
import org.acoli.sc.extract.PDFExtractionConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class testConfigs {
    String pathToConfigFile = "configs.json";
    @Test
    public void testReading() throws IOException {
        Config config = PDFExtractionConfiguration.readConfigs(pathToConfigFile);
        Assert.assertNotNull(config);
        List<SourceDescription> sources = config.getSourceDescriptions();
        Assert.assertNotNull(sources);
    }
    @Test
    public void testHTMLSource() throws IOException {
        Config config = PDFExtractionConfiguration.readConfigs(pathToConfigFile);
        List<SourceDescription> sds = config.getSourceDescriptions();
        SourceDescription htmlSource = sds.get(0);
        Assert.assertEquals(SourceType.html, htmlSource.getType());
        Assert.assertEquals("http://lrec-conf.org/workshops/lrec2018/W29/papers.html", htmlSource.getUrl().toString());
        Assert.assertNotNull(htmlSource.getExtractorConfig());
    }
    @Test
    public void testPDFSource() throws IOException {
        Config config = PDFExtractionConfiguration.readConfigs(pathToConfigFile);
        List<SourceDescription> sds = config.getSourceDescriptions();
        SourceDescription source = sds.get(1);
        Assert.assertEquals(SourceType.pdf, source.getType());
        Assert.assertEquals("http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf", source.getUrl().toString());
        Assert.assertEquals(-1, source.getExtractorConfig().getAuthorHeight());
    }
}
