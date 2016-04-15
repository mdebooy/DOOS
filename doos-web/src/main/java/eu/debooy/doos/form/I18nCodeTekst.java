/**
 * Copyright 2014 Marco de Booij
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
package eu.debooy.doos.form;

import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doosutils.form.Formulier;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nCodeTekst
    extends Formulier implements Cloneable, Comparable<I18nCodeTekst> {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private Long    codeId;
  private String  taalKode;
  private String  tekst;

  public I18nCodeTekst() {}

  public I18nCodeTekst(I18nCodeTekstDto i18nCodetekst) {
    this.codeId   = i18nCodetekst.getCodeId();
    this.taalKode = i18nCodetekst.getTaalKode();
    this.tekst    = i18nCodetekst.getTekst();
  }

  public I18nCodeTekst(Long codeId, String taalKode, String tekst) {
    this.codeId   = codeId;
    this.taalKode = taalKode;
    this.tekst    = tekst;
  }

  public I18nCodeTekst clone() throws CloneNotSupportedException {
    I18nCodeTekst clone = (I18nCodeTekst) super.clone();

    return clone;
  }

  public int compareTo(I18nCodeTekst andere) {
    return new CompareToBuilder().append(codeId, andere.codeId)
                                 .append(taalKode, andere.taalKode)
                                 .toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeTekst)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    I18nCodeTekst andere  = (I18nCodeTekst) object;
    return new EqualsBuilder().append(codeId, andere.codeId)
                              .append(taalKode, andere.taalKode).isEquals();
  }

  public Long getCodeId() {
    return codeId;
  }

  public String getTaalKode() {
    return taalKode;
  }

  public String getTekst() {
    return tekst;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(taalKode).toHashCode();
  }

  /**
   * Is er iets gewijzigd?
   * 
   * @return
   */
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  /**
   * Zet de gegevens in de I18nCodeTekstDTO
   *
   * @param i18nCodeTekst
   */
  public void persist(I18nCodeTekstDto i18nCodeTekstDto) {
    if (null == this.codeId
        || !this.codeId.equals(i18nCodeTekstDto.getCodeId())) {
      i18nCodeTekstDto.setCodeId(this.codeId);
    }
    if (null == this.taalKode
        || !this.taalKode.equals(i18nCodeTekstDto.getTaalKode())) {
      i18nCodeTekstDto.setTaalKode(this.taalKode);
    }
    if (null == this.tekst
        || !this.tekst.equals(i18nCodeTekstDto.getTekst())) {
      i18nCodeTekstDto.setTekst(this.tekst);
    }
  }

  public void setCodeId(Long codeId) {
    if (null == this.codeId
        || !this.codeId.equals(codeId)) {
      gewijzigd   = true;
      this.codeId = codeId;
    }
  }

  public void setTaalKode(String taalKode) {
    if (null == this.taalKode
        || !this.taalKode.equals(taalKode)) {
      gewijzigd     = true;
      this.taalKode = taalKode;
    }
  }

  public void setTekst(String tekst) {
    if (null == this.tekst
        || !this.tekst.equals(tekst)) {
      gewijzigd   = true;
      this.tekst  = tekst;
    }
  }
}
