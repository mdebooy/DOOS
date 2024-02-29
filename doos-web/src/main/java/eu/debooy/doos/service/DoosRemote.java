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
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;
import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.model.SelectItem;
import javax.inject.Named;
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

  private IProperty   propertyManager = null;
  private TaalDto     standaardTaal   = null;
  private TaalService taalService     = null;

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
  public String iso6391ToIso639t2(String iso6391) {
    return getTaalService().iso6391(iso6391).getIso6392t();
  }
}
