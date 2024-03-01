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

import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.domain.Dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="LIJSTEN", schema="DOOS")
public class LijstDto extends Dto implements Comparable<LijstDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_LIJST         = "lijst";
  public static final String  COL_LIJSTNAAM     = "lijstnaam";
  public static final String  COL_OMSCHRIJVING  = "omschrijving";

  @Column(name="LIJST", length=64000, nullable=false)
  private String  lijst;
  @Id
  @Column(name="LIJSTNAAM", length=25, nullable=false, unique=true)
  private String  lijstnaam;
  @Column(name="OMSCHRIJVING", length=100, nullable=false)
  private String  omschrijving;

  @Override
  public int compareTo(LijstDto lijst) {
    return new CompareToBuilder().append(lijstnaam, lijst.lijstnaam)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LijstDto)) {
      return false;
    }
    LijstDto  dto = (LijstDto) object;
    return new EqualsBuilder().append(lijstnaam, dto.lijstnaam).isEquals();
  }

  public String getLijst() {
    return lijst;
  }

  public String getLijstnaam() {
    return lijstnaam;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(lijstnaam).toHashCode();
  }

  public void setLijst(String lijst) {
    this.lijst        = lijst;
  }

  public void setLijstnaam(String lijstnaam) {
    this.lijstnaam  = DoosUtils.stripToLowercase(lijstnaam);
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = DoosUtils.strip(omschrijving);
  }
}
