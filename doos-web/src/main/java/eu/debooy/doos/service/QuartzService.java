/**
 * Copyright (c) 2019 Marco de Booij
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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IQuartz;
import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.model.QuartzjobData;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.openejb.quartz.JobKey;
import org.apache.openejb.quartz.SchedulerException;
import org.apache.openejb.quartz.Trigger;
import org.apache.openejb.quartz.impl.StdSchedulerFactory;
import org.apache.openejb.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosQuartzService")
@Path("/quartz")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class QuartzService implements IQuartz {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(QuartzService.class);

  private QuartzjobService  quartzjobService;

  public QuartzService() {
    LOGGER.debug("init QuartzService");
  }

  @GET
  @Lock(LockType.READ)
  @Override
  public Response getQuartz() {
    List<QuartzjobData>  quartzInfo  = new ArrayList<>();
    try {
      var scheduler = StdSchedulerFactory.getDefaultScheduler();
      for (String groep : scheduler.getJobGroupNames()) {
        for (var jobKey :
                 scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groep))) {
          for (var trigger : scheduler.getTriggersOfJob(jobKey)) {
            quartzInfo.add(vulQuartzjobData(jobKey, trigger));
          }
        }
      }
    } catch (SchedulerException e) {
      LOGGER.error(e.getLocalizedMessage());
    }
    return Response.ok().entity(quartzInfo).build();
  }

  @Lock(LockType.READ)
  @Override
  public Collection<QuartzjobData> getQuartzInfo(String groep) {
    List<QuartzjobData>  quartzInfo  = new ArrayList<>();
    try {
      var scheduler = StdSchedulerFactory.getDefaultScheduler();
      for (var jobKey :
               scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groep))) {
        for (var trigger : scheduler.getTriggersOfJob(jobKey)) {
          quartzInfo.add(vulQuartzjobData(jobKey, trigger));
        }
      }
    } catch (SchedulerException e) {
      LOGGER.error(e.getLocalizedMessage());
    }

    return quartzInfo;
  }

  @GET
  @Path("/{groep}")
  @Lock(LockType.READ)
  @Override
  public Response getQuartzPerGroep(
      @PathParam(QuartzjobDto.COL_GROEP) String groep) {
    List<QuartzjobData>  quartzInfo  = new ArrayList<>();

    try {
      var scheduler = StdSchedulerFactory.getDefaultScheduler();
      for (var jobKey :
               scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groep))) {
        for (var trigger : scheduler.getTriggersOfJob(jobKey)) {
          quartzInfo.add(vulQuartzjobData(jobKey, trigger));
        }
      }
    } catch (SchedulerException e) {
      LOGGER.error(e.getLocalizedMessage());
    }
    return Response.ok().entity(quartzInfo).build();
  }

  private QuartzjobService getQuartzjobService() {
    if (null == quartzjobService) {
      quartzjobService  = (QuartzjobService)
          new JNDI.JNDINaam().metBean(QuartzjobService.class).locate();
    }

    return quartzjobService;
  }

  @Lock(LockType.READ)
  @Override
  public Collection<QuartzjobData> getQuartzjobs(String groep) {
    LOGGER.debug("getQuartzjobs({})", groep);
    Collection<QuartzjobData> quartzjobs  = new ArrayList<>();

    try {
      getQuartzjobService().getPerGroep(groep).forEach(job -> {
        LOGGER.debug(job.toString());
        quartzjobs.add(new QuartzjobData(job.getCron(), job.getGroep(),
                                         job.getJavaclass(), job.getJob(),
                                         job.getOmschrijving()));
      });
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return quartzjobs;
  }

  private QuartzjobData vulQuartzjobData(JobKey jobKey, Trigger trigger) {
    var quartz  = new QuartzjobData();
    quartz.setEndTime(trigger.getEndTime());
    quartz.setGroep(jobKey.getGroup());
    quartz.setJob(jobKey.getName());
    quartz.setNextFireTime(trigger.getNextFireTime());
    quartz.setOmschrijving(trigger.getDescription());
    quartz.setPreviousFireTime(trigger.getPreviousFireTime());
    quartz.setStartTime(trigger.getStartTime());

    return quartz;
  }
}
