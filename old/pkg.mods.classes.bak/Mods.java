//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.27 um 02:35:26 PM CEST 
//


package org.acoli.sc.mods.classes.bak;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.loc.gov/mods/v3}name" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}titleInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}originInfo"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}location" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}typeOfResource"/>
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
    "recordInfo",
    "name",
    "titleInfo",
    "originInfo",
    "location",
    "typeOfResource"
})
@XmlRootElement(name = "mods")
public class Mods {

    @XmlElement(required = true)
    protected RecordInfo recordInfo;
    protected List<Name> name;
    @XmlElement(required = true)
    protected TitleInfo titleInfo;
    @XmlElement(required = true)
    protected OriginInfo originInfo;
    @XmlElement(required = true)
    protected List<Location> location;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String typeOfResource;

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

}
