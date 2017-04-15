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
package eu.debooy.doos;

import eu.debooy.doos.component.bean.DoosBean;
import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.controller.I18nTekstManager;
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doos.service.I18nLijstService;
import eu.debooy.doos.service.LijstService;
import eu.debooy.doos.service.ParameterService;
import eu.debooy.doos.service.PropertyService;
import eu.debooy.doos.service.TaalService;
import eu.debooy.doosutils.service.JNDI;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doos")
@SessionScoped
public class Doos extends DoosBean {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(Doos.class);

  protected transient I18nCodeService   i18nCodeService;
  protected transient I18nLijstService  i18nLijstService;
  protected transient II18nTekst        i18nTekstManager;
  protected transient LijstService      lijstService;
  protected transient ParameterService  parameterService;
  protected transient IProperty         propertyService;
  protected transient TaalService       taalService;

  public static final String  ADMIN_ROLE                = "doos-admin";
  public static final String  APPLICATIE_NAAM           = "DOOS";
  public static final String  CACHE_REDIRECT            = "/admin/cache.xhtml";
  public static final String  CACHEITEMS_REDIRECT       =
      "/admin/cacheitems.xhtml";
  public static final String  I18NCODE_REDIRECT         =
      "/i18n/i18nCode.xhtml";
  public static final String  I18NCODES_REDIRECT        =
      "/i18n/i18nCodes.xhtml";
  public static final String  I18NCODETEKST_REDIRECT    =
      "/i18n/i18nCodeTekst.xhtml";
  public static final String  I18NLIJST_REDIRECT        =
      "/i18n/i18nLijst.xhtml";
  public static final String  I18NLIJSTEN_REDIRECT      =
      "/i18n/i18nLijsten.xhtml";
  public static final String  I18NSELECTIE_REDIRECT     =
      "/i18n/i18nSelectie.xhtml";
  public static final String  I18NUPLOAD_REDIRECT       =
      "/i18n/i18nUpload.xhtml";
  public static final String  LIJST_REDIRECT            =
      "/lijsten/lijst.xhtml";
  public static final String  LIJSTEN_REDIRECT          =
      "/lijsten/lijsten.xhtml";
  public static final String  PARAMETER_REDIRECT        =
      "/parameters/parameter.xhtml";
  public static final String  PARAMETERS_REDIRECT       =
      "/parameters/parameters.xhtml";
  public static final String  PARAMETERUPLOAD_REDIRECT  =
      "/parameters/parameterUpload.xhtml";
  public static final String  TAAL_REDIRECT             = "/talen/taal.xhtml";
  public static final String  TALEN_REDIRECT            = "/talen/talen.xhtml";
  public static final String  USER_ROLE                 = "doos-user";

  public Doos() {
    LOGGER.debug("Nieuwe DOOS Sessie geopend.");
    setAdminRole(getExternalContext().isUserInRole(ADMIN_ROLE));
    setApplicatieNaam(APPLICATIE_NAAM);
    setUserRole(getExternalContext().isUserInRole(USER_ROLE));
    setPath(getExternalContext().getRequestContextPath());
    addMenuitem(CACHE_REDIRECT,         "menu.cache");
    if (isAdministrator()) {
      addMenuitem(APP_PARAMS_REDIRECT,  "menu.applicatieparameters");
    }
    addMenuitem(I18NCODES_REDIRECT,     "menu.i18nCodes");
    addMenuitem(I18NLIJSTEN_REDIRECT,   "menu.i18nLijsten");
    addMenuitem(LIJSTEN_REDIRECT,       "menu.lijsten");
    addMenuitem(PARAMETERS_REDIRECT,    "menu.parameters");
    addMenuitem(TALEN_REDIRECT,         "menu.talen");
  }

  /**
   * Geef de I18nCodeService. Als die nog niet gekend is haal het dan op.
   * 
   * @return I18nCodeService
   */
  protected I18nCodeService getI18nCodeService() {
    if (null == i18nCodeService) {
      i18nCodeService = (I18nCodeService)
          new JNDI.JNDINaam().metBean(I18nCodeService.class).locate();
    }

    return i18nCodeService;
  }

  /**
   * Geef de I18nLijstService. Als die nog niet gekend is haal het dan op.
   * 
   * @return I18nLijstService
   */
  protected I18nLijstService getI18nLijstService() {
    if (null == i18nLijstService) {
      i18nLijstService  = (I18nLijstService)
          new JNDI.JNDINaam().metBean(I18nLijstService.class).locate();
    }

    return i18nLijstService;
  }

  /**
   * Geef de I18nTekstManager. Als die nog niet gekend is haal het dan op.
   * 
   * @return I18nTekstManager
   */
  protected II18nTekst getI18nTekstManager() {
    if (null == i18nTekstManager) {
      i18nTekstManager  = (II18nTekst)
          new JNDI.JNDINaam().metBean(I18nTekstManager.class)
                             .metInterface(II18nTekst.class).locate();
    }

    return i18nTekstManager;
  }

  /**
   * Geef de LijstService. Als die nog niet gekend is haal het dan op.
   * 
   * @return LijstService
   */
  protected LijstService getLijstService() {
    if (null == lijstService) {
      lijstService  = (LijstService)
          new JNDI.JNDINaam().metBean(LijstService.class).locate();
    }

    return lijstService;
  }

  /**
   * Geef de TaalService. Als die nog niet gekend is haal het dan op.
   * 
   * @return TaalService
   */
  protected TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }

  /**
   * Geef de ParameterService. Als die nog niet gekend is haal het dan op.
   * 
   * @return ParameterService
   */
  protected ParameterService getParameterService() {
    if (null == parameterService) {
      parameterService = (ParameterService)
          new JNDI.JNDINaam().metBean(ParameterService.class).locate();
    }

    return parameterService;
  }

  /**
   * Geef de PropertyService. Als die nog niet gekend is haal het dan op.
   * 
   * @return PropertyService
   */
  protected IProperty getPropertyService() {
    if (null == propertyService) {
      propertyService  = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyService.class)
                             .metInterface(IProperty.class).locate();
    }

    return propertyService;
  }
}
