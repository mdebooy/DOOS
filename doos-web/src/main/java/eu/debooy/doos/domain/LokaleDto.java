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

package eu.debooy.doos.domain;

import eu.debooy.doosutils.domain.Dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="LOKALEN", schema="DOOS")
public class LokaleDto extends Dto implements Comparable<LokaleDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_CODE        = "code";
  public static final String  COL_EERSTE_TAAL = "eersteTaal";
  public static final String  COL_TWEEDE_TAAL = "tweedeTaal";

  @Id
  @Column(name="CODE", length=50, nullable=false)
  private String  code;
  @Column(name="EERSTE_TAAL", length=3, nullable=false)
  private String  eersteTaal;
  @Column(name="TWEEDE_TAAL", length=3)
  private String  tweedeTaal;

  @Override
  public int compareTo(LokaleDto lokaleDto) {
    return new CompareToBuilder().append(code, lokaleDto.code)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LokaleDto)) {
      return false;
    }
    var lokaleDto = (LokaleDto) object;
    return new EqualsBuilder().append(code, lokaleDto.code)
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

  public void setCode(String code) {
    this.code         = code;
  }

  public void setEersteTaal(String eersteTaal) {
    if (null == eersteTaal) {
      this.eersteTaal = null;
    } else {
      this.eersteTaal = eersteTaal.toLowerCase();
    }
  }

  public void setTweedeTaal(String tweedeTaal) {
    if (null == tweedeTaal) {
      this.tweedeTaal = null;
    } else {
      this.tweedeTaal = tweedeTaal.toLowerCase();
    }
  }
}
