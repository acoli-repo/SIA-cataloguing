//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.08.05 um 02:45:39 PM CEST 
//


package org.acoli.sc.mods.classes.old;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://www.loc.gov/mods/v3}titleInfo" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}name" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}typeOfResource"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}genre"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}originInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}language" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}physicalDescription"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}abstract"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}subject" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}classification"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}relatedItem" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}identifier"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}location" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}accessCondition"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}recordInfo"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "titleInfo",
    "name",
    "typeOfResource",
    "genre",
    "originInfo",
    "language",
    "physicalDescription",
    "_abstract",
    "subject",
    "classification",
    "relatedItem",
    "identifier",
    "location",
    "accessCondition",
    "recordInfo"
})
@XmlRootElement(name = "mods")
public class Mods {

    @XmlElement(required = true)
    protected List<TitleInfo> titleInfo;
    @XmlElement(required = true)
    protected List<Name> name;
    @XmlElement(required = true)
    protected String typeOfResource;
    @XmlElement(required = true)
    protected String genre;
    @XmlElement(required = true)
    protected OriginInfo originInfo;
    @XmlElement(required = true)
    protected List<Language> language;
    @XmlElement(required = true)
    protected PhysicalDescription physicalDescription;
    @XmlElement(name = "abstract", required = true)
    protected Abstract _abstract;
    @XmlElement(required = true)
    protected List<Subject> subject;
    @XmlElement(required = true)
    protected Classification classification;
    @XmlElement(required = true)
    protected List<RelatedItem> relatedItem;
    @XmlElement(required = true)
    protected Identifier identifier;
    @XmlElement(required = true)
    protected List<Location> location;
    @XmlElement(required = true)
    protected String accessCondition;
    @XmlElement(required = true)
    protected RecordInfo recordInfo;

    /**
     * Gets the value of the titleInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the titleInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTitleInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TitleInfo }
     * 
     * 
     */
    public List<TitleInfo> getTitleInfo() {
        if (titleInfo == null) {
            titleInfo = new ArrayList<TitleInfo>();
        }
        return this.titleInfo;
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
     * Ruft den Wert der physicalDescription-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalDescription }
     *     
     */
    public PhysicalDescription getPhysicalDescription() {
        return physicalDescription;
    }

    /**
     * Legt den Wert der physicalDescription-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalDescription }
     *     
     */
    public void setPhysicalDescription(PhysicalDescription value) {
        this.physicalDescription = value;
    }

    /**
     * Ruft den Wert der abstract-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Abstract }
     *     
     */
    public Abstract getAbstract() {
        return _abstract;
    }

    /**
     * Legt den Wert der abstract-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Abstract }
     *     
     */
    public void setAbstract(Abstract value) {
        this._abstract = value;
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
     * Ruft den Wert der classification-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Classification }
     *     
     */
    public Classification getClassification() {
        return classification;
    }

    /**
     * Legt den Wert der classification-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Classification }
     *     
     */
    public void setClassification(Classification value) {
        this.classification = value;
    }

    /**
     * Gets the value of the relatedItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelatedItem }
     * 
     * 
     */
    public List<RelatedItem> getRelatedItem() {
        if (relatedItem == null) {
            relatedItem = new ArrayList<RelatedItem>();
        }
        return this.relatedItem;
    }

    /**
     * Ruft den Wert der identifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Legt den Wert der identifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setIdentifier(Identifier value) {
        this.identifier = value;
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
     * Ruft den Wert der accessCondition-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessCondition() {
        return accessCondition;
    }

    /**
     * Legt den Wert der accessCondition-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessCondition(String value) {
        this.accessCondition = value;
    }

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

}
