//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.28 um 06:10:15 PM CEST 
//


package org.acoli.sc.mods.classes;

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
 * generated in the org.acoli.sc.mods.classes package. 
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

    private final static QName _Number_QNAME = new QName("http://www.loc.gov/mods/v3", "number");
    private final static QName _Affiliation_QNAME = new QName("http://www.loc.gov/mods/v3", "affiliation");
    private final static QName _Genre_QNAME = new QName("http://www.loc.gov/mods/v3", "genre");
    private final static QName _Issuance_QNAME = new QName("http://www.loc.gov/mods/v3", "issuance");
    private final static QName _Publisher_QNAME = new QName("http://www.loc.gov/mods/v3", "publisher");
    private final static QName _Topic_QNAME = new QName("http://www.loc.gov/mods/v3", "topic");
    private final static QName _End_QNAME = new QName("http://www.loc.gov/mods/v3", "end");
    private final static QName _Text_QNAME = new QName("http://www.loc.gov/mods/v3", "text");
    private final static QName _RecordIdentifier_QNAME = new QName("http://www.loc.gov/mods/v3", "recordIdentifier");
    private final static QName _Date_QNAME = new QName("http://www.loc.gov/mods/v3", "date");
    private final static QName _DateIssued_QNAME = new QName("http://www.loc.gov/mods/v3", "dateIssued");
    private final static QName _Start_QNAME = new QName("http://www.loc.gov/mods/v3", "start");
    private final static QName _Caption_QNAME = new QName("http://www.loc.gov/mods/v3", "caption");
    private final static QName _Abstract_QNAME = new QName("http://www.loc.gov/mods/v3", "abstract");
    private final static QName _TypeOfResource_QNAME = new QName("http://www.loc.gov/mods/v3", "typeOfResource");
    private final static QName _Title_QNAME = new QName("http://www.loc.gov/mods/v3", "title");
    private final static QName _NamePart_QNAME = new QName("http://www.loc.gov/mods/v3", "namePart");
    private final static QName _DisplayForm_QNAME = new QName("http://www.loc.gov/mods/v3", "displayForm");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.acoli.sc.mods.classes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Extent }
     * 
     */
    public Extent createExtent() {
        return new Extent();
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
     * Create an instance of {@link TitleInfo }
     * 
     */
    public TitleInfo createTitleInfo() {
        return new TitleInfo();
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link RoleTerm }
     * 
     */
    public RoleTerm createRoleTerm() {
        return new RoleTerm();
    }

    /**
     * Create an instance of {@link OriginInfo }
     * 
     */
    public OriginInfo createOriginInfo() {
        return new OriginInfo();
    }

    /**
     * Create an instance of {@link Place }
     * 
     */
    public Place createPlace() {
        return new Place();
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
     * Create an instance of {@link Language }
     * 
     */
    public Language createLanguage() {
        return new Language();
    }

    /**
     * Create an instance of {@link LanguageTerm }
     * 
     */
    public LanguageTerm createLanguageTerm() {
        return new LanguageTerm();
    }

    /**
     * Create an instance of {@link Part }
     * 
     */
    public Part createPart() {
        return new Part();
    }

    /**
     * Create an instance of {@link Detail }
     * 
     */
    public Detail createDetail() {
        return new Detail();
    }

    /**
     * Create an instance of {@link Subject }
     * 
     */
    public Subject createSubject() {
        return new Subject();
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
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "number")
    public JAXBElement<BigInteger> createNumber(BigInteger value) {
        return new JAXBElement<BigInteger>(_Number_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "affiliation")
    public JAXBElement<String> createAffiliation(String value) {
        return new JAXBElement<String>(_Affiliation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "genre")
    public JAXBElement<String> createGenre(String value) {
        return new JAXBElement<String>(_Genre_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "issuance")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIssuance(String value) {
        return new JAXBElement<String>(_Issuance_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "publisher")
    public JAXBElement<String> createPublisher(String value) {
        return new JAXBElement<String>(_Publisher_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "topic")
    public JAXBElement<String> createTopic(String value) {
        return new JAXBElement<String>(_Topic_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "end")
    public JAXBElement<BigInteger> createEnd(BigInteger value) {
        return new JAXBElement<BigInteger>(_End_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "text")
    public JAXBElement<String> createText(String value) {
        return new JAXBElement<String>(_Text_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "recordIdentifier")
    public JAXBElement<String> createRecordIdentifier(String value) {
        return new JAXBElement<String>(_RecordIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "date")
    public JAXBElement<String> createDate(String value) {
        return new JAXBElement<String>(_Date_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "start")
    public JAXBElement<BigInteger> createStart(BigInteger value) {
        return new JAXBElement<BigInteger>(_Start_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "caption")
    public JAXBElement<String> createCaption(String value) {
        return new JAXBElement<String>(_Caption_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "abstract")
    public JAXBElement<String> createAbstract(String value) {
        return new JAXBElement<String>(_Abstract_QNAME, String.class, null, value);
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

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "displayForm")
    public JAXBElement<String> createDisplayForm(String value) {
        return new JAXBElement<String>(_DisplayForm_QNAME, String.class, null, value);
    }

}
