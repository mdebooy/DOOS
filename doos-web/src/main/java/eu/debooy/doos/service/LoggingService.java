/**
 * Copyright (c) 2018 Marco de Booij
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

import eu.debooy.doos.access.LoggingDao;
import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doos.form.Logging;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosLoggingService")
@Path("/logging")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.WRITE)
public class LoggingService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(LoggingService.class);

  @SuppressWarnings("java:S6813")
  @Inject
  private LoggingDao  loggingDao;

  public LoggingService() {
    LOGGER.debug("init LoggingService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Long cleanup(Long retention) {
    return loggingDao.cleanup(retention);
  }

  @GET
  @Path("/package/{pkg}")
  public Response getAppLogging(@PathParam(LoggingDto.PAR_PACKAGE) String pkg) {
    try {
     return Response.ok().entity(loggingDao.getPackageLogging(pkg)).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new ArrayList<>()).build();
    }
  }

  @GET
  public Response getLogging() {
    return Response.ok().entity(loggingDao.getAll()).build();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<LoggingDto> getPackageLogging(String pkg) {
    try {
      return loggingDao.getPackageLogging(pkg);
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return new ArrayList<>();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LoggingDto logging(Long logId) {
    return loggingDao.getByPrimaryKey(logId);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Logging> query() {
    Collection<Logging> logging = new ArrayList<>();
    try {
      loggingDao.getAll().forEach(rij -> logging.add(new Logging(rij)));
    } catch (NullPointerException | ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return logging;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Logging> query(String applicatie) {
    Collection<Logging> logging = new ArrayList<>();

    try {
      loggingDao.getPackageLogging(applicatie)
                .forEach(rij -> logging.add(new Logging(rij)));
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return logging;
  }
}
