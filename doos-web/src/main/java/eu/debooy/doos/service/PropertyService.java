/**
 * Copyright (c) 2016 Marco de Booij
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

import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.KeyValue;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.service.JNDI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

  private final Map<String, String> properties        = new HashMap<>();
  private       ParameterService    parameterService;

  public PropertyService() {
    LOGGER.debug("init PropertyService");
  }

  @Lock(LockType.WRITE)
  @Override
  public void clear() {
    properties.clear();
  }

  @Lock(LockType.WRITE)
  @Override
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
  @Override
  public String getAppProperty(String property) {
    var waarde  = getProperty(property);
    // Applicatie parameter niet gevonden. Probeer default.
    if (DoosUtils.isBlankOrNull(waarde)) {
      var sleutel = "default" + property.substring(property.indexOf('.'));
      waarde  = getProperty(sleutel);
      LOGGER.debug("Toegevoegd: " + property);
      properties.put(property, "_@" + sleutel + "@_");
    }

    return waarde;
  }

  @Lock(LockType.READ)
  @Override
  public Collection<KeyValue> getCache() {
    Set<KeyValue> params  = new HashSet<>();
    properties.entrySet()
              .forEach(entry -> params.add(new KeyValue(entry.getKey(),
                                                        entry.getValue())));

    return params;
  }

  @Lock(LockType.READ)
  @Override
  public List<Applicatieparameter> getProperties(String prefix) {
    DoosFilter<ParameterDto>  filter      = new DoosFilter<>();
    List<Applicatieparameter> parameters  = new ArrayList<>();

    filter.addFilter("sleutel", prefix + ".%");
    getParameterService().query(filter).forEach(parameter ->
      parameters.add(
          new Applicatieparameter("app_param." +
                                  parameter.getSleutel()
                                           .substring(prefix.length()+1),
                                  parameter.getSleutel(),
                                  parameter.getWaarde())));

    return parameters;
  }

  @Lock(LockType.READ)
  @Override
  public String getProperty(String property) {
    var waarde  = "";
    if (properties.containsKey(property)) {
      waarde  = properties.get(property);
      if (waarde.startsWith("_@") && waarde.endsWith("@_")) {
        waarde  = getProperty(waarde.substring(2, waarde.length()-2));
      }
    } else {
      ParameterDto  dto;
      try {
        dto = getParameterService().parameter(property);
      } catch (ObjectNotFoundException e) {
        LOGGER.debug("Property niet gevonden: " + property);
        return waarde;
      }

      LOGGER.debug("Toegevoegd: " + property);
      properties.put(property, dto.getWaarde());
      waarde  = properties.get(property);
      if (waarde.startsWith("_@") && waarde.endsWith("@_")) {
        waarde  = getProperty(property.substring(2, property.length()-2));
      }
    }

    return waarde;
  }

  @Lock(LockType.READ)
  @Override
  public int size() {
    return properties.size();
  }

  @Lock(LockType.WRITE)
  @Override
  public void update(Applicatieparameter property) {
    var sleutel   = property.getSleutel();
    var parameter = getParameterService().parameter(sleutel);
    if (null == parameter) {
      LOGGER.debug("Property not found: " + sleutel);
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                        "Property: " + sleutel);
    }
    parameter.setWaarde(property.getWaarde());
    getParameterService().save(new Parameter(parameter));
    update(property.getSleutel(), property.getWaarde());
  }

  @Lock(LockType.WRITE)
  @Override
  public void update(String property, String waarde) {
    properties.computeIfAbsent(property, k ->  waarde);
  }
}
