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
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="LIJSTEN", schema="DOOS")
public class LijstDto extends Dto implements Comparable<LijstDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Lob
  @Column(name="JASPER_REPORT", nullable=false)
  private byte[]  jasperReport;
  @Column(name="LIJST", length=64000, nullable=false)
  private String  lijst;
  @Id
  @Column(name="LIJSTNAAM", length=25, nullable=false, unique=true)
  private String  lijstnaam;
  @Column(name="OMSCHRIJVING", length=100, nullable=false)
  private String  omschrijving;

  // constructors
  public LijstDto() {
  }

  /**
   * Constructor met verplichte velden.
   */
  public LijstDto(String lijstnaam, String omschrijving, String lijst,
                  byte[] jasperReport) {
    this.jasperReport = jasperReport;
    this.lijst        = lijst;
    this.lijstnaam    = lijstnaam;
    this.omschrijving = omschrijving;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    LijstDto  clone = (LijstDto) super.clone();

    return clone;
  }

  public int compareTo(LijstDto lijst) {
    return new CompareToBuilder().append(lijstnaam, lijst.lijstnaam)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LijstDto)) {
      return false;
    }
    LijstDto  lijst = (LijstDto) object;
    return new EqualsBuilder().append(lijstnaam, lijst.lijstnaam).isEquals();
  }

  /**
   * @return the jasperReport
   */
  public byte[] getJasperReport() {
    return jasperReport;
  }

  /**
   * @return de lijst
   */
  public String getLijst() {
    return lijst;
  }

  /**
   * @return de lijstnaam
   */
  public String getLijstnaam() {
    return lijstnaam;
  }

  /**
   * @return the omschrijving
   */
  public String getOmschrijving() {
    return omschrijving;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(lijstnaam).toHashCode();
  }

  /**
   * @param jasperReport the jasperReport to set
   */
  public void setJasperReport(byte[] jasperReport) {
    this.jasperReport = jasperReport;
  }

  /**
   * @param lijst de lijst
   */
  public void setLijst(String lijst) {
    this.lijst  = lijst;
  }

  /**
   * @param lijstnaam de lijstnaam
   */
  public void setLijstnaam(String lijstnaam) {
    this.lijstnaam  = lijstnaam;
  }

  /**
   * @param omschrijving the omschrijving to set
   */
  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }
}
