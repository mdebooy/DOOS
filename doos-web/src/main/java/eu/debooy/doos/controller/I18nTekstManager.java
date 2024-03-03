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
import eu.debooy.doos.form.Taal;
import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doos.service.I18nLijstService;
import eu.debooy.doos.service.PropertyService;
import eu.debooy.doos.service.TaalService;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.KeyValue;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
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

  private final Map<String, Map<String, String>>  codes = new HashMap<>();

  private I18nCodeService   i18nCodeService   = null;
  private I18nLijstService  i18nLijstService  = null;
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
  public Collection<SelectItem> getI18nLijst(String code) {
    return getI18nLijst(code, getStandaardTaal().getIso6391());
  }

  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getI18nLijst(String code,
                                             Comparator<I18nSelectItem>
                                                 comparator) {
    return getI18nLijst(code, getStandaardTaal().getIso6391(), comparator);
  }

  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getI18nLijst(String code, String taal) {
    return getI18nLijst(code, getStandaardTaal().getIso6391(),
                        new I18nSelectItem.VolgordeComparator());
  }

  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getI18nLijst(String code, String taal,
                                             Comparator<I18nSelectItem>
                                                 comparator) {
    List<SelectItem>    items     = new LinkedList<>();
    Set<I18nSelectItem> rijen     = new TreeSet<>(comparator);
    var                 resultaat =
        getI18nLijstService().getI18nSelectItems(code);
    for (Entry<String, Integer> entry : resultaat.entrySet()) {
      rijen.add(new I18nSelectItem(entry.getKey(), entry.getValue(),
                                   getI18nTekst(code + "." + entry.getKey(),
                                                taal)));
    }
    rijen.forEach(rij -> items.add(new SelectItem(rij.getCode(),
                                                  rij.getWaarde())));

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

  /**
   * @deprecated
   * Gebruik de method getTaal6391() uit DoosRemote service.
   *
   * @param iso6391
   * @param taal6391
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getIso6391Naam(String iso6391, String taal6391) {
    return getTaalService().iso6391(iso6391)
                           .getNaam(getTaalService().iso6391(taal6391)
                                                    .getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6392b() uit DoosRemote service.
   *
   * @param iso6392b
   * @param taal6392b
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getIso6392bNaam(String iso6392b, String taal6392b) {
    return getTaalService().iso6392b(iso6392b)
                           .getNaam(getTaalService().iso6392b(taal6392b)
                                                    .getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6392t() uit DoosRemote service.
   *
   * @param iso6392t
   * @param taal6392t
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getIso6392tNaam(String iso6392t, String taal6392t) {
    return getTaalService().iso6392t(iso6392t).getNaam(taal6392t);
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6393() uit DoosRemote service.
   *
   * @param iso6393
   * @param taal6393
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getIso6393Naam(String iso6393, String taal6393) {
    return getTaalService().iso6393(iso6393)
                           .getNaam(getTaalService().iso6393(taal6393)
                                                    .getIso6392t());
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

  /**
   * @deprecated
   * Gebruik de method getTaal6391() uit DoosRemote service.
   *
   * @param taalKode
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getTaal(String taalKode) {
    return getTaalService().iso6391(taalKode)
                           .getNaam(getStandaardTaal().getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6391() uit DoosRemote service.
   *
   * @param taalKode
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getTaalIso6391(String iso6391) {
    return getTaalService().iso6391(iso6391)
                           .getNaam(getStandaardTaal().getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6392b() uit DoosRemote service.
   *
   * @param iso6392b
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392b(String iso6392b) {
    return getTaalService().iso6392b(iso6392b)
                           .getNaam(getStandaardTaal().getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6392t() uit DoosRemote service.
   *
   * @param iso6392t
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392t(String iso6392t) {
    return getTaalService().iso6392t(iso6392t)
                           .getNaam(getStandaardTaal().getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTaal6393() uit DoosRemote service.
   *
   * @param iso6393
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String getTaalIso6393(String iso6393) {
    return getTaalService().iso6393(iso6393)
                           .getNaam(getStandaardTaal().getIso6392t());
  }

  private TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6391() uit DoosRemote service.
   *
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalen() {
    return getTalenIso6391();
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6391() uit DoosRemote service.
   *
   * @param taal
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalen(String taal) {
    return getTalenIso6391(taal);
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6391() uit DoosRemote service.
   *
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6391() {
    return getTalenIso6391(getStandaardTaal().getIso6391());
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6391() uit DoosRemote service.
   *
   * @param iso6391
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6391(String iso6391) {
    Collection<SelectItem>  items = new LinkedList<>();

    getTaalService().queryIso6391(iso6391)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6391(),
                                                        rij.getNaam())));

    return items;
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6392b() uit DoosRemote service.
   *
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6392b() {
    return getTalenIso6392b(getStandaardTaal().getIso6392b());
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6392b() uit DoosRemote service.
   *
   * @param iso6392b
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6392b(String iso6392b) {
    Collection<SelectItem>  items     = new LinkedList<>();

    getTaalService().queryIso6392b(iso6392b)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6392b(),
                                                        rij.getNaam())));

    return items;
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6392t() uit DoosRemote service.
   *
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6392t() {
    return getTalenIso6392t(getStandaardTaal().getIso6392t());
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6392t() uit DoosRemote service.
   *
   * @param iso6392t
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6392t(String iso6392t) {
    Collection<SelectItem>  items = new LinkedList<>();

    getTaalService().queryIso6392t(iso6392t)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6392t(),
                                                        rij.getNaam())));

    return items;
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6393() uit DoosRemote service.
   *
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6393() {
    return getTalenIso6393(getStandaardTaal().getIso6393());
  }

  /**
   * @deprecated
   * Gebruik de method getTalen6393() uit DoosRemote service.
   *
   * @param iso6393
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public Collection<SelectItem> getTalenIso6393(String iso6393) {
    Collection<SelectItem>  items = new LinkedList<>();

    getTaalService().queryIso6393(iso6393)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6393(),
                                                        rij.getNaam())));

    return items;
  }

  /**
   * @deprecated
   * Gebruik de method iso6391ToIso6392t() uit DoosRemote service.
   *
   * @param iso6391
   * @return
   */
  @Deprecated(forRemoval = true, since = "4.1.0")
  @Lock(LockType.READ)
  @Override
  public String iso6391ToIso6392t(String iso6391) {
    return getTaalService().iso6391(iso6391).getIso6392t();
  }

  @Lock(LockType.READ)
  @Override
  public int size() {
    return codes.size();
  }
}