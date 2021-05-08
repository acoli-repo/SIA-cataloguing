package DeprecatedTestingUnit;

import org.acoli.glaser.metadata.unit.extract.MetadataFromPDF;
import org.acoli.glaser.metadata.unit.old.PDF2XML;
import org.acoli.glaser.metadata.unit.extract.PDFExtractionConfiguration;
import org.acoli.glaser.metadata.unit.extract.PDFMetadataExtractor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class testRun {
    String resourcesDir;


    public PDFExtractionConfiguration makeExtractionConfig(){

        PDFExtractionConfiguration config = new PDFExtractionConfiguration();
        config.setAuthorFont(5);
        config.setAuthorHeight(16);
        config.setTitleFont(4);
        config.setTitleHeight(19);
        config.setPageFont(3);
        config.setPageHeight(15);
        return config;
    }
    @Before
    public void setUp() {
        resourcesDir = System.getProperty("user.dir")+"/src/test/resources/";
    }


    @Test
    public void extractOneFileWithSourceHandler() throws MalformedURLException {
        PDFExtractionConfiguration config = makeExtractionConfig();

        MetadataFromPDF mfp = new MetadataFromPDF(new URL("file://"+resourcesDir+"/testPaper.pdf"), false, config);
        mfp.run();
        assert mfp.getMetadata().size() == 1;

    }
    @Test
    public void extractOneFileWithOnlyExtractPackage() throws IOException, ParserConfigurationException, SAXException {
        File pdfFile = new File(resourcesDir+"/resultData/ackema.pdf");
        PDF2XML pdf2XML = new PDF2XML(resourcesDir+"/tmpDir/");
        File xmlFile = pdf2XML.pdfToXml(pdfFile);

        pdf2XML.removeDtdFromFile(xmlFile);
        PDFExtractionConfiguration config = makeExtractionConfig();
        PDFMetadataExtractor pme = new PDFMetadataExtractor(config);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document xmlDocument = builder.parse(xmlFile);
        assert pme.getMetadata(xmlDocument).authors.size() != 0;
    }

}
