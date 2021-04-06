import org.acoli.glaser.metadata.pdf.read.DataReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class testParser {

    @Test
    public void testListParsing() throws Exception {
        DataReader dataReader = new DataReader();
        List<String> pdfNames =  dataReader.parseInputName("documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471/items.jsonl");
        assertEquals(138, pdfNames.size());
    }

    @Test
    public void testOutputParsin() throws Exception{
        DataReader dataReader = new DataReader();
        List<ArrayList<String>> outputAuthors = dataReader.parseOutputAuthors("documentation/output-data-format/047006471-output.jsonl");
        assertEquals(264, outputAuthors.size());
    }

    @Test
    public void testPDFReading(){  //3 out of 4 Matching Pdfs
        DataReader dataReader = new DataReader();
        List<String> pdfNames = new ArrayList<String>();
        pdfNames.add("bury.pdf");
        pdfNames.add("bates.pdf");
        pdfNames.add("arad.pdf");
        pdfNames.add("armin.pdf");
        List<String> foundPdfs = dataReader.retrieveFilesFromList(pdfNames, "documentation/samples/input-examples/https-www-phon-ucl-ac-uk/047006471");
        assertEquals(3,foundPdfs.size());
    }
}
