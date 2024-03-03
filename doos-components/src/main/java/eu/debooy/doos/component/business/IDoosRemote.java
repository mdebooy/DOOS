/*
 * Copyright (c) 2024 Marco de Booij
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

package eu.debooy.doos.component.business;

import java.util.Collection;
import javax.ejb.Remote;
import javax.faces.model.SelectItem;


/**
 * @author Marco de Booij
 */
@Remote
public interface IDoosRemote {
  void                    clear();
  // Voor iedereen
  String                  getAppProperty(String property);
  String                  getIso6391Naam(String iso6391, String taal6391);
  String                  getIso6392bNaam(String iso6392b, String taal6392b);
  String                  getIso6392tNaam(String iso6392t, String taal6392t);
  String                  getIso6393Naam(String iso6393, String taal6393);
  String                  getTaal(String iso6391);
  String                  getTaal(String iso6391, String taal6391);
  String                  getTaalIso6391(String iso6391);
  String                  getTaalIso6391(String iso6391, String taal6391);
  String                  getTaalIso6392b(String iso6392b);
  String                  getTaalIso6392b(String iso6392b, String taal6392b);
  String                  getTaalIso6392t(String iso6392t);
  String                  getTaalIso6392t(String iso6392t, String taal6392t);
  String                  getTaalIso6393(String iso6393);
  String                  getTaalIso6393(String iso6393, String taal6393);
  String                  getProperty(String property);
  Collection<SelectItem>  getTalen();
  Collection<SelectItem>  getTalen(boolean metNull);
  Collection<SelectItem>  getTalen(String iso6391);
  Collection<SelectItem>  getTalen(String iso6391, boolean metNull);
  Collection<SelectItem>  getTalenIso6391();
  Collection<SelectItem>  getTalenIso6391(boolean metNull);
  Collection<SelectItem>  getTalenIso6391(String iso6391);
  Collection<SelectItem>  getTalenIso6391(String iso6391, boolean metNull);
  Collection<SelectItem>  getTalenIso6392b();
  Collection<SelectItem>  getTalenIso6392b(boolean metNull);
  Collection<SelectItem>  getTalenIso6392b(String iso6392b);
  Collection<SelectItem>  getTalenIso6392b(String iso6392b, boolean metNull);
  Collection<SelectItem>  getTalenIso6392t();
  Collection<SelectItem>  getTalenIso6392t(boolean metNull);
  Collection<SelectItem>  getTalenIso6392t(String iso6392t);
  Collection<SelectItem>  getTalenIso6392t(String iso6392t, boolean metNull);
  Collection<SelectItem>  getTalenIso6393();
  Collection<SelectItem>  getTalenIso6393(boolean metNull);
  Collection<SelectItem>  getTalenIso6393(String iso6393);
  Collection<SelectItem>  getTalenIso6393(String iso6393, boolean metNull);
  String                  iso6391ToIso6392t(String iso6391);
}
