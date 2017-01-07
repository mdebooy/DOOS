/**
 * Copyright 2016 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class MailData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private Date      sentDate    = new Date();
  private Map<String, String>
                    bcc         = new HashMap<String, String>();
  private Map<String, String>
                    cc          = new HashMap<String, String>();
  private Map<String, String>
                    header      = new HashMap<String, String>();
  private Map<String, String>
                    to          = new HashMap<String, String>();
  private String    contentType = "text/html";
  private String    from        = ""; 
  private String    message     = "";
  private String    subject     = "";

  public MailData() {
  }

  public void addBcc(String bcc) {
    this.bcc.put(bcc, bcc);
  }

  public void addCc(String cc) {
    this.cc.put(cc, cc);
  }

  public void addHeader(String type, String value) {
    this.header.put(type, value);
  }

  public void addTo(String to) {
    this.to.put(to, to);
  }

  public Map<String, String> getBcc() {
    return bcc;
  }

  public int getBccSize() {
    return bcc.size();
  }

  public Map<String, String> getCc() {
    return cc;
  }

  public int getCcSize() {
    return cc.size();
  }

  public Map<String, String> getHeader() {
    return header;
  }

  public int getHeaderSize() {
    return header.size();
  }

  public String getContentType() {
    return contentType;
  }

  public String getFrom() {
    return from;
  }

  public String getMessage() {
    return message;
  }

  public Date getSentDate() {
    if (sentDate == null) {
      sentDate  = new Date();
    }

    return (Date) sentDate.clone();
  }

  public String getSubject() {
    return subject;
  }

  public Map<String, String> getTo() {
    return to;
  }

  public int getToSize() {
    return to.size();
  }

  public void setBcc(Map<String, String> bcc) {
    this.bcc = bcc;
  }

  public void setBcc(String bcc) {
    this.bcc.put(bcc, bcc);
  }

  public void setCc(Map<String, String> cc) {
    this.cc = cc;
  }

  public void setCc(String cc) {
    this.cc.put(cc, cc);
  }

  public void setHeader(Map<String, String> header) {
    this.header = header;
  }

  public void setHeader(String type, String value) {
    this.header.put(type, value);
  }

  public void setContentType(String contentType) {
    if (contentType == null
        || contentType.isEmpty()) {
      this.contentType = "text/html";
    }

    this.contentType = contentType;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setSentDate(Date sentDate) {
    if (null == sentDate) {
      this.sentDate = new Date();
    } else {
      this.sentDate = (Date) sentDate.clone();
    }
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setTo(Map<String, String> to) {
    this.to = to;
  }

  public void setTo(String to) {
    this.to.put(to, to);
  }

  public boolean equals(Object object) {
    if (null == object) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!(object instanceof MailData)) {
      return false;
    }

    MailData  mailData  = (MailData) object;
    return new EqualsBuilder().appendSuper(super.equals(object))
                              .append(bcc, mailData.bcc)
                              .append(cc, mailData.cc)
                              .append(contentType, mailData.contentType)
                              .append(from, mailData.from)
                              .append(header, mailData.header)
                              .append(message, mailData.message)
                              .append(sentDate, mailData.sentDate)
                              .append(subject, mailData.subject)
                              .append(to, mailData.to)
                              .isEquals();
  }

  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(bcc)
                                      .append(cc)
                                      .append(contentType)
                                      .append(from)
                                      .append(header)
                                      .append(message)
                                      .append(sentDate)
                                      .append(subject)
                                      .append(to)
                                      .toHashCode();
  }

  /**
   * Transfer the values of a HashTable into a String.
   * 
   * @param hashtable
   * @return
   */
  private String hashToString(Map<String, String> hashtable) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String string : hashtable.values()) {
      stringBuilder.append(", ").append(string);
    }

    return stringBuilder.toString().replaceFirst(", ", "");
  }

  public String toString() {
    StringBuilder string  = new StringBuilder();

    return string.append("{contentType=").append(getContentType())
                 .append(" from=").append(getFrom())
                 .append(" to=").append(hashToString(getTo()))
                 .append(" cc=").append(hashToString(getCc()))
                 .append(" bcc=").append(hashToString(getBcc()))
                 .append(" header=").append(hashToString(getHeader()))
                 .append(" sentDate=").append(getSentDate())
                 .append(" subject=").append(getSubject())
                 .append(" message=").append(getMessage())
                 .append('}')
                 .toString();
  }
}
