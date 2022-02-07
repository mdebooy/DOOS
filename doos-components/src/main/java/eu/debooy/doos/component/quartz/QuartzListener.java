/**
 * Copyright 2019 Marco de Booij
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
package eu.debooy.doos.component.quartz;

import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.component.business.IQuartz;
import eu.debooy.doos.model.QuartzjobData;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.openejb.quartz.CronScheduleBuilder;
import org.apache.openejb.quartz.Job;
import static org.apache.openejb.quartz.JobBuilder.newJob;
import org.apache.openejb.quartz.JobKey;
import org.apache.openejb.quartz.ScheduleBuilder;
import org.apache.openejb.quartz.Scheduler;
import org.apache.openejb.quartz.SchedulerException;
import static org.apache.openejb.quartz.TriggerBuilder.newTrigger;
import org.apache.openejb.quartz.impl.StdSchedulerFactory;
import org.apache.openejb.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Zet de parameter QuartzGroup in de web.xml:
 * <blockquote><pre>{@code
 *  <context-param>
 *    <param-name>QuartzGroup</param-name>
 *    <param-value>groep</param-value>
 *  </context-param>
 * }</pre></blockquote>
 *
 * en 'activeer' met:
 * <blockquote><pre>{@code
 *  <listener>
 *    <listener-class>eu.debooy.doos.component.quartz.QuartzListener</listener-class>
 *  </listener>
 * }</pre></blockquote>
 *
 * @author Marco de Booij
 */
public class QuartzListener implements ServletContextListener {
  private static final Logger LOGGER  =
      LoggerFactory.getLogger(QuartzListener.class);

  @Override
  public void contextInitialized(ServletContextEvent event) {
    var ctx         = event.getServletContext();
    var groep       = ctx.getInitParameter("QuartzGroup");
    var properties  = new Properties();

    getProperties(properties);

    LOGGER.debug(String.format("init QuartzListener (%s)", groep));
    try {
      var scheduler = StdSchedulerFactory.getDefaultScheduler();
      removeQuartzjobs(scheduler, groep);
      startQuartzjobs(scheduler, groep);

      if (!scheduler.isStarted()) {
        scheduler.start();
      }
    } catch (SchedulerException e) {
      LOGGER.error(e.getMessage());
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    LOGGER.debug("destroy QuartzListener");
  }

  private static ScheduleBuilder<?> createSchedule(String cronExpression){
    return CronScheduleBuilder.cronSchedule(cronExpression);
  }

  private void getProperties(Properties  properties) {
    try {
      var rijen = ((IProperty) new JNDI.JNDINaam()
                                       .metBeanNaam("PropertyService")
                                       .metInterface(IProperty.class)
                                       .metAppNaam("doos")
                                       .locate())
                      .getProperties("org.quartz");
      for (Applicatieparameter rij : rijen) {
        properties.put(rij.getSleutel(), rij.getWaarde());
      }
    } catch (ObjectNotFoundException e) {
      // Geen parameters gevonden.
    }
  }

  private List<QuartzjobData> getQuartzjobs(String groep) {
    List<QuartzjobData> quartzjobs  = new ArrayList<>();
    try {
      var rijen = ((IQuartz) new JNDI.JNDINaam().metBeanNaam("QuartzService")
                                                .metInterface(IQuartz.class)
                                                .metAppNaam("doos")
                                                .locate())
                      .getQuartzjobs(groep);
      for (QuartzjobData rij : rijen) {
        quartzjobs.add(rij);
      }
    } catch (ObjectNotFoundException e) {
      // Geen jobs gevonden.
    }
    LOGGER.debug(String.format("#Jobs found for %s: %s",
                               groep, quartzjobs.size()));

    return quartzjobs;
  }

  private boolean removeQuartzjobs(Scheduler scheduler, String groep) {
    var         success = true;
    Set<JobKey> jobKeys;

    try {
      jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groep));
    } catch (SchedulerException e) {
      LOGGER.error(e.getLocalizedMessage());
      return false;
    }
    LOGGER.debug(String.format("#removeQuartzjobs (%s): %s",
                               groep, jobKeys.size()));

    for (var jobKey : jobKeys) {
      try {
        if (!scheduler.deleteJob(jobKey)) {
          success = false;
          LOGGER.error(String.format("Removing of %s failed.",
                                     jobKey.toString()));
        }
      } catch (SchedulerException e) {
        LOGGER.error(e.getLocalizedMessage());
        success = false;
      }
    }

    return success;
  }

  private boolean startQuartzjobs(Scheduler scheduler, String groep) {
    var success     = true;
    var quartzjobs  = getQuartzjobs(groep);
    LOGGER.debug(String.format("#startQuartzjobs (%s): %s",
                               groep, quartzjobs.size()));

    for (var quartzjob : quartzjobs) {
      try {
        Class<? extends Job> jobclass =
          (Class<? extends Job>) Class.forName(quartzjob.getJavaclass());
        var job     = newJob().ofType(jobclass)
                              .withIdentity(quartzjob.getJob(), groep)
                              .build();
        var trigger =
            newTrigger().withIdentity(quartzjob.getJob(), groep)
                        .withDescription(quartzjob.getOmschrijving())
                        .withSchedule(createSchedule(quartzjob.getCron()))
                        .build();
        scheduler.scheduleJob(job, trigger);
      } catch (ClassNotFoundException | SchedulerException e) {
        LOGGER.error(String.format("%s,%s - %s",
                                   groep, quartzjob.getJob(), e.getMessage()));
        success = false;
      }
    }

    return success;
  }
}
