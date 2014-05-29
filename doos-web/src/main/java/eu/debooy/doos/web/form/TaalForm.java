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

import org.apache.commons.lang.builder.EqualsBuilder;


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

    if (!new EqualsBuilder().append(this.eigennaam,
                                    taal.getEigennaam()).isEquals()) {
      taal.setEigennaam(this.eigennaam);
    }
    if (!new EqualsBuilder().append(this.taal, taal.getTaal()).isEquals()) {
      taal.setTaal(this.taal);
    }
    if (!new EqualsBuilder().append(this.taalKode,
                                    taal.getTaalKode()).isEquals()) {
      taal.setTaalKode(this.taalKode);
    }
  }

  /**
   * @param eigennaam de waarde van eigennaam
   */
  public void setEigennaam(String eigennaam) {
    if (!new EqualsBuilder().append(this.eigennaam, eigennaam).isEquals()) {
      gewijzigd         = true;
      if (null == eigennaam) {
        this.eigennaam  = null;
      } else {
        this.eigennaam  = eigennaam;
      }
    }
  }

  /**
   * @param taal de waarde van taal
   */
  public void setTaal(String taal) {
    if (!new EqualsBuilder().append(this.taal, taal).isEquals()) {
      gewijzigd   = true;
      if (null == taal) {
        this.taal = null;
      } else {
        this.taal = taal;
      }
    }
  }

  /**
   * @param taalKode de waarde van taalKode
   */
  public void setTaalKode(String taalKode) {
    if (!new EqualsBuilder().append(this.taalKode, taalKode).isEquals()) {
      gewijzigd       = true;
      if (null == taalKode) {
        this.taalKode = null;
      } else {
        this.taalKode = taalKode;
      }
    }
  }
}
