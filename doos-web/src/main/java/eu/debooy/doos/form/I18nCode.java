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

import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doosutils.form.Formulier;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nCode
    extends Formulier implements Cloneable, Comparable<I18nCode> {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  code;
  private Long    codeId;
  private Long    teksten;

  public I18nCode() {}

  public I18nCode(I18nCodeDto i18nCode) {
    this.code     = i18nCode.getCode();
    this.codeId   = i18nCode.getCodeId();
    this.teksten  = Long.valueOf(i18nCode.getTeksten().size());
  }

  public I18nCode(String code) {
    this.code   = code;
  }

  public I18nCode clone() throws CloneNotSupportedException {
    I18nCode  clone = (I18nCode) super.clone();

    return clone;
  }

  public int compareTo(I18nCode andere) {
    return new CompareToBuilder().append(code, andere.code).toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof I18nCode)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    I18nCode  andere  = (I18nCode) object;
    return new EqualsBuilder().append(code, andere.code).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getCodeId() {
    return codeId;
  }

  public Long getTeksten() {
    return teksten;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(code).toHashCode();
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
   * Zet de gegevens in de I18nCodeDto
   *
   * @param I18nCodeDto i18nCode
   */
  public void persist(I18nCodeDto i18nCode) {
    if (!new EqualsBuilder().append(code, i18nCode.getCode()).isEquals()) {
      i18nCode.setCode(this.code);
    }
    if (!new EqualsBuilder().append(codeId, i18nCode.getCodeId()).isEquals()) {
      i18nCode.setCodeId(this.codeId);
    }
  }

  public void setCode(String code) {
    if (!new EqualsBuilder().append(this.code, code).isEquals()) {
      gewijzigd = true;
      this.code = code;
    }
  }

  public void setCodeId(Long codeId) {
    if (!new EqualsBuilder().append(this.codeId, codeId).isEquals()) {
      gewijzigd   = true;
      this.codeId = codeId;
    }
  }
}
