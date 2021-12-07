//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.27 um 02:35:26 PM CEST 
//


package org.acoli.sc.mods.classes.bak;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;choice>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}physicalLocation"/>
 *         &lt;element ref="{http://www.loc.gov/mods/v3}url"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "physicalLocation",
    "url"
})
@XmlRootElement(name = "location")
public class Location {

    protected PhysicalLocation physicalLocation;
    protected Url url;

    /**
     * Ruft den Wert der physicalLocation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalLocation }
     *     
     */
    public PhysicalLocation getPhysicalLocation() {
        return physicalLocation;
    }

    /**
     * Legt den Wert der physicalLocation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalLocation }
     *     
     */
    public void setPhysicalLocation(PhysicalLocation value) {
        this.physicalLocation = value;
    }

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Url }
     *     
     */
    public Url getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Url }
     *     
     */
    public void setUrl(Url value) {
        this.url = value;
    }

}
