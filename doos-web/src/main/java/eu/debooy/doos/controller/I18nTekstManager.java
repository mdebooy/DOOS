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
package eu.debooy.doos.controller;

import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doos.service.PropertyService;
import eu.debooy.doos.service.TaalService;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.KeyValue;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 *
 * Deze class vervangt de verschillende properties bestanden voor I18N. In
 * plaats van uit de bestanden komen de teksten uit de database. Zodra een
 * tekst gevraagd is wordt deze in de cache gehouden in alle talen die aanwezig
 * zijn. Deze cache kan geleegd worden indien dan nodig is. Een reden hiervoor
 * kan wezen dat er een taal is bijgevoegd of een tekst veranderd.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class I18nTekstManager implements II18nTekst {
  private static final  Logger  LOGGER    =
      LoggerFactory.getLogger(I18nTekstManager.class);
  private static final  String  ONBEKEND  = "???";

  private final Map<String, Map<String, String>>  codes = new HashMap<>();

  private I18nCodeService   i18nCodeService   = null;
  private IProperty         propertyService   = null;
  private TaalDto           standaardTaal     = null;
  private TaalService       taalService       = null;

  @Lock(LockType.WRITE)
  @Override
  public void clear() {
    codes.clear();
    standaardTaal = null;
  }

  @Lock(LockType.READ)
  @Override
  public Collection<KeyValue> getCache() {
    Set<KeyValue> cache = new HashSet<>();
    var           taal  = getStandaardTaal().getIso6391();
    for (Entry<String, Map<String, String>> entry : codes.entrySet()) {
      var codeteksten = entry.getValue();
      cache.add(new KeyValue(entry.getKey(),
                             DoosUtils.nullToEmpty(codeteksten.get(taal))));
    }

    return cache;
  }

  private I18nCodeService getI18nCodeService() {
    if (null == i18nCodeService) {
      i18nCodeService  = (I18nCodeService)
          new JNDI.JNDINaam().metBean(I18nCodeService.class).locate();
    }

    return i18nCodeService;
  }

  @Lock(LockType.READ)
  @Override
  public String getI18nTekst(String code) {
    return getI18nTekst(code, getStandaardTaal().getIso6391());
  }

  @Lock(LockType.READ)
  @Override
  public String getI18nTekst(String code, String taal) {
    if (!codes.containsKey(code)) {
      I18nCodeDto dto;
      try {
        dto = getI18nCodeService().i18nCode(code);
      } catch (ObjectNotFoundException e) {
        LOGGER.error("I18N Tekst {} niet gevonden.", code);
        return ONBEKEND + code + ";" + taal + ONBEKEND;
      }
      LOGGER.debug("Toegevoegd: {}", code);
      Map<String, String> teksten = new HashMap<>();
      for (I18nCodeTekstDto tekstDto: dto.getTeksten()) {
        teksten.put(tekstDto.getTaalKode(), tekstDto.getTekst());
      }
      codes.put(code, teksten);
    }

    var talen = codes.get(code);
    if (talen.containsKey(taal)) {
      return talen.get(taal);
    }
    if (talen.containsKey(getStandaardTaal().getIso6391())) {
      return talen.get(getStandaardTaal().getIso6391());
    }

    LOGGER.error("I18N Tekst {} ({}) niet gevonden.", code, taal);
    return ONBEKEND + code + ";" + taal + ONBEKEND;
  }

  private IProperty getPropertyService() {
    if (null == propertyService) {
      propertyService  = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyService.class)
                             .metInterface(IProperty.class).locate();
    }

    return propertyService;
  }

  @Lock(LockType.READ)
  private TaalDto getStandaardTaal() {
    if (null != standaardTaal) {
      return standaardTaal;
    }

    try {
      standaardTaal =
        getTaalService().iso6391(getPropertyService()
                        .getProperty(ComponentsConstants.DEFAULT_TAAL));
        LOGGER.error("Parameter {} niet gevonden, probeer nu {}.",
                     ComponentsConstants.DEFAULT_TAAL,
                     ComponentsConstants.DEF_TAAL);
    } catch (ObjectNotFoundException e) {
      standaardTaal = getTaalService().iso6391(ComponentsConstants.DEF_TAAL);
    }

    return standaardTaal;
  }

  private TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }

  @Lock(LockType.READ)
  @Override
  public int size() {
    return codes.size();
  }
}