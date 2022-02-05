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

import eu.debooy.doos.domain.I18nLijstDto;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nLijst extends Formulier implements Comparable<I18nLijst> {
  private static final  long  serialVersionUID  = 1L;

  private String  code;
  private Long    lijstId;
  private String  omschrijving;

  public I18nLijst() {}

  public I18nLijst(I18nLijstDto i18nLijstDto) {
    this.code         = i18nLijstDto.getCode();
    this.lijstId      = i18nLijstDto.getLijstId();
    this.omschrijving = i18nLijstDto.getOmschrijving();
  }

  public I18nLijst(String code) {
    this.code   = code;
  }

  @Override
  public int compareTo(I18nLijst andere) {
    return new CompareToBuilder().append(lijstId, andere.lijstId)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nLijst)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    I18nLijst  andere  = (I18nLijst) object;
    return new EqualsBuilder().append(lijstId, andere.lijstId).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getLijstId() {
    return lijstId;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(lijstId).toHashCode();
  }

  public void persist(I18nLijstDto i18nLijstDto) {
    if (!new EqualsBuilder().append(code, i18nLijstDto.getCode()).isEquals()) {
      i18nLijstDto.setCode(this.code);
    }
    if (!new EqualsBuilder().append(lijstId, i18nLijstDto.getLijstId())
                            .isEquals()) {
      i18nLijstDto.setLijstId(this.lijstId);
    }
    if (!new EqualsBuilder().append(omschrijving,
                                    i18nLijstDto.getOmschrijving())
                            .isEquals()) {
      i18nLijstDto.setOmschrijving(this.omschrijving);
    }
  }

  public void setCode(String code) {
    if (!new EqualsBuilder().append(this.code, code).isEquals()) {
      gewijzigd = true;
      this.code = code;
    }
  }

  public void setLijstId(Long lijstId) {
    if (!new EqualsBuilder().append(this.lijstId, lijstId).isEquals()) {
      gewijzigd     = true;
      this.lijstId  = lijstId;
    }
  }

  public void setOmschrijving(String omschrijving) {
    if (!new EqualsBuilder().append(this.omschrijving, omschrijving)
                            .isEquals()) {
      gewijzigd         = true;
      this.omschrijving = omschrijving;
    }
  }
}
