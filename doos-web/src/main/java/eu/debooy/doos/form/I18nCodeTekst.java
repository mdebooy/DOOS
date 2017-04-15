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

  private Long    codeId;
  private String  taalKode;
  private String  tekst;

  public I18nCodeTekst() {}

  public I18nCodeTekst(I18nCodeTekstDto i18nCodetekstDto) {
    this.codeId   = i18nCodetekstDto.getCodeId();
    this.taalKode = i18nCodetekstDto.getTaalKode();
    this.tekst    = i18nCodetekstDto.getTekst();
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

  public void persist(I18nCodeTekstDto i18nCodeTekstDto) {
    if (!new EqualsBuilder().append(codeId, i18nCodeTekstDto.getCodeId())
                            .isEquals()) {
      i18nCodeTekstDto.setCodeId(codeId);
    }
    if (!new EqualsBuilder().append(taalKode, i18nCodeTekstDto.getTaalKode())
                            .isEquals()) {
      i18nCodeTekstDto.setTaalKode(taalKode);
    }
    if (!new EqualsBuilder().append(tekst, i18nCodeTekstDto.getTekst())
                            .isEquals()) {
      i18nCodeTekstDto.setTekst(tekst);
    }
  }

  public void setCodeId(Long codeId) {
    if (!new EqualsBuilder().append(this.codeId, codeId).isEquals()) {
      gewijzigd   = true;
      this.codeId = codeId;
    }
  }

  public void setTaalKode(String taalKode) {
    if (!new EqualsBuilder().append(this.taalKode, taalKode).isEquals()) {
      gewijzigd     = true;
      this.taalKode = taalKode;
    }
  }

  public void setTekst(String tekst) {
    if (!new EqualsBuilder().append(this.tekst, tekst).isEquals()) {
      gewijzigd   = true;
      this.tekst  = tekst;
    }
  }
}
