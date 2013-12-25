/**
 * Copyright 2011 Marco de Booij
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

import eu.debooy.doos.component.I18nCodeComponent;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class I18nUploadBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(I18nUploadBean.class);

  private boolean       overschrijven     = false;
  private boolean       utf8              = false;
  private int           gelezen           = 0;
  private int           nieuweCodes       = 0;
  private int           nieuweWaardes     = 0;
  private int           gewijzigdeWaardes = 0;
  private String        taal              = "";
  private UploadedFile  bestand;

  public static final String BEAN_NAME = "i18nUploadBean";

  public I18nUploadBean() {
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
   * @return de gewijzigdeWaardes
   */
  public int getGewijzigdeWaardes() {
    return gewijzigdeWaardes;
  }

  /**
   * @return de nieuweCodes
   */
  public int getNieuweCodes() {
    return nieuweCodes;
  }

  /**
   * @return de nieuweWaardes
   */
  public int getNieuweWaardes() {
    return nieuweWaardes;
  }

  /**
   * @return de taal
   */
  public String getTaal() {
    return taal;
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

    overschrijven     = false;
    utf8              = false;
    gelezen           = 0;
    nieuweCodes       = 0;
    nieuweWaardes     = 0;
    gewijzigdeWaardes = 0;
    taal              = "";
    bestand           = null;
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
    I18nCodeComponent i18nCodeComponent = new I18nCodeComponent();

    gelezen           = 0;
    nieuweCodes       = 0;
    nieuweWaardes     = 0;
    gewijzigdeWaardes = 0;

    if (null == bestand) {
      addError("load.error");
      return;
    }

    String  split[]   = FilenameUtils.getBaseName(bestand.getName()).split("_");
    taal  = split[split.length-1].toLowerCase();

    Properties  properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [" + e.getMessage() + "].");
      addError("load.error");
      return;
    }

    Enumeration<Object> enums         = properties.keys();
    I18nCodeDto         i18nCode      = null;
    I18nCodeTekstDto    i18nCodeTekst = null;
    while (enums.hasMoreElements()) {
      String      code  = enums.nextElement().toString();
      String      tekst = properties.getProperty(code);
      try {
        if (isUtf8()) {
          tekst = new String(tekst.getBytes("ISO-8859-1"), "UTF-8");
        }
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("Tekst " + tekst + " [" + e.getMessage() + "]");
        addError("errors.encoding", code);
      }
      if (code.length() <= 100
          && tekst.length() <= 1024) {
        try {
          i18nCode  = i18nCodeComponent.getI18nCode(code);
        } catch (ObjectNotFoundException e) {
          i18nCode  = new I18nCodeDto(code);
          i18nCodeComponent.insert(i18nCode);
          nieuweCodes++;
        }
  
        Long  codeId  = i18nCode.getCodeId();
        try {
          i18nCodeTekst = i18nCodeComponent.getI18nCodeTekst(codeId, taal);
          if (overschrijven && !tekst.equals(i18nCodeTekst.getTekst())) {
            i18nCodeTekst.setTekst(tekst);
            i18nCodeComponent.update(i18nCodeTekst);
            gewijzigdeWaardes++;
          }
        } catch (ObjectNotFoundException e) {
          // TODO Doen op de JPA manier.
          i18nCodeTekst = new I18nCodeTekstDto(i18nCode.getCodeId(), taal,
                                               tekst);
          i18nCodeComponent.insert(i18nCodeTekst);
          nieuweWaardes++;
        }
      } else {
        if (code.length() > 100) {
          addError(PersistenceConstants.MAXLENGTH,
                   new Object[] {getTekst("label.code"), 100});
        }
        if (tekst.length() > 1024) {
          addError(PersistenceConstants.MAXLENGTH,
                   new Object[] {getTekst("label.tekst"), 1024});
        }
      }
    }

    gelezen = properties.size();
  }
}
