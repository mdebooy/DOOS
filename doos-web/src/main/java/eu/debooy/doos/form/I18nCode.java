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
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nCode extends Formulier implements Comparable<I18nCode> {
  private static final  long  serialVersionUID  = 1L;

  private String  code;
  private Long    codeId;
  private Long    teksten;

  public I18nCode() {}

  public I18nCode(I18nCodeDto i18nCodeDto) {
    this.code     = i18nCodeDto.getCode();
    this.codeId   = i18nCodeDto.getCodeId();
    this.teksten  = Long.valueOf(i18nCodeDto.getTeksten().size());
  }

  public I18nCode(String code) {
    this.code   = code;
  }

  @Override
  public int compareTo(I18nCode andere) {
    return new CompareToBuilder().append(code, andere.code).toComparison();
  }

  @Override
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(code).toHashCode();
  }

  public void persist(I18nCodeDto i18nCodeDto) {
    i18nCodeDto.setCode(this.code);
    i18nCodeDto.setCodeId(this.codeId);
  }

  public void setCode(String code) {
    this.code   = DoosUtils.strip(code);
  }

  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }
}
