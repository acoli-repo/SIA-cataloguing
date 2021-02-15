import org.acoli.glaser.metadata.pdf.*;
import org.acoli.glaser.metadata.pdf.config.Config;
import org.acoli.glaser.metadata.pdf.config.SourceDescriptions;
import org.acoli.glaser.metadata.pdf.config.SourceTypes;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class testConfigs {
    String pathToConfigFile = "configs.json";
    @Test
    public void testReading() throws IOException {
        Config config = MainRunner.readConfigs(pathToConfigFile);
        Assert.assertNotNull(config);
        List<SourceDescriptions> sources = config.getSources();
        Assert.assertNotNull(sources);
    }
    @Test
    public void testHTMLSource() throws IOException {
        Config config = MainRunner.readConfigs(pathToConfigFile);
        List<SourceDescriptions> sds = config.getSources();
        SourceDescriptions htmlSource = sds.get(0);
        Assert.assertEquals(SourceTypes.html, htmlSource.getType());
        Assert.assertEquals("http://lrec-conf.org/workshops/lrec2018/W29/papers.html", htmlSource.getUrl().toString());
        Assert.assertNotNull(htmlSource.getExtractorConfig());
    }
    @Test
    public void testPDFSource() throws IOException {
        Config config = MainRunner.readConfigs(pathToConfigFile);
        List<SourceDescriptions> sds = config.getSources();
        SourceDescriptions source = sds.get(1);
        Assert.assertEquals(SourceTypes.pdf, source.getType());
        Assert.assertEquals("http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf", source.getUrl().toString());
        Assert.assertEquals(-1, source.getExtractorConfig().getAuthorHeight());
    }
}
