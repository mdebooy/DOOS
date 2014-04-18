/**
 * Copyright 2009 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.web.controller;

import eu.debooy.doos.component.Export;
import eu.debooy.doos.component.TaalComponent;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doos.web.form.TaalForm;
import eu.debooy.doos.web.model.Taal;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class TaalBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
    LoggerFactory.getLogger(TaalBean.class);

  public static final String  BEAN_NAME = "taalBean";

  private List<Taal>        talen;
  private List<SelectItem>  selectTalen;
  private Taal              filter;
  private Taal              taal;
  private TaalForm          formulier;

  public TaalBean() {
    setExportTypes("ODS", "ODT", "PDF");
  }

  /**
   * Stop de laatste aktie.
   */
  public void cancel() {
    if (isZoek()) {
      setGefilterd(false);
      talen = null;
    }
    setAktie(PersistenceConstants.RETRIEVE);
    formulier = null;
    taal      = null;
  }

  /**
   * Schrijf de nieuwe Taal in de database.
   */
  @Override
  public void create() {
    if (null != formulier && isNieuw()) {
      if (valideerForm() ) {
        TaalComponent taalComponent = new TaalComponent();
        try {
          formulier.persist(taal.getTaal());
          taalComponent.insert(taal.getTaal());
          if (null == talen) {
            talen = new ArrayList<Taal>(1);
          }
          talen.add(taal);
          addInfo(PersistenceConstants.CREATED, taal.getTaal().getTaalKode());
          setRetrieveMode();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   taal.getTaal().getTaalKode());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("create() niet toegestaan.");
    }
  }

  /**
   * Verwijder de Taal uit de database en de List.
   */
  @Override
  public void delete() {
    if (null != formulier && isVerwijder()) {
      TaalComponent taalComponent = new TaalComponent();
      try {
        taalComponent.delete(taal.getTaal());
        talen.remove(taal);
        addInfo(PersistenceConstants.DELETED, taal.getTaal().getTaalKode());
        setRetrieveMode();
      } catch (ObjectNotFoundException e) {
        addError(PersistenceConstants.NOTFOUND, taal.getTaal().getTaalKode());
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("delete() niet toegestaan.");
    }
  }

  /**
   * Exporteer de Talen.
   */
  public void export() {
    if (!isGeldigExportType(getType())) {
      addError("error.wrongexporttype", getType());
    }

    ExportData  exportData  = new ExportData();

    exportData.addMetadata("application", DoosBase.APPLICATIE_NAAM);
//TODO   exportData.addMetadata("auteur",      getGebruikerNaam());
    exportData.addMetadata("lijstnaam",   "talen");

    exportData.setKleuren(getLijstKleuren());

    exportData.setKolommen(new String[] { "code", "taal", "eigennaam" });

    exportData.setType(getType());

    exportData.addVeld("ReportTitel",     getTekst("doos.titel.talen"));
    exportData.addVeld("LabelCode",       getTekst("label.code"));
    exportData.addVeld("LabelTaal",       getTekst("label.taal"));
    exportData.addVeld("LabelEigennaam",  getTekst("label.taal.eigennaam"));

    for (Taal lijn : talen) {
      exportData.addData(new String[] {lijn.getTaal().getTaalKode(),
                                       lijn.getTaal().getTaal(),
                                       lijn.getTaal().getEigennaam()});
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
   * Geef de geselecteerde/nieuwe Taal.
   * 
   * @return
   */
  public TaalForm getTaal() {
    return formulier;
  }

  /**
   * Geef alle talen.
   * 
   * @return
   */
  public List<Taal> getTalen() {
    if (null == talen) {
      try {
        Collection<TaalDto> rows  = null;
        if (isZoek()) {
          formulier.persist(taal.getTaal());
          rows  = new TaalComponent().getAll(taal.getTaal()
                                                 .<TaalDto>makeFilter());
          setGefilterd(true);
          formulier = null;
          taal      = null;
          addInfo(PersistenceConstants.SEARCHED,
                  new Object[] {Integer.toString(rows.size()),
                                getTekst("doos.titel.talen").toLowerCase()});
        } else {
          rows  = new TaalComponent().getAll();
        }
        talen = new ArrayList<Taal>(rows.size());
        for (TaalDto taalDto : rows) {
          talen.add(new Taal(taalDto));
        }
      } catch (ObjectNotFoundException e) {
        talen = new ArrayList<Taal>(0);
        addInfo(PersistenceConstants.NOROWS,
                new Object[] {getTekst("doos.titel.talen").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }

    return talen;
  }

  /**
   * Geef de talen als een List met SelectItem.
   * 
   * @return
   */
  // TODO Sorteren op taal.
  public List<SelectItem> getSelectTalen() {
    if (null == selectTalen) {
      try {
        Collection<TaalDto> rows  = new TaalComponent().getAll();
        selectTalen = new LinkedList<SelectItem>();
        for (TaalDto taalDto : rows) {
          Taal  item  = new Taal(taalDto);
          selectTalen.add(new SelectItem(item.getTaal().getTaalKode(),
                                         item.getTaal().getTaal()));
        }
      } catch (ObjectNotFoundException e) {
        selectTalen = new LinkedList<SelectItem>();
        addInfo(PersistenceConstants.NOROWS,
            new Object[] {getTekst("doos.titel.talen").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }

    return selectTalen;
  }

  /**
   * Start nieuwe taal
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    taal      = new Taal(new TaalDto());
    formulier = new TaalForm();
    setSubTitel("doos.titel.taal.create");
  }

  /**
   * Reset de TaalBean.
   */
  @Override
  public void reset() {
    super.reset();

    filter      = null;
    formulier   = null;
    selectTalen = null;
    taal        = null;
    talen       = null;
  }

  /**
   * Bewaar de Taal in de database en in de List.
   */
  @Override
  public void save() {
    if (null != formulier && isWijzig()) {
      if (valideerForm()) {
        TaalComponent taalComponent = new TaalComponent();
        try {
          if (formulier.isGewijzigd()) {
            formulier.persist(taal.getTaal());
            taalComponent.update(taal.getTaal());
            addInfo(PersistenceConstants.UPDATED, taal.getTaal().getTaalKode());
          }
          setRetrieveMode();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   taal.getTaal().getTaalKode());
        } catch (ObjectNotFoundException e) {
          addError(PersistenceConstants.NOTFOUND, taal.getTaal().getTaalKode());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("save() niet toegestaan.");
    }
  }

  /**
   * Zoek de Taal(en) in de database.
   */
  @Override
  public void search() {
    if (null != formulier && isZoek()) {
      talen = null;
    } else {
      LOGGER.error("search() niet toegestaan.");
    }
  }

  /**
   * Zet de controller in 'Retrieve' modus.
   */
  private void setRetrieveMode() {
    setAktie(PersistenceConstants.RETRIEVE);
    formulier   = null;
    selectTalen = null;
    taal        = null;
  }

  /**
   * Valideer de invoer.
   */
  @Override
  public boolean valideerForm() {
    boolean correct = true;

    String  waarde  = formulier.getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.code"));
    } else {
      if (waarde.length() != 2) {
        correct = false;
        addError(PersistenceConstants.FIXLENGTH,
                 new Object[] {getTekst("label.code"), 2});
      }
    }

    waarde  = formulier.getEigennaam();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.taal.eigennaam"));
    }

    waarde  = formulier.getTaal();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.taal"));
    }

    return correct;
  }

  /**
   * @param taal
   */
  public void verwijder(Taal taal) {
    setAktie(PersistenceConstants.DELETE);
    this.taal       = taal;
    this.formulier  = new TaalForm(taal.getTaal());
    setSubTitel("doos.titel.taal.delete");
  }

  /**
   * @param taal
   */
  public void wijzig(Taal taal) {
    setAktie(PersistenceConstants.UPDATE);
    this.taal       = taal;
    this.formulier  = new TaalForm(taal.getTaal());
    setSubTitel("doos.titel.taal.update");
  }

  /**
   * Start zoek Taal(en)
   */
  public void zoek() {
    setAktie(PersistenceConstants.SEARCH);
    if (null == filter) {
      filter  = new Taal(new TaalDto());
    }
    taal      = filter;
    formulier = new TaalForm(taal.getTaal());
    setSubTitel("doos.titel.taal.search");
  }
}
