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
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.io.IOException;
import java.util.Scanner;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.myfaces.custom.fileupload.UploadedFile;
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

  private static final  String  TIT_CREATE    = "doos.titel.lijst.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.lijst.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.lijst.update";

  private Lijst           lijst;
  private UploadedFile    bestand;

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    lijst     = new Lijst();

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
      addInfo(PersistenceConstants.DELETED, lijstnaam);
      redirect(LIJSTEN_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, lijstnaam);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public UploadedFile getBestand() {
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

    lijst = new Lijst(getLijstService().lijst(lijstnaam));
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(getTekst(TIT_RETRIEVE));
    redirect(LIJST_REDIRECT);
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

    var lijstDto  = new LijstDto();
    lijst.persist(lijstDto);
    if (DoosUtils.isNotBlankOrNull(bestand)) {
      try (var scanner  = new Scanner(bestand.getInputStream())) {
        var report      = scanner.useDelimiter("\\A").next();
        lijstDto.setLijst(report);
      } catch (IOException e) {
        LOGGER.error(e.getClass().getSimpleName() + " "
                      + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
        return;
      }
    }

    try {
      getLijstService().save(lijstDto);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          addInfo(PersistenceConstants.CREATED, lijst.getLijstnaam());
          update();
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, lijst.getLijstnaam());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      redirect(LIJSTEN_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, lijst.getLijstnaam());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, lijst.getLijstnaam());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void setBestand(UploadedFile bestand) {
    this.bestand  = bestand;
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
