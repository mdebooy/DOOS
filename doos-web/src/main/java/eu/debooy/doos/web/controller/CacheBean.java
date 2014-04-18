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
package eu.debooy.doos.web.controller;

import eu.debooy.doos.component.I18nCodeComponent;
import eu.debooy.doos.component.ParameterComponent;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class CacheBean extends DoosController {
  private static final  long  serialVersionUID  = 1L;

  public static final   String  BEAN_NAME = "cacheBean";

  /**
   * Maak de cache met I18N teksten leeg.
   */
  public void clearI18nTeksten() {
    new I18nCodeComponent().clearI18nTeksten();
    redirect(CACHE_REDIRECT);
  }

  /**
   * Maak de cache met properties leeg.
   */
  public void clearProperties() {
    new ParameterComponent().clearProperties();
    redirect(CACHE_REDIRECT);
  }

  /**
   * Geef het aantal I18N teksten in de cache.
   * @return
   */
  public int getI18nTeksten() {
    return new I18nCodeComponent().getI18nTeksten();
  }

  /**
   * Geef het aantal properties in de cache.
   * @return
   */
  public int getProperties() {
    return new ParameterComponent().getProperties();
  }
}
