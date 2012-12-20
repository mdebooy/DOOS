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
package eu.debooy.doos.business;

import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doosutils.components.business.II18nTekst;
import eu.debooy.doosutils.components.business.IProperty;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class I18nTekstManager implements II18nTekst {
  private static final  Logger  LOGGER        =
      LoggerFactory.getLogger(I18nTekstManager.class);
  private static final  String  ONBEKEND      = "???";

  private I18nCodeManager       i18nCodeManager;
  private Map<String, Map<String, String>>
      codes = new HashMap<String, Map<String, String>>();
  private IProperty             propertyBean;
  private String                standaardTaal;

  private String getStandaardTaal() {
    if (null == standaardTaal) {
      standaardTaal = getPropertyManager().getProperty("jsf.default.taal");
    }

    return standaardTaal;
  }

  @Lock(LockType.WRITE)
  @Override
  public void clear() {
    standaardTaal = null;
    codes.clear();
  }

  private I18nCodeManager getI18nCodeManager() {
    if (null == i18nCodeManager) {
      i18nCodeManager  = (I18nCodeManager)
          new JNDI.JNDINaam().metBean(I18nCodeManager.class).locate();
    }

    return i18nCodeManager;
  }

  private IProperty getPropertyManager() {
    if (null == propertyBean) {
      propertyBean  = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyManager.class)
                             .metInterface(IProperty.class).locate();
    }

    return propertyBean;
  }

  @Lock(LockType.READ)
  @Override
  public String getI18nTekst(String code) {
    return getI18nTekst(code, standaardTaal);
  }

  @Lock(LockType.READ)
  @Override
  public String getI18nTekst(String code, String taal) {
    if (!codes.containsKey(code)) {
      I18nCodeDto dto = null;
      try {
        dto = getI18nCodeManager().getI18nCode(code);
      } catch (ObjectNotFoundException e) {
        LOGGER.error("I18N Tekst " + code + " niet gevonden.");
        return ONBEKEND + code + ";" + taal + ONBEKEND;
      }
      LOGGER.debug("Toegevoegd: " + code);
      Map<String, String> teksten = new HashMap<String, String>();
      for (I18nCodeTekstDto tekstDto: dto.getTeksten()) {
        teksten.put(tekstDto.getId().getTaalKode(), tekstDto.getTekst());
      }
      codes.put(code, teksten);
    }

    Map<String, String> talen = codes.get(code);
    if (talen.containsKey(taal)) {
      return talen.get(taal);
    }
    if (talen.containsKey(getStandaardTaal())) {
      return talen.get(getStandaardTaal());
    }

    LOGGER.error("I18N Tekst " + code + " (" + taal + ") niet gevonden.");
    return ONBEKEND + code + ";" + taal + ONBEKEND;
  }

  @Lock(LockType.READ)
  @Override
  public int size() {
    return codes.size();
  }
}
