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

import eu.debooy.doos.component.I18nCodeComponent;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.web.form.I18nCodeForm;
import eu.debooy.doos.web.form.I18nCodeTekstForm;
import eu.debooy.doos.web.model.I18nCode;
import eu.debooy.doos.web.model.I18nCodeTekst;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class I18nCodeBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(I18nCodeBean.class);

  public static final String BEAN_NAME = "i18nCodeBean";

  private I18nCode          filter;
  private I18nCode          i18nCode;
  private I18nCodeForm      formulier;
  private I18nCodeTekst     i18nCodeTekst;
  private I18nCodeTekstForm detailformulier;
  private List<I18nCode>    i18nCodes;

  public I18nCodeBean() {
  }

  /**
   * Cancel een CRUD aktie.
   */
  public void cancel() {
    i18nCode  = null;
    setRetrieveMode();
    cancelDetail();
  }

  /**
   * Cancel een CRUD aktie.
   */
  public void cancelDetail() {
    i18nCodeTekst = null;
    setRetrieveModeDetail();
  }

  /**
   * Schrijf de nieuwe I18nCode in de database.
   */
  @Override
  public void create() {
    if (null != formulier && isNieuw()) {
      if (valideerForm() ) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          formulier.persist(i18nCode.getI18nCode());
          i18nCodeComponent.insert(i18nCode.getI18nCode());
          if (null == i18nCodes) {
            i18nCodes = new ArrayList<I18nCode>(1);
          }
          i18nCodes.add(i18nCode);
          addInfo(PersistenceConstants.CREATED,
                  i18nCode.getI18nCode().getCode());
          setAktie(PersistenceConstants.UPDATE);
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   i18nCode.getI18nCode().getCode());
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
   * Schrijf de nieuwe I18nCode in de database.
   */
  @Override
  public void createDetail() {
    if (null != detailformulier && isNieuwDetail()) {
      if (valideerDetailForm() ) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          detailformulier.persist(i18nCodeTekst.getI18nCodeTekst());
          // TODO Waarom de codeId zelf opvullen?
          i18nCodeTekst.getI18nCodeTekst()
                       .setCodeId(i18nCode.getI18nCode().getCodeId());
          i18nCode.getI18nCode().add(i18nCodeTekst.getI18nCodeTekst());
          i18nCodeComponent.update(i18nCode.getI18nCode());
          addInfo(PersistenceConstants.CREATED,
                  i18nCodeTekst.getI18nCodeTekst().getTaalKode());
          setRetrieveModeDetail();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   i18nCodeTekst.getI18nCodeTekst().getTaalKode());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createDetail() niet toegestaan.");
    }
  }

  /**
   * Verwijder de I18nCode uit de database en de List.
   */
  @Override
  public void delete() {
    if (null != formulier && isVerwijder()) {
      I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
      try {
        i18nCodeComponent.delete(i18nCode.getI18nCode());
        i18nCodes.remove(i18nCode);
        addInfo(PersistenceConstants.DELETED, i18nCode.getI18nCode().getCode());
        setRetrieveMode();
      } catch (ObjectNotFoundException e) {
        addError(PersistenceConstants.NOTFOUND,
                 i18nCode.getI18nCode().getCode());
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("delete() niet toegestaan.");
    }
  }

  /**
   * Verwijder de I18nCodeTekst uit de database.
   */
  @Override
  public void deleteDetail() {
    if (null != detailformulier && isVerwijderDetail()) {
      I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
      try {
        i18nCode.getI18nCode().getTeksten()
                              .remove(i18nCodeTekst.getI18nCodeTekst());
        i18nCodeComponent.delete(i18nCodeTekst.getI18nCodeTekst());
        addInfo(PersistenceConstants.DELETED,
                i18nCodeTekst.getI18nCodeTekst().getTaalKode());
        i18nCodeComponent.update(i18nCode.getI18nCode());
        setRetrieveModeDetail();
      } catch (ObjectNotFoundException e) {
        addError(PersistenceConstants.NOTFOUND,
                 i18nCodeTekst.getI18nCodeTekst().getTaalKode());
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteDetail() niet toegestaan.");
    }
  }

  /**
   * Geef alle I18N codes.
   * 
   * @return
   */
  public List<I18nCode> getI18nCodes() {
    if (null == i18nCodes) {
      try {
        Collection<I18nCodeDto> rows  = null;
        if (isZoek()) {
          formulier.persist(i18nCode.getI18nCode());
          rows      = new I18nCodeComponent()
                            .getAll(i18nCode.getI18nCode()
                                            .<I18nCodeDto>makeFilter(true));
          setGefilterd(true);
          formulier = null;
          i18nCode  = null;
          addInfo(PersistenceConstants.SEARCHED,
                  new Object[] {Integer.toString(rows.size()),
                                getTekst("doos.titel.i18nCodes").toLowerCase()});
        } else {
          rows  = new I18nCodeComponent().getAll();
        }
        i18nCodes = new ArrayList<I18nCode>(rows.size());
        for (I18nCodeDto i18nCodeDto : rows) {
          i18nCodes.add(new I18nCode(i18nCodeDto));
        }
      } catch (ObjectNotFoundException e) {
        i18nCodes = new ArrayList<I18nCode>(0);
        addInfo(PersistenceConstants.NOROWS,
                new Object[] {getTekst("doos.titel.i18nCodes").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }

    return i18nCodes;
  }

  /**
   * Geef alle I18N Code Teksten.
   * 
   * @return
   */
  public List<I18nCodeTekst> getI18nCodeTeksten() {
    List<I18nCodeTekst> i18nCodeTeksten = null;
    try {
      List<I18nCodeTekstDto>  rows  = i18nCode.getI18nCode().getTeksten();
      i18nCodeTeksten = new ArrayList<I18nCodeTekst>(rows.size());
      for (I18nCodeTekstDto i18nCodeTekstDto : rows) {
        i18nCodeTeksten.add(new I18nCodeTekst(i18nCodeTekstDto));
      }
    } catch (ObjectNotFoundException e) {
      i18nCodeTeksten = null;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }

    return i18nCodeTeksten;
  }

  /**
   * Geef de geselecteerde/nieuwe I18nCode.
   * 
   * @return
   */
  public I18nCodeForm getI18nCode() {
    return formulier;
  }

  /**
   * Geef de geselecteerde/nieuwe I18nCodeTekst.
   * 
   * @return
   */
  public I18nCodeTekstForm getI18nCodeTekst() {
    return detailformulier;
  }

  /**
   * Start nieuwe i18nCode
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    formulier = new I18nCodeForm();
    i18nCode  = new I18nCode(new I18nCodeDto());
    setSubTitel("doos.titel.i18nCode.create");
  }

  /**
   * Start nieuwe i18nCodeTekst
   */
  public void nieuwI18nCodeTekst() {
    setDetailAktie(PersistenceConstants.CREATE);
    detailformulier = new I18nCodeTekstForm();
    i18nCodeTekst   = new I18nCodeTekst(new I18nCodeTekstDto());
    setDetailSubTitel("doos.titel.i18nCodeTekst.create");
  }

  /**
   * Reset de I18nCodeBean.
   */
  @Override
  public void reset() {
    super.reset();

    setRetrieveMode();
    setRetrieveModeDetail();

    filter    = null;
    i18nCodes = null;
  }

  /**
   * Bewaar de I18nCode in de database en in de List.
   */
  @Override
  public void save() {
    if (null != formulier && isWijzig()) {
      if (valideerForm()) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          if (formulier.isGewijzigd()) {
            formulier.persist(i18nCode.getI18nCode());
            i18nCodeComponent.update(i18nCode.getI18nCode());
            addInfo(PersistenceConstants.UPDATED,
                    i18nCode.getI18nCode().getCode());
          }
          setRetrieveMode();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   i18nCode.getI18nCode().getCode());
        } catch (ObjectNotFoundException e) {
          addError(PersistenceConstants.NOTFOUND,
                   i18nCode.getI18nCode().getCode());
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
   * Bewaar de I18nCode in de database en in de List.
   */
  @Override
  public void saveDetail() {
    if (null != detailformulier && isWijzigDetail()) {
      if (valideerDetailForm()) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          if (detailformulier.isGewijzigd()) {
            detailformulier.persist(i18nCodeTekst.getI18nCodeTekst());
            i18nCodeComponent.update(i18nCodeTekst.getI18nCodeTekst());
            addInfo(PersistenceConstants.UPDATED,
                    i18nCodeTekst.getI18nCodeTekst().getTaalKode());
          }
          setRetrieveModeDetail();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   i18nCodeTekst.getI18nCodeTekst().getTaalKode());
        } catch (ObjectNotFoundException e) {
          addError(PersistenceConstants.NOTFOUND,
                   i18nCodeTekst.getI18nCodeTekst().getTaalKode());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("saveDetail() niet toegestaan.");
    }
  }

  /**
   * Zoek de i18nCode(s) in de database.
   */
  @Override
  public void search() {
    if (null != formulier && isZoek()) {
      i18nCodes = null;
      i18nCodes = getI18nCodes();
    } else {
      LOGGER.error("search() niet toegestaan.");
    }
  }

  /**
   * Valideer de invoer.
   */
  @Override
  public boolean valideerForm() {
    boolean correct = true;
    String  waarde  = formulier.getCode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.code"));
    }
    if (waarde.length() > 100) {
      correct = false;
      addError(PersistenceConstants.MAXLENGTH,
               new Object[] {getTekst("label.code"), 100});
    }

    return correct;
  }

  /**
   * Zet de controller in 'Retrieve' modus.
   */
  private void setRetrieveMode() {
    setAktie(PersistenceConstants.RETRIEVE);
    formulier   = null;
    i18nCode    = null;
  }

  /**
   * Zet de controller in 'Retrieve' modus.
   */
  private void setRetrieveModeDetail() {
    setDetailAktie(PersistenceConstants.RETRIEVE);
    detailformulier = null;
    i18nCodeTekst   = null;
  }

  /**
   * Valideer de 'detail' invoer.
   */
  @Override
  public boolean valideerDetailForm() {
    boolean correct = true;
    String  waarde  = detailformulier.getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.taal"));
    }

    waarde  = detailformulier.getTekst();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.tekst"));
    }
    if (waarde.length() > 1024) {
      correct = false;
      addError(PersistenceConstants.MAXLENGTH,
               new Object[] {getTekst("label.tekst"), 1024});
    }

    return correct;
  }

  /**
   * @param i18nCode
   */
  public void verwijder(I18nCode i18nCode) {
    setAktie(PersistenceConstants.DELETE);
    this.i18nCode = i18nCode;
    formulier     = new I18nCodeForm(i18nCode.getI18nCode());
    setSubTitel("doos.titel.i18nCode.delete");
  }

  /**
   * @param i18nCode
   */
  public void verwijder(I18nCodeTekst i18nCodeTekst) {
    setDetailAktie(PersistenceConstants.DELETE);
    this.i18nCodeTekst  = i18nCodeTekst;
    detailformulier     = new I18nCodeTekstForm(i18nCodeTekst.getI18nCodeTekst());
    setDetailSubTitel("doos.titel.i18nCodeTekst.delete");
  }

  /**
   * @param i18nCode
   */
  public void wijzig(I18nCode i18nCode) {
    setAktie(PersistenceConstants.UPDATE);
    this.i18nCode = i18nCode;
    formulier     = new I18nCodeForm(i18nCode.getI18nCode());
    setSubTitel("doos.titel.i18nCode.update");
  }

  /**
   * @param i18nCodeTekst
   */
  public void wijzig(I18nCodeTekst i18nCodeTekst) {
    setDetailAktie(PersistenceConstants.UPDATE);
    detailformulier     =
        new I18nCodeTekstForm(i18nCodeTekst.getI18nCodeTekst());
    this.i18nCodeTekst  = i18nCodeTekst;
    setDetailSubTitel("doos.titel.i18nCodeTekst.update");
  }

  /**
   * Start zoek I18nCode(s)
   */
  public void zoek() {
    setAktie(PersistenceConstants.SEARCH);
    if (null == filter) {
      filter  = new I18nCode(new I18nCodeDto());
    }
    i18nCode  = filter;
    formulier = new I18nCodeForm(i18nCode.getI18nCode());
    setSubTitel("doos.titel.i18nCode.search");
  }
}
