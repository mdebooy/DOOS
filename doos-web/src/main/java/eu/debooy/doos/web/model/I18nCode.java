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
package eu.debooy.doos.web.model;

import eu.debooy.doos.domain.I18nCodeDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nCode implements Serializable, Comparable<I18nCode>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  private I18nCodeDto i18nCode;

  public I18nCode(I18nCodeDto i18nCode) {
    this.i18nCode = i18nCode;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    I18nCode param = (I18nCode) super.clone();
    param.i18nCode = (I18nCodeDto) this.i18nCode.clone();
    return param;
  }

  @Override
  public int compareTo(I18nCode other) {
    return new CompareToBuilder().append(i18nCode, other.i18nCode)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof I18nCode)) {
      return false;
    }
    I18nCode other  = (I18nCode) obj;
    return new EqualsBuilder().append(i18nCode, other.i18nCode).isEquals();
  }

  public I18nCodeDto getI18nCode() {
    return i18nCode;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(i18nCode).toHashCode();
  }

  public void setI18nCode(I18nCodeDto i18nCode) {
    this.i18nCode = i18nCode;
  }
}
