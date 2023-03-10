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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosParameterService")
@Path("/parameters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
    var dto = new ParameterDto();
    parameter.persist(dto);

    parameterDao.create(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String sleutel) {
    var parameter = parameterDao.getByPrimaryKey(sleutel);
    parameterDao.delete(parameter);
  }

  @GET
  @Path("/applicatie/{applicatie}")
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getAppParameters(@PathParam(ParameterDto.PAR_APPLICATIE)
                                    String applicatie) {
    Collection<ParameterDto>  parameters  = new ArrayList<>();

    try {
      parameters  = parameterDao.getAppParameters(applicatie);
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return Response.ok().entity(parameters).build();
  }

  @GET
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getParameters() {
    Collection<ParameterDto>  parameters  = new ArrayList<>();

    try {
      parameters  = parameterDao.getAll();
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return Response.ok().entity(parameters).build();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
    return parameterDao.getByPrimaryKey(sleutel);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Parameter> query(DoosFilter<ParameterDto> filter) {
    Collection<Parameter> params  = new ArrayList<>();

    try {
      parameterDao.getAll(filter).forEach(rij -> params.add(new Parameter(rij)));
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return params;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(ParameterDto parameter) {
    parameterDao.update(parameter);
    getPropertyManager().update(parameter.getSleutel(), parameter.getWaarde());
  }
}
