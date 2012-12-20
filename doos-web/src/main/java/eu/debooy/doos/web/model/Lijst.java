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

import eu.debooy.doos.domain.LijstDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Lijst implements Serializable, Comparable<Lijst>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  private LijstDto lijst;

  public Lijst(LijstDto lijst) {
    this.lijst = lijst;
  }

  public LijstDto getLijst() {
    return lijst;
  }

  public void setLijst(LijstDto lijst) {
    this.lijst = lijst;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(lijst).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Lijst)) {
      return false;
    }
    Lijst other  = (Lijst) obj;
    return new EqualsBuilder().append(lijst, other.lijst).isEquals();
  }

  @Override
  public int compareTo(Lijst other) {
    return new CompareToBuilder().append(lijst, other.lijst)
                                 .toComparison();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    Lijst param = (Lijst) super.clone();
    param.lijst = (LijstDto) this.lijst.clone();
    return param;
  }
}
