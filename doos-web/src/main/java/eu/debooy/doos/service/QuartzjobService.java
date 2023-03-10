/**
 * Copyright (c) 2019 Marco de Booij
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

import eu.debooy.doos.access.QuartzjobDao;
import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.domain.QuartzjobPK;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
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
@Named("doosQuartzjobService")
@Path("/quartzjobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Lock(LockType.WRITE)
public class QuartzjobService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(QuartzjobService.class);

  @Inject
  private QuartzjobDao  quartzjobDao;

  public QuartzjobService() {
    LOGGER.debug("init QuartzjobService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(QuartzjobPK sleutel) {
    var quartzjob = quartzjobDao.getByPrimaryKey(sleutel);
    quartzjobDao.delete(quartzjob);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Collection<Quartzjob> getPerGroep(String groep) {
    Collection<Quartzjob> quartzjobs  = new ArrayList<>();

    try {
      quartzjobDao.getPerGroep(groep)
                .forEach(rij -> quartzjobs.add(new Quartzjob(rij)));
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return quartzjobs;
  }

  @GET
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getQuartzjobs() {
    try {
      return Response.ok().entity(quartzjobDao.getAll()).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new ArrayList<>()).build();
    }
  }

  @GET
  @Path("/{groep}")
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getQuatrzjobsPerGroep(
      @PathParam(QuartzjobDto.COL_GROEP) String groep) {
    try {
      return Response.ok().entity(quartzjobDao.getPerGroep(groep)).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new ArrayList<>()).build();
    }
  }

  public QuartzjobDto quartzjob(QuartzjobPK sleutel) {
    return quartzjobDao.getByPrimaryKey(sleutel);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Quartzjob> query() {
    Collection<Quartzjob>  quartzjobs = new ArrayList<>();

    try {
      quartzjobDao.getAll().forEach(rij -> quartzjobs.add(new Quartzjob(rij)));
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return quartzjobs;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(QuartzjobDto quartzjob) {
    quartzjobDao.update(quartzjob);
  }
}
