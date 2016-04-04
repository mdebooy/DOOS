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
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.components.business.IProperty;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosParameterService")
@Lock(LockType.WRITE)
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public class ParameterService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ParameterService.class);

  @Inject
  private ParameterDao    parameterDao;

  private IProperty       propertyManager;
  private Set<Parameter>  parameters;

  /**
   * Initialisatie.
   */
  public ParameterService() {
    LOGGER.debug("init ParameterService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String sleutel) {
    ParameterDto  parameter = parameterDao.getByPrimaryKey(sleutel);
    parameterDao.delete(parameter);
    parameters.remove(new Parameter(parameter));
  }

  private IProperty getPropertyManager() {
    if (null == propertyManager) {
      propertyManager  = (IProperty)
        new JNDI.JNDINaam().metBean(PropertyService.class)
                           .metInterface(IProperty.class).locate();
    }

    return propertyManager;
  }

  /**
   * Geef een Parameter.
   * 
   * @param String sleutel
   * @return ParameterDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public ParameterDto parameter(String sleutel) {
    ParameterDto  parameter = parameterDao.getByPrimaryKey(sleutel);

    return parameter;
  }

  /**
   * Geef alle Parameters.
   * 
   * @return Collection<Parameter>
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Parameter> query() {
    if (null == parameters) {
      parameters  = new HashSet<Parameter>();
      Collection<ParameterDto>  rijen = parameterDao.getAll();
      for (ParameterDto rij : rijen) {
        parameters.add(new Parameter(rij));
      }
    }

    return parameters;
  }

  /**
   * 
   * @param DoosFilter<ParameterDto> filter
   * @return Collection<ParameterDto>
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Parameter> query(DoosFilter<ParameterDto> filter) {
    Set<Parameter>  params  = new HashSet<Parameter>();
    Collection<ParameterDto>  rijen = parameterDao.getAll(filter);
    for (ParameterDto rij : rijen) {
      params.add(new Parameter(rij));
    }

    return params;
  }

  /**
   * Maak of wijzig de Parameter in de database.
   * 
   * @param Parameter parameter
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(Parameter parameter) {
    ParameterDto  dto = new ParameterDto();
    parameter.persist(dto);

    parameterDao.update(dto);
    getPropertyManager().update(parameter.getSleutel(), parameter.getWaarde());

    if (null != parameters) {
      parameters.remove(parameter);
      parameters.add(parameter);
    }
  }
}
