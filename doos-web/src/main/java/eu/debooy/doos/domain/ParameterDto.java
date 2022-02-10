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
package eu.debooy.doos.domain;

import eu.debooy.doosutils.domain.Dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="PARAMS", schema="DOOS")
public class ParameterDto extends Dto implements Comparable<ParameterDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_SLEUTEL = "sleutel";
  public static final String  COL_WAARDE  = "waarde";

  @Id
  @Column(name="SLEUTEL", length=100, nullable=false)
  private String  sleutel;
  @Column(name="WAARDE", length=255, nullable=false)
  private String  waarde;

  public ParameterDto() {}

  public ParameterDto(String sleutel) {
    this.sleutel  = sleutel;
  }

  public ParameterDto(String sleutel, String waarde) {
    this.sleutel    = sleutel;
    this.waarde     = waarde;
  }

  @Override
  public int compareTo(ParameterDto parameter) {
    return new CompareToBuilder().append(sleutel, parameter.sleutel)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ParameterDto)) {
      return false;
    }
    ParameterDto  parameter = (ParameterDto) object;
    return new EqualsBuilder().append(sleutel, parameter.sleutel).isEquals();
  }

  public String getSleutel() {
    return sleutel;
  }

  public String getWaarde() {
    return waarde;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(sleutel).toHashCode();
  }

  public void setSleutel(String sleutel) {
    this.sleutel  = sleutel.toLowerCase();
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }
}
