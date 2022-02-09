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
import eu.debooy.doosutils.conversie.ByteArray;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
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

  private Lijst           lijst;
  private LijstDto        lijstDto;
  private UploadedFile    bestand;

  public void create() {
    lijst     = new Lijst();
    lijstDto  = new LijstDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.lijst.create");
    redirect(LIJST_REDIRECT);
  }

  public void delete(String lijstnaam) {
    try {
      getLijstService().delete(lijstnaam);
      addInfo(PersistenceConstants.DELETED, lijstnaam);
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

  public Collection<Lijst> getLijsten() {
    return getLijstService().query();
  }

  public void save() {
    var messages  = LijstValidator.valideer(lijst, bestand, getAktie());
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    lijst.persist(lijstDto);
    if (DoosUtils.isNotBlankOrNull(bestand)) {
      try (var scanner  = new Scanner(bestand.getInputStream())) {
        var report        = scanner.useDelimiter("\\A").next();
        var jasperReport  =
            JasperCompileManager
              .compileReport(new ByteArrayInputStream(report.getBytes()));

        lijstDto.setLijst(report);
        lijstDto.setJasperReport(ByteArray.toByteArray(jasperReport));
      } catch (IOException | JRException e) {
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

  public void update(String lijstnaam) {
    lijstDto  = getLijstService().lijst(lijstnaam);
    lijst     = new Lijst(lijstDto);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.lijst.update");
    redirect(LIJST_REDIRECT);
  }
}
