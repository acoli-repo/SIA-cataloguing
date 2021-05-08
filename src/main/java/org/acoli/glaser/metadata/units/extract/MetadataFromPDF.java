package org.acoli.glaser.metadata.units.extract;

import org.acoli.glaser.metadata.deprecatedCode.*;
import org.acoli.glaser.metadata.units.old.PDF2XML;
import org.acoli.glaser.metadata.units.util.Metadata;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Contains logic that extracts Metadata objects from PDFs. Will often use XML as an intermediate format, as it is more
 * easier parsable. See PDF2XML for that.
 */
public class MetadataFromPDF extends MetadataSourceHandler {

    private boolean downloadFailed = false;
    private boolean transformationFailed = false;
    private boolean finished = false;
    private static Logger LOG = Logger.getLogger(MetadataFromPDF.class.getName());
    private boolean split = false;
    List<Metadata> mds = new ArrayList<>();
    private PDFExtractionConfiguration extractionConfig;

    public MetadataFromPDF(URL urlToPDF, boolean split) {
        this.source = urlToPDF;
        this.split = split;
    }
    public MetadataFromPDF(URL urlToPDF) {
        this.source = urlToPDF;
        this.extractionConfig = PDFExtractionConfiguration.emptyConfig();
    }
    public MetadataFromPDF(URL urlToPDF, boolean split, PDFExtractionConfiguration config) {
        this.source = urlToPDF;
        this.split = split;
        this.extractionConfig = config;
    }

    /**
     * Loads all pdf files from a local folder. May still be used for testing purposes.
     * @TV womöglich kannst du das doch verwenden wenn die Daten jetzt lokal sind?
     */
    @Deprecated
    static List<File> collectPDFsInDir(File directory) {
        // TODO: make this recursive
        List<File> files = new ArrayList<>();
        if (!directory.isDirectory()) {
            LOG.warning("Not a directory!");
        }
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    LOG.info("Detected pdf: "+file.getPath());
                    files.add(file);
                }
            }
        }
        LOG.info("Found "+files.size()+" pdf files.");
        return files;
    }

    /**
     * Connects to a basic FileHandler, downloads the file and returns it if successful.
     * @param url
     * @return
     */
    File downloadURL(URL url) {
        FileHandler fh = new FileHandler();
        try {
            return fh.downloadFileToTemp(url);
        } catch (IOException e) {
            e.printStackTrace();
            this.downloadFailed = true;
        }
        return null;
    }

    File transformPDFIntoDocumentAndRemoveDTDReturnsFile(File pdf) {
        PDF2XML pdf2xml = new PDF2XML("tempDir");
        File xml = null;
        try {
            xml = pdf2xml.pdfToXml(pdf);
            pdf2xml.removeDtdFromFile(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Wrapper function around transformPDFIntoDocumentAndRemoveDTDReturnsFile that will also
     * create a DOM from the resulting XML.
     * @param pdf
     * @return
     */
    Document transformPDFIntoDocumentAndRemoveDTD(File pdf) {
        try {
            File xml = transformPDFIntoDocumentAndRemoveDTDReturnsFile(pdf);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.parse(xml);
            return document;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            this.transformationFailed = true;
            return null;
        }
    }

    private Metadata getMetadataFromPDFAsXML(Document paper) {
        try {
            PDFMetadataExtractor extractor = new PDFMetadataExtractor(this.extractionConfig);
            return extractor.getMetadata(paper);
        } catch (RuntimeException e) {
            System.err.println("Creation of PDFMetadataExtractor failed: "+e.getMessage());
        }
        return null;
    }

    /**
     * Connects to a new Splitter and sends the (xml-)file for splitting.
     * @param paperAsXML
     * @return
     */
    public List<Document> splitPages(File paperAsXML) {
        try {
            Splitter splitter = new Splitter();
            List<Document> papers = splitter.splitIntoDistinctPapers(paperAsXML);
            return papers;
        }
        catch (ParserConfigurationException | FileNotFoundException | XMLStreamException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void run() {
        File pdfFile = downloadURL(this.source);
        if (!downloadFailed) {
            if (this.split) {
                for (Document pdfFileAsXMLDocument : splitPages(transformPDFIntoDocumentAndRemoveDTDReturnsFile(pdfFile))) {
                    this.mds.add(getMetadataFromPDFAsXML(pdfFileAsXMLDocument));
                }
            } else {
                Document pdfFileAsXMLDocument = transformPDFIntoDocumentAndRemoveDTD(pdfFile);
                    this.mds.add(getMetadataFromPDFAsXML(pdfFileAsXMLDocument));
            }
        }
        finished = true;
    }

    @Override
    public List<Metadata> getMetadata() {
        return this.mds;
    }

    @Override
    public boolean finished() {
        return finished;
    }

    @Override
    public boolean success() {
        return finished && !this.mds.isEmpty() && !transformationFailed && !downloadFailed;
    }

    @Override
    public boolean foundOtherSourcesThatRequireHandling() {
        return false;
    }

    @Override
    public List<MetadataSourceHandler> getHandlersForOtherSources() {
        return null;
    }
}