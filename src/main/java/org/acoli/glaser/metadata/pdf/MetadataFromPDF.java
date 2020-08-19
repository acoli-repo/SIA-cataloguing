package org.acoli.glaser.metadata.pdf;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/** support for XML-enhanced TSV formats as used by SketchEngine, CWB and the TreeTagger chunker <br/>
 *      captures SGML/XML markup only, process TSV content via CoNLL2RDF */
public class MetadataFromPDF extends MetadataSourceHandler {

    private boolean downloadFailed;
    private boolean transformationFailed;
    private boolean finished;
    List<Metadata> mds;

    public MetadataFromPDF(URL urlToPDF) {
        this.source = urlToPDF;
        this.downloadFailed = false;
        this.transformationFailed = false;
        this.finished = false;
        this.mds = new ArrayList<>();
    }
    static List<File> collectPDFsInDir(File directory) {
        // TODO: make this recursive
        List<File> files = new ArrayList<>();
        if (!directory.isDirectory()) {
            System.err.println("Not a directory!");
        }
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    System.err.println("Detected pdf: "+file.getPath());
                    files.add(file);
                }
            }
        }
        System.err.println("Found "+files.size()+" pdf files.");
        return files;
    }

    public Metadata extractMetadata() {
        File pdf = null;
        System.err.println("NOT IMPLEMENTED YET");
        Document parsedDocument = this.transformPDFIntoDocumentAndRemoveDTD(pdf);
        return getMetadataFromPDFAsXML(parsedDocument);
    }

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

    Document transformPDFIntoDocumentAndRemoveDTD(File pdf) {
        PDF2XML pdf2xml = new PDF2XML("tempDir");
        try {
            File xml = pdf2xml.pdfToXml(pdf);

            pdf2xml.removeDtdFromFile(xml);
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
        extractor.titleFont = 21;
        extractor.titleHeight = 26;
        extractor.pageHeight = 20;
        extractor.authorFont = 16;
        extractor.authorHeight = 21;
        return extractor.getMetadata(paper);
    }

    @Override
    public void run() {
        File pdfFile = downloadURL(this.source);
        if (!downloadFailed) {
            Document pdfFileAsXMLDocument = transformPDFIntoDocumentAndRemoveDTD(pdfFile);
            this.mds.add(getMetadataFromPDFAsXML(pdfFileAsXMLDocument));
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