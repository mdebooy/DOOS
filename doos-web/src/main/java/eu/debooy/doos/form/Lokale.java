/*
 * Copyright (c) 2024 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */

package eu.debooy.doos.form;

import eu.debooy.doos.domain.LokaleDto;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Lokale extends Formulier implements Comparable<Lokale> {
  private static final  long  serialVersionUID  = 1L;

  private String  code;
  private String  eersteTaal;
  private String  tweedeTaal;

  public Lokale() {}

  public Lokale(LokaleDto lokale) {
    code        = lokale.getCode();
    eersteTaal  = lokale.getEersteTaal();
    tweedeTaal  = lokale.getTweedeTaal();
  }

  @Override
  public int compareTo(Lokale lokale) {
    return new CompareToBuilder().append(code, lokale.code)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Lokale)) {
      return false;
    }
    var lokale  = (Lokale) object;
    return new EqualsBuilder().append(code, lokale.code)
                              .isEquals();
  }

  public String getCode() {
    return code;
  }

  public String getEersteTaal() {
    return eersteTaal;
  }

  public String getTweedeTaal() {
    return tweedeTaal;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(code).toHashCode();
  }

  public void persist(LokaleDto lokaleDto) {
    lokaleDto.setCode(code);
    lokaleDto.setEersteTaal(eersteTaal);
    lokaleDto.setTweedeTaal(tweedeTaal);
  }

  public void setCode(String code) {
    this.code       = DoosUtils.strip(code);
  }

  public void setEersteTaal(String eersteTaal) {
    this.eersteTaal = DoosUtils.stripToLowerCase(eersteTaal);
  }

  public void setTweedeTaal(String tweedeTaal) {
    this.tweedeTaal = DoosUtils.stripToLowerCase(tweedeTaal);
  }
}
