/**
 * Copyright 2009 Marco de Booij
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

import eu.debooy.doos.access.ParameterDao;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;


/**
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
@Stateless
public class ParameterManager {
  @Inject
  private ParameterDao  parameterDao;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ParameterDto createParameter(ParameterDto parameter) {
    return parameterDao.create(parameter);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void deleteParameter(ParameterDto parameter) {
    parameterDao.delete(parameter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<ParameterDto> getAll() {
    return parameterDao.getAll();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<ParameterDto> getAll(DoosFilter<ParameterDto> filter) {
    return parameterDao.getAll(filter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public ParameterDto getParameter(String sleutel) {
    return parameterDao.getByPrimaryKey(sleutel);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public ParameterDto updateParameter(ParameterDto parameter) {
    return parameterDao.update(parameter);
  }
}
