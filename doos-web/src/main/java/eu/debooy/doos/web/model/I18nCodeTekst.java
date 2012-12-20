/**
 * Copyright 2012 Marco de Booij
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
package eu.debooy.doos.web.model;

import eu.debooy.doos.domain.I18nCodeTekstDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nCodeTekst implements Serializable, Comparable<I18nCodeTekst>,
                                      Cloneable {
  private static final  long  serialVersionUID  = 1L;

  private I18nCodeTekstDto  i18nCodeTekst;

  public I18nCodeTekst(I18nCodeTekstDto i18nCodeTekst) {
    this.i18nCodeTekst  = i18nCodeTekst;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    I18nCodeTekst param = (I18nCodeTekst) super.clone();
    param.i18nCodeTekst = (I18nCodeTekstDto) this.i18nCodeTekst.clone();
    return param;
  }

  @Override
  public int compareTo(I18nCodeTekst other) {
    return new CompareToBuilder().append(i18nCodeTekst, other.i18nCodeTekst)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof I18nCodeTekst)) {
      return false;
    }
    I18nCodeTekst other  = (I18nCodeTekst) obj;
    return new EqualsBuilder().append(i18nCodeTekst, other.i18nCodeTekst)
                              .isEquals();
  }

  public I18nCodeTekstDto getI18nCodeTekst() {
    return i18nCodeTekst;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(i18nCodeTekst).toHashCode();
  }

  public void setI18nCodeTekst(I18nCodeTekstDto i18nCodeTekst) {
    this.i18nCodeTekst  = i18nCodeTekst;
  }
}
