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

import eu.debooy.doos.access.ParameterDao;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosParameterService")
@Lock(LockType.WRITE)
public class ParameterService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ParameterService.class);

  @Inject
  private ParameterDao    parameterDao;

  private IProperty       propertyManager;

  public ParameterService() {
    LOGGER.debug("init ParameterService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(Parameter parameter) {
    ParameterDto  dto = new ParameterDto();
    parameter.persist(dto);

    parameterDao.create(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String sleutel) {
    ParameterDto  parameter = parameterDao.getByPrimaryKey(sleutel);
    parameterDao.delete(parameter);
  }

  private IProperty getPropertyManager() {
    if (null == propertyManager) {
      propertyManager  = (IProperty)
        new JNDI.JNDINaam().metBean(PropertyService.class)
                           .metInterface(IProperty.class).locate();
    }

    return propertyManager;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public ParameterDto parameter(String sleutel) {
    ParameterDto  parameter = parameterDao.getByPrimaryKey(sleutel);

    return parameter;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Parameter> query() {
    Collection<Parameter> parameters  = new ArrayList<Parameter>();
    try {
      for (ParameterDto rij : parameterDao.getAll()) {
        parameters.add(new Parameter(rij));
      }
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return parameters;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Parameter> query(DoosFilter<ParameterDto> filter) {
    Collection<Parameter> params  = new ArrayList<Parameter>();
    try {
      for (ParameterDto rij : parameterDao.getAll(filter)) {
        params.add(new Parameter(rij));
      }
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return params;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(Parameter parameter) {
    ParameterDto  dto = new ParameterDto();
    parameter.persist(dto);

    save(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(ParameterDto parameter) {
    parameterDao.update(parameter);
    getPropertyManager().update(parameter.getSleutel(), parameter.getWaarde());
  }
}
