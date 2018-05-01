/**
 * Copyright 2018 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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
package eu.debooy.doos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class ChartElement implements Comparable<ChartElement>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private String  categorie;
  private Number  number;
  private String  string;

  public ChartElement(Object[] rij) {
    string  = (String) rij[0];
    number  = (Number) rij[1];
    if (rij.length == 3) {
      
    } else {
      categorie = "";
    }
  }

  public ChartElement(String string, Number number) {
    categorie   = "";
    this.number = number;
    this.string = string;
  }

  public ChartElement(String string, Number number, String categorie) {
    this.categorie  = categorie;
    this.number     = number;
    this.string     = string;
  }

  public int compareTo(ChartElement stringNumber) {
    return new CompareToBuilder().append(string, stringNumber.string)
                                 .toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof ChartElement)) {
      return false;
    }

    ChartElement  andere = (ChartElement) object;
    return new EqualsBuilder().append(string, andere.string).isEquals();
  }

  public String getCategorie() {
    return categorie;
  }

  public Number getNumber() {
    return number;
  }

  public String getString() {
    return string;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(string).toHashCode();
  }

  public void setCategorie(String categorie) {
    if (null == categorie) {
      this.categorie  = "";
    } else {
      this.categorie  = categorie;
    }
  }

  public void setNumber(Number number) {
    this.number = number;
  }

  public void setString(String string) {
    this.string = string;
  }

  public String toString() {
    return "ChartElement (string=" + string + ", number=" + number
            + ", categorie=" + categorie + ")";
  }
}
