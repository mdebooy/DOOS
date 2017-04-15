/**
 * Copyright 2011 Marco de Booij
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
package eu.debooy.doos.component;

import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doosutils.components.Applicatieparameter;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class Properties implements Serializable {
  private static final  long    serialVersionUID  = 1L;

  private Applicatieparameter property;

  @EJB
  private IProperty           propertyBean;

  /**
   * Stop de laatste aktie.
   */
  public void cancel() {
    property  = null;
  }

  /**
   * Geef de geselecteerde parameter/property.
   * 
   * @return Applicatieparameter
   */
  public Applicatieparameter getProperty() {
    return property;
  }

  /**
   * Geef de gevraagde parameter/property.
   * 
   * @return String
   */
  public String getProperty(String sleutel) {
    return propertyBean.getProperty(sleutel);
  }

  /**
   * Geef de parameters/properties van een applicatie.
   * 
   * @param applicatie
   * @return List<Applicatieparameter>
   */
  public List<Applicatieparameter> properties(String prefix) {
    return propertyBean.getProperties(prefix);
  }

  /**
   * Bewaar de Applicatieparameter in de database.
   */
  public void save() {
    propertyBean.update(property);
    property  = null;
  }

  /**
   * Geef de waarde van een parameter/property.
   * 
   * @param property
   * @return String
   */
  public String value(String property) {
    return propertyBean.getProperty(property);
  }

  /**
   * @param property
   */
  public void wijzig(Applicatieparameter property) {
    this.property = property;
    save();
  }
}
