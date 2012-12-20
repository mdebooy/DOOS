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

import eu.debooy.doos.domain.TaalDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Taal implements Serializable, Comparable<Taal>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  private TaalDto taal;

  public Taal(TaalDto taal) {
    this.taal = taal;
  }

  public TaalDto getTaal() {
    return taal;
  }

  public void setTaal(TaalDto taal) {
    this.taal = taal;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(taal).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Taal)) {
      return false;
    }
    Taal other  = (Taal) obj;
    return new EqualsBuilder().append(taal, other.taal).isEquals();
  }

  @Override
  public int compareTo(Taal other) {
    return new CompareToBuilder().append(taal, other.taal)
                                 .toComparison();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    Taal param = (Taal) super.clone();
    param.taal = (TaalDto) this.taal.clone();
    return param;
  }
}
