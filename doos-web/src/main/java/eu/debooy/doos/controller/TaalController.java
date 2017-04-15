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
import eu.debooy.doos.component.Export;
import eu.debooy.doos.form.Taal;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doos.validator.TaalValidator;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosTaal")
@SessionScoped
public class TaalController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(TaalController.class);

  private Taal  taal;

  /**
   * Prepareer een nieuw Taal.
   */
  public void create() {
    taal  = new Taal();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.taal.create");
    redirect(TAAL_REDIRECT);
  }

  /**
   * Verwijder de Taal
   * 
   * @param String taalKode
   * @param String eigennaam
   */
  public void delete(String taalKode, String eigennaam) {
    try {
      getTaalService().delete(taalKode);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, eigennaam);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.DELETED, eigennaam);
  }

  /**
   * Geef de geselecteerde taal.
   * 
   * @return Taal
   */
  public Taal getTaal() {
    return taal;
  }

  /**
   * Geef de lijst met talen.
   * 
   * @return Collection<Taal> met Taal objecten.
   */
  public Collection<Taal> getTalen() {
    return getTaalService().query();
  }

  /**
   * Persist de Taal
   * 
   * @param Taal
   */
  public void save() {
    List<Message> messages  = TaalValidator.valideer(taal);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      getTaalService().save(taal);
      switch (getAktie().getAktie()) {
      case PersistenceConstants.CREATE:
        addInfo(PersistenceConstants.CREATED, taal.getEigennaam());
        break;
      case PersistenceConstants.UPDATE:
        addInfo(PersistenceConstants.UPDATED, taal.getEigennaam());
        break;
      default:
        addError("error.aktie.wrong", getAktie().getAktie());
        break;
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, taal.getEigennaam());
      return;
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taal.getEigennaam());
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.UPDATED, taal.getEigennaam());

    redirect(TALEN_REDIRECT);
  }

  /**
   * Exporteer de Talen.
   */
  public void talenLijst() {

    ExportData  exportData  = new ExportData();

    exportData.addMetadata("application", APPLICATIE_NAAM);
    exportData.addMetadata("auteur",      getGebruikerNaam());
    exportData.addMetadata("lijstnaam",   "talen");

    exportData.setParameters(getLijstParameters());

    exportData.setKolommen(new String[] { "code", "taal", "eigennaam" });

    exportData.setType(getType());

    exportData.addVeld("ReportTitel",     getTekst("doos.titel.talen"));
    exportData.addVeld("LabelCode",       getTekst("label.code"));
    exportData.addVeld("LabelTaal",       getTekst("label.taal"));
    exportData.addVeld("LabelEigennaam",  getTekst("label.taal.eigennaam"));

    Set<Taal> lijnen  = new TreeSet<Taal>();
    lijnen.addAll(getTalen());
    for (Taal lijn : lijnen) {
      exportData.addData(new String[] {lijn.getTaalKode(),
                                       lijn.getTaal(),
                                       lijn.getEigennaam()});
    }

    HttpServletResponse response  =
        (HttpServletResponse) FacesContext.getCurrentInstance()
                                          .getExternalContext().getResponse();
    try {
      Export.export(response, exportData);
    } catch (IllegalArgumentException e) {
      generateExceptionMessage(e);
      return;
    } catch (TechnicalException e) {
      generateExceptionMessage(e);
      return;
    }

    FacesContext.getCurrentInstance().responseComplete();
  }

  /**
   * Zet de Taal die gewijzigd gaat worden klaar.
   * 
   * @param String taalKode
   */
  public void update(String taalKode) {
    taal  = new Taal(getTaalService().taal(taalKode));
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.taal.update");
    redirect(TAAL_REDIRECT);
  }
}
