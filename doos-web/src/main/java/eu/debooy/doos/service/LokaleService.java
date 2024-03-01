/*
 * Copyright (c) 2024 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
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

import eu.debooy.doos.access.LokaleDao;
import eu.debooy.doos.domain.LokaleDto;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import java.util.ArrayList;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosLokaleService")
@Path("/lokalen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Lock(LockType.WRITE)
public class LokaleService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(LokaleService.class);

  @SuppressWarnings("java:S6813")
  @Inject
  private LokaleDao lokaleDao;

  public LokaleService() {
    LOGGER.debug("init LokaleService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String code) {
    LokaleDto lokale  = lokaleDao.getByPrimaryKey(code);
    lokaleDao.delete(lokale);
  }

  @GET
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getLokalen() {
    try {
      return Response.ok().entity(lokaleDao.getAll()).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new ArrayList<>()).build();
    }
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LokaleDto lokale(String code) {
    return lokaleDao.getByPrimaryKey(code);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(LokaleDto lokale) {
    lokaleDao.create(lokale);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void update(LokaleDto lokale) {
    lokaleDao.update(lokale);
  }
}
