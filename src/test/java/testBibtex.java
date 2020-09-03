import org.acoli.glaser.metadata.pdf.Metadata;
import org.acoli.glaser.metadata.pdf.MetadataFromBibtex;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class testBibtex {


    @Test
    public void testBibtexParsing() {
        String bibtex = "@InProceedings{YOO18.6, author = {Gwanghoon Yoo and Jeesun Nam}, title = {A Hybrid Approach to Sentiment Analysis Enhanced by Sentiment Lexicons and Polarity Shifting Devices}, booktitle = {Proceedings of the Eleventh International Conference on Language Resources and Evaluation (LREC 2018)}, year = {2018}, month = {may}, date = {7-12}, location = {Miyazaki, Japan}, editor = {Kiyoaki Shirai}, publisher = {European Language Resources Association (ELRA)}, address = {Paris, France}, isbn = {979-10-95546-24-5}, language = {english} }";
        MetadataFromBibtex mfb = new MetadataFromBibtex(bibtex);
        mfb.run();
        Metadata md = mfb.getMetadata().get(0);
        assertEquals("A Hybrid Approach to Sentiment Analysis Enhanced by Sentiment Lexicons and Polarity Shifting Devices",md.title);
        assertEquals("Proceedings of the Eleventh International Conference on Language Resources and Evaluation (LREC 2018)", md.booktitle);
        assertEquals(2018, (int) md.year);
        assertEquals("Paris, France", md.location);
        assertEquals(new HashSet<>(Arrays.asList("Gwanghoon Yoo", "Jeesun Nam")), new HashSet<>(md.authors));
    }

}
