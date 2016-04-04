/**
 * Copyright 2016 Marco de Booij
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
package eu.debooy.doos.form;

import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doosutils.form.Formulier;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Taal extends Formulier implements Cloneable, Comparable<Taal> {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  eigennaam;
  private String  taal;
  private String  taalKode;

  public Taal() {}

  public Taal(TaalDto taalDto) {
    eigennaam = taalDto.getEigennaam();
    taal      = taalDto.getTaal();
    taalKode  = taalDto.getTaalKode();
  }

  /**
   * Sorteren op de eigennaam van het taal.
   */
  public static class EigennaamComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Taal taal1, Taal taal2) {
      return taal1.eigennaam.compareTo(taal2.eigennaam);
    }
  }

  /**
   * Sorteren op de naam van het taal.
   */
  public static class TaalComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Taal taal1, Taal taal2) {
      return taal1.taal.compareTo(taal2.taal);
    }
  }

  public Taal clone() throws CloneNotSupportedException {
    Taal clone = (Taal) super.clone();

    return clone;
  }

  public int compareTo(Taal andere) {
    return new CompareToBuilder().append(taalKode, andere.taalKode)
                                 .toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof Taal)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    Taal  andere  = (Taal) object;
    return new EqualsBuilder().append(taalKode, andere.taalKode).isEquals();
  }

  /**
   * @return de eigennaam
   */
  public String getEigennaam() {
    return eigennaam;
  }

  /**
   * @return de taal
   */
  public String getTaal() {
    return taal;
  }

  /**
   * @return de taalKode
   */
  public String getTaalKode() {
    return taalKode;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(taalKode).toHashCode();
  }

  /**
   * @return de gewijzigd
   */
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  /**
   * Zet de gegevens in een TaalDto
   *
   * @param TaalDto
   */
  public void persist(TaalDto parameter) {
    if (!new EqualsBuilder().append(eigennaam,
                                    parameter.getEigennaam()).isEquals()) {
      parameter.setEigennaam(eigennaam);
    }
    if (!new EqualsBuilder().append(taal,
                                    parameter.getTaal()).isEquals()) {
      parameter.setTaal(this.taal);
    }
    if (!new EqualsBuilder().append(taalKode,
                                    parameter.getTaalKode()).isEquals()) {
      parameter.setTaalKode(taalKode);
    }
  }

  /**
   * @param eigennaam de waarde van eigennaam
   */
  public void setEigennaam(String eigennaam) {
    if (!new EqualsBuilder().append(this.eigennaam, eigennaam).isEquals()) {
      gewijzigd       = true;
      this.eigennaam  = eigennaam;
    }
  }

  /**
   * @param taal de waarde van taal
   */
  public void setTaal(String taal) {
    if (!new EqualsBuilder().append(this.taal, taal).isEquals()) {
      gewijzigd = true;
      this.taal = taal;
    }
  }

  /**
   * @param taalKode de waarde van taalKode
   */
  public void setTaalKode(String taalKode) {
    if (!new EqualsBuilder().append(this.taalKode, taalKode).isEquals()) {
      gewijzigd     = true;
      this.taalKode = taalKode;
    }
  }
}
