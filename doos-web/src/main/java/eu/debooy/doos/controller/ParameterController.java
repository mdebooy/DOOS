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
import eu.debooy.doos.form.Parameter;
import eu.debooy.doos.form.Upload;
import eu.debooy.doos.validator.ParameterValidator;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosParameter")
@SessionScoped
public class ParameterController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(ParameterController.class);

  private Parameter parameter;
  private Upload    upload;

  private void addParameter(Parameter param) {
    try {
      var aanwezig  = getParameterService().parameter(param.getSleutel());
      if (upload.isOverschrijven()
          && !param.getWaarde().equals(aanwezig.getWaarde())) {
        param.persist(aanwezig);
        getParameterService().save(aanwezig);
        upload.addGewijzigd();
      }
    } catch (ObjectNotFoundException e) {
      getParameterService().create(param);
      try {
        upload.addNieuw();
      } catch (DuplicateObjectException ex) {
        LOGGER.error("{} [{}]", param.getWaarde(), e.getLocalizedMessage());
        addError(PersistenceConstants.DUPLICATE, param.getSleutel());
      }
    } catch (DuplicateObjectException e) {
      LOGGER.error("{} [{}]", param.getWaarde(), e.getLocalizedMessage());
      addError(PersistenceConstants.DUPLICATE, param.getSleutel());
    } catch (NullPointerException e) {
      addError(PersistenceConstants.NOTFOUND, param.getSleutel());
    }
  }

  public void batch() {
    upload  = new Upload();
    setSubTitel("doos.titel.parameter.upload");
    redirect(PARAMETERUPLOAD_REDIRECT);
  }

  public void create() {
    parameter = new Parameter();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.parameter.create");
    redirect(PARAMETER_REDIRECT);
  }

  public void delete(String sleutel) {
    try {
      getParameterService().delete(sleutel);
      addInfo(PersistenceConstants.DELETED, sleutel);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, sleutel);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public Parameter getParameter() {
    return parameter;
  }

  public Collection<Parameter> getParameters() {
    return getParameterService().query();
  }

  public Upload getUpload() {
    return upload;
  }

  public void save() {
    var messages  = ParameterValidator.valideer(parameter);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      getParameterService().save(parameter);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          addInfo(PersistenceConstants.CREATED, parameter.getSleutel());
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, parameter.getSleutel());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      redirect(PARAMETERS_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, parameter.getSleutel());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, parameter.getSleutel());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void update(String sleutel) {
    parameter = new Parameter(getParameterService().parameter(sleutel));
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.parameter.update");
    redirect(PARAMETER_REDIRECT);
  }

  public void uploading() {
    var bestand = upload.getBestand();
    if (null == bestand) {
      addError("errors.nofile");
      return;
    }

    var properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [{}].", e.getLocalizedMessage());
      addError("errors.upload", bestand.getName());
      return;
    }

    upload.reset();

    for (Entry<Object, Object> entry : properties.entrySet()) {
      var sleutel = entry.getKey().toString();
      var waarde  = entry.getValue().toString();
      if (upload.isUtf8()) {
        waarde = new String(waarde.getBytes(StandardCharsets.ISO_8859_1),
                                            StandardCharsets.UTF_8);
      }
      var param     = new Parameter(sleutel, waarde);
      var messages  = ParameterValidator.valideer(param);
      if (messages.isEmpty()) {
        addParameter(param);
      } else {
        addMessage(messages);
      }
    }

    addInfo("message.upload", bestand.getName());

    upload.setGelezen(properties.size());
  }
}
