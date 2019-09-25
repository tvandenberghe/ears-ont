package be.naturalsciences.bmdc.ears.ontology.rest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(namespace="http://www.eurofleets.eu/", name="message")
public class MessageBean
  implements Serializable, IResponseMessage
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String code;
  private String description;
  private int status;
  
  public MessageBean() {}
  
  public MessageBean(String code, int status, String description)
  {
    this.code = code;
    this.description = description;
    this.status = status;
  }
  




  @XmlElement(namespace="http://www.eurofleets.eu/", name="code")
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  




  @XmlElement(namespace="http://www.eurofleets.eu/", name="description")
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getSummary()
  {
    return getDescription() + ": identifier " + getCode();
  }
  
  public int getStatus() {
    return status;
  }
  
  public void setStatus(int status) {
    this.status = status;
  }
  
  public boolean isBad()
  {
    return false;
  }
}