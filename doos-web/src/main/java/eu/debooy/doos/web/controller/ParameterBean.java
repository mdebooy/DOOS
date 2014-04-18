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

import eu.debooy.doos.component.ParameterComponent;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.web.form.ParameterForm;
import eu.debooy.doos.web.model.Parameter;
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
public class ParameterBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
    LoggerFactory.getLogger(ParameterBean.class);

  public static final String  BEAN_NAME = "parameterBean";

  private List<Parameter> parameters;
  private Parameter       filter;
  private Parameter       parameter;
  private ParameterForm   formulier;

  public ParameterBean() {
    setInvoer(getExternalContext().isUserInRole(DoosBase.ADMIN_ROLE));
  }

  /**
   * Stop de laatste aktie.
   */
  public void cancel() {
    if (isZoek()) {
      setGefilterd(false);
      parameters  = null;
    }
    setRetrieveMode();
  }

  /**
   * Schrijf de nieuwe Parameter in de database.
   */
  @Override
  public void create() {
    if (null != formulier && isNieuw()) {
      if (valideerForm() ) {
        ParameterComponent parameterComponent = new ParameterComponent();
        try {
          formulier.persist(parameter.getParameter());
          parameterComponent.insert(parameter.getParameter());
          if (null == parameters) {
            parameters  = new ArrayList<Parameter>(1);
          }
          parameters.add(parameter);
          addInfo(PersistenceConstants.CREATED,
                  parameter.getParameter().getSleutel());
          setRetrieveMode();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   parameter.getParameter().getSleutel());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createParameter() niet 0toegestaan.");
    }
  }

  /**
   * Verwijder de Parameter uit de database en de List.
   */
  @Override
  public void delete() {
    if (null != parameter && isVerwijder()) {
      ParameterComponent parameterComponent = new ParameterComponent();
      try {
        parameterComponent.delete(parameter.getParameter());
        parameters.remove(parameter);
        addInfo(PersistenceConstants.DELETED,
                parameter.getParameter().getSleutel());
        setRetrieveMode();
      } catch (ObjectNotFoundException e) {
        addError(PersistenceConstants.NOTFOUND,
                 parameter.getParameter().getSleutel());
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteParameter() niet toegestaan.");
    }
  }

  /**
   * Geef de geselecteerde/nieuwe Parameter.
   * 
   * @return
   */
  public ParameterForm getParameter() {
    return formulier;
  }

  /**
   * Geef de geselecteerde/nieuwe Parameter.
   * 
   * @return
   */
  public List<Parameter> getParameters() {
    if (null == parameters) {
      try {
        Collection<ParameterDto>  rows  = null;
        if (isZoek()) {
          formulier.persist(parameter.getParameter());
          rows      = new ParameterComponent()
                            .getAll(parameter.getParameter()
                                             .<ParameterDto>makeFilter(true));
          setGefilterd(true);
          formulier = null;
          parameter = null;
          addInfo(PersistenceConstants.SEARCHED,
                  new Object[] {Integer.toString(rows.size()),
                                getTekst("doos.titel.parameters")
                                  .toLowerCase()});
        } else {
          rows  = new ParameterComponent().getAll();
        }
        parameters  = new ArrayList<Parameter>(rows.size());
        for (ParameterDto parameterDto : rows) {
          parameters.add(new Parameter(parameterDto));
        }
      } catch (ObjectNotFoundException e) {
        parameters  = new ArrayList<Parameter>(0);
        addInfo(PersistenceConstants.NOROWS,
                new Object[] {getTekst("doos.titel.parameters").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("RT: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }

    return parameters;
  }

  /**
   * Start nieuwe Parameter
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    parameter = new Parameter(new ParameterDto());
    formulier = new ParameterForm();
    setSubTitel("doos.titel.parameter.create");
  }

  /**
   * Reset de ParameterBean.
   */
  @Override
  public void reset() {
    super.reset();

    filter      = null;
    formulier   = null;
    parameter   = null;
    parameters  = null;
  }

  /**
   * Bewaar de Parameter in de database en in de List.
   */
  @Override
  public void save() {
    if (null != formulier && isWijzig()) {
      if (valideerForm()) {
        ParameterComponent parameterComponent = new ParameterComponent();
        try {
          if (formulier.isGewijzigd()) {
            formulier.persist(parameter.getParameter());
            parameterComponent.update(parameter.getParameter());
            addInfo(PersistenceConstants.UPDATED,
                    parameter.getParameter().getSleutel());
          }
          setRetrieveMode();
        } catch (DuplicateObjectException e) {
          addError(PersistenceConstants.DUPLICATE,
                   parameter.getParameter().getSleutel());
        } catch (ObjectNotFoundException e) {
          addError(PersistenceConstants.NOTFOUND,
                   parameter.getParameter().getSleutel());
        } catch (DoosRuntimeException e) {
          LOGGER.error("RT: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("saveParameter() niet toegestaan.");
    }
  }

  /**
   * Zoek de Parameter(s) in de database.
   */
  @Override
  public void search() {
    if (null != formulier && isZoek()) {
      parameters  = null;
    } else {
      LOGGER.error("searchParameter() niet toegestaan.");
    }
  }

  /**
   * Zet de controller in 'Retrieve' modus.
   */
  private void setRetrieveMode() {
    setAktie(PersistenceConstants.RETRIEVE);
    formulier = null;
    parameter = null;
  }

  /**
   * Valideer de invoer.
   */
  @Override
  public boolean valideerForm() {
    boolean correct = true;
    String  waarde  = formulier.getSleutel();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.sleutel"));
    }

    waarde  = formulier.getWaarde();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError(PersistenceConstants.REQUIRED, getTekst("label.waarde"));
    }

    return correct;
  }

  /**
   * @param parameter
   */
  public void verwijder(Parameter parameter) {
    setAktie(PersistenceConstants.DELETE);
    this.parameter  = parameter;
    formulier       = new ParameterForm(parameter.getParameter());
    setSubTitel("doos.titel.parameter.delete");
  }

  /**
   * @param parameter
   */
  public void wijzig(Parameter parameter) {
    setAktie(PersistenceConstants.UPDATE);
    this.parameter  = parameter;
    formulier       = new ParameterForm(parameter.getParameter());
    setSubTitel("doos.titel.parameter.update");
  }

  /**
   * Start zoek Parameter(s)
   */
  public void zoek() {
    setAktie(PersistenceConstants.SEARCH);
    if (null == filter) {
      filter  = new Parameter(new ParameterDto());
    }
    parameter = filter;
    formulier = new ParameterForm(parameter.getParameter());
    setSubTitel("doos.titel.parameter.search");
  }
}
