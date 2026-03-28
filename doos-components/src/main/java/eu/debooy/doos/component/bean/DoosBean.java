/**
 * Copyright 2012 Marco de Booij
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
package eu.debooy.doos.component.bean;

import eu.debooy.doos.component.Properties;
import eu.debooy.doos.component.business.IDoosRemote;
import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doosutils.Aktie;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.CDI;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 *
 * Methodes mogen niet final zijn omdat CDI dit niet toelaat.
 */
public class DoosBean implements Serializable {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(DoosBean.class.getName());

  protected static final  String  DD        = "Dropdown.";
  protected static final  String  DD_ADMIN  = "admin";

  public static final String  APP_LOG_REDIRECT    = "/admin/log.xhtml";
  public static final String  APP_LOGS_REDIRECT   = "/admin/logs.xhtml";
  public static final String  APP_PARAM_REDIRECT  = "/admin/parameter.xhtml";
  public static final String  APP_PARAMS_REDIRECT = "/admin/parameters.xhtml";

  public static final String  PAR_RETURNTO  = "returnTo";

  @EJB
  private transient IDoosRemote doosRemote;
  @EJB
  private transient II18nTekst  i18nTekst;

  private String                    actieveTab;
  private boolean                   adminRole         = false;
  private Aktie                     aktie             =
      new Aktie(PersistenceConstants.RETRIEVE);
  private String                    applicatieNaam    = "DoosBean";
  private String                    defTaal;
  private String                    deletetekst       = "";
  private String                    deletetitel       = "";
  private Aktie                     detailAktie       =
      new Aktie(PersistenceConstants.RETRIEVE);
  private String                    detailDeletetekst = "";
  private String                    detailDeletetitel = "";
  private String                    detailReturnTo    = null;
  private String                    detailSubTitel    = null;
  private final Map<String, Map<String, String>>
                                    dropdownmenus     = new LinkedHashMap<>();
  private Gebruiker                 gebruiker         = null;
  private String                    iso6392t          = null;
  private final Map<String, String> menu              = new LinkedHashMap<>();
  private String                    path              = null;
  private Properties                property          = null;
  private String                    returnTo          = null;
  private String                    type              = null;
  private String                    subTitel          = null;
  private boolean                   userRole          = false;
  private boolean                   viewRole          = false;

