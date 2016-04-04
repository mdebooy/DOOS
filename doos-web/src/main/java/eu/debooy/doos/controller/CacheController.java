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
package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doosutils.KeyValue;

import java.util.Collection;
import java.util.HashSet;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named("doosCache")
@SessionScoped
public class CacheController extends Doos {
  private static final  long    serialVersionUID  = 1L;

  public static final String  I18NCODES   = "I18nCodes";
  public static final String  PARAMETERS  = "Parameters";

  private String  sleutel;
  private String  type;
  private String  waarde;

  /**
   * Maak de cache met I18N teksten leeg.
   */
  public void clearI18nTeksten() {
    getI18nTekstManager().clear();
  }

  /**
   * Maak de cache met properties leeg.
   */
  public void clearProperties() {
    getPropertyService().clear();
  }

  public Collection<KeyValue> getCache() {
    if (I18NCODES.equalsIgnoreCase(type)) {
      setSubTitel("doos.titel.i18nCodes.cache");
      sleutel = "label.code";
      waarde  = "label.tekst";
      return getI18nTekstManager().getCache();
    }
    if (PARAMETERS.equalsIgnoreCase(type)) {
      setSubTitel("doos.titel.parameters.cache");
      sleutel = "label.sleutel";
      waarde  = "label.waarde";
      return getPropertyService().getCache();
    }

    setSubTitel("errors.wrongtype");
    return new HashSet<KeyValue>();
  }

  /**
   * Geef het aantal I18N teksten in de cache.
   * 
   * @return
   */
  public int getI18nTeksten() {
    return getI18nTekstManager().size();
  }

  public void getI18nTekstenCache() {
    type  = I18NCODES;

    redirect(CACHEITEMS_REDIRECT);
  }

  /**
   * Geef het aantal properties in de cache.
   * 
   * @return int
   */
  public int getProperties() {
    return getPropertyService().size();
  }

  public void getPropertiesCache() {
    type  = PARAMETERS;

    redirect(CACHEITEMS_REDIRECT);
  }

  /**
   * @return de sleutel
   */
  public String getSleutel() {
    return sleutel;
  }

  /**
   * @return de waarde
   */
  public String getWaarde() {
    return waarde;
  }
}
