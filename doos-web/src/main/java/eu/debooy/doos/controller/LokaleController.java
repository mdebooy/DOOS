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

package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doos.domain.LokaleDto;
import eu.debooy.doos.form.Lokale;
import eu.debooy.doos.validator.LokaleValidator;
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
@Named("doosLokale")
@SessionScoped
public class LokaleController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(LokaleController.class);

  private static final  String  LBL_LOKALE  = "label.lokale";

  private static final  String  TIT_CREATE  = "doos.titel.lokale.create";
  private static final  String  TIT_UPDATE  = "doos.titel.lokale.update";

  private Lokale    lokale;
  private LokaleDto lokaleDto;

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    lokale     = new Lokale();
    lokaleDto  = new LokaleDto();

    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(LOKALE_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var code  = lokale.getCode();
    try {
      getLokaleService().delete(code);
      addInfo(PersistenceConstants.DELETED, code);
      lokale      = new Lokale();
      lokaleDto   = new LokaleDto();
      redirect(LOKALEN_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
    } catch (DoosRuntimeException e) {
      LOGGER.error(String.format(ComponentsConstants.ERR_RUNTIME,
                                 e.getLocalizedMessage()), e);
      generateExceptionMessage(e);
    }
  }

  public Lokale getLokale() {
    return lokale;
  }

  public void retrieve() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec      = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(LokaleDto.COL_CODE)) {
      addError(ComponentsConstants.GEENPARAMETER, LokaleDto.COL_CODE);
      return;
    }

    var code    = ec.getRequestParameterMap().get(LokaleDto.COL_CODE);

    try {
      lokaleDto = getLokaleService().lokale(code);
      lokale    = new Lokale(lokaleDto);
      update();
      redirect(LOKALE_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, getTekst(LBL_LOKALE));
    }
  }

  public void save() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = LokaleValidator.valideer(lokale);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    var code  = lokale.getCode();
    try {
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          lokale.persist(lokaleDto);
          getLokaleService().save(lokaleDto);
          addInfo(PersistenceConstants.CREATED, code);
          update();
          break;
        case PersistenceConstants.UPDATE:
          lokale.persist(lokaleDto);
          getLokaleService().update(lokaleDto);
          addInfo(PersistenceConstants.UPDATED, code);
          update();
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, code);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
    } catch (DoosRuntimeException e) {
      LOGGER.error(String.format(ComponentsConstants.ERR_RUNTIME,
                                 e.getLocalizedMessage()), e);
      generateExceptionMessage(e);
    }
  }

  public void update() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE, lokaleDto.getCode()));
  }
}
