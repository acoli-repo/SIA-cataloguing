//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.05 um 02:45:39 PM CEST 
//


package org.acoli.sc.mods.classes.old;

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

    private final static QName _SubTitle_QNAME = new QName("http://www.loc.gov/mods/v3", "subTitle");
    private final static QName _Affiliation_QNAME = new QName("http://www.loc.gov/mods/v3", "affiliation");
    private final static QName _Genre_QNAME = new QName("http://www.loc.gov/mods/v3", "genre");
    private final static QName _Publisher_QNAME = new QName("http://www.loc.gov/mods/v3", "publisher");
    private final static QName _Topic_QNAME = new QName("http://www.loc.gov/mods/v3", "topic");
    private final static QName _RecordIdentifier_QNAME = new QName("http://www.loc.gov/mods/v3", "recordIdentifier");
    private final static QName _RecordContentSource_QNAME = new QName("http://www.loc.gov/mods/v3", "recordContentSource");
    private final static QName _Extent_QNAME = new QName("http://www.loc.gov/mods/v3", "extent");
    private final static QName _PlaceTerm_QNAME = new QName("http://www.loc.gov/mods/v3", "placeTerm");
    private final static QName _InternetMediaType_QNAME = new QName("http://www.loc.gov/mods/v3", "internetMediaType");
    private final static QName _AccessCondition_QNAME = new QName("http://www.loc.gov/mods/v3", "accessCondition");
    private final static QName _TypeOfResource_QNAME = new QName("http://www.loc.gov/mods/v3", "typeOfResource");
    private final static QName _DateOther_QNAME = new QName("http://www.loc.gov/mods/v3", "dateOther");
    private final static QName _NamePart_QNAME = new QName("http://www.loc.gov/mods/v3", "namePart");
    private final static QName _Frequency_QNAME = new QName("http://www.loc.gov/mods/v3", "frequency");

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
     * Create an instance of {@link TitleInfo }
     * 
     */
    public TitleInfo createTitleInfo() {
        return new TitleInfo();
    }

    /**
     * Create an instance of {@link Title }
     * 
     */
    public Title createTitle() {
        return new Title();
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
     * Create an instance of {@link PhysicalDescription }
     * 
     */
    public PhysicalDescription createPhysicalDescription() {
        return new PhysicalDescription();
    }

    /**
     * Create an instance of {@link Abstract }
     * 
     */
    public Abstract createAbstract() {
        return new Abstract();
    }

    /**
     * Create an instance of {@link Subject }
     * 
     */
    public Subject createSubject() {
        return new Subject();
    }

    /**
     * Create an instance of {@link Classification }
     * 
     */
    public Classification createClassification() {
        return new Classification();
    }

    /**
     * Create an instance of {@link RelatedItem }
     * 
     */
    public RelatedItem createRelatedItem() {
        return new RelatedItem();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link Url }
     * 
     */
    public Url createUrl() {
        return new Url();
    }

    /**
     * Create an instance of {@link Identifier }
     * 
     */
    public Identifier createIdentifier() {
        return new Identifier();
    }

    /**
     * Create an instance of {@link RecordInfo }
     * 
     */
    public RecordInfo createRecordInfo() {
        return new RecordInfo();
    }

    /**
     * Create an instance of {@link ModsCollection }
     * 
     */
    public ModsCollection createModsCollection() {
        return new ModsCollection();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "subTitle")
    public JAXBElement<String> createSubTitle(String value) {
        return new JAXBElement<String>(_SubTitle_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "recordContentSource")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRecordContentSource(String value) {
        return new JAXBElement<String>(_RecordContentSource_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "extent")
    public JAXBElement<String> createExtent(String value) {
        return new JAXBElement<String>(_Extent_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "placeTerm")
    public JAXBElement<String> createPlaceTerm(String value) {
        return new JAXBElement<String>(_PlaceTerm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "internetMediaType")
    public JAXBElement<String> createInternetMediaType(String value) {
        return new JAXBElement<String>(_InternetMediaType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "accessCondition")
    public JAXBElement<String> createAccessCondition(String value) {
        return new JAXBElement<String>(_AccessCondition_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "typeOfResource")
    public JAXBElement<String> createTypeOfResource(String value) {
        return new JAXBElement<String>(_TypeOfResource_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "dateOther")
    public JAXBElement<String> createDateOther(String value) {
        return new JAXBElement<String>(_DateOther_QNAME, String.class, null, value);
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
    @XmlElementDecl(namespace = "http://www.loc.gov/mods/v3", name = "frequency")
    public JAXBElement<String> createFrequency(String value) {
        return new JAXBElement<String>(_Frequency_QNAME, String.class, null, value);
    }

}
