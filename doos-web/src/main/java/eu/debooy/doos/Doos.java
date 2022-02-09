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
import eu.debooy.doos.service.LoggingService;
import eu.debooy.doos.service.ParameterService;
import eu.debooy.doos.service.PropertyService;
import eu.debooy.doos.service.QuartzjobService;
import eu.debooy.doos.service.TaalService;
import eu.debooy.doosutils.DoosUtils;
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
  protected transient LoggingService    loggingService;
  protected transient ParameterService  parameterService;
  protected transient QuartzjobService  quartzjobService;
  protected transient IProperty         propertyService;
  protected transient TaalService       taalService;

  protected transient String  gebruikerIso6392t = null;

  protected static final String  ADMIN_ROLE                = "doos-admin";
  protected static final String  APP_QUARTZ_REDIRECT       =
      "/admin/quartzjobs.xhtml";
  protected static final String  APPLICATIE_NAAM           = "DOOS";
  protected static final String  CACHE_REDIRECT            =
      "/admin/cache.xhtml";
  protected static final String  CACHEITEMS_REDIRECT       =
      "/admin/cacheitems.xhtml";
  protected static final String  CHART_REDIRECT            =
      "/charts/chart.xhtml";
  protected static final String  I18NCODE_REDIRECT         =
      "/i18n/i18nCode.xhtml";
  protected static final String  I18NCODES_REDIRECT        =
      "/i18n/i18nCodes.xhtml";
  protected static final String  I18NCODETEKST_REDIRECT    =
      "/i18n/i18nCodeTekst.xhtml";
  protected static final String  I18NLIJST_REDIRECT        =
      "/i18n/i18nLijst.xhtml";
  protected static final String  I18NLIJSTEN_REDIRECT      =
      "/i18n/i18nLijsten.xhtml";
  protected static final String  I18NSELECTIE_REDIRECT     =
      "/i18n/i18nSelectie.xhtml";
  protected static final String  I18NUPLOAD_REDIRECT       =
      "/i18n/i18nUpload.xhtml";
  protected static final String  LIJST_REDIRECT            =
      "/lijsten/lijst.xhtml";
  protected static final String  LIJSTEN_REDIRECT          =
      "/lijsten/lijsten.xhtml";
  protected static final String  LOG_REDIRECT              =
      "/logging/log.xhtml";
  protected static final String  LOGGING_REDIRECT          =
      "/logging/logging.xhtml";
  protected static final String  PARAMETER_REDIRECT        =
      "/parameters/parameter.xhtml";
  protected static final String  PARAMETERS_REDIRECT       =
      "/parameters/parameters.xhtml";
  protected static final String  PARAMETERUPLOAD_REDIRECT  =
      "/parameters/parameterUpload.xhtml";
  protected static final String  QUARTZJOB_REDIRECT        =
      "/quartzjobs/quartzjob.xhtml";
  protected static final String  QUARTZJOBS_REDIRECT       =
      "/quartzjobs/quartzjobs.xhtml";
  protected static final String  QUARTZJOBUPLOAD_REDIRECT  =
      "/quartzjobs/quartzjobUpload.xhtml";
  protected static final String  TAAL_REDIRECT             =
      "/talen/taal.xhtml";
  protected static final String  TAALNAAM_REDIRECT         =
      "/talen/taalnaam.xhtml";
  protected static final String  TALEN_REDIRECT            =
      "/talen/talen.xhtml";
  protected static final String  USER_ROLE                 = "doos-user";

  public Doos() {
    LOGGER.debug("Nieuwe DOOS Sessie geopend.");
    // Negeer de melding over overridable call: CDI doesn't allow to proxy that.
    setAdminRole(getExternalContext().isUserInRole(ADMIN_ROLE));
    setApplicatieNaam(APPLICATIE_NAAM);
    setUserRole(getExternalContext().isUserInRole(USER_ROLE));
    setPath(getExternalContext().getRequestContextPath());
    if (isAdministrator()) {
      addMenuitem("Dropdown.admin", "menu.administratie");
      addDropdownmenuitem(DD_ADMIN, APP_LOGS_REDIRECT,
          "menu.applicatielogs");
      addDropdownmenuitem(DD_ADMIN, APP_PARAMS_REDIRECT,
          "menu.applicatieparameters");
      addDropdownmenuitem(DD_ADMIN, APP_QUARTZ_REDIRECT,
          "menu.quartzjobs");
    }
    addMenuitem(CACHE_REDIRECT,         "menu.cache");
    addMenuitem(CHART_REDIRECT,         "menu.chart");
    addMenuitem(I18NCODES_REDIRECT,     "menu.i18nCodes");
    addMenuitem(I18NLIJSTEN_REDIRECT,   "menu.i18nLijsten");
    addMenuitem(LIJSTEN_REDIRECT,       "menu.lijsten");
    addMenuitem(LOGGING_REDIRECT,       "menu.logging");
    addMenuitem(PARAMETERS_REDIRECT,    "menu.parameters");
    addMenuitem(QUARTZJOBS_REDIRECT,    "menu.quartzjobs");
    addMenuitem(TALEN_REDIRECT,         "menu.talen");
  }

  protected String getGebruikersIso639t2() {
    if (null == gebruikerIso6392t) {
      gebruikerIso6392t =
          DoosUtils.nullToValue(getTaalService().iso6391(getGebruikersTaal())
                                                .getIso6392t(),
                                getGebruikersTaal());
    }

    return gebruikerIso6392t;
  }

  protected I18nCodeService getI18nCodeService() {
    if (null == i18nCodeService) {
      i18nCodeService = (I18nCodeService)
          new JNDI.JNDINaam().metBean(I18nCodeService.class).locate();
    }

    return i18nCodeService;
  }

  protected I18nLijstService getI18nLijstService() {
    if (null == i18nLijstService) {
      i18nLijstService  = (I18nLijstService)
          new JNDI.JNDINaam().metBean(I18nLijstService.class).locate();
    }

    return i18nLijstService;
  }

  protected II18nTekst getI18nTekstManager() {
    if (null == i18nTekstManager) {
      i18nTekstManager  = (II18nTekst)
          new JNDI.JNDINaam().metBean(I18nTekstManager.class)
                             .metInterface(II18nTekst.class).locate();
    }

    return i18nTekstManager;
  }

  protected LijstService getLijstService() {
    if (null == lijstService) {
      lijstService  = (LijstService)
          new JNDI.JNDINaam().metBean(LijstService.class).locate();
    }

    return lijstService;
  }

  protected LoggingService getLoggingService() {
    if (null == loggingService) {
      loggingService  = (LoggingService)
          new JNDI.JNDINaam().metBean(LoggingService.class).locate();
    }

    return loggingService;
  }

  protected ParameterService getParameterService() {
    if (null == parameterService) {
      parameterService = (ParameterService)
          new JNDI.JNDINaam().metBean(ParameterService.class).locate();
    }

    return parameterService;
  }

  protected IProperty getPropertyService() {
    if (null == propertyService) {
      propertyService  = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyService.class)
                             .metInterface(IProperty.class).locate();
    }

    return propertyService;
  }

  protected QuartzjobService getQuartzjobService() {
    if (null == quartzjobService) {
      quartzjobService = (QuartzjobService)
          new JNDI.JNDINaam().metBean(QuartzjobService.class).locate();
    }

    return quartzjobService;
  }

  protected TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }
}
