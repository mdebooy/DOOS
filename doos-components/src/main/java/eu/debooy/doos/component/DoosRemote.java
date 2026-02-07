/*
 * Copyright (c) 2025 Marco de Booij
 *  
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
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

package eu.debooy.doos.component;

import eu.debooy.doos.component.business.IDoosRemote;
import eu.debooy.doos.model.I18nSelectItem;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class DoosRemote implements Serializable {
  @EJB
  private transient IDoosRemote remote;

  protected DoosRemote() {}

  public String appProperty(String property) {
    return remote.getAppProperty(property);
  }

  public void clear() {
    remote.clear();
  }

  public Collection<SelectItem> getTalen() {
    return remote.getTalen();
  }

  public Collection<SelectItem> getTalenIso6391() {
    return remote.getTalenIso6391(false);
  }

  public Collection<SelectItem> getTalenIso6392b() {
    return remote.getTalenIso6392b();
  }

  public Collection<SelectItem> getTalenIso6392t() {
    return remote.getTalenIso6392t(false);
  }

  public Collection<SelectItem> getTalenIso6393() {
    return remote.getTalenIso6393();
  }

  public Collection<SelectItem> i18nLijst(String code) {
    return remote.getI18nLijst(code);
  }

  public Collection<SelectItem> i18nLijst(String code,
                                          Comparator<I18nSelectItem>
                                              comparator) {
    return remote.getI18nLijst(code, comparator);
  }

  public Collection<SelectItem> i18nLijst(String code, String taal) {
    return remote.getI18nLijst(code, taal);
  }

  public Collection<SelectItem> i18nLijst(String code, String taal,
                                          Comparator<I18nSelectItem>
                                              comparator) {
    return remote.getI18nLijst(code, taal, comparator);
  }

  public String iso6391Naam(String iso6391, String taal6391) {
    return remote.getIso6391Naam(iso6391, taal6391);
  }

  public String iso6391ToIso6392t(String iso6391) {
    return remote.iso6391ToIso6392t(iso6391);
  }

  public String iso6392bNaam(String iso6392b, String taal6392b) {
    return remote.getIso6392bNaam(iso6392b, taal6392b);
  }

  public String iso6392tNaam(String iso6392t, String taal6392t) {
    return remote.getIso6392tNaam(iso6392t, taal6392t);
  }

  public String iso6393Naam(String iso6393, String taal6393) {
    return remote.getIso6393Naam(iso6393, taal6393);
  }

  public String property(String property) {
    return remote.getProperty(property);
  }
  public String taal(String iso6391) {
    return remote.getTaal(iso6391);
  }

  public String taal(String iso6391, String taal6391) {
    return remote.getTaal(iso6391, taal6391);
  }

  public String taalIso6391(String iso6391) {
    return remote.getTaalIso6391(iso6391);
  }

  public String taalIso6391(String iso6391, String taal6391) {
    return remote.getTaalIso6391(iso6391, taal6391);
  }

  public String taalIso6392b(String iso6392b) {
    return remote.getTaalIso6392b(iso6392b);
  }

  public String taalIso6392b(String iso6392b, String taal6392b) {
    return remote.getTaalIso6392b(iso6392b, taal6392b);
  }

  public String taalIso6392t(String iso6392t) {
    return remote.getTaalIso6392t(iso6392t);
  }

  public String taalIso6392t(String iso6392t, String taal6392t) {
    return remote.getTaalIso6392t(iso6392t, taal6392t);
  }

  public String taalIso6393(String iso6393) {
    return remote.getTaalIso6393(iso6393);
  }

  public String taalIso6393(String iso6393, String taal6393) {
    return remote.getTaalIso6393(iso6393, taal6393);
  }

  public Collection<SelectItem> talen(String iso6391) {
    return remote.getTalen(iso6391);
  }

  public Collection<SelectItem> talen(String iso6391, boolean metNull) {
    return remote.getTalen(iso6391, metNull);
  }

  public Collection<SelectItem> talen(boolean metNull) {
    return remote.getTalen(metNull);
  }

  public Collection<SelectItem> talenIso6391(String iso6391) {
    return remote.getTalenIso6391(iso6391);
  }

  public Collection<SelectItem> talenIso6391(String iso6391,
                                                boolean metNull) {
    return remote.getTalenIso6391(iso6391, metNull);
  }

  public Collection<SelectItem> talenIso6391(boolean metNull) {
    return remote.getTalenIso6391(metNull);
  }

  public Collection<SelectItem> talenIso6392b(String iso6392b) {
    return remote.getTalenIso6392b(iso6392b);
  }

  public Collection<SelectItem> talenIso6392b(String iso6392b,
                                                 boolean metNull) {
    return remote.getTalenIso6392b(iso6392b, metNull);
  }

  public Collection<SelectItem> talenIso6392b(boolean metNull) {
    return remote.getTalenIso6392b(metNull);
  }

  public Collection<SelectItem> talenIso6392t(String iso6392t) {
    return remote.getTalenIso6392t(iso6392t);
  }

  public Collection<SelectItem> talenIso6392t(String iso6392t,
                                                 boolean metNull) {
    return remote.getTalenIso6392t(iso6392t, metNull);
  }

  public Collection<SelectItem> talenIso6392t(boolean metNull) {
    return remote.getTalenIso6392t(metNull);
  }

  public Collection<SelectItem> talenIso6393(String iso6393) {
    return remote.getTalenIso6393(iso6393);
  }

  public Collection<SelectItem> talenIso6393(String iso6393,
                                                boolean metNull) {
    return remote.getTalenIso6393(iso6393, metNull);
  }

  public Collection<SelectItem> talenIso6393(boolean metNull) {
    return remote.getTalenIso6393(metNull);
  }
}
