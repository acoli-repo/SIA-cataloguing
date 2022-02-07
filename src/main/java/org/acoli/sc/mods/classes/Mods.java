//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.28 um 06:10:15 PM CEST 
//


package org.acoli.sc.mods.classes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}recordInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}abstract"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}titleInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}name" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}typeOfResource"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}genre"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}originInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}location" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}language" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}part"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}subject" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "recordInfo",
    "_abstract",
    "titleInfo",
    "name",
    "typeOfResource",
    "genre",
    "originInfo",
    "location",
    "language",
    "part",
    "subject"
})
@XmlRootElement(name = "mods")
public class Mods {

    @XmlElement(required = true)
    protected RecordInfo recordInfo;
    @XmlElement(name = "abstract", required = true)
    protected String _abstract;
    @XmlElement(required = true)
    protected TitleInfo titleInfo;
    @XmlElement(required = true)
    protected List<Name> name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String typeOfResource;
    @XmlElement(required = false)
    protected String genre;
    @XmlElement(required = true)
    protected OriginInfo originInfo;
    @XmlElement(required = true)
    protected List<Location> location;
    @XmlElement(required = false)
    protected List<Language> language;
    //@XmlElement(required = false)
    //protected List<LanguageTerm> languageTerm;
    @XmlElement(required = true)
    protected Part part;
    @XmlElement(required = false)
    protected List<Subject> subject;
    @XmlAttribute(name = "version", required = true)
    protected BigDecimal version;

    /**
     * Ruft den Wert der recordInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RecordInfo }
     *     
     */
    public RecordInfo getRecordInfo() {
        return recordInfo;
    }

    /**
     * Legt den Wert der recordInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordInfo }
     *     
     */
    public void setRecordInfo(RecordInfo value) {
        this.recordInfo = value;
    }

    /**
     * Ruft den Wert der abstract-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Legt den Wert der abstract-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstract(String value) {
        this._abstract = value;
    }

    /**
     * Ruft den Wert der titleInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TitleInfo }
     *     
     */
    public TitleInfo getTitleInfo() {
        return titleInfo;
    }

    /**
     * Legt den Wert der titleInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TitleInfo }
     *     
     */
    public void setTitleInfo(TitleInfo value) {
        this.titleInfo = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the name property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Name }
     * 
     * 
     */
    public List<Name> getName() {
        if (name == null) {
            name = new ArrayList<Name>();
        }
        return this.name;
    }

    /**
     * Ruft den Wert der typeOfResource-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfResource() {
        return typeOfResource;
    }

    /**
     * Legt den Wert der typeOfResource-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfResource(String value) {
        this.typeOfResource = value;
    }

    /**
     * Ruft den Wert der genre-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Legt den Wert der genre-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenre(String value) {
        this.genre = value;
    }

    /**
     * Ruft den Wert der originInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link OriginInfo }
     *     
     */
    public OriginInfo getOriginInfo() {
        return originInfo;
    }

    /**
     * Legt den Wert der originInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginInfo }
     *     
     */
    public void setOriginInfo(OriginInfo value) {
        this.originInfo = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Location }
     * 
     * 
     */
    public List<Location> getLocation() {
        if (location == null) {
            location = new ArrayList<Location>();
        }
        return this.location;
    }

    /**
     * Gets the value of the language property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the language property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Language }
     * 
     * 
     */
    public List<Language> getLanguage() {
        if (language == null) {
            language = new ArrayList<Language>();
        }
        return this.language;
    }
    
    
    /**
     * Gets the value of the languageTerm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the language property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguageTerm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LanguageTerm }
     * 
     * 
     */
//    public List<LanguageTerm> getLanguageTerm() {
//        if (languageTerm == null) {
//            languageTerm = new ArrayList<LanguageTerm>();
//        }
//        return this.languageTerm;
//    }


    /**
     * Ruft den Wert der part-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Part }
     *     
     */
    public Part getPart() {
        return part;
    }

    /**
     * Legt den Wert der part-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Part }
     *     
     */
    public void setPart(Part value) {
        this.part = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subject }
     * 
     * 
     */
    public List<Subject> getSubject() {
        if (subject == null) {
            subject = new ArrayList<Subject>();
        }
        return this.subject;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

}
