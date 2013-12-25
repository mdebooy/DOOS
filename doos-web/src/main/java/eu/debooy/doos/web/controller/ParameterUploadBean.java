/**
 * Copyright 2013 Marco de Booij
 *
 * Licensed under de EUPL, Version 1.1 or - as soon they will be approved by
 * de European Commission - subsequent versions of de EUPL (the "Licence");
 * you may not use this work except in compliance with de Licence. You may
 * obtain a copy of de Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under de Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See de Licence for de specific taal governing permissions and
 * limitations under de Licence.
 */
package eu.debooy.doos.web.controller;

import eu.debooy.doos.component.ParameterComponent;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class ParameterUploadBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(ParameterUploadBean.class);

  private boolean       overschrijven         = false;
  private boolean       utf8                  = false;
  private int           gelezen               = 0;
  private int           nieuweParameters      = 0;
  private int           gewijzigdeParameters  = 0;
  private UploadedFile  bestand;

  public static final String BEAN_NAME = "parameterUploadBean";

  public ParameterUploadBean() {
  }

  /**
   * @return het bestand
   */
  public UploadedFile getBestand() {
    return bestand;
  }

  /**
   * @return de gelezen
   */
  public int getGelezen() {
    return gelezen;
  }

  /**
   * @return de gewijzigdeParameters
   */
  public int getgewijzigdeParameters() {
    return gewijzigdeParameters;
  }

  /**
   * @return de nieuweParameters
   */
  public int getnieuweParameters() {
    return nieuweParameters;
  }

  /**
   * @return de overschrijven
   */
  public boolean isOverschrijven() {
    return overschrijven;
  }

  /**
   * @return de overschrijven
   */
  public boolean isUtf8() {
    return utf8;
  }

  /**
   * Uitbreiding van de reset met de bean variabelen.
   */
  @Override
  public void reset() {
    super.reset();

    overschrijven         = false;
    utf8                  = false;
    gelezen               = 0;
    nieuweParameters      = 0;
    gewijzigdeParameters  = 0;
    bestand               = null;
  }

  /**
   * @param bestand het bestand
   */
  public void setBestand(UploadedFile bestand) {
    this.bestand  = bestand;
  }

  /**
   * @param overschrijven overschrijven?
   */
  public void setOverschrijven(boolean overschrijven) {
    this.overschrijven  = overschrijven;
  }

  /**
   * @param utf8 utf8?
   */
  public void setUtf8(boolean utf8) {
    this.utf8  = utf8;
  }

  /**
   * Laad het propertiesbestand met I18N teksten in en sla ze op in de
   * database. 
   */
  public void upload() {
    ParameterComponent  parameterComponent  = new ParameterComponent();

    gelezen               = 0;
    nieuweParameters      = 0;
    gewijzigdeParameters  = 0;

    if (null == bestand) {
      addError("load.error");
      return;
    }

    Properties  properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [" + e.getMessage() + "].");
      addError("load.error");
      return;
    }

    Enumeration<Object> enums     = properties.keys();
    ParameterDto        parameter = null;
    while (enums.hasMoreElements()) {
      String  sleutel = enums.nextElement().toString();
      String  waarde  = properties.getProperty(sleutel);
      try {
        if (isUtf8()) {
          waarde = new String(waarde.getBytes("ISO-8859-1"), "UTF-8");
        }
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("Waarde " + waarde + " [" + e.getMessage() + "]");
        addError("errors.encoding", sleutel);
      }
      if (sleutel.length() <= 100
          && waarde.length() <= 255) {
        try {
          parameter = parameterComponent.getParameter(sleutel);
          if (overschrijven && !waarde.equals(parameter.getWaarde())) {
            parameter.setWaarde(waarde);
            parameterComponent.update(parameter);
            gewijzigdeParameters++;
          }
        } catch (ObjectNotFoundException e) {
          parameter = new ParameterDto(sleutel, waarde);
          parameterComponent.insert(parameter);
          nieuweParameters++;
        }
  
      } else {
        if (sleutel.length() > 100) {
          addError(PersistenceConstants.MAXLENGTH,
                   new Object[] {getTekst("label.sleutel"), 100});
        }
        if (waarde.length() > 255) {
          addError(PersistenceConstants.MAXLENGTH,
                   new Object[] {getTekst("label.waarde"), 1024});
        }
      }
    }

    gelezen = properties.size();
  }
}
