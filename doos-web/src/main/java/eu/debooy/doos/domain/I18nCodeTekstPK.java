/**
 * Copyright 2011 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Embeddable
public class I18nCodeTekstPK implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  @Column(name="CODE_ID", nullable=false)
	private Long   codeId;
	@Column(name="TAAL_KODE", length=2, nullable=false)
	private String taalKode;

  public I18nCodeTekstPK () {}

  public I18nCodeTekstPK (Long codeId, String taalKode) {
    this.codeId   = codeId;
    this.taalKode = taalKode;
  }
  
	/**
   * @return de codeId
   */
  public Long getCodeId() {
    return codeId;
  }

  /**
   * @return de taalKode
   */
  public String getTaalKode() {
    return taalKode;
  }

  /**
   * @param codeId de codeId
   */
  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }

  /**
   * @param taalKode de taalKode
   */
  public void setTaalKode(String taalKode) {
    this.taalKode = taalKode.toLowerCase();
  }

 @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeTekstPK)) {
      return false;
    }
    I18nCodeTekstPK  i18nCodeWaardePK  = (I18nCodeTekstPK) object;
    return new EqualsBuilder().append(codeId, i18nCodeWaardePK.getCodeId())
                              .append(taalKode, i18nCodeWaardePK.taalKode)
                              .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(taalKode).toHashCode();
  }

  @Override
  public String toString() {
    return new StringBuilder().append("I18nCodeTekstPK")
                              .append(" (codeId=").append(codeId)
                              .append(", taalKode=").append(taalKode)
                              .append(")").toString();
  }
}