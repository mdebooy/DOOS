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
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.myfaces.custom.fileupload.UploadedFile;
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

  /**
   * Prepareer een Upload
   */
  public void batch() {
    upload  = new Upload();
    setSubTitel("doos.titel.parameter.upload");
    redirect(PARAMETERUPLOAD_REDIRECT);
  }

  /**
   * Prepareer een nieuwe Parameter.
   */
  public void create() {
    parameter = new Parameter();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.parameter.create");
    redirect(PARAMETER_REDIRECT);
  }

  /**
   * Verwijder de Parameter
   * 
   * @param String sleutel
   */
  public void delete(String sleutel) {
    try {
      getParameterService().delete(sleutel);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, sleutel);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo("info.delete", sleutel);
  }

  /**
   * Geef de geselecteerde Parameter.
   * 
   * @return Parameter
   */
  public Parameter getParameter() {
    return parameter;
  }

  /**
   * Geef de lijst met parameters.
   * 
   */
  public Collection<Parameter> getParameters() {
    return getParameterService().query();
  }

  /**
   * Geef de Upload informatie.
   * 
   * @return Upload
   */
  public Upload getUpload() {
    return upload;
  }

  /**
   * Persist de Parameter
   */
  public void save() {
    List<Message> messages  = ParameterValidator.valideer(parameter);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      getParameterService().save(parameter);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, parameter.getSleutel());
      return;
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, parameter.getSleutel());
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }

    redirect(PARAMETERS_REDIRECT);
  }


  /**
   * Zet de Parameter die gewijzigd gaat worden klaar.
   * 
   * @param String sleutel
   */
  public void update(String sleutel) {
    parameter = new Parameter(getParameterService().parameter(sleutel));
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.parameter.update");
    redirect(PARAMETER_REDIRECT);
  }

  /**
   * Laad het propertiesbestand met parameters in en sla ze op in de database. 
   */
  public void uploading() {
    UploadedFile  bestand = upload.getBestand();
    if (null == bestand) {
      addError("errors.nofile");
      return;
    }

    Properties  properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [" + e.getMessage() + "].");
      addError("errors.upload", bestand.getName());
      return;
    }

    upload.reset();

    for (Entry<Object, Object> rij : properties.entrySet()) {
      String  sleutel = rij.getKey().toString();
      String  waarde  = rij.getValue().toString();
      try {
        if (upload.isUtf8()) {
          waarde = new String(waarde.getBytes("ISO-8859-1"), "UTF-8");
        }
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("Waarde " + waarde + " [" + e.getMessage() + "]");
        addError("errors.encoding", sleutel);
      }
      Parameter     param     = new Parameter(sleutel, waarde);
      List<Message> messages  = ParameterValidator.valideer(param);
      if (messages.isEmpty()) {
        try {
          ParameterDto  aanwezig  = getParameterService().parameter(sleutel);
          if (upload.isOverschrijven()
              && !waarde.equals(aanwezig.getWaarde())) {
            param.persist(aanwezig);
            getParameterService().save(aanwezig);
            upload.addGewijzigd();
          }
        } catch (ObjectNotFoundException e) {
          getParameterService().create(param);
          upload.addNieuw();
        }
      } else {
        addMessage(messages);
      }
    }

    upload.setGelezen(properties.size());
  }
}
