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
import eu.debooy.doos.web.model.Parameter;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class ParameterBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
    LoggerFactory.getLogger(ParameterBean.class);

  public static final String  BEAN_NAME = "parameterBean";

  private List<Parameter> parameters;
  private Parameter       parameter;
  private Parameter       filter;

  public ParameterBean() {
    try {
      Collection<ParameterDto>  rows  = new ParameterComponent().getAll();
      parameters  = new ArrayList<Parameter>(rows.size());
      for (ParameterDto parameterDto : rows) {
        parameters.add(new Parameter(parameterDto));
      }
    } catch (ObjectNotFoundException e) {
      parameters  = null;
      addInfo("info.norows",
          new Object[] {getTekst("doos.title.parameters").toLowerCase()});
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
    parameter = null;
  }

  /**
   * Schrijf de nieuwe Parameter in de database.
   */
  public void createParameter() {
    if (null != parameter
        && isNieuw()) {
      if (valideerForm() ) {
        ParameterComponent parameterComponent = new ParameterComponent();
        try {
          parameterComponent.insert(parameter.getParameter());
          if (null == parameters) {
            parameters  = new ArrayList<Parameter>(1);
          }
          parameters.add(parameter);
          addInfo("info.create", parameter.getParameter().getSleutel());
          setAktie(PersistenceConstants.RETRIEVE);
          parameter = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createParameter() niet toegestaan.");
    }
  }

  /**
   * Verwijder de Parameter uit de database en de List.
   */
  public void deleteParameter() {
    if (null != parameter
        && isVerwijder()) {
      ParameterComponent parameterComponent = new ParameterComponent();
      try {
        parameterComponent.delete(parameter.getParameter());
        parameters.remove(parameter);
        addInfo("info.delete", parameter.getParameter().getSleutel());
        setAktie(PersistenceConstants.RETRIEVE);
        parameter = null;
      } catch (ObjectNotFoundException e) {
        addError("persistence.notfound");
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
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
  public Parameter getParameter() {
    return parameter;
  }

  /**
   * Geef de geselecteerde/nieuwe Parameter.
   * 
   * @return
   */
  public List<Parameter> getParameters() {
    return parameters;
  }

  /**
   * Start nieuwe Parameter
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    parameter = new Parameter(new ParameterDto());
    setSubTitel("title.parameter.create");
  }

  /**
   * Bewaar de Parameter in de database en in de List.
   */
  public void saveParameter() {
    if (null != parameter
        && isWijzig()) {
      if (valideerForm()) {
        ParameterComponent parameterComponent = new ParameterComponent();
        try {
          parameterComponent.update(parameter.getParameter());
          parameters.remove(parameter);
          parameters.add(parameter);
          addInfo("info.update", parameter.getParameter().getSleutel());
          setAktie(PersistenceConstants.RETRIEVE);
          parameter = null;
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
      LOGGER.error("saveParameter() niet toegestaan.");
    }
  }

  /**
   * Zoek de Parameter(s) in de database.
   */
  public void searchParameter() {
    if (null != parameter
        && isZoek()) {
      try {
        Collection<ParameterDto>  rows  =
            new ParameterComponent()
                  .getAll(parameter.getParameter().<ParameterDto>makeFilter());
        parameters  = new ArrayList<Parameter>(rows.size());
        for (ParameterDto parameterDto : rows) {
          parameters.add(new Parameter(parameterDto));
        }
        setGefilterd(true);
        parameter   = null;
        addInfo("info.search",
                new Object[] {Integer.toString(parameters.size()),
                              getTekst("doos.title.parameters").toLowerCase()});
      } catch (ObjectNotFoundException e) {
        parameters  = null;
        addInfo("info.norows",
            new Object[] {getTekst("doos.title.parameters").toLowerCase()});
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
    String  waarde  = parameter.getParameter().getSleutel();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.key"));
    }

    waarde  = parameter.getParameter().getWaarde();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.value"));
    }

    return correct;
  }

  /**
   * @param parameter
   */
  public void verwijder(Parameter parameter) {
    setAktie(PersistenceConstants.DELETE);
    this.parameter  = parameter;
    setSubTitel("title.parameter.delete");
  }

  /**
   * @param parameter
   */
  public void wijzig(Parameter parameter) {
    setAktie(PersistenceConstants.UPDATE);
    this.parameter  = parameter;
    setSubTitel("title.parameter.update");
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
    setSubTitel("title.parameter.search");
  }
}
