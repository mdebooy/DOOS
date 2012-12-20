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

  private I18nCode        filter;
  private I18nCode        i18nCode;
  private I18nCodeTekst   i18nCodeTekst;
  private List<I18nCode>  i18nCodes;

  public I18nCodeBean() {
    try {
      Collection<I18nCodeDto> rows  = new I18nCodeComponent().getAll();
      i18nCodes = new ArrayList<I18nCode>(rows.size());
      for (I18nCodeDto i18nCodeDto : rows) {
        i18nCodes.add(new I18nCode(i18nCodeDto));
      }
    } catch (ObjectNotFoundException e) {
      i18nCodes = null;
      addInfo("info.norows",
          new Object[] {getTekst("doos.title.i18nCodes").toLowerCase()});
    } catch (DoosRuntimeException e) {
      LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }
  }

  /**
   * Schrijf de nieuwe I18nCode in de database.
   */
  public void addI18nCodeTekst() {
    if (null != i18nCodeTekst
        && isNieuwDetail()) {
      if (valideerDetailForm() ) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          I18nCodeTekstDto  dto =
              new I18nCodeTekstDto(i18nCode.getI18nCode(),
                                   i18nCodeTekst.getI18nCodeTekst()
                                                .getId().getTaalKode(),
                                   i18nCodeTekst.getI18nCodeTekst().getTekst());
          i18nCodeComponent.insert(dto);
          i18nCode.getI18nCode().add(dto);
          i18nCodeComponent.update(i18nCode.getI18nCode());
          i18nCodes.remove(i18nCode);
          i18nCodes.add(i18nCode);
          addInfo("info.create", i18nCodeTekst.getI18nCodeTekst().getId()
                                              .getTaalKode());
          setDetailAktie(PersistenceConstants.RETRIEVE);
          i18nCodeTekst = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("addI18nCodeTekst() niet toegestaan.");
    }
  }

  /**
   * Cancel een CRUD aktie.
   */
  public void cancel() {
    setAktie(PersistenceConstants.RETRIEVE);
    i18nCode  = null;
    cancelDetail();

    redirect(I18NCODES_REDIRECT);
  }

  /**
   * Cancel een CRUD aktie.
   */
  public void cancelDetail() {
    setDetailAktie(PersistenceConstants.RETRIEVE);
    i18nCodeTekst = null;
  }

  /**
   * Schrijf de nieuwe I18nCode in de database.
   */
  public void createI18nCode() {
    if (null != i18nCode
        && isNieuw()) {
      if (valideerForm() ) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          i18nCodeComponent.insert(i18nCode.getI18nCode());
          if (null == i18nCodes) {
            i18nCodes = new ArrayList<I18nCode>(1);
          }
          i18nCodes.add(i18nCode);
          addInfo("info.create", i18nCode.getI18nCode().getCode());
          setAktie(PersistenceConstants.RETRIEVE);
          i18nCode  = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createI18nCode() niet toegestaan.");
    }
  }

  /**
   * Verwijder de I18nCode uit de database en de List.
   */
  public void deleteI18nCode() {
    if (null != i18nCode
        && isVerwijder()) {
      I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
      try {
        i18nCodeComponent.delete(i18nCode.getI18nCode());
        i18nCodes.remove(i18nCode);
        addInfo("info.delete", i18nCode.getI18nCode().getCode());
        setAktie(PersistenceConstants.RETRIEVE);
        i18nCode  = null;

        redirect(I18NCODES_REDIRECT);
      } catch (ObjectNotFoundException e) {
        addError("persistence.notfound");
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteI18nCode() niet toegestaan.");
    }
  }

  /**
   * Geef alle I18N codes.
   * 
   * @return
   */
  public List<I18nCode> getI18nCodes() {
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
      LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }

    return i18nCodeTeksten;
  }

  /**
   * Geef de geselecteerde/nieuwe I18nCode.
   * 
   * @return
   */
  public I18nCode getI18nCode() {
    return i18nCode;
  }

  /**
   * Geef de geselecteerde/nieuwe I18nCodeTekst.
   * 
   * @return
   */
  public I18nCodeTekst getI18nCodeTekst() {
    return i18nCodeTekst;
  }

  /**
   * Start nieuwe i18nCode
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    i18nCode  = new I18nCode(new I18nCodeDto());
    setSubTitel("title.i18nCode.create");

    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Start nieuwe i18nCode
   */
  public void nieuwDetail() {
    setDetailAktie(PersistenceConstants.CREATE);
    i18nCodeTekst   = new I18nCodeTekst(new I18nCodeTekstDto());
    setDetailSubTitel("title.i18nCodeTekst.create");
  }

  /**
   * Verwijder de I18nCodeTekst uit de database.
   */
  public void removeI18nCodeTekst() {
    if (null != i18nCodeTekst
        && isVerwijderDetail()) {
      I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
      try {
        i18nCode.getI18nCode().getTeksten()
                .remove(i18nCodeTekst.getI18nCodeTekst());
        i18nCodeComponent.delete(i18nCodeTekst.getI18nCodeTekst());
        addInfo("info.delete", i18nCodeTekst.getI18nCodeTekst().getId()
                                            .getTaalKode());
        i18nCodeComponent.update(i18nCode.getI18nCode());
        i18nCodes.remove(i18nCode);
        i18nCodes.add(i18nCode);
        setDetailAktie(PersistenceConstants.RETRIEVE);
        i18nCodeTekst = null;
      } catch (ObjectNotFoundException e) {
        addError("persistence.notfound");
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteI18nCodeTekst() niet toegestaan.");
    }
  }

  /**
   * Bewaar de I18nCode in de database en in de List.
   */
  public void saveI18nCode() {
    if (null != i18nCode
        && getAktie() == PersistenceConstants.UPDATE) {
      if (valideerForm()) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          i18nCodeComponent.update(i18nCode.getI18nCode());
          i18nCodes.remove(i18nCode);
          i18nCodes.add(i18nCode);
          addInfo("info.update", i18nCode.getI18nCode().getCode());
          setAktie(PersistenceConstants.RETRIEVE);
          i18nCode  = null;

          redirect(I18NCODES_REDIRECT);
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
      LOGGER.error("saveI18nCode() niet toegestaan.");
    }
  }

  /**
   * Bewaar de I18nCode in de database en in de List.
   */
  public void saveI18nCodeTekst() {
    if (null != i18nCodeTekst
        && isWijzigDetail()) {
      if (valideerDetailForm()) {
        I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();
        try {
          i18nCode.getI18nCode().getTeksten()
                                .remove(i18nCodeTekst.getI18nCodeTekst());
          i18nCode.getI18nCode().getTeksten()
                                .add(i18nCodeTekst.getI18nCodeTekst());
          i18nCodeComponent.update(i18nCodeTekst.getI18nCodeTekst());
          i18nCodes.remove(i18nCode);
          i18nCodes.add(i18nCode);
          addInfo("info.update", i18nCodeTekst.getI18nCodeTekst().getId()
                                              .getTaalKode());
          setDetailAktie(PersistenceConstants.RETRIEVE);
          i18nCodeTekst = null;
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
      LOGGER.error("saveI18nCodeTekst() niet toegestaan.");
    }
  }

  /**
   * Zoek de i18nCode(s) in de database.
   */
  public void searchI18nCode() {
    if (null != i18nCode
        && isZoek()) {
      try {
        Collection<I18nCodeDto> rows  =
            new I18nCodeComponent().getAll(i18nCode.getI18nCode()
                                                   .<I18nCodeDto>makeFilter());
        i18nCodes = new ArrayList<I18nCode>(rows.size());
        for (I18nCodeDto i18nCodeDto : rows) {
          i18nCodes.add(new I18nCode(i18nCodeDto));
        }
        setGefilterd(true);
        i18nCode  = null;
        addInfo("info.search",
                new Object[] {Integer.toString(i18nCodes.size()),
                              getTekst("doos.title.i18nCodes").toLowerCase()});
      } catch (ObjectNotFoundException e) {
        i18nCodes = null;
        addInfo("info.norows",
            new Object[] {getTekst("doos.title.i18nCodes").toLowerCase()});
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
    String  waarde  = i18nCode.getI18nCode().getCode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.code"));
    }
    if (waarde.length() > 100) {
      correct = false;
      addError("errors.maxlength", new Object[] {getTekst("label.code"), 100});
    }

    return correct;
  }

  /**
   * Valideer de 'detail' invoer.
   */
  @Override
  public boolean valideerDetailForm() {
    boolean correct = true;
    String  waarde  = i18nCodeTekst.getI18nCodeTekst().getId().getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.language"));
    }

    waarde  = i18nCodeTekst.getI18nCodeTekst().getTekst();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.tekst"));
    }
    if (waarde.length() > 1024) {
      correct = false;
      addError("errors.maxlength",
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
    setSubTitel("title.i18nCode.delete");

    redirect(I18NCODE_REDIRECT);
  }

  /**
   * @param i18nCode
   */
  public void verwijderDetail(I18nCodeTekst i18nCodeTekst) {
    setDetailAktie(PersistenceConstants.DELETE);
    this.i18nCodeTekst  = i18nCodeTekst;
    setDetailSubTitel("title.i18nCodeTekst.delete");
  }

  /**
   * @param i18nCode
   */
  public void wijzig(I18nCode i18nCode) {
    setAktie(PersistenceConstants.UPDATE);
    this.i18nCode = i18nCode;
    setSubTitel("title.i18nCode.view");

    redirect(I18NCODE_REDIRECT);
  }

  /**
   * @param i18nCodeTekst
   */
  public void wijzigDetail(I18nCodeTekst i18nCodeTekst) {
    setDetailAktie(PersistenceConstants.UPDATE);
    this.i18nCodeTekst  = i18nCodeTekst;
    setDetailSubTitel("title.i18nCodeTekst.update");
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
    setSubTitel("title.i18nCode.search");
  }
}
