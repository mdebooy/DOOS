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
import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.service.CDI;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class I18nTeksten implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  // TODO Uit de database halen.
  private String  taal  = "nl";

  @EJB
  private II18nTekst  i18nTekstBean;
  
  private Gebruiker   gebruiker;

  public Collection<SelectItem> i18nLijst(String code, String taal,
                                          Comparator<I18nSelectItem>
                                              comparator) {
    return i18nTekstBean.getI18nLijst(code, taal, comparator);
  }

  public String taal(String taalKode) {
    return i18nTekstBean.getTaal(taalKode);
  }

  public Collection<SelectItem> talen() {
    return i18nTekstBean.getTalen();
  }

  public String tekst(String code) {
    if (DoosUtils.isBlankOrNull(code)) {
      return "<null>";
    }

    if (null == gebruiker) {
      gebruiker = (Gebruiker) CDI.getBean("gebruiker");
      if (null != gebruiker) {
        taal  = gebruiker.getLocale().getLanguage();
      }
    }

    return i18nTekstBean.getI18nTekst(code, taal);
  }
}
