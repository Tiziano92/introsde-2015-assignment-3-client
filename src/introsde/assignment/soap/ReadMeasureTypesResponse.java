
package introsde.assignment.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for readMeasureTypesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readMeasureTypesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="measureType" type="{http://soap.assignment.introsde/}customMeasureDefinition" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readMeasureTypesResponse", propOrder = {
    "measureType"
})
public class ReadMeasureTypesResponse {

    protected CustomMeasureDefinition measureType;

    /**
     * Gets the value of the measureType property.
     * 
     * @return
     *     possible object is
     *     {@link CustomMeasureDefinition }
     *     
     */
    public CustomMeasureDefinition getMeasureType() {
        return measureType;
    }

    /**
     * Sets the value of the measureType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomMeasureDefinition }
     *     
     */
    public void setMeasureType(CustomMeasureDefinition value) {
        this.measureType = value;
    }

}
