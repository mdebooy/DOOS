/**
 * Copyright 2017 Marco de Booij
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

import eu.debooy.doos.component.Properties;
import eu.debooy.doos.component.bean.DoosBean;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import eu.debooy.doosutils.service.CDI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("appParam")
@SessionScoped
public class AppParamController extends DoosBean implements Serializable {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(AppParamController.class);

  private static final  String  COL_SLEUTEL = "sleutel";

  private static final  String  TIT_UPDATE  = "doos.titel.appparam.update";

  private static final  String  PARAMETER_REDIRECT  = "/admin/parameter.xhtml";
  private static final  String  PARAMETERS_REDIRECT = "/admin/parameters.xhtml";

  private List<String>  specialeParameters;

  private Properties  properties  = null;
  private String      sleutel;
  private String      waarde;

  public void addSpeciaal(String parameter) {
    if (null == specialeParameters) {
      specialeParameters  = new ArrayList<>();
    }
    specialeParameters.add(parameter);
  }

  public String getParameter() {
    return "app_param" + sleutel.substring(sleutel.indexOf('.'));
  }

  protected Properties getProperties() {
    if (null == properties) {
      properties  = CDI.getBean(Properties.class);
    }

    return properties;
  }

  public String getSleutel() {
    return sleutel;
  }

  public String getWaarde() {
    return waarde;
  }

  public void initSpeciaal() {
    specialeParameters  = new ArrayList<>();
  }

  public boolean isSpeciaal(String parameter) {
    if (null == specialeParameters) {
      return false;
    }

    return specialeParameters.contains(parameter);
  }

  public void save() {
    var messages  = valideer();
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      var property  = new Applicatieparameter(getParameter(), sleutel, waarde);
      getProperties().wijzig(property);
      if (PersistenceConstants.UPDATE == getAktie().getAktie()) {
        addInfo(PersistenceConstants.UPDATED, getTekst(getParameter()));
      } else {
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, getTekst(getParameter()));
      return;
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, getTekst(getParameter()));
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
      return;
    }

    redirect(PARAMETERS_REDIRECT);
  }

  public void setSleutel(String sleutel) {
    this.sleutel  = sleutel;
  }

  public void setWaarde(String waarde) {
    this.waarde   = waarde;
  }

  public void update() {
    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(COL_SLEUTEL)) {
      addError(ComponentsConstants.GEENPARAMETER, COL_SLEUTEL);
      return;
    }
    sleutel = ec.getRequestParameterMap().get(COL_SLEUTEL);
    waarde  = getProperties().value(sleutel);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE));
    redirect(PARAMETER_REDIRECT);
  }

  public void update(String sleutel) {
    this.sleutel  = sleutel;
    waarde        = getProperties().value(sleutel);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE));
    redirect(PARAMETER_REDIRECT);
  }

  protected List<Message> valideer() {
    List<Message> fouten  = new ArrayList<>();

    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{"_I18N.label.waarde"})
                            .build());
    } else if (waarde.length() > 255) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{"_I18N.label.waarde", 255})
                            .build());
    }

    return fouten;
  }
}
