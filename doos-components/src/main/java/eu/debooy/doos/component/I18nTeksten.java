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
import eu.debooy.doosutils.DoosConstants;
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

  private String  taal  = "nl";

  @EJB
  private II18nTekst  i18nTekstBean;

  private Gebruiker   gebruiker;

  protected I18nTeksten() {}

  public Collection<SelectItem> i18nLijst(String code, String taal,
                                          Comparator<I18nSelectItem>
                                              comparator) {
    return i18nTekstBean.getI18nLijst(code, taal, comparator);
  }

  public String taal(String iso6391) {
    return i18nTekstBean.getTaalIso6391(iso6391);
  }

  public String taalIso6391(String iso6391) {
    return i18nTekstBean.getTaalIso6391(iso6391);
  }

  public String taalIso6392b(String iso6392b) {
    return i18nTekstBean.getTaalIso6392b(iso6392b);
  }

  public String taalIso6392t(String iso6392t) {
    return i18nTekstBean.getTaalIso6392t(iso6392t);
  }

  public String taalIso6393(String iso6393) {
    return i18nTekstBean.getTaalIso6393(iso6393);
  }

  public Collection<SelectItem> talen() {
    return talenIso6391();
  }

  public Collection<SelectItem> talenIso6391() {
    return i18nTekstBean.getTalenIso6391();
  }

  public Collection<SelectItem> talenIso6392b() {
    return i18nTekstBean.getTalenIso6392b();
  }

  public Collection<SelectItem> talenIso6392t() {
    return i18nTekstBean.getTalenIso6392t();
  }

  public Collection<SelectItem> talenIso6393() {
    return i18nTekstBean.getTalenIso6393();
  }

  public String tekst(String code) {
    if (null == gebruiker) {
      gebruiker = (Gebruiker) CDI.getBean("gebruiker");
      if (null != gebruiker) {
        taal  = gebruiker.getLocale().getLanguage();
      }
    }

    return tekst(code, taal);
  }

  public String tekst(String code, String taal) {
    if (DoosUtils.isBlankOrNull(code)
        || DoosUtils.isBlankOrNull(taal)) {
      return DoosConstants.NULL;
    }

    return i18nTekstBean.getI18nTekst(code, taal);
  }
}
