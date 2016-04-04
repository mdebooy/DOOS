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
import eu.debooy.doosutils.form.Formulier;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Lijst extends Formulier implements Cloneable, Comparable<Lijst> {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  lijstnaam;
  private String  omschrijving;

  public Lijst() {}

  public Lijst(LijstDto lijst) {
    this.lijstnaam    = lijst.getLijstnaam();
    this.omschrijving = lijst.getOmschrijving();
  }

  public Lijst clone() throws CloneNotSupportedException {
    Lijst clone = (Lijst) super.clone();

    return clone;
  }

  public int compareTo(Lijst andere) {
    return new CompareToBuilder().append(lijstnaam, andere.lijstnaam)
                                 .toComparison();
  }

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

  /**
   * @return de lijstnaam
   */
  public String getLijstnaam() {
    return lijstnaam;
  }

  /**
   * @return de omschrijving
   */
  public String getOmschrijving() {
    return omschrijving;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(lijstnaam).toHashCode();
  }

  /**
   * Is er iets gewijzigd?
   * 
   * @return
   */
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  /**
   * Zet de gegevens in de LijstDTO
   *
   * @param i18nCodeTekst
   */
  public void persist(LijstDto lijst) {
    if (!new EqualsBuilder().append(lijstnaam,
                                    lijst.getLijstnaam()).isEquals()) {
      lijst.setLijstnaam(this.lijstnaam);
    }
    if (!new EqualsBuilder().append(omschrijving,
                                    lijst.getOmschrijving()).isEquals()) {
      lijst.setOmschrijving(this.omschrijving);
    }
  }

  /**
   * @param lijstnaam de waarde van lijstnaam
   */
  public void setLijstnaam(String lijstnaam) {
    if (!new EqualsBuilder().append(this.lijstnaam, lijstnaam).isEquals()) {
      gewijzigd       = true;
      this.lijstnaam  = lijstnaam;
    }
  }

  /**
   * @param omschrijving de waarde van omschrijving
   */
  public void setOmschrijving(String omschrijving) {
    if (!new EqualsBuilder().append(this.omschrijving, omschrijving)
                            .isEquals()) {
      gewijzigd         = true;
      this.omschrijving = omschrijving;
    }
  }
}
