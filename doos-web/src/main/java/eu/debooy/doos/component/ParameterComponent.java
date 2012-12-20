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

import eu.debooy.doos.business.ParameterManager;
import eu.debooy.doos.business.PropertyManager;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doosutils.components.business.IProperty;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;


/**
 * @author Marco de Booij
 */
public class ParameterComponent {
  private ParameterManager  parameterManager;
  private IProperty         propertyManager;

  public void clearProperties() {
    getPropertyManager().clear();
  }

  public void delete(ParameterDto parameter) {
    getParameterManager().deleteParameter(parameter);
    getPropertyManager().delete(parameter.getSleutel());
  }

  public Collection<ParameterDto> getAll() {
    return getParameterManager().getAll();
  }

  public Collection<ParameterDto> getAll(DoosFilter<ParameterDto> filter) {
    return getParameterManager().getAll(filter);
  }

  private ParameterManager getParameterManager() {
    if (null == parameterManager) {
      parameterManager  = (ParameterManager)
          new JNDI.JNDINaam().metBean(ParameterManager.class).locate();
    }

    return parameterManager;
  }

  public int getProperties() {
    return getPropertyManager().size();
  }

  private IProperty getPropertyManager() {
    if (null == propertyManager) {
      propertyManager  = (IProperty)
        new JNDI.JNDINaam().metBean(PropertyManager.class)
                           .metInterface(IProperty.class).locate();
    }

    return propertyManager;
  }

  public void insert(ParameterDto parameter) {
    getParameterManager().createParameter(parameter);
  }

  public void update(ParameterDto parameter) {
    getParameterManager().updateParameter(parameter);
    getPropertyManager().update(parameter.getSleutel(), parameter.getWaarde());

  }
}
