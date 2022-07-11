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
package eu.debooy.doos.component.business;

import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doosutils.KeyValue;
import java.util.Collection;
import java.util.Comparator;
import javax.ejb.Remote;
import javax.faces.model.SelectItem;


/**
 * @author Marco de Booij
 */
// TODO Toegang beperken via rollen.
@Remote
public interface II18nTekst {
  void                    clear();
  Collection<KeyValue>    getCache();
  // Voor iedereen
  Collection<SelectItem>  getI18nLijst(String code);
  Collection<SelectItem>  getI18nLijst(String code,
                                       Comparator<I18nSelectItem> comparator);
  Collection<SelectItem>  getI18nLijst(String code, String taal);
  Collection<SelectItem>  getI18nLijst(String code, String taal,
                                       Comparator<I18nSelectItem> comparator);
  String                  getI18nTekst(String code);
  String                  getI18nTekst(String code, String taal);
  int                     size();
  String                  getTaal(String iso6391);
  String                  getTaalIso6391(String iso6391);
  String                  getTaalIso6392b(String iso6392b);
  String                  getTaalIso6392t(String iso6392t);
  String                  getTaalIso6393(String iso6393);
  Collection<SelectItem>  getTalen();
  Collection<SelectItem>  getTalen(String iso6391);
  Collection<SelectItem>  getTalenIso6391();
  Collection<SelectItem>  getTalenIso6391(String iso6391);
  Collection<SelectItem>  getTalenIso6392b();
  Collection<SelectItem>  getTalenIso6392b(String iso6392b);
  Collection<SelectItem>  getTalenIso6392t();
  Collection<SelectItem>  getTalenIso6392t(String iso6392t);
  Collection<SelectItem>  getTalenIso6393();
  Collection<SelectItem>  getTalenIso6393(String iso6393);
}
