/**
 * Copyright 2017 Marco de Booij
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
import eu.debooy.doos.domain.I18nLijstDto;
import eu.debooy.doos.domain.I18nSelectieDto;
import eu.debooy.doos.form.I18nLijst;
import eu.debooy.doos.form.I18nLijstCode;
import eu.debooy.doos.form.I18nSelectie;
import eu.debooy.doos.validator.I18nLijstValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosI18nLijst")
@SessionScoped
public class I18nLijstController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(I18nLijstController.class);

  private static final  String  DTIT_UPDATE =
      "doos.titel.i18nLijstTekst.update";
  private static final  String  TIT_CREATE    = "doos.titel.i18nLijst.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.i18nLijst.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.i18nLijst.update";

  private I18nLijst     i18nLijst;
  private I18nLijstCode i18nLijstCode;
  private I18nSelectie  i18nSelectie;

  public void create() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    i18nLijst    = new I18nLijst();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(I18NLIJST_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var code    = i18nLijst.getCode();
    var lijstId = i18nLijst.getLijstId();
    try {
      getI18nLijstService().delete(lijstId);
      addInfo(PersistenceConstants.DELETED, code);
      redirect(I18NLIJSTEN_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public I18nLijst getI18nLijst() {
    return i18nLijst;
  }

  public I18nSelectie getI18nSelectie() {
    return i18nSelectie;
  }

  public JSONArray getI18nSelecties() {
    var i18nSelecties  = new JSONArray();

    getI18nLijstService().getI18nSelecties(i18nLijst.getCode())
                         .forEach(rij -> i18nSelecties.add(rij.toJSON()));

    return i18nSelecties;
  }

  public void retrieve() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(I18nLijstDto.COL_LIJSTID)) {
      addError(ComponentsConstants.GEENPARAMETER, I18nLijstDto.COL_LIJSTID);
      return;
    }

    var lijstId = Long.valueOf(ec.getRequestParameterMap()
                                 .get(I18nLijstDto.COL_LIJSTID));

    i18nLijst    = new I18nLijst(getI18nLijstService().i18nLijst(lijstId));
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(getTekst(TIT_RETRIEVE));
    redirect(I18NLIJST_REDIRECT);
  }

  public void retrieveDetail() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(I18nSelectieDto.COL_CODE)) {
      addError(ComponentsConstants.GEENPARAMETER, I18nSelectieDto.COL_CODE);
      return;
    }

    i18nSelectie =
        new I18nSelectie(getI18nLijstService()
            .getI18nSelectie(i18nLijst.getCode(),
                             ec.getRequestParameterMap()
                               .get(I18nSelectieDto.COL_CODE)));
    try {
      i18nLijstCode =
          new I18nLijstCode(getI18nLijstService()
              .i18nLijstCode(i18nSelectie.getCodeId(), i18nLijst.getLijstId()));
    } catch (ObjectNotFoundException e) {
      i18nLijstCode = null;
    }

    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel(getTekst(DTIT_UPDATE));
    redirect(I18NSELECTIE_REDIRECT);
  }

  public void save() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

   var messages  = I18nLijstValidator.valideer(i18nLijst);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      getI18nLijstService().save(i18nLijst);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          i18nLijst.setLijstId(i18nLijst.getLijstId());
          addInfo(PersistenceConstants.CREATED, i18nLijst.getCode());
          update();
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, i18nLijst.getCode());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      setSubTitel(i18nLijst.getCode());
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nLijst.getCode());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nLijst.getCode());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void saveDetail() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    try {
      if (null == i18nSelectie.getVolgorde()
          || i18nSelectie.getVolgorde().equals(Integer.valueOf("0"))) {
        if (null != i18nLijstCode) {
          getI18nLijstService().delete(i18nLijstCode.getCodeId(),
                                       i18nLijstCode.getLijstId());
        }
      } else {
        setI18nLijstCode();
        getI18nLijstService().save(i18nLijstCode);
      }
      if (getDetailAktie().getAktie() == PersistenceConstants.UPDATE) {
        addInfo(PersistenceConstants.UPDATED, i18nSelectie.getCode());
      } else {
        addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
      }
      redirect(I18NLIJST_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nSelectie.getCode());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nSelectie.getCode());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  private void setI18nLijstCode() {
    if (null == i18nLijstCode) {
      i18nLijstCode =
          new I18nLijstCode(i18nSelectie.getCodeId(),
                            i18nLijst.getLijstId(),
                            i18nSelectie.getVolgorde());
    } else {
      if (!i18nSelectie.getVolgorde()
                       .equals(i18nLijstCode.getVolgorde())) {
        i18nLijstCode.setVolgorde(i18nSelectie.getVolgorde());
      }
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
