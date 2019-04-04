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
public class Taal extends Formulier implements Comparable<Taal> {
  private static final  long  serialVersionUID  = 1L;

  private String  eigennaam;
  private String  taal;
  private String  taalKode;

  public Taal() {}

  public Taal(TaalDto taalDto) {
    eigennaam = taalDto.getEigennaam();
    taal      = taalDto.getTaal();
    taalKode  = taalDto.getTaalKode();
  }

  public static class EigennaamComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    public int compare(Taal taal1, Taal taal2) {
      return taal1.eigennaam.compareTo(taal2.eigennaam);
    }
  }

  public static class TaalComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    public int compare(Taal taal1, Taal taal2) {
      return taal1.taal.compareTo(taal2.taal);
    }
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

  public String getEigennaam() {
    return eigennaam;
  }

  public String getTaal() {
    return taal;
  }

  public String getTaalKode() {
    return taalKode;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(taalKode).toHashCode();
  }

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

  public void setEigennaam(String eigennaam) {
    if (!new EqualsBuilder().append(this.eigennaam, eigennaam).isEquals()) {
      gewijzigd       = true;
      this.eigennaam  = eigennaam;
    }
  }

  public void setTaal(String taal) {
    if (!new EqualsBuilder().append(this.taal, taal).isEquals()) {
      gewijzigd = true;
      this.taal = taal;
    }
  }

  public void setTaalKode(String taalKode) {
    if (!new EqualsBuilder().append(this.taalKode, taalKode).isEquals()) {
      gewijzigd     = true;
      this.taalKode = taalKode;
    }
  }
}
