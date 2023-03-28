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

import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Parameter
    extends Formulier implements Comparable<Parameter> {
  private static final  long  serialVersionUID  = 1L;

  private String  sleutel;
  private String  waarde;

  public Parameter() {}

  public Parameter(ParameterDto parameter) {
    sleutel = parameter.getSleutel();
    waarde  = parameter.getWaarde();
  }

  public Parameter(String sleutel, String waarde) {
    this.sleutel  = sleutel;
    this.waarde   = waarde;
  }

  @Override
  public int compareTo(Parameter andere) {
    return new CompareToBuilder().append(sleutel, andere.sleutel)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Parameter)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    Parameter andere  = (Parameter) object;
    return new EqualsBuilder().append(sleutel, andere.sleutel).isEquals();
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

  public void persist(ParameterDto parameterDto) {
    parameterDto.setSleutel(sleutel);
    parameterDto.setWaarde(waarde);
  }

  public void setSleutel(String sleutel) {
    this.sleutel  = sleutel;
  }

  public void setWaarde(String waarde) {
    this.waarde = waarde;
  }
}
