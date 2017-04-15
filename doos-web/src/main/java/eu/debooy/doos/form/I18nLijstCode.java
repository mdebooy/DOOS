/**
 * Copyright 2017 Marco de Booij
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

import eu.debooy.doos.domain.I18nLijstCodeDto;
import eu.debooy.doosutils.form.Formulier;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nLijstCode
    extends Formulier implements Cloneable, Comparable<I18nLijstCode> {
  private static final  long  serialVersionUID  = 1L;

  private Long      codeId;
  private Long      lijstId;
  private Integer   volgorde;

  public I18nLijstCode() {}

  public I18nLijstCode(I18nLijstCodeDto i18nLijstCodeDto) {
    this.codeId   = i18nLijstCodeDto.getCodeId();
    this.lijstId  = i18nLijstCodeDto.getLijstId();
    this.volgorde = i18nLijstCodeDto.getVolgorde();
  }

  public I18nLijstCode(Long codeId, Long lijstId, Integer volgorde) {
    this.codeId   = codeId;
    this.lijstId  = lijstId;
    this.volgorde = volgorde;
  }

  public I18nLijstCode clone() throws CloneNotSupportedException {
    I18nLijstCode clone = (I18nLijstCode) super.clone();

    return clone;
  }

  public int compareTo(I18nLijstCode andere) {
    return new CompareToBuilder().append(codeId, andere.codeId)
                                 .append(lijstId, andere.lijstId)
                                 .toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof I18nLijstCode)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    I18nLijstCode andere  = (I18nLijstCode) object;
    return new EqualsBuilder().append(codeId, andere.codeId)
                              .append(lijstId, andere.lijstId).isEquals();
  }

  public Long getCodeId() {
    return codeId;
  }

  public Long getLijstId() {
    return lijstId;
  }

  public Integer getVolgorde() {
    return volgorde;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(lijstId).toHashCode();
  }

  /**
   * Zet de gegevens in de I18nLijstCodeDto
   *
   * @param I18nLijstCodeDto
   */
  public void persist(I18nLijstCodeDto i18nLijstCodeDto) {
    if (!new EqualsBuilder().append(codeId, i18nLijstCodeDto.getCodeId())
                            .isEquals()) {
      i18nLijstCodeDto.setCodeId(codeId);
    }
    if (!new EqualsBuilder().append(codeId, i18nLijstCodeDto.getLijstId())
                            .isEquals()) {
      i18nLijstCodeDto.setLijstId(lijstId);
    }
    if (!new EqualsBuilder().append(codeId, i18nLijstCodeDto.getVolgorde())
                            .isEquals()) {
      i18nLijstCodeDto.setVolgorde(volgorde);
    }
  }

  public void setCodeId(Long codeId) {
    if (!new EqualsBuilder().append(this.codeId, codeId).isEquals()) {
      gewijzigd   = true;
      this.codeId = codeId;
    }
  }

  public void setTaalKode(Long lijstId) {
    if (!new EqualsBuilder().append(this.lijstId, lijstId).isEquals()) {
      gewijzigd     = true;
      this.lijstId  = lijstId;
    }
  }

  public void setTekst(Integer volgorde) {
    if (!new EqualsBuilder().append(this.volgorde, volgorde).isEquals()) {
      gewijzigd     = true;
      this.volgorde = volgorde;
    }
  }
}
