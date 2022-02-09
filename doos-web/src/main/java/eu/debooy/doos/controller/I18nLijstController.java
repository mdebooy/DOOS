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
import eu.debooy.doos.domain.I18nLijstCodeDto;
import eu.debooy.doos.domain.I18nLijstDto;
import eu.debooy.doos.form.I18nLijst;
import eu.debooy.doos.form.I18nSelectie;
import eu.debooy.doos.validator.I18nLijstValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
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

  private I18nLijst         i18nLijst;
  private I18nLijstCodeDto  i18nLijstCodeDto;
  private I18nLijstDto      i18nLijstDto;
  private I18nSelectie      i18nSelectie;

  public void create() {
    i18nLijst    = new I18nLijst();
    i18nLijstDto = new I18nLijstDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.i18nlijst.create");
    redirect(I18NLIJST_REDIRECT);
  }

  public void delete(Long codeId, String code) {
    try {
      getI18nLijstService().delete(codeId);
      addInfo(PersistenceConstants.DELETED, code);
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

  public Collection<I18nLijst> getI18nLijsten() {
    return getI18nLijstService().query();
  }

  public I18nSelectie getI18nSelectie() {
    return i18nSelectie;
  }

  public Collection<I18nSelectie> getI18nSelecties() {
    Collection<I18nSelectie>  lijst = new ArrayList<>();

    getI18nLijstService().getI18nSelecties(i18nLijst.getCode())
                                                    .forEach(rij -> {
      rij.setWaarde(getTekst(rij.getSelectie() + "." + rij.getCode()));
      lijst.add(rij);
    });

    return lijst;
  }

  public void retrieve(Long codeId) {
    i18nLijstDto = getI18nLijstService().i18nLijst(codeId);
    i18nLijst    = new I18nLijst(i18nLijstDto);
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(i18nLijst.getCode());
    redirect(I18NLIJST_REDIRECT);
  }

  public void save() {
    var messages  = I18nLijstValidator.valideer(i18nLijst);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      i18nLijst.persist(i18nLijstDto);
      getI18nLijstService().save(i18nLijstDto);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          i18nLijst.setLijstId(i18nLijstDto.getLijstId());
          addInfo(PersistenceConstants.CREATED, i18nLijst.getCode());
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, i18nLijst.getCode());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      setAktie(PersistenceConstants.RETRIEVE);
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

  public void saveI18nSelectie() {
    try {
      if (i18nSelectie.getVolgorde().equals(Integer.valueOf("0"))) {
        if (null != i18nLijstCodeDto) {
          getI18nLijstService().delete(i18nLijstCodeDto.getCodeId(),
                                       i18nLijstCodeDto.getLijstId());
        }
      } else {
        if (null == i18nLijstCodeDto) {
          i18nLijstCodeDto  =
              new I18nLijstCodeDto(i18nSelectie.getCodeId(),
                                   i18nLijstDto.getLijstId(),
                                   i18nSelectie.getVolgorde());
        } else {
          if (!i18nSelectie.getVolgorde()
                           .equals(i18nLijstCodeDto.getVolgorde())) {
            i18nLijstCodeDto.setVolgorde(i18nSelectie.getVolgorde());
          }
        }
        getI18nLijstService().save(i18nLijstCodeDto);
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

  public void update(Long lijstId) {
    i18nLijstDto  = getI18nLijstService().i18nLijst(lijstId);
    i18nLijst     = new I18nLijst(i18nLijstDto);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.i18nlijst.update");
    redirect(I18NLIJST_REDIRECT);
  }

  public void updateI18nSelectie(String code) {
    i18nSelectie =
        new I18nSelectie(getI18nLijstService()
            .getI18nSelectie(i18nLijst.getCode(), code));
    i18nSelectie.setWaarde(getTekst(i18nSelectie.getSelectie() + "."
                                    + i18nSelectie.getCode()));
    try {
      i18nLijstCodeDto  =
          getI18nLijstService().i18nLijstCode(i18nSelectie.getCodeId(),
                                              i18nLijst.getLijstId());
    } catch (ObjectNotFoundException e) {
      i18nLijstCodeDto  = null;
    }
    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel("doos.titel.i18nlijsttekst.update");
    redirect(I18NSELECTIE_REDIRECT);
  }
}
