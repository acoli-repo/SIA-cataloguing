package org.acoli.glaser.metadata.pdf;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.print.Doc;
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

    /**
     * @param urlToPDF the URL that points to the PDF.
     * @param split previously indicate if the PDF contains one or more sources of metadata (e.g.) a proceedings collection
     */
    public MetadataFromPDF(URL urlToPDF, boolean split) {
        this.source = urlToPDF;
        this.split = split;
    }
    public MetadataFromPDF(URL urlToPDF) {
        this.source = urlToPDF;
    }

    /**
     * Loads all pdf files from a local folder. May still be used for testing purposes.
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

    @Deprecated
    public Metadata extractMetadata() {
        File pdf = null;
        LOG.warning("NOT IMPLEMENTED YET");
        Document parsedDocument = this.transformPDFIntoDocumentAndRemoveDTD(pdf);
        return getMetadataFromPDFAsXML(parsedDocument);
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

    @Deprecated
    public static void main(String[] argv) throws Exception {

        System.err.println("Reading from file "+argv[0]);
        if (argv[0].contains("DS_Store") || argv[0].contains("test-nodtd.html.xml"))
            System.exit(0);
        File file = new File(System.getProperty("user.dir")+"/"+argv[0]);
        boolean split = false;
        if (argv.length > 1 && (argv[1].toLowerCase().contains("-s") || argv[1].toLowerCase().contains("--split")))
            split = true;
        List<File> pdfs = collectPDFsInDir(file);
        System.err.println("Split: "+split);
        PDF2XML pdf2xml = new PDF2XML("tempDir");
        Metadata2TTL m2t = new Metadata2TTL();
        for (File pdf : pdfs) {

        	File xml = pdf2xml.pdfToXml(pdf);

        	pdf2xml.removeDtdFromFile(xml);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();

			List<Document> papers = new ArrayList<>();
			if (split)
			    papers = new Splitter().splitIntoDistinctPapers(xml);
			else {
                Document document = builder.parse(xml);
                papers.add(document);
            }
            System.err.println("Starting to index "+papers.size()+" papers..");
//			papers.remove(0);
			for (Document paper : papers) {
			    Splitter.printDocument(paper, System.out);
                Metadata md = getMetadataFromPDFAsXML(paper);
                md.fileName = pdf.getName(); // TODO: Make this more sophisticated like in zotero

				if (!MetadataValidator.isFullyPopulated(md)) {
//					System.out.println("===CHECK===");
//					System.out.println(md);
				} else {
					System.out.println("Correct!");
					System.out.println(md);
				}
				break;
			}
		}

    }

    private static Metadata getMetadataFromPDFAsXML(Document paper) {
        PDFMetadataExtractor extractor = new PDFMetadataExtractor();
        // TODO: Parameterize below somewhere
        extractor.titleFont = 9;
        extractor.titleHeight = 25;
        extractor.pageHeight = 15;
        extractor.authorFont = 7;
        extractor.authorHeight = 21;
        return extractor.getMetadata(paper);
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