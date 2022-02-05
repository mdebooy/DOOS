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

  public static final String  CACHE_I18N    = "doos.titel.i18nCodes.cache";
  public static final String  CACHE_PARAM   = "doos.titel.parameters.cache";
  public static final String  I18NCODES     = "I18nCodes";
  public static final String  LBL_I18N_KEY  = "label.code";
  public static final String  LBL_I18N_VAL  = "label.tekst";
  public static final String  LBL_PARAM_KEY = "label.sleutel";
  public static final String  LBL_PARAM_VAL = "label.waarde";
  public static final String  PARAMETERS    = "Parameters";

  private String  sleutel;
  private String  type;
  private String  waarde;

  public void clearI18nTeksten() {
    getI18nTekstManager().clear();
  }

  public void clearProperties() {
    getPropertyService().clear();
  }

  public Collection<KeyValue> getCache() {
    if (I18NCODES.equalsIgnoreCase(type)) {
      return getI18nTekstManager().getCache();
    }
    if (PARAMETERS.equalsIgnoreCase(type)) {
      return getPropertyService().getCache();
    }

    return new HashSet<>();
  }

  public int getI18nTeksten() {
    return getI18nTekstManager().size();
  }

  public void getI18nTekstenCache() {
    setSubTitel(CACHE_I18N);
    sleutel = LBL_I18N_KEY;
    waarde  = LBL_I18N_VAL;
    type    = I18NCODES;

    redirect(CACHEITEMS_REDIRECT);
  }

  public int getProperties() {
    return getPropertyService().size();
  }

  public void getPropertiesCache() {
    setSubTitel(CACHE_PARAM);
    sleutel = LBL_PARAM_KEY;
    waarde  = LBL_PARAM_VAL;
    type    = PARAMETERS;

    redirect(CACHEITEMS_REDIRECT);
  }

  public String getSleutel() {
    return sleutel;
  }

  public String getWaarde() {
    return waarde;
  }
}
