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
import eu.debooy.doos.form.Taal;
import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doos.service.I18nLijstService;
import eu.debooy.doos.service.PropertyService;
import eu.debooy.doos.service.TaalService;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.KeyValue;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.CDI;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;

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

  private I18nCodeService       i18nCodeService;
  private I18nLijstService      i18nLijstService;
  private Map<String, Map<String, String>>
                                codes     =
      new HashMap<String, Map<String, String>>();
  private IProperty             propertyService;
  private String                standaardTaal;
  private TaalService           taalService;

  @Lock(LockType.WRITE)
  public void clear() {
    standaardTaal = null;
    codes.clear();
  }

  @Lock(LockType.READ)
  public Collection<KeyValue> getCache() {
    Set<KeyValue> cache     = new HashSet<KeyValue>();
    String        taal      = getStandaardTaal();
    Gebruiker     gebruiker = (Gebruiker) CDI.getBean("gebruiker");
    if (null != gebruiker) {
      taal  = gebruiker.getLocale().getLanguage();
    }
    for (Entry<String, Map<String, String>> entry : codes.entrySet()) {
      Map<String, String> codeteksten = (Map<String, String>) entry.getValue();
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
  public Collection<SelectItem> getI18nLijst(String code) {
    return getI18nLijst(code, getStandaardTaal());
  }

  @Lock(LockType.READ)
  public Collection<SelectItem> getI18nLijst(String code,
                                             Comparator<I18nSelectItem>
                                                 comparator) {
    return getI18nLijst(code, getStandaardTaal(), comparator);
  }

  @Lock(LockType.READ)
  public Collection<SelectItem> getI18nLijst(String code, String taal) {
    return getI18nLijst(code, getStandaardTaal(),
                        new I18nSelectItem.VolgordeComparator());
  }

  @Lock(LockType.READ)
  public Collection<SelectItem> getI18nLijst(String code, String taal,
                                             Comparator<I18nSelectItem>
                                                 comparator) {
    List<SelectItem>      items     = new LinkedList<SelectItem>();
    Set<I18nSelectItem>   rijen     =
        new TreeSet<I18nSelectItem>(comparator);
    Map<String, Integer>  resultaat =
        getI18nLijstService().getI18nSelectItems(code);
    for (Entry<String, Integer> entry : resultaat.entrySet()) {
      rijen.add(new I18nSelectItem(entry.getKey(), entry.getValue(),
                                   getI18nTekst(code + "." + entry.getKey(),
                                                taal)));
    }
    for (I18nSelectItem rij : rijen) {
      items.add(new SelectItem(rij.getCode(), rij.getWaarde()));
    }

    return items;
  }

  private I18nLijstService getI18nLijstService() {
    if (null == i18nLijstService) {
      i18nLijstService  = (I18nLijstService)
          new JNDI.JNDINaam().metBean(I18nLijstService.class).locate();
    }

    return i18nLijstService;
  }

  @Lock(LockType.READ)
  public String getI18nTekst(String code) {
    return getI18nTekst(code, getStandaardTaal());
  }

  @Lock(LockType.READ)
  public String getI18nTekst(String code, String taal) {
    if (!codes.containsKey(code)) {
      I18nCodeDto dto = null;
      try {
        dto = getI18nCodeService().i18nCode(code);
      } catch (ObjectNotFoundException e) {
        LOGGER.error("I18N Tekst " + code + " niet gevonden.");
        return ONBEKEND + code + ";" + taal + ONBEKEND;
      }
      LOGGER.debug("Toegevoegd: " + code);
      Map<String, String> teksten = new HashMap<String, String>();
      for (I18nCodeTekstDto tekstDto: dto.getTeksten()) {
        teksten.put(tekstDto.getTaalKode(), tekstDto.getTekst());
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

  private IProperty getPropertyService() {
    if (null == propertyService) {
      propertyService  = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyService.class)
                             .metInterface(IProperty.class).locate();
    }

    return propertyService;
  }

  @Lock(LockType.READ)
  private String getStandaardTaal() {
    if (null == standaardTaal) {
      standaardTaal = getPropertyService().getProperty("default.taal");
    }

    return standaardTaal;
  }

  @Lock(LockType.READ)
  public String getTaal(String taalKode) {
    return getTaalService().taal(taalKode).getTaal();
  }

  private TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }

  @Lock(LockType.READ)
  public Collection<SelectItem> getTalen() {
    Collection<SelectItem>  items = new LinkedList<SelectItem>();
    Set<Taal>               rijen =
        new TreeSet<Taal>(new Taal.TaalComparator());
    rijen.addAll(getTaalService().query());
    for (Taal rij : rijen) {
      items.add(new SelectItem(rij.getTaalKode(), rij.getTaal()));
    }

    return items;
  }

  @Lock(LockType.READ)
  public int size() {
    return codes.size();
  }
}