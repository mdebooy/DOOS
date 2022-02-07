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
package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doos.component.business.IQuartz;
import eu.debooy.doos.domain.QuartzjobPK;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doos.model.QuartzjobData;
import eu.debooy.doos.validator.QuartzjobValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.openejb.quartz.SchedulerException;
import org.apache.openejb.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosQuartzjob")
@SessionScoped
public class QuartzjobController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(QuartzjobController.class);

  private Quartzjob quartzjob;

  @EJB
  private final IQuartz quartzService = null;

  public void create() {
    quartzjob = new Quartzjob();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.quartzjob.create");
    redirect(QUARTZJOB_REDIRECT);
  }

  public void delete(String groep, String job) {
    try {
      getQuartzjobService().delete(new QuartzjobPK(groep, job));
      addInfo(PersistenceConstants.DELETED, groep + "," + job);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, groep + "," + job);
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }
  }

  public Quartzjob getQuartzjob() {
    return quartzjob;
  }

  public Collection<Quartzjob> getQuartzjobs() {
    return getQuartzjobService().query();
  }

  public Collection<QuartzjobData> getScheduledQuartzjobs() {
    Collection<QuartzjobData> quartzjobs  = new ArrayList<>();
    try {
      var scheduler = StdSchedulerFactory.getDefaultScheduler();

      for (String groep : scheduler.getJobGroupNames()) {
        quartzjobs.addAll(quartzService.getQuartzInfo(groep));
      }
    } catch (SchedulerException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
    }

    return quartzjobs;
  }

  public void save() {
    var messages  = QuartzjobValidator.valideer(quartzjob);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      getQuartzjobService().save(quartzjob);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          addInfo(PersistenceConstants.CREATED, quartzjob.getOmschrijving());
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, quartzjob.getOmschrijving());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      redirect(QUARTZJOBS_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, quartzjob.getOmschrijving());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, quartzjob.getOmschrijving());
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }
  }

  public void update(String groep, String job) {
    quartzjob =
        new Quartzjob(getQuartzjobService().quartzjob(new QuartzjobPK(groep,
                                                                      job)));
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.quartzjob.update");
    redirect(QUARTZJOB_REDIRECT);
  }
}
