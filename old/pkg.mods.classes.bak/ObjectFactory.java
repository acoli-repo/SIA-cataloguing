//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.27 um 02:35:26 PM CEST 
//


package org.acoli.sc.mods.classes.bak;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.acoli.sc.mods package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DateIssued_QNAME = new QName("http://www.loc.gov/mods/v3", "dateIssued");
    private final static QName _TypeOfResource_QNAME = new QName("http://www.loc.gov/mods/v3", "typeOfResource");
    private final static QName _RecordIdentifier_QNAME = new QName("http://www.loc.gov/mods/v3", "recordIdentifier");
    private final static QName _Title_QNAME = new QName("http://www.loc.gov/mods/v3", "title");
    private final static QName _NamePart_QNAME = new QName("http://www.loc.gov/mods/v3", "namePart");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.acoli.sc.mods
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mods }
     * 
     */
    public Mods createMods() {
        return new Mods();
    }

    /**
     * Create an instance of {@link RecordInfo }
     * 
     */
    public RecordInfo createRecordInfo() {
        return new RecordInfo();
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link TitleInfo }
     * 
     */
    public TitleInfo createTitleInfo() {
        return new TitleInfo();
    }

    /**
     * Create an instance of {@link OriginInfo }
     * 
     */
    public OriginInfo createOriginInfo() {
        return new OriginInfo();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link PhysicalLocation }
     * 
     */
    public PhysicalLocation createPhysicalLocation() {
        return new PhysicalLocation();
    }

    /**
     * Create an instance of {@link Url }
     * 
     */
    public Url createUrl() {
        return new Url();
    }

    /**
     * Create an instance of {@link ModsCollection }
     * 
     */
    public ModsCollection createModsCollection() {
        return new ModsCollection();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "dateIssued")
    public JAXBElement<BigInteger> createDateIssued(BigInteger value) {
        return new JAXBElement<BigInteger>(_DateIssued_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "typeOfResource")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTypeOfResource(String value) {
        return new JAXBElement<String>(_TypeOfResource_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "recordIdentifier")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRecordIdentifier(String value) {
        return new JAXBElement<String>(_RecordIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "namePart")
    public JAXBElement<String> createNamePart(String value) {
        return new JAXBElement<String>(_NamePart_QNAME, String.class, null, value);
    }

}
