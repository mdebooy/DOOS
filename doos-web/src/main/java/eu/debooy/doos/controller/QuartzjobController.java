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
import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.domain.QuartzjobPK;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doos.validator.QuartzjobValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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

  private static final  String  LBL_QUARTZJOB = "label.quartzjob";
  private static final  String  TIT_CREATE    = "doos.titel.quartzjob.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.quartzjob.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.quartzjob.update";

  private Quartzjob     quartzjob;
  private QuartzjobDto  quartzjobDto;

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    quartzjob     = new Quartzjob();
    quartzjobDto  = new QuartzjobDto();

    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(QUARTZJOB_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var groep = quartzjob.getGroep();
    var job   = quartzjob.getJob();
    try {
      getQuartzjobService().delete(new QuartzjobPK(groep, job));
      quartzjob     = new Quartzjob();
      quartzjobDto  = new QuartzjobDto();
      addInfo(PersistenceConstants.DELETED, groep + "," + job);
      redirect(QUARTZJOBS_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, groep + "," + job);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public Quartzjob getQuartzjob() {
    return quartzjob;
  }

  public void retrieve() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!checkEcParameters(ec.getRequestParameterMap(),
                           QuartzjobDto.COL_GROEP, QuartzjobDto.COL_JOB)) {
      return;
    }

    var groep = ec.getRequestParameterMap().get(QuartzjobDto.COL_GROEP);
    var job   = ec.getRequestParameterMap().get(QuartzjobDto.COL_JOB);

    try {
      quartzjobDto  = getQuartzjobService().quartzjob(new QuartzjobPK(groep,
                                                                      job));
      quartzjob     = new Quartzjob(quartzjobDto);
      setAktie(PersistenceConstants.RETRIEVE);
      setSubTitel(getTekst(TIT_RETRIEVE));
      redirect(QUARTZJOB_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_QUARTZJOB);
    }
  }

  public void save() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = QuartzjobValidator.valideer(quartzjob);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          quartzjob.persist(quartzjobDto);
          getQuartzjobService().save(quartzjobDto);
          addInfo(PersistenceConstants.CREATED, quartzjob.getOmschrijving());
          update();
          break;
        case PersistenceConstants.UPDATE:
          quartzjob.persist(quartzjobDto);
          getQuartzjobService().save(quartzjobDto);
          addInfo(PersistenceConstants.UPDATED, quartzjob.getOmschrijving());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, quartzjob.getOmschrijving());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, quartzjob.getOmschrijving());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void update() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE));
  }
}
