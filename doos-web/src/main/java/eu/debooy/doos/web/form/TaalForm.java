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

import eu.debooy.doos.domain.TaalDto;

import java.io.Serializable;


/**
 * @author Marco de Booij
 */
public class TaalForm implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  eigennaam;
  private String  taal;
  private String  taalKode;

  public TaalForm() {}

  public TaalForm(TaalDto taal) {
    this.eigennaam  = taal.getEigennaam();
    this.taal       = taal.getTaal();
    this.taalKode   = taal.getTaalKode();
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

  /**
   * Is er iets gewijzigd?
   * 
   * @return
   */
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  /**
   * Zet de gegevens in de TaalDTO
   *
   * @param taal
   */
  public void persist(TaalDto taal) {
    if (!gewijzigd) {
      return;
    }

    if (!this.eigennaam.equals(taal.getEigennaam())) {
      taal.setEigennaam(this.eigennaam);
    }
    if (!this.taal.equals(taal.getTaal())) {
      taal.setTaal(this.taal);
    }
    if (!this.taalKode.equals(taal.getTaalKode())) {
      taal.setTaalKode(this.taalKode);
    }
  }

  /**
   * @param eigennaam de waarde van eigennaam
   */
  public void setEigennaam(String eigennaam) {
    if (null == this.eigennaam
        || !this.eigennaam.equals(eigennaam)) {
      gewijzigd       = true;
      this.eigennaam  = eigennaam;
    }
  }

  /**
   * @param taal de waarde van taal
   */
  public void setTaal(String taal) {
    if (null == this.taal
        || !this.taal.equals(taal)) {
      gewijzigd = true;
      this.taal = taal;
    }
  }

  /**
   * @param taalKode de waarde van taalKode
   */
  public void setTaalKode(String taalKode) {
    if (null == this.taalKode
        || !this.taalKode.equals(taalKode)) {
      gewijzigd     = true;
      this.taalKode = taalKode;
    }
  }
}
