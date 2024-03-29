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
  private static final  long  serialVersionUID  = 1L;

  private Applicatieparameter property;

  @EJB
  private IProperty propertyBean;

  public void cancel() {
    property  = null;
  }

  public String appValue(String property) {
    return propertyBean.getAppProperty(property);
  }

  public Applicatieparameter getProperty() {
    return property;
  }

  public String getProperty(String sleutel) {
    return propertyBean.getProperty(sleutel);
  }

  public List<Applicatieparameter> properties(String prefix) {
    return propertyBean.getProperties(prefix);
  }

  public void save() {
    propertyBean.update(property);
    property  = null;
  }

  public String value(String property) {
    return propertyBean.getProperty(property);
  }

  public void wijzig(Applicatieparameter property) {
    this.property = property;
    save();
  }
}
