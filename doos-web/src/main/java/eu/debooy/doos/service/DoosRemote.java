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

package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IDoosRemote;
import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;
import eu.debooy.doos.model.I18nSelectItem;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;
import jakarta.ejb.EJB;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosRemote")
@Lock(LockType.WRITE)
public class DoosRemote implements IDoosRemote {
  private static final  Logger  LOGGER    =
      LoggerFactory.getLogger(DoosRemote.class);

  private I18nLijstService  i18nLijstService  = null;
  private IProperty         propertyManager   = null;
  private TaalDto           standaardTaal     = null;
  private TaalService       taalService       = null;

  @EJB
  private II18nTekst  i18nTekstBean;

  public DoosRemote() {
    LOGGER.debug("init DoosRemote");
  }

  @Lock(LockType.WRITE)
  @Override
  public void clear() {
    standaardTaal = null;
  }

  @Lock(LockType.READ)
  @Override
  public String getAppProperty(String property) {
    return getPropertyManager().getAppProperty(property);
  }

  @Lock(LockType.READ)
  @Override
  public String getIso6391Naam(String iso6391, String taal6391) {
    return getTaalService().iso6391(iso6391)
                           .getNaam(getTaalService().iso6391(taal6391)
                                                    .getIso6392t());
  }

  @Lock(LockType.READ)
  @Override
  public String getIso6392bNaam(String iso6392b, String taal6392b) {
    return getTaalService().iso6392b(iso6392b)
                           .getNaam(getTaalService().iso6392b(taal6392b)
                                                    .getIso6392t());
  }

  @Lock(LockType.READ)
  @Override
  public String getIso6392tNaam(String iso6392t, String taal6392t) {
    return getTaalService().iso6392t(iso6392t).getNaam(taal6392t);
  }

