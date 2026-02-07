/**
 * Copyright 2016 Marco de Booij
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
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doos.form.Lijst;
import eu.debooy.doos.validator.LijstValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosLijst")
@SessionScoped
public class LijstController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(LijstController.class);

  private static final  String  LBL_LIJST     = "label.lijstnaam";
  private static final  String  TIT_CREATE    = "doos.titel.lijst.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.lijst.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.lijst.update";

  private Lijst     lijst;
  private LijstDto  lijstDto;
  private File      bestand;

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    lijst     = new Lijst();
    lijstDto  = new LijstDto();

    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(LIJST_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    String lijstnaam  = lijst.getLijstnaam();
    try {
      getLijstService().delete(lijstnaam);
      lijst     = new Lijst();
      lijstDto  = new LijstDto();
      addInfo(PersistenceConstants.DELETED, lijstnaam);
      redirect(LIJSTEN_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, lijstnaam);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public File getBestand() {
    return bestand;
  }

  public Lijst getLijst() {
    return lijst;
  }

  public void retrieve() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(LijstDto.COL_LIJSTNAAM)) {
      addError(ComponentsConstants.GEENPARAMETER, LijstDto.COL_LIJSTNAAM);
      return;
    }

    var lijstnaam = ec.getRequestParameterMap().get(LijstDto.COL_LIJSTNAAM);

    try {
      lijstDto  = getLijstService().lijst(lijstnaam);
      lijst     = new Lijst(lijstDto);
      setAktie(PersistenceConstants.RETRIEVE);
      setDeletetekst(lijst.getLijstnaam());
      setSubTitel(getTekst(TIT_RETRIEVE));
      redirect(LIJST_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_LIJST);
    }
  }

  public void save() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = LijstValidator.valideer(lijst, bestand, getAktie());
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    var naam  = lijst.getLijstnaam();
    try {
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE -> {
          lijst.persist(lijstDto);
          getLijstService().save(lijstDto);
          addInfo(PersistenceConstants.CREATED, naam);
          update();
        }
        case PersistenceConstants.UPDATE -> {
          lijst.persist(lijstDto);
          getLijstService().save(lijstDto);
          addInfo(PersistenceConstants.UPDATED, naam);
        }
        default -> addError(ComponentsConstants.WRONGREDIRECT,
                            getAktie().getAktie());
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, naam);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, naam);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void setBestand(File bestand) {
    this.bestand  = bestand;
  }

  public void update() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setDeletetekst(lijst.getLijstnaam());
    setSubTitel(getTekst(TIT_UPDATE));
  }
}
