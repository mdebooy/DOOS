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
import eu.debooy.doos.domain.ParameterDto;
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
import java.util.Map.Entry;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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

  private static final  String  LBL_PARAMETER = "label.parameter";
  private static final  String  TIT_CREATE    = "doos.titel.parameter.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.parameter.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.parameter.update";
  private static final  String  TIT_UPLOAD    = "doos.titel.parameter.upload";

  private Parameter     parameter;
  private ParameterDto  parameterDto;
  private Upload        upload;

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
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    upload  = new Upload();

    setSubTitel(getTekst(TIT_UPLOAD));
    redirect(PARAMETERUPLOAD_REDIRECT);
  }

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    parameter     = new Parameter();
    parameterDto  = new ParameterDto();

    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(PARAMETER_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var sleutel = parameter.getSleutel();
    try {
      getParameterService().delete(sleutel);
      parameter     = new Parameter();
      parameterDto  = new ParameterDto();
      addInfo(PersistenceConstants.DELETED, sleutel);
      redirect(PARAMETERS_REDIRECT);
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

  public Upload getUpload() {
    return upload;
  }

  public void retrieve() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(ParameterDto.COL_SLEUTEL)) {
      addError(ComponentsConstants.GEENPARAMETER, ParameterDto.COL_SLEUTEL);
      return;
    }

    var sleutel = ec.getRequestParameterMap().get(ParameterDto.COL_SLEUTEL);

    try {
      parameter = new Parameter(getParameterService().parameter(sleutel));
      setAktie(PersistenceConstants.RETRIEVE);
      setSubTitel(getTekst(TIT_RETRIEVE));
      redirect(PARAMETER_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_PARAMETER);
    }
  }

  public void save() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = ParameterValidator.valideer(parameter);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    var sleutel = parameter.getSleutel();
    try {
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          parameter.persist(parameterDto);
          getParameterService().save(parameterDto);
          addInfo(PersistenceConstants.CREATED, sleutel);
          update();
          break;
        case PersistenceConstants.UPDATE:
          parameter.persist(parameterDto);
          getParameterService().save(parameterDto);
          addInfo(PersistenceConstants.UPDATED, sleutel);
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      redirect(PARAMETERS_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, sleutel);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, sleutel);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void update() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE));
  }

  public void uploading() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

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

    verwerkProperties(properties);

    addInfo("message.upload", bestand.getName());
    upload.setGelezen(properties.size());
  }

  private void verwerkProperties(Properties properties) {
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
  }
}
