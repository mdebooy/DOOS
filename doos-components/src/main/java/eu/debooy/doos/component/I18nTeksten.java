/**
 * Copyright 2011 Marco de Booij
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
package eu.debooy.doos.component;

import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.service.CDI;
import eu.debooy.doosutils.service.JNDI;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class I18nTeksten implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private           Gebruiker gebruiker = null;
  private transient IProperty property  = null;
  private           String    taal      = null;

  @EJB
  private transient II18nTekst  i18nTekstBean;

  protected I18nTeksten() {}

  private Gebruiker getGebruiker() {
    if (null == gebruiker) {
      gebruiker = (Gebruiker) CDI.getBean("gebruiker");
    }

    return gebruiker;
  }

  protected IProperty getProperty() {
    if (null == property) {
      property  = (IProperty) new JNDI.JNDINaam()
                                      .metBeanNaam("PropertyService")
                                      .metInterface(IProperty.class)
                                      .metAppNaam("doos")
                                      .locate();
    }

    return property;
  }

  private String getTaal() {
    if (null != taal) {
      return taal;
    }

    if (null == gebruiker) {
      getGebruiker();
      if (null != gebruiker) {
        taal  = gebruiker.getLocale().getLanguage();
        return taal;
      }
    }

    taal  = getProperty().getProperty(ComponentsConstants.DEFAULT_TAAL);
    if (DoosUtils.isBlankOrNull(taal)) {
      taal  = ComponentsConstants.DEF_TAAL;
    }

    return taal;
  }

  public String tekst(String code) {
    return tekst(code, getTaal());
  }

  public String tekst(String code, String taal) {
    if (DoosUtils.isBlankOrNull(code)
        || DoosUtils.isBlankOrNull(taal)) {
      return DoosConstants.NULL;
    }

    return i18nTekstBean.getI18nTekst(code, taal);
  }
}
