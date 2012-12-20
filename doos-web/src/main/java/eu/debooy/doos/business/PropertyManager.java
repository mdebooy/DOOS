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
package eu.debooy.doos.business;

import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doosutils.components.business.IProperty;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class PropertyManager implements IProperty {
  private static final  Logger  LOGGER          =
      LoggerFactory.getLogger(PropertyManager.class);

  private Map<String, String> properties        = new HashMap<String, String>();
  private ParameterManager    parameterManager;

  @Lock(LockType.WRITE)
  public void clear() {
    properties.clear();
  }

  @Lock(LockType.WRITE)
  public void delete(String property) {
    if (properties.containsKey(property)) {
      properties.remove(property);
    }
  }

  private ParameterManager getParameterManager() {
    if (null == parameterManager) {
      parameterManager  = (ParameterManager)
          new JNDI.JNDINaam().metBean(ParameterManager.class).locate();
    }

    return parameterManager;
  }

  @Lock(LockType.READ)
  @Override
  public Map<String, String> getProperties(String applicatie) {
    DoosFilter<ParameterDto>  filter      = new DoosFilter<ParameterDto>();
    filter.addFilter("email", applicatie + ".%");
    Collection<ParameterDto>  parameters  =
        getParameterManager().getAll(filter);
    Map<String, String>       props       = new Hashtable<String, String>();
    for (ParameterDto parameter : parameters) {
      props.put(parameter.getSleutel(), parameter.getWaarde());
    }

    return props;
  }

  @Lock(LockType.READ)
  @Override
  public String getProperty(String property) {
    if (properties.containsKey(property)) {
      return properties.get(property);
    } else {
      ParameterDto  dto = null;
      try {
        dto = getParameterManager().getParameter(property);
      } catch (ObjectNotFoundException e) {
        return null;
      }
      LOGGER.debug("Toegevoegd: " + property);
      properties.put(property, dto.getWaarde());
      return properties.get(property);
    }
  }

  @Lock(LockType.READ)
  public int size() {
    return properties.size();
  }

  @Lock(LockType.WRITE)
  @Override
  public void update(String applicatie, Map<String, String> props) {
    // TODO Auto-generated method stub
  }

  @Lock(LockType.WRITE)
  @Override
  public void update(String property, String waarde) {
    if (properties.containsKey(property)) {
      properties.put(property, waarde);
    }
  }
}