  public DoosBean() {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("DoosBean gemaakt.");
    }
  }

  public boolean checkEcParameters(Map<String, String> ecParameters,
                                   String... parameters) {
    var correct = true;

    for (var parameter : parameters) {
      if (!ecParameters.containsKey(parameter)) {
        addError(ComponentsConstants.GEENPARAMETER, parameter);
        correct = false;
      }
    }

    return correct;
  }

  public static UIComponent findComponent(UIComponent baseComp, String id) {
    if (baseComp.getId().endsWith(id)) {
      return baseComp;
    }
    if (baseComp.getChildCount() <= 0) {
      return null;
    }
    Iterator<?> iter  = baseComp.getChildren().iterator();
    UIComponent component;
    do {
      if (!iter.hasNext()) {
        return null;
      }
      UIComponent comp  = (UIComponent) iter.next();
      component = findComponent(comp, id);
    } while (component == null);

    return component;
  }

  protected void addDropdownmenuitem(String dropdownmenu,
                                     String item, String tekst) {
    dropdownmenus.computeIfAbsent(dropdownmenu, key -> new LinkedHashMap<>());

    if (item.startsWith("http://")
        || item.startsWith("https://")) {
      dropdownmenus.get(dropdownmenu).put(item, tekst);
    } else {
      dropdownmenus.get(dropdownmenu).put(path + item, tekst);
    }
  }

  protected void addError(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_ERROR, code, params);
  }

  protected void addFatal(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_FATAL, code, params);
  }

  protected void addInfo(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_INFO, code, params);
  }

  protected void addMenuitem(String item, String tekst) {
    if (item.startsWith("http://")
        || item.startsWith("https://")) {
      menu.put(item, tekst);
    } else {
      menu.put(path + item, tekst);
    }
  }

  protected void addMessage(Severity severity, String code,
                                  Object... params) {
    var detail  = getTekst(code, params);
    var summary = getTekst(code, params);
    var msg     = new FacesMessage(severity, summary, detail);

    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  protected int addMessage(List<Message> messages) {
    var informatief = 0;
    for (var message : messages) {
      var params  = message.getParams();
      // Parameters die beginnen met "_I18N." moeten 'vertaald' worden.
      for (var  i = 0; i < params.length; i++) {
        if (params[i] instanceof String param
            && param.startsWith("_I18N.")) {
          params[i] = getTekst(param.substring(6));
        }
      }
      var code    = message.getMessage();
      switch (message.getSeverity()) {
        case Message.ERROR -> addError(code, params);
        case Message.FATAL -> addFatal(code, params);
        case Message.INFO -> {
          addInfo(code, params);
          informatief++;
        }
        default -> {
          addWarning(code, params);
          informatief++;
        }
      }
    }

    return informatief;
  }

  protected void addWarning(String code, Object... params) {
    addMessage(FacesMessage.SEVERITY_WARN, code, params);
  }

  protected void generateExceptionMessage(Exception exception) {
    addError("generic.Exception", exception.getMessage(), exception);
  }

  public String getActieveTab() {
    return actieveTab;
  }

  public Aktie getAktie() {
    return aktie;
  }

  public String getApplicatieNaam() {
    return applicatieNaam;
  }

  protected DoosBean getApplicationBean(String name) {
    return (DoosBean) getExternalContext().getApplicationMap().get(name);
  }

  protected DoosBean getBean(Class<?> clazz) {
    return (DoosBean) CDI.getBean(clazz);
  }

  protected DoosBean getBean(String naam) {
    return (DoosBean) CDI.getBean(naam);
  }

  public String getDefTaal() {
    if (null == defTaal) {
      defTaal = getParameter(ComponentsConstants.DEFAULT_TAAL);
      if (DoosUtils.isBlankOrNull(defTaal)) {
        defTaal = ComponentsConstants.DEF_TAAL;
      }
    }

    return defTaal;
  }

  public String getDeletetekst() {
    return DoosUtils.nullToValue(deletetekst, "master")
                    .replace("'", "\\\'");
  }

  public String getDeletetitel() {
    return DoosUtils.nullToValue(deletetitel, "master")
                    .replace("'", "\\\'");
  }

public Aktie getDetailAktie() {
    return detailAktie;
  }

  public String getDetailDeletetekst() {
    return DoosUtils.nullToValue(detailDeletetekst, "detail")
                    .replace("'", "\\\'");
  }

  public String getDetailDeletetitel() {
    return DoosUtils.nullToValue(detailDeletetitel, "master")
                    .replace("'", "\\\'");
  }

  public String getDetailReturnTo() {
    return DoosUtils.nullToEmpty(detailReturnTo);
  }

  public String getDetailSubTitel() {
    return detailSubTitel;
  }

  public Set<Entry<String, String>> getDropdownmenu(String dropdownmenu) {
    if (dropdownmenus.containsKey(dropdownmenu)) {
      return dropdownmenus.get(dropdownmenu).entrySet();
    }

    return new LinkedHashMap<String, String>().entrySet();
  }

  protected ExternalContext getExternalContext() {
    var facesContext  = FacesContext.getCurrentInstance();

    return facesContext.getExternalContext();
  }

  protected Gebruiker getGebruiker() {
    if (null == gebruiker) {
      gebruiker = CDI.getBean(Gebruiker.class);
    }

    return gebruiker;
  }

  public String getGebruikersEmail() {
    if (null == gebruiker) {
      gebruiker = CDI.getBean(Gebruiker.class);
    }

    return gebruiker.getEmail();
  }

  public String getGebruikerNaam() {
    var resultaat = getGebruiker().getUserName();
    if (DoosUtils.isNotBlankOrNull(resultaat)) {
      return resultaat;
    }

    return getGebruiker().getUserId();
  }

  public String getGebruikersTaal() {
    if (null == gebruiker) {
      gebruiker = CDI.getBean(Gebruiker.class);
    }

    return gebruiker.getLocale().getLanguage();
  }

  public String getGebruikersTaalInIso6392t() {
    if (null == iso6392t) {
      iso6392t  = doosRemote.iso6391ToIso6392t(getGebruikersTaal());
    }

    return iso6392t;
  }

  public Collection<SelectItem> getI18nLijst(String code, String taal,
                                             Comparator<I18nSelectItem>
                                                 comparator) {
    return doosRemote.getI18nLijst(code, taal, comparator);
  }

  protected Map<String, String> getLijstParameters() {
    Map<String, String> lijstParameters = new HashMap<>();
    var prefix  = getApplicatieNaam().toLowerCase() + ".lijst";
    var start   = prefix.length() + 1;
    // Haal de default lijst parameters op.
    var rijen = getParameters("default.lijst");
    for (Entry<String,String> rij  : rijen.entrySet()) {
      lijstParameters.put(rij.getKey().substring(14), rij.getValue());
    }

    // Haal de lijst parameters voor de applicatie op.
    rijen = getParameters(prefix);
    for (Entry<String,String> rij  : rijen.entrySet()) {
      lijstParameters.put(rij.getKey().substring(start), rij.getValue());
    }

    return lijstParameters;
  }

  public Set<Entry<String, String>> getMenu() {
    return menu.entrySet();
  }

  public String getMessage(Locale locale, String message, Object... params) {
    if (null == params) {
      return message;
    }

    var formatter = new MessageFormat(message, locale);

    return formatter.format(params);
  }

  public String getMessage(String message, Object... params) {
    return getMessage(getGebruiker().getLocale(), message, params);
  }

  public String getParameter(String parameter) {
    String  waarde;
    try {
      waarde  = getProperty().value(parameter);
    } catch (ObjectNotFoundException e) {
      addWarning("errors.notfound.parameter", parameter);
      return "";
    }

    return waarde;
  }

  public Map<String, String> getParameters(String prefix) {
    Map<String, String>       parameters  = new HashMap<>();
    List<Applicatieparameter> rijen;
    try {
      rijen  = getProperty().properties(prefix);
      for (Applicatieparameter rij : rijen) {
        parameters.put(rij.getSleutel(), rij.getWaarde());
      }
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return parameters;
  }

  public String getPath() {
    return path;
  }

  private Properties getProperty() {
    if (null == property) {
      property  = CDI.getBean(Properties.class);
    }

    return property;
  }

  public String getReturnTo() {
    return DoosUtils.nullToEmpty(returnTo);
  }

  public String getSubTitel() {
    return subTitel;
  }

  public String getTekst(Locale locale, String code, Object... params) {
    var tekst = i18nTekst.getI18nTekst(code, locale.getLanguage());

    if (null == params) {
      return tekst;
    }

    var formatter = new MessageFormat(tekst);

    return formatter.format(params);
  }

  public String getTekst(String code, Object... params) {
    return getTekst(getGebruiker().getLocale(), code, params);
  }

  public String getType() {
    return type;
  }

  public void invokeAction(String action) {
    if (null != action) {
      var tk          = new StringTokenizer(action, ".", false);
      var beannaam    = tk.nextToken();
      var bean        = getBean(beannaam);
      var methodName  = tk.nextToken();
      try {
        var clazz   = new Class[0];
        var method  = bean.getClass().getMethod(methodName, clazz);
        method.invoke(bean, new Object());
      } catch (IllegalAccessException | IllegalArgumentException |
               InvocationTargetException | NoSuchMethodException  |
               SecurityException e) {
        LOGGER.error("{} : {}",
                     e.getClass().getSimpleName(), e.getLocalizedMessage());
      }
    }
  }

  public boolean isAdministrator() {
    return adminRole;
  }

  public boolean isGerechtigd() {
    return userRole || viewRole;
  }

  public boolean isUser() {
    return userRole;
  }

  public boolean isView() {
    return viewRole;
  }

  protected void redirect() {
    redirect("/index.xhtml");
  }

  protected void redirect(String path) {
    try {
      var redirect  = String.format("%s%s",
                          getExternalContext().getRequestContextPath(), path);
      getExternalContext().redirect(redirect);
    } catch (IOException e) {
      generateExceptionMessage(e);
    }
  }

  public void setActieveTab(String actieveTab) {
    this.actieveTab         = actieveTab;
  }

  public void setAdminRole(boolean adminRole) {
    this.adminRole          = adminRole;
  }

  public void setAktie(char aktie) {
    this.aktie.setAktie(aktie);
  }

  public void setAktie(Aktie aktie) {
    this.aktie              = aktie;
  }

  public void setApplicatieNaam(String applicatieNaam) {
    this.applicatieNaam     = applicatieNaam;
  }

  public void setDeletetekst(String deletetekst) {
    this.deletetekst        = deletetekst;
  }

  public void setDeletetitel(String deletetitel) {
    this.deletetitel        = deletetitel;
  }

  public void setDetailAktie(Aktie detailAktie) {
    this.detailAktie        = detailAktie;
  }

  public void setDetailAktie(char detailAktie) {
    this.detailAktie.setAktie(detailAktie);
  }

  public void setDetailDeletetekst(String detailDeletetekst) {
    this.detailDeletetekst  = detailDeletetekst;
  }

  public void setDetailDeletetitel(String detailDeletetitel) {
    this.detailDeletetitel  = detailDeletetitel;
  }

  public void setDetailReturnTo(String returnTo) {
    this.detailReturnTo     = returnTo;
  }

  public void setDetailReturnTo(ExternalContext ec, String returnTo) {
    if (ec.getRequestParameterMap().containsKey(PAR_RETURNTO)) {
      setDetailReturnTo(ec.getRequestParameterMap().get(PAR_RETURNTO));
    } else {
      setDetailReturnTo(returnTo);
    }
  }

  public void setDetailSubTitel(String detailSubTitel) {
    this.detailSubTitel     = detailSubTitel;
  }

  public void setPath(String path) {
    this.path               = path;
  }

  public void setReturnTo(String returnTo) {
    this.returnTo           = returnTo;
  }

  public void setReturnTo(ExternalContext ec, String returnTo) {
    if (ec.getRequestParameterMap().containsKey(PAR_RETURNTO)) {
      setReturnTo(ec.getRequestParameterMap().get(PAR_RETURNTO));
    } else {
      setReturnTo(returnTo);
    }
  }

  public void setSubTitel(String subTitel) {
    this.subTitel       = subTitel;
  }

  public void setType(String type) {
    this.type           = type;
  }

  public void setUserRole(boolean userRole) {
    this.userRole       = userRole;
  }

  public void setViewRole(boolean viewRole) {
    this.viewRole       = viewRole;
  }
}
