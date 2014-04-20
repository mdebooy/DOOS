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
package eu.debooy.doos.web.form;

import eu.debooy.doos.domain.LijstDto;

import java.io.Serializable;


/**
 * @author Marco de Booij
 */
public class LijstForm  implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  lijstnaam;
  private String  omschrijving;

  public LijstForm() {}

  public LijstForm(LijstDto lijst) {
    this.lijstnaam    = lijst.getLijstnaam();
    this.omschrijving = lijst.getOmschrijving();
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
    if (!gewijzigd) {
      return;
    }

    if (null == this.lijstnaam
        || !this.lijstnaam.equals(lijst.getLijstnaam())) {
      lijst.setLijstnaam(this.lijstnaam);
    }
    if (null == this.omschrijving
        || !this.omschrijving.equals(lijst.getOmschrijving())) {
      lijst.setOmschrijving(this.omschrijving);
    }
  }

  /**
   * @param lijstnaam de waarde van lijstnaam
   */
  public void setLijstnaam(String lijstnaam) {
    if (null == this.lijstnaam
        || !this.lijstnaam.equals(lijstnaam)) {
      gewijzigd       = true;
      this.lijstnaam  = lijstnaam;
    }
  }

  /**
   * @param omschrijving de waarde van omschrijving
   */
  public void setOmschrijving(String omschrijving) {
    if (null == this.omschrijving
        || !this.omschrijving.equals(omschrijving)) {
      gewijzigd         = true;
      this.omschrijving = omschrijving;
    }
  }
}
