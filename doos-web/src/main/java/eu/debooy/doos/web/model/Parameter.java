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

import eu.debooy.doos.domain.ParameterDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Parameter implements Serializable, Comparable<Parameter>,
                                  Cloneable {
  private static final  long  serialVersionUID  = 1L;

  private ParameterDto  parameter;

  public Parameter(ParameterDto parameter) {
    this.parameter  = parameter;
  }

  public ParameterDto getParameter() {
    return parameter;
  }

  public void setParameter(ParameterDto parameter) {
    this.parameter  = parameter;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(parameter).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Parameter)) {
      return false;
    }
    Parameter other  = (Parameter) obj;
    return new EqualsBuilder().append(parameter, other.parameter).isEquals();
  }

  @Override
  public int compareTo(Parameter other) {
    return new CompareToBuilder().append(parameter, other.parameter)
                                 .toComparison();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    Parameter param = (Parameter) super.clone();
    param.parameter = (ParameterDto) this.parameter.clone();
    return param;
  }
}
