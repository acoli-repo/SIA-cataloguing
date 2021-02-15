package org.acoli.glaser.metadata.pdf.extract;

import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Splits a XML Document (from PDF) into distinct pages. Currently we use only the occurrence of the word "Abstract" in a single
 * field as a hint. This won't miss recognize a page with the word Abstract in a sentence but would probably fail if the word
 * is highlighted e.g. only word in italics in a sentence.
 */
public class Splitter {

    XMLInputFactory xif;
    DocumentBuilder db;
    XMLEventFactory xef;
    private static Logger LOG = Logger.getLogger(Splitter.class.getName());

    public Splitter() throws ParserConfigurationException {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        this.xif = xif;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        this.db = dbf.newDocumentBuilder();
        this.xef = XMLEventFactory.newFactory();
    }


    public List<List<XMLEvent>> splitIntoPages(File file) throws FileNotFoundException, XMLStreamException {
        List<List<XMLEvent>> pages = new ArrayList<>();
        Reader xml = new FileReader(file);
        XMLEventReader reader = this.xif.createXMLEventReader(xml);
        int pageCount = 0;
        while (reader.hasNext()) {
            XMLEvent nextEventPeeked = reader.peek();
            if (nextEventPeeked.isStartElement() && nextEventPeeked.asStartElement().getName().toString().equals("page")) {
                List<XMLEvent> nextPage = collectPage(reader);
                pageCount++;
                pages.add(nextPage);
            } else {
                reader.next();
            }
        }
        LOG.info("Document has " + pageCount + " pages.");
        return pages;
    }

    Document mergePagesIntoDocument(List<List<XMLEvent>> pages) throws XMLStreamException {
        Document paper = db.newDocument();
        XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(new DOMResult(paper));
        int eventNo = 0;
        // We artificially insert one root element that contains multiple page children
        writer.add(xef.createStartElement(QName.valueOf("pdf2xml"), null, null));

        for (List<XMLEvent> page : pages) {
            for (XMLEvent event : page) {
                //						System.err.println(event.getEventType());
                eventNo++;
//                                System.err.println("Writing "+eventNo+"/"+nextPage.size());
                writer.add(event);
            }
        }
        return paper;
    }

    public List<Document> splitIntoDistinctPapers(File file) throws FileNotFoundException, XMLStreamException {
        LOG.info("Splitting..");
        List<Document> papers = new ArrayList<>();
        int pageCount = 0;
        Reader xml = new FileReader(file);

        List<List<XMLEvent>> pages = splitIntoPages(file);
        List<List<XMLEvent>> buffer = new ArrayList<>();
        for (int i = 0; i < pages.size(); i++) {
            if (isFirstPageOfAPaper(pages.get(i))) {
                if (!buffer.isEmpty()) {
                    // A first page and we already found stuff, so a new paper has started
                    Document paper = mergePagesIntoDocument(buffer);
                    papers.add(paper);
                    buffer.clear();
                }
                buffer.add(pages.get(i));
            } else {
                if (!buffer.isEmpty()) {
                    // It's not a first page but there is one in the buffer already, so
                    // we can expect that it's not clutter.
                    buffer.add(pages.get(i));
                }
            }
        }
        papers.add(mergePagesIntoDocument(buffer));
        LOG.info("Document contains " + papers.size() + " papers.");
        return papers;
    }


    List<XMLEvent> collectPage(XMLEventReader xer) throws XMLStreamException {
        List<XMLEvent> pageBuffer = new ArrayList<>();
        while (xer.hasNext()) {
            XMLEvent nextEvent = xer.nextEvent();
            pageBuffer.add(nextEvent);
            if (nextEvent.isEndElement()) {
                EndElement ee = nextEvent.asEndElement();
                if (ee.getName().toString().equals("page")) {
                    break;
                }
            }
        }
        LOG.info("Collected page with " + pageBuffer.size() + " events.");
        return pageBuffer;
    }

    boolean isFirstPageOfAPaper(List<XMLEvent> page) {
        boolean hasAbstract = false;
        for (int i = 0; i < page.size(); i++) {
            XMLEvent event = page.get(i);
            if (event.isStartElement()) {
                StartElement se = event.asStartElement();
            }
            if (event.isCharacters()) {
                if (event.asCharacters().getData().trim().equals("Abstract")) {
                    XMLEvent previous = page.get(i - 1);
                    //						if (previous.isStartElement() &&
                    //								previous.asStartElement().getAttributeByName(QName.valueOf("height")).toString().equals("18"))
                    hasAbstract = true;
                }
            }
        }
        return hasAbstract;
    }

}
