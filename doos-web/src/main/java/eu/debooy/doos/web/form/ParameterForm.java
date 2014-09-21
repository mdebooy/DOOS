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

import eu.debooy.doos.domain.ParameterDto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;


/**
 * @author Marco de Booij
 */
public class ParameterForm implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  sleutel;
  private String  waarde;

  public ParameterForm() {}

  public ParameterForm(ParameterDto parameter) {
    this.sleutel  = parameter.getSleutel();
    this.waarde   = parameter.getWaarde();
  }

  /**
   * @return de sleutel
   */
  public String getSleutel() {
    return sleutel;
  }

  /**
   * @return de waarde
   */
  public String getWaarde() {
    return waarde;
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
   * @param parameter
   */
  public void persist(ParameterDto parameter) {
    if (!gewijzigd) {
      return;
    }

    if (!new EqualsBuilder().append(this.sleutel,
                                    parameter.getSleutel()).isEquals()) {
      parameter.setSleutel(this.sleutel);
    }
    if (!new EqualsBuilder().append(this.waarde,
                                    parameter.getWaarde()).isEquals()) {
      parameter.setWaarde(this.waarde);
    }
  }

  /**
   * @param sleutel de waarde van sleutel
   */
  public void setSleutel(String sleutel) {
    if (!new EqualsBuilder().append(this.sleutel, sleutel).isEquals()) {
      gewijzigd     = true;
      this.sleutel  = sleutel;
    }
  }

  /**
   * @param waarde de waarde van waarde
   */
  public void setWaarde(String waarde) {
    if (!new EqualsBuilder().append(this.waarde, waarde).isEquals()) {
      gewijzigd   = true;
      this.waarde = waarde;
    }
  }
}