  @Lock(LockType.READ)
  @Override
  public String getIso6393Naam(String iso6393, String taal6393) {
    return getTaalService().iso6393(iso6393)
                           .getNaam(getTaalService().iso6393(taal6393)
                                                    .getIso6392t());
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
    for (Map.Entry<String, Integer> entry : resultaat.entrySet()) {
      rijen.add(new I18nSelectItem(entry.getKey(), entry.getValue(),
                                   i18nTekstBean.getI18nTekst(code + "."
                                                              + entry.getKey(),
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
  public String getProperty(String property) {
    return getPropertyManager().getProperty(property);
  }

  @Lock(LockType.READ)
  private IProperty getPropertyManager() {
    if (null == propertyManager) {
      propertyManager  = (IProperty)
        new JNDI.JNDINaam().metBean(PropertyService.class)
                           .metInterface(IProperty.class).locate();
    }

    return propertyManager;
  }

  @Lock(LockType.READ)
  private TaalDto getStandaardTaal() {
    if (null != standaardTaal) {
      return  standaardTaal;
    }

    try {
      standaardTaal =
        getTaalService().iso6391(getPropertyManager()
                        .getProperty(ComponentsConstants.DEFAULT_TAAL));
        LOGGER.error("Parameter {} niet gevonden, probeer nu {}.",
                     ComponentsConstants.DEFAULT_TAAL,
                     ComponentsConstants.DEF_TAAL);
    } catch (ObjectNotFoundException e) {
      standaardTaal = getTaalService().iso6391(ComponentsConstants.DEF_TAAL);
    }

    return standaardTaal;
  }

  @Lock(LockType.READ)
  @Override
  public String getTaal(String iso6391) {
    return getTaalIso6391(iso6391);
  }

  @Lock(LockType.READ)
  @Override
  public String getTaal(String iso6391, String taal6391) {
    return getTaalIso6391(iso6391, taal6391);
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6391(String iso6391) {
    return getTaalIso6391(iso6391, getStandaardTaal().getIso6391());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6391(String iso6391, String taal6391) {
    return getTaalService().iso6391(iso6391)
                           .getNaam(getTaalService().iso6391(taal6391)
                                                    .getIso6392t());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392b(String iso6392b) {
    return getTaalIso6392b(iso6392b, getStandaardTaal().getIso6392b());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392b(String iso6392b, String taal6392b) {
    return getTaalService().iso6392b(iso6392b)
                           .getNaam(getTaalService().iso6392b(taal6392b)
                                                    .getIso6392t());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392t(String iso6392t) {
    return getTaalIso6392t(iso6392t, getStandaardTaal().getIso6392t());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6392t(String iso6392t, String taal6392t) {
    return getTaalService().iso6392t(iso6392t).getNaam(taal6392t);
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6393(String iso6393) {
    return getTaalIso6393(iso6393, getStandaardTaal().getIso6393());
  }

  @Lock(LockType.READ)
  @Override
  public String getTaalIso6393(String iso6393, String taal6393) {
    return getTaalService().iso6393(iso6393)
                           .getNaam(getTaalService().iso6393(taal6393)
                                                    .getIso6392t());
  }

  @Lock(LockType.READ)
  private TaalService getTaalService() {
    if (null == taalService) {
      taalService = (TaalService)
          new JNDI.JNDINaam().metBean(TaalService.class).locate();
    }

    return taalService;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalen() {
    return getTalenIso6391(false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalen(boolean metNull) {
    return getTalenIso6391(getStandaardTaal().getIso6391(), metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalen(String iso6391) {
    return getTalenIso6391(iso6391, false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalen(String iso6391, boolean metNull) {
    return getTalenIso6391(iso6391, metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6391() {
    return getTalenIso6391(false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6391(boolean metNull) {
    return getTalenIso6391(getStandaardTaal().getIso6391(), metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6391(String iso6391) {
    return getTalenIso6391(iso6391, false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6391(String iso6391,
                                                boolean metNull) {
    Collection<SelectItem>  items = new LinkedList<>();

    if (metNull) {
      items.add(new SelectItem("", "--"));
    }

    getTaalService().queryIso6391(iso6391)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6391(),
                                                        rij.getNaam())));

    return items;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392b() {
    return getTalenIso6392b(false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392b(boolean metNull) {
    return getTalenIso6392b(getStandaardTaal().getIso6392b(), metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392b(String iso6392b) {
    return getTalenIso6392b(iso6392b, false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392b(String iso6392b,
                                                 boolean metNull) {
    Collection<SelectItem>  items = new LinkedList<>();

    if (metNull) {
      items.add(new SelectItem("", "--"));
    }

    getTaalService().queryIso6392b(iso6392b)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6392b(),
                                                        rij.getNaam())));

    return items;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392t() {
    return getTalenIso6392t(false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392t(boolean metNull) {
    return getTalenIso6392t(getStandaardTaal().getIso6392t(), metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392t(String iso6392t) {
    return getTalenIso6392t(iso6392t, false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6392t(String iso6392t,
                                                 boolean metNull) {
    Collection<SelectItem>  items = new LinkedList<>();

    if (metNull) {
      items.add(new SelectItem("", "--"));
    }

    getTaalService().queryIso6392t(iso6392t)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6392t(),
                                                        rij.getNaam())));

    return items;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6393() {
    return getTalenIso6393(false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6393(boolean metNull) {
    return getTalenIso6393(getStandaardTaal().getIso6393(), metNull);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6393(String iso6393) {
    return getTalenIso6393(iso6393, false);
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<SelectItem> getTalenIso6393(String iso6393,
                                                boolean metNull) {
    Collection<SelectItem>  items = new LinkedList<>();

    if (metNull) {
      items.add(new SelectItem("", "--"));
    }

    getTaalService().queryIso6393(iso6393)
                    .stream()
                    .sorted(new Taal.NaamComparator())
                    .forEachOrdered(
                        rij -> items.add(new SelectItem(rij.getIso6393(),
                                                        rij.getNaam())));

    return items;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public String iso6391ToIso6392t(String iso6391) {
    return getTaalService().iso6391(iso6391).getIso6392t();
  }
}
