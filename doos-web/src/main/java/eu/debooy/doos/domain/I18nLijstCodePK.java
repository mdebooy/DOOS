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
package eu.debooy.doos.domain;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nLijstCodePK implements Serializable {
  private static final  long  serialVersionUID  = 1L;

	private Long  codeId;
	private Long  lijstId;

  public I18nLijstCodePK() {}

  public I18nLijstCodePK(Long codeId, Long lijstId) {
    this.codeId   = codeId;
    this.lijstId  = lijstId;
  }

  @Override
   public boolean equals(Object object) {
     if (!(object instanceof I18nLijstCodePK)) {
       return false;
     }
     I18nLijstCodePK  i18nCodeWaardePK  = (I18nLijstCodePK) object;
     return new EqualsBuilder().append(codeId, i18nCodeWaardePK.codeId)
                               .append(lijstId, i18nCodeWaardePK.lijstId)
                               .isEquals();
   }

  public Long getCodeId() {
    return codeId;
  }

  public Long getLijstId() {
    return lijstId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(lijstId).toHashCode();
  }

  public void setCodeId(Long codeId) {
    this.codeId   = codeId;
  }

  public void setLijstId(Long lijstId) {
    this.lijstId  = lijstId;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("I18nLijstCodePK")
                              .append(" (codeId=").append(codeId)
                              .append(", lijstId=").append(lijstId)
                              .append(")").toString();
  }
}