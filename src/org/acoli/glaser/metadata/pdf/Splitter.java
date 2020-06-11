package org.acoli.glaser.metadata.pdf;

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

public class Splitter {

    XMLInputFactory xif;
    DocumentBuilder db;
    XMLEventFactory xef;

    public Splitter() throws ParserConfigurationException {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        this.xif = xif;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        this.db = dbf.newDocumentBuilder();
        this.xef = XMLEventFactory.newFactory();
    }

    void printFirstAndLastElement(List<XMLEvent> xes) {
        System.err.println("===");
        System.err.println(xes.get(0));
        System.err.println(xes.get(xes.size()-1));
        System.err.println("===");
    }

    List<List<XMLEvent>> splitIntoPages(File file) throws FileNotFoundException, XMLStreamException {
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
        System.err.println("Document has " + pageCount + " pages.");
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

    List<Document> splitIntoDistinctPapers(File file) throws FileNotFoundException, XMLStreamException {
        // TODO: Add clutter detection maybe?
        System.err.println("Splitting..");
        List<Document> papers = new ArrayList<>();
        int pageCount = 0;
        Reader xml = new FileReader(file);

        List<List<XMLEvent>> pages = splitIntoPages(file);
        // TODO: now run through all pages, remove clutter and set beginning and end of each paper
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
        System.err.println("Document contains " + papers.size() + " papers.");
        return papers;
    }


    Document collectPaper(XMLEventReader xer, int from, int to) throws XMLStreamException {
        Document paper = this.db.newDocument();
        XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(new DOMResult(paper));
        boolean collecting = false; // This is true during the page that
        int currentPageNumber = -1;
        XMLEventFactory xef = XMLEventFactory.newFactory();
        writer.add(xef.createStartElement(QName.valueOf("pdf2xml"), null, null));
//        paper.createElement("pdf2xml");
        while (xer.hasNext()) {
            XMLEvent nextEvent = xer.nextEvent();
            if (nextEvent.isStartElement() && nextEvent.asStartElement().getName().toString().equals("page")) {
                int pageNumber = Integer.parseInt(nextEvent.asStartElement().getAttributeByName(QName.valueOf("number")).getValue());
                System.err.println("Found page "+pageNumber);
                currentPageNumber = pageNumber;
            }
            if (currentPageNumber >= from && currentPageNumber <= to) {
                writer.add(nextEvent);
            }
        }
        return paper;
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
        System.err.println("Collected page with " + pageBuffer.size() + " events.");
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
                if (event.asCharacters().getData().strip().equals("Abstract")) {
                    XMLEvent previous = page.get(i - 1);
                    //						if (previous.isStartElement() &&
                    //								previous.asStartElement().getAttributeByName(QName.valueOf("height")).toString().equals("18"))
                    hasAbstract = true;
                }
            }
        }
        return hasAbstract;
    }

    void outputElementNames(List<XMLEvent> xes) {
        for (XMLEvent xe : xes) {
            System.err.println(xe.getEventType());
        }
    }

    public static void printDocument(Document doc, OutputStream out) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc),
                    new StreamResult(new OutputStreamWriter(out, "UTF-8")));
        } catch (Exception e) {
            System.err.println("Couldn't print");
        }
    }
}


//	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	DocumentBuilder db;
//	Document subtree = null;
//
//	XMLEventReader current = this.getXmlReader(); // retrieve the Reader in it's current state
//		try {
//				db = dbf.newDocumentBuilder();
//				subtree = db.newDocument();
//
//				// Using an XMLWriter that writes directly to a DOM Object
//				XMLEventWriter writer;
//				writer = XMLOutputFactory.newInstance().createXMLEventWriter(new DOMResult(subtree));
//
//				if ((span.begin) != this.indexOfNextXMLEvent) {
//				LOGGER.warning("Beginning to collect from span "+span+" while next element reader will return is "+this.indexOfNextXMLEvent);
//				}
//
//				for (int i = span.begin; i <= span.end; i++){
//				XMLEvent next = current.nextEvent();
//				//System.out.println("Trying to write at index "+i+": "+next);
//				writer.add(next);
//				this.indexOfNextXMLEvent++; // update reader index
//
//
//				}
//				this.setXmlReader(current);
//
//				}catch (XMLStreamException | ParserConfigurationException e ) {
//				System.err.println("Unable to collect the subtree with index " + this.currentSpanIndex);
//				e.printStackTrace();
//				}
//				return subtree;
