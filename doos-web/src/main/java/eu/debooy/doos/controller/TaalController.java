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
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.domain.TaalnaamDto;
import eu.debooy.doos.form.Taal;
import eu.debooy.doos.form.Taalnaam;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doos.validator.TaalValidator;
import eu.debooy.doos.validator.TaalnaamValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.util.Collection;
import java.util.HashSet;
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

  private Taal        taal;
  private TaalDto     taalDto;
  private Taalnaam    taalnaam;
  private TaalnaamDto taalnaamDto;

  public void create() {
    taal    = new Taal();
    taalDto = new TaalDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.taal.create");
    redirect(TAAL_REDIRECT);
  }

  public void createTaalnaam() {
    taalnaam     = new Taalnaam();
    taalnaam.setIso6392t(getGebruikersIso6392t());
    taalnaamDto  = new TaalnaamDto();
    taalnaamDto.setIso6392t(getGebruikersIso6392t());
    setDetailAktie(PersistenceConstants.CREATE);
    setDetailSubTitel("doos.titel.taalnaam.create");
    redirect(TAALNAAM_REDIRECT);
  }

  public void delete(Long taalId) {
    String  naam;
    try {
      taalDto = getTaalService().taal(taalId);
      naam    = taalDto.getNaam(getGebruikersIso6392t());
      getTaalService().delete(taalId);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taalId);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.DELETED, naam);
  }

  public void deleteTaalnaam(String iso6392t) {
    try {
      taalDto.removeTaalnaam(iso6392t);
      getTaalService().save(taalDto);
      addInfo(PersistenceConstants.DELETED, "'" + iso6392t + "'");
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taal);
    } catch (DoosRuntimeException e) {
      LOGGER.error(String.format(ComponentsConstants.ERR_RUNTIME,
                                 e.getLocalizedMessage()), e);
      generateExceptionMessage(e);
    }
  }

  public Taal getTaal() {
    return taal;
  }

  public Taalnaam getTaalnaam() {
    return taalnaam;
  }

  public Collection<Taalnaam> getTaalnamen() {
    Collection<Taalnaam> taalnamen  = new HashSet<>();
    taalDto.getTaalnamen().forEach(rij -> taalnamen.add(new Taalnaam(rij)));

    return taalnamen;
  }

  public Collection<Taal> getTalen() {
    return getTaalService().queryIso6392t(getGebruikersIso6392t());
  }

  public void retrieve(Long taalId) {
    taalDto    = getTaalService().taal(taalId);
    taal       = new Taal(taalDto, getGebruikersIso6392t());
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(taal.getNaam());
    redirect(TAAL_REDIRECT);
  }

  public void save() {
    var messages  = TaalValidator.valideer(taal);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      taal.persist(taalDto);
      getTaalService().save(taalDto);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          addInfo(PersistenceConstants.CREATED, taal.getIso6392t());
          setAktie(PersistenceConstants.UPDATE);
          setSubTitel("doos.titel.taal.update");
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, taal.getIso6392t());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, taal.getIso6392t());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taal.getIso6392t());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void saveTaalnaam() {
    var messages  = TaalnaamValidator.valideer(taalnaam);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    if (getDetailAktie().getAktie() == PersistenceConstants.CREATE
        && taalDto.hasTaalnaam(taalnaam.getIso6392t())) {
      addError(PersistenceConstants.DUPLICATE, taalnaam.getIso6392t());
      return;
    }

    try {
      taalnaamDto  = new TaalnaamDto();
      taalnaam.persist(taalnaamDto);
      taalDto.addNaam(taalnaamDto);
      if (taal.getIso6392t().equals(taalnaam.getIso6392t())) {
        taal.setEigennaam(taalnaam.getNaam());
      }
      if (getGebruikersIso6392t().equals(taalnaam.getIso6392t())) {
        taal.setNaam(taalnaam.getNaam());
      }
      getTaalService().save(taalDto);
      switch (getDetailAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          addInfo(PersistenceConstants.CREATED,
                  "'" + taalnaam.getIso6392t() + "'");
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED,
                  "'" + taalnaam.getIso6392t() + "'");
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT,
                   getDetailAktie().getAktie()) ;
          break;
      }
      redirect(TAAL_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, taalnaam.getIso6392t());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taalnaam.getIso6392t());
    } catch (DoosRuntimeException e) {
      LOGGER.error(String.format(ComponentsConstants.ERR_RUNTIME,
                                 e.getLocalizedMessage()), e);
      generateExceptionMessage(e);
    }
  }

  public void talenLijst() {
    var       exportData  = new ExportData();

    exportData.addMetadata("application", APPLICATIE_NAAM);
    exportData.addMetadata("auteur",      getGebruikerNaam());
    exportData.addMetadata("lijstnaam",   "talen");

    exportData.setParameters(getLijstParameters());

    exportData.setKolommen(new String[] { "iso6392t", "iso6391", "taal",
                                          "eigennaam" });

    exportData.setType(getType());

    exportData.addVeld("ReportTitel",     getTekst("doos.titel.talen"));
    exportData.addVeld("LabelIso6391",    getTekst("label.iso6391"));
    exportData.addVeld("LabelIso6392t",   getTekst("label.iso6392t"));
    exportData.addVeld("LabelTaal",       getTekst("label.taal"));
    exportData.addVeld("LabelEigennaam",  getTekst("label.taal.eigennaam"));

    Set<Taal> lijnen      = new TreeSet<>();
    lijnen.addAll(getTalen());
    lijnen.forEach(
        lijn -> exportData.addData(new String[] {lijn.getIso6392t(),
                                                 lijn.getIso6391(),
                                                 lijn.getNaam(),
                                                 lijn.getEigennaam()}));

    var response  = (HttpServletResponse) FacesContext.getCurrentInstance()
                                                      .getExternalContext()
                                                      .getResponse();
    try {
      Export.export(response, exportData);
      FacesContext.getCurrentInstance().responseComplete();
    } catch (IllegalArgumentException | TechnicalException e) {
      generateExceptionMessage(e);
    }
  }

  public void update(Long taalId) {
    taalDto = getTaalService().taal(taalId);
    taal    = new Taal(taalDto, getGebruikersIso6392t());
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.taal.update");
    redirect(TAAL_REDIRECT);
  }

  public void updateTaalnaam(String iso6392t) {
    taalnaamDto = taalDto.getTaalnaam(iso6392t);
    taalnaam    = new Taalnaam(taalnaamDto);
    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel("doos.titel.taalnaam.update");
    redirect(TAALNAAM_REDIRECT);
  }
}
