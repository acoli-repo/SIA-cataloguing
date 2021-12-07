//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2021.09.28 um 06:10:15 PM CEST 
//


package org.acoli.sc.mods.classes;

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
 *         &lt;element ref="{http://www.loc.gov/mods/v3}roleTerm"/>
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
    "roleTerm"
})
@XmlRootElement(name = "role")
public class Role {

    @XmlElement(required = true)
    protected RoleTerm roleTerm;

    /**
     * Ruft den Wert der roleTerm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RoleTerm }
     *     
     */
    public RoleTerm getRoleTerm() {
        return roleTerm;
    }

    /**
     * Legt den Wert der roleTerm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleTerm }
     *     
     */
    public void setRoleTerm(RoleTerm value) {
        this.roleTerm = value;
    }

}
