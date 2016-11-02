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
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.conversie.ByteArray;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;


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

  /**
   * Prepareer een nieuw Lijst.
   */
  public void create() {
    lijst     = new Lijst();
    lijstDto  = new LijstDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.lijst.create");
    redirect(LIJST_REDIRECT);
  }

  /**
   * Verwijder de Lijst
   * 
   * @param String lijstnaam
   */
  public void delete(String lijstnaam) {
    try {
      getLijstService().delete(lijstnaam);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, lijstnaam);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.DELETED, lijstnaam);
  }

  /**
   * @return het bestand
   */
  public UploadedFile getBestand() {
    return bestand;
  }

  /**
   * Geef de geselecteerde taal.
   * 
   * @return Taal
   */
  public Lijst getLijst() {
    return lijst;
  }

  /**
   * Geef de lijst met lijsten.
   * 
   * @return Collection<Lijst> met Lijst objecten.
   */
  public Collection<Lijst> getLijsten() {
    return getLijstService().query();
  }

  /**
   * Persist de Lijst
   * 
   * @param Lijst
   */
  public void save() {
    List<Message> messages  = LijstValidator.valideer(lijst, bestand,
                                                      getAktie());
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    Scanner scanner = null;
    try {
      lijst.persist(lijstDto);
      if (DoosUtils.isNotBlankOrNull(bestand)) {
        scanner = new Scanner(bestand.getInputStream());
        String        report        = scanner.useDelimiter("\\A").next();
        JasperReport  jasperReport  =
            JasperCompileManager
              .compileReport(new ByteArrayInputStream(report.getBytes()));

        lijstDto.setLijst(report);
        lijstDto.setJasperReport(ByteArray.toByteArray(jasperReport));
      }
      getLijstService().save(lijstDto);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, lijst.getLijstnaam());
      return;
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, lijst.getLijstnaam());
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    } catch (IOException e) {
      LOGGER.error("IO: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    } catch (JRException e) {
      LOGGER.error("JR: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    } finally {
      if (null != scanner) {
        scanner.close();
      }
    }

    redirect(LIJSTEN_REDIRECT);
  }

  /**
   * @param bestand het bestand
   */
  public void setBestand(UploadedFile bestand) {
    this.bestand  = bestand;
  }

  /**
   * Zet de Lijst die gewijzigd gaat worden klaar.
   * 
   * @param String lijstnaam
   */
  public void update(String lijstnaam) {
    lijstDto  = getLijstService().lijst(lijstnaam);
    lijst     = new Lijst(lijstDto);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.lijst.update");
    redirect(LIJST_REDIRECT);
  }
}
