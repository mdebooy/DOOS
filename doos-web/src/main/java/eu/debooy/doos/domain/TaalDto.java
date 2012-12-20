/**
 * Copyright 2009 Marco de Booij
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
@Table(name="TALEN", schema="DOOS")
public class TaalDto extends Dto implements Comparable<TaalDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Column(name="EIGENNAAM", length=100, nullable=false)
  private String  eigennaam;

  @Column(name="TAAL", length=100, nullable=false)
  private String  taal;

  @Id
  @Column(name="TAAL_KODE", length=2, nullable=false)
  private String  taalKode;

  // constructors
  public TaalDto() {
  }

  /**
   * Constructor for primary key
   */
  public TaalDto(String taalKode) {
    this.taalKode  = taalKode.toLowerCase();
  }

  /**
   * Constructor for required fields
   */
  public TaalDto(String taalKode, String eigennaam, String taal) {
    this.eigennaam  = eigennaam;
    this.taalKode   = taalKode.toLowerCase();
    this.taal       = taal;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    TaalDto  clone = (TaalDto) super.clone();

    return clone;
  }

  @Override
  public int compareTo(TaalDto taal) {
    return new CompareToBuilder().append(taalKode, taal.taalKode)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TaalDto)) {
      return false;
    }
    TaalDto  andere = (TaalDto) object;
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(taalKode).toHashCode();
  }

  /**
   * @param eigennaam de eigennaam
   */
  public void setEigennaam(String eigennaam) {
    this.eigennaam = eigennaam;
  }

  /**
   * @param taal de taal
   */
  public void setTaal(String taal) {
    this.taal = taal;
  }

  /**
   * @param taalKode de taalKode
   */
  public void setTaalKode(String taalKode) {
    this.taalKode = taalKode.toLowerCase();
  }
}
