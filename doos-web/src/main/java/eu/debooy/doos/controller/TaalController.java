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
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
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

  private static final  String  DTIT_CREATE   = "doos.titel.taalnaam.create";
  private static final  String  DTIT_UPDATE   = "doos.titel.taalnaam.update";
  private static final  String  LBL_TAAL      = "label.taal";
  private static final  String  LBL_TAALNAAM  = "label.taalnaam";
  private static final  String  TIT_CREATE    = "doos.titel.taal.create";
  private static final  String  TIT_UPDATE    = "doos.titel.taal.update";

  private Taal        taal;
  private TaalDto     taalDto;
  private Taalnaam    taalnaam;

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    taal    = new Taal();
    taalDto = new TaalDto();

    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(TAAL_REDIRECT);
  }

  public void createDetail() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    taalnaam  = new Taalnaam();
    taalnaam.setIso6392t(getGebruikersIso6392t());

    setDetailAktie(PersistenceConstants.CREATE);
    setDetailSubTitel(getTekst(DTIT_CREATE));
    redirect(TAALNAAM_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var taalId  = taal.getTaalId();
    try {
      getTaalService().delete(taalId);
      taal      = new Taal();
      taalDto   = new TaalDto();
      taalnaam  = new Taalnaam();
      addInfo(PersistenceConstants.DELETED, taal.getNaam());
      redirect(TALEN_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taalId);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void deleteDetail() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var iso6392t  = taalnaam.getIso6392t();
    try {
      taalDto.removeTaalnaam(iso6392t);
      getTaalService().save(taalDto);
      addInfo(PersistenceConstants.DELETED, "'" + iso6392t + "'");
      if (getGebruikersIso6392t().equals(taalnaam.getIso6392t())) {
        taal.setNaam(null);
        setSubTitel(getTekst(TIT_UPDATE, taal.getNaam()));
      }
      taalnaam  = new Taalnaam();
      redirect(TAAL_REDIRECT);
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

  public JSONArray getTaalnamen() {
    var taalnamen = new JSONArray();

    taalDto.getTaalnamen().forEach(rij -> taalnamen.add(rij.toJSON()));

    return taalnamen;
  }

  public void retrieve() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec      = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(TaalDto.COL_TAALID)) {
      addError(ComponentsConstants.GEENPARAMETER, TaalDto.COL_TAALID);
      return;
    }

    var taalId  = Long.valueOf(ec.getRequestParameterMap()
                                 .get(TaalDto.COL_TAALID));

    try {
      taalDto    = getTaalService().taal(taalId);
      taal       = new Taal(taalDto, getGebruikersIso6392t());
      setAktie(PersistenceConstants.RETRIEVE);
      setSubTitel(taal.getNaam());
      redirect(TAAL_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_TAAL);
    }
  }

  public void retrieveDetail() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(TaalnaamDto.COL_ISO6392T)) {
      addError(ComponentsConstants.GEENPARAMETER, TaalnaamDto.COL_ISO6392T);
      return;
    }

    taalnaam  =
        new Taalnaam(taalDto.getTaalnaam(ec.getRequestParameterMap()
                                              .get(TaalnaamDto.COL_ISO6392T)));

    try {
      setDetailAktie(PersistenceConstants.UPDATE);
      setDetailSubTitel(getTekst(DTIT_UPDATE));
      redirect(TAALNAAM_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_TAALNAAM);
    }
  }

  public void save() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = TaalValidator.valideer(taal);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          taal.persist(taalDto);
          getTaalService().save(taalDto);
          taal.setTaalId(taalDto.getTaalId());
          addInfo(PersistenceConstants.CREATED, taal.getIso6392t());
          update();
          break;
        case PersistenceConstants.UPDATE:
          taal.persist(taalDto);
          getTaalService().save(taalDto);
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

  public void saveDetail() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

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

    var iso6392t  = taalnaam.getIso6392t();
    try {
      switch (getDetailAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          vulTaalnaam(iso6392t);
          addInfo(PersistenceConstants.CREATED, "'" + iso6392t + "'");
          break;
        case PersistenceConstants.UPDATE:
          vulTaalnaam(iso6392t);
          if (getGebruikersIso6392t().equals(iso6392t)) {
            taal.setNaam(taalnaam.getNaam());
            setSubTitel(getTekst(TIT_UPDATE, taalnaam.getNaam()));
          }
          addInfo(PersistenceConstants.UPDATED, "'" + iso6392t + "'");
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT,
                   getDetailAktie().getAktie()) ;
          break;
      }
      redirect(TAAL_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, iso6392t);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, iso6392t);
    } catch (DoosRuntimeException e) {
      LOGGER.error(String.format(ComponentsConstants.ERR_RUNTIME,
                                 e.getLocalizedMessage()), e);
      generateExceptionMessage(e);
    }
  }

  public void talenlijst() {
    if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var       exportData  = new ExportData();

    exportData.addMetadata("application", APPLICATIE_NAAM);
    exportData.addMetadata("auteur",      getGebruikerNaam());
    exportData.addMetadata("lijstnaam",   "talen");

    exportData.setParameters(getLijstParameters());

    exportData.setKolommen(new String[] { TaalDto.COL_ISO6392T,
                                          TaalDto.COL_ISO6391,
                                          "taal", "eigennaam" });

    exportData.setType(getType());

    exportData.addVeld("ReportTitel",     getTekst("doos.titel.talen"));
    exportData.addVeld("LabelIso6391",    getTekst("label.iso6391"));
    exportData.addVeld("LabelIso6392t",   getTekst("label.iso6392t"));
    exportData.addVeld("LabelTaal",       getTekst("label.taal"));
    exportData.addVeld("LabelEigennaam",  getTekst("label.taal.eigennaam"));

    Set<Taal> lijnen      = new TreeSet<>();
    lijnen.addAll(getTaalService().queryIso6392t(getGebruikersIso6392t()));
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

  public void update() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE, taal.getNaam()));
  }

  public void updateDetail() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel(getTekst(DTIT_UPDATE));
  }

  private void vulTaalnaam(String iso6392t) {
    var taalnaamDto  = new TaalnaamDto();

    taalnaam.persist(taalnaamDto);
    taalDto.addNaam(taalnaamDto);
    if (taal.getIso6392t().equals(iso6392t)) {
      taal.setEigennaam(taalnaam.getNaam());
    }
    getTaalService().save(taalDto);
  }
}
