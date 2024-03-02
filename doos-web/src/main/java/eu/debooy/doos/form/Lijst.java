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

import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.form.Formulier;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Lijst extends Formulier implements Comparable<Lijst> {
  private static final  long  serialVersionUID  = 1L;

  private String  lijstnaam;
  private String  omschrijving;

  public Lijst() {}

  public Lijst(LijstDto lijstDto) {
    lijstnaam     = lijstDto.getLijstnaam();
    omschrijving  = lijstDto.getOmschrijving();
  }

  @Override
  public int compareTo(Lijst andere) {
    return new CompareToBuilder().append(lijstnaam, andere.lijstnaam)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Lijst)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    Lijst andere  = (Lijst) object;
    return new EqualsBuilder().append(lijstnaam, andere.lijstnaam).isEquals();
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

  public void persist(LijstDto lijstDto) {
    lijstDto.setLijstnaam(getLijstnaam());
    lijstDto.setOmschrijving(getOmschrijving());
  }

  public void setLijstnaam(String lijstnaam) {
    this.lijstnaam  = DoosUtils.stripToLowerCase(lijstnaam);
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = DoosUtils.strip(omschrijving);
  }
}
