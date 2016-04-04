/**
 * Copyright 2016 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.service;

import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.KeyValue;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.components.business.IProperty;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosPropertyService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class PropertyService implements IProperty {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(PropertyService.class);


  private Map<String, String> properties        = new HashMap<String, String>();
  private ParameterService    parameterService;

  /**
   * Initialisatie.
   */
  public PropertyService() {
    LOGGER.debug("init PropertyService");
  }

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

  private ParameterService getParameterService() {
    if (null == parameterService) {
      parameterService  = (ParameterService)
          new JNDI.JNDINaam().metBean(ParameterService.class).locate();
    }

    return parameterService;
  }

  @Lock(LockType.READ)
  public Collection<KeyValue> getCache() {
    Set<KeyValue> params  = new HashSet<KeyValue>();
    for (Entry<String, String> entry : properties.entrySet()) {
      params.add(new KeyValue(entry.getKey(), entry.getValue()));
    }

    return params;
  }

  @Lock(LockType.READ)
  public List<Applicatieparameter> getProperties(String applicatie) {
    DoosFilter<ParameterDto>  filter      = new DoosFilter<ParameterDto>();
    filter.addFilter("sleutel", applicatie + ".%");
    Collection<Parameter>     rijen       =
        getParameterService().query(filter);
    List<Applicatieparameter> parameters  =
        new ArrayList<Applicatieparameter>(rijen.size());
    for (Parameter parameter : rijen) {
      parameters.add(
          new Applicatieparameter("app_param." +
                                  parameter.getSleutel()
                                           .substring(applicatie.length()+1),
                                  parameter.getSleutel(),
                                  parameter.getWaarde()));
    }

    return parameters;
  }

  @Lock(LockType.READ)
  public String getProperty(String property) {
    if (properties.containsKey(property)) {
      return properties.get(property);
    } else {
      try {
        ParameterDto  dto = getParameterService().parameter(property);
        LOGGER.debug("Toegevoegd: " + property);
        properties.put(property, dto.getWaarde());
        return properties.get(property);
      } catch (ObjectNotFoundException e) {
        LOGGER.debug(e.getLocalizedMessage());
        return "";
      }
    }
  }

  @Lock(LockType.READ)
  public int size() {
    return properties.size();
  }

  @Lock(LockType.WRITE)
  public void update(Applicatieparameter property) {
    String  sleutel = property.getSleutel();
    try {
      ParameterDto  parameter = getParameterService().parameter(sleutel);
      parameter.setWaarde(property.getWaarde());
      getParameterService().save(new Parameter(parameter));
      update(property.getSleutel(), property.getWaarde());
    } catch (ObjectNotFoundException e) {
      LOGGER.debug(e.getLocalizedMessage());
    }
  }

  @Lock(LockType.WRITE)
  public void update(String property, String waarde) {
    if (properties.containsKey(property)) {
      properties.put(property, waarde);
    }
  }
}
