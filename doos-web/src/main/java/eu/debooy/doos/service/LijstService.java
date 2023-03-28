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

import eu.debooy.doos.access.LijstDao;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doos.form.Lijst;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
@Named("doosLijstService")
@Path("/lijsten")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Lock(LockType.WRITE)
public class LijstService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(LijstService.class);

  @Inject
  private LijstDao    lijstDao;

  public LijstService() {
    LOGGER.debug("init LijstService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String lijstnaam) {
    var lijst = lijstDao.getByPrimaryKey(lijstnaam);
    lijstDao.delete(lijst);
  }

  @GET
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getLijsten() {
    List<LijstDto>  lijsten = new ArrayList<>();

    try {
      lijsten = lijstDao.getAll();
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return Response.ok().entity(lijsten).build();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LijstDto lijst(String lijstnaam) {
    return lijstDao.getByPrimaryKey(lijstnaam);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Lijst> query() {
    Collection<Lijst> lijsten = new ArrayList<>();

    try {
      lijstDao.getAll().forEach(rij -> lijsten.add(new Lijst(rij)));
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return lijsten;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(LijstDto lijstDto) {
    lijstDao.update(lijstDto);
  }
}
