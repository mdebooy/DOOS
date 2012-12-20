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

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class TaalBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
    LoggerFactory.getLogger(TaalBean.class);

  public static final String  BEAN_NAME = "taalBean";

  private List<Taal>        talen;
  private List<SelectItem>  selectTalen;
  private Taal              filter;
  private Taal              taal;

  public TaalBean() {
    try {
      Collection<TaalDto> rows  = new TaalComponent().getAll();
      talen       = new ArrayList<Taal>(rows.size());
      selectTalen = new LinkedList<SelectItem>();
      for (TaalDto taalDto : rows) {
        Taal  lijn  = new Taal(taalDto);
        talen.add(lijn);
        selectTalen.add(new SelectItem(lijn.getTaal().getTaalKode(),
                                       lijn.getTaal().getTaal()));
      }
    } catch (ObjectNotFoundException e) {
      talen = null;
      addInfo("info.norows",
          new Object[] {getTekst("doos.title.talen").toLowerCase()});
    } catch (DoosRuntimeException e) {
      LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }
  }

  /**
   * Stop de laatste aktie.
   */
  public void cancel() {
    setAktie(PersistenceConstants.RETRIEVE);
    taal  = null;
  }

  /**
   * Schrijf de nieuwe Taal in de database.
   */
  public void createTaal() {
    if (null != taal
        && isNieuw()) {
      if (valideerForm() ) {
        TaalComponent taalComponent = new TaalComponent();
        try {
          taalComponent.insert(taal.getTaal());
          if (null == talen) {
            talen = new ArrayList<Taal>(1);
          }
          talen.add(taal);
          addInfo("info.create", taal.getTaal().getTaalKode());
          setAktie(PersistenceConstants.RETRIEVE);
          taal  = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createTaal() niet toegestaan.");
    }
  }

  /**
   * Verwijder de Taal uit de database en de List.
   */
  public void deleteTaal() {
    if (null != taal
        && isVerwijder()) {
      TaalComponent taalComponent = new TaalComponent();
      try {
        taalComponent.delete(taal.getTaal());
        talen.remove(taal);
        addInfo("info.delete", taal.getTaal().getTaalKode());
        setAktie(PersistenceConstants.RETRIEVE);
        taal  = null;
      } catch (ObjectNotFoundException e) {
        addError("persistence.notfound");
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteTaal() niet toegestaan.");
    }
  }

  /**
   * Exporteer de Talen.
   */
  public void export() {
    ExportData  exportData  = new ExportData();

    exportData.addMetadata("application", DoosBase.APPLICATION_NAME);
    exportData.addMetadata("auteur",      getUserId());
    exportData.addMetadata("lijstnaam",   "talen");

    exportData.setKleuren(getLijstKleuren());

    exportData.setKolommen(new String[] { "code", "taal", "eigennaam" });

    exportData.setType(getType());

    exportData.addVeld("ReportTitel",     getTekst("doos.title.languages"));
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
  public Taal getTaal() {
    return taal;
  }

  /**
   * Geef alle talen.
   * 
   * @return
   */
  public List<Taal> getTalen() {
    return talen;
  }

  /**
   * Geef de talen als een List met SelectItem.
   * 
   * @return
   */
  public List<SelectItem> getSelectTalen() {
    return selectTalen;
  }

  /**
   * Start nieuwe taal
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    taal      = new Taal(new TaalDto());
    setSubTitel("title.taal.create");
  }

  /**
   * Bewaar de Taal in de database en in de List.
   */
  public void saveTaal() {
    if (null != taal
        && isWijzig()) {
      if (valideerForm()) {
        TaalComponent taalComponent = new TaalComponent();
        try {
          taalComponent.update(taal.getTaal());
          talen.remove(taal);
          talen.add(taal);
          addInfo("info.update", taal.getTaal().getTaalKode());
          setAktie(PersistenceConstants.RETRIEVE);
          taal  = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (ObjectNotFoundException e) {
          addError("persistence.notfound");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("saveTaal() niet toegestaan.");
    }
  }

  /**
   * Zoek de Taal(en) in de database.
   */
  public void searchTaal() {
    if (null != taal
        && isZoek()) {
      try {
        Collection<TaalDto> rows  =
            new TaalComponent().getAll(taal.getTaal().<TaalDto>makeFilter());
        talen = new ArrayList<Taal>(rows.size());
        for (TaalDto taalDto : rows) {
          talen.add(new Taal(taalDto));
        }
        setGefilterd(true);
        taal      = null;
        addInfo("info.search",
                new Object[] {Integer.toString(talen.size()),
                              getTekst("doos.title.languages").toLowerCase()});
      } catch (ObjectNotFoundException e) {
        talen     = null;
        addInfo("info.norows",
            new Object[] {getTekst("doos.title.talen").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }
  }

  /**
   * Valideer de invoer.
   */
  @Override
  public boolean valideerForm() {
    boolean correct = true;

    String  waarde  = taal.getTaal().getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.code"));
    } else {
      if (waarde.length() != 2) {
        correct = false;
        addError("errors.fixlength", new Object[] {getTekst("label.code"), 2});
      }
    }

    waarde  = taal.getTaal().getEigennaam();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.taal.eigennaam"));
    }

    waarde  = taal.getTaal().getTaal();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.taal"));
    }

    return correct;
  }

  /**
   * @param taal
   */
  public void verwijder(Taal taal) {
    setAktie(PersistenceConstants.DELETE);
    this.taal = taal;
    setSubTitel("title.taal.delete");
  }

  /**
   * @param taal
   */
  public void wijzig(Taal taal) {
    setAktie(PersistenceConstants.UPDATE);
    this.taal = taal;
    setSubTitel("title.taal.update");
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
    setSubTitel("title.taal.search");
  }
}
