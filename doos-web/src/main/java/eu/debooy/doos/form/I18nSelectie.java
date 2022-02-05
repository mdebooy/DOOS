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

import eu.debooy.doos.domain.I18nSelectieDto;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nSelectie
    extends Formulier implements Comparable<I18nSelectie> {
  private static final  long  serialVersionUID  = 1L;

  private String  code;
  private Long    codeId;
  private String  selectie;
  private Integer volgorde;
  private String  waarde;

  public I18nSelectie() {}

  public I18nSelectie(I18nSelectieDto i18nSelectieDto) {
    this.code     = i18nSelectieDto.getCode();
    this.codeId   = i18nSelectieDto.getCodeId();
    this.selectie = i18nSelectieDto.getSelectie();
    this.volgorde = i18nSelectieDto.getVolgorde();
  }

  @Override
  public int compareTo(I18nSelectie andere) {
    return new CompareToBuilder().append(codeId, andere.codeId)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeTekst)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    I18nSelectie andere  = (I18nSelectie) object;
    return new EqualsBuilder().append(codeId, andere.codeId).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getCodeId() {
    return codeId;
  }

  public String getSelectie() {
    return selectie;
  }

  public Integer getVolgorde() {
    return volgorde;
  }

  public String getWaarde() {
    return waarde;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).toHashCode();
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

  public void setSelectie(String selectie) {
    if (!new EqualsBuilder().append(this.selectie, selectie).isEquals()) {
      gewijzigd     = true;
      this.selectie = selectie;
    }
  }

  public void setVolgorde(Integer volgorde) {
    if (!new EqualsBuilder().append(this.volgorde, volgorde).isEquals()) {
      gewijzigd     = true;
      this.volgorde = volgorde;
    }
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }
}
