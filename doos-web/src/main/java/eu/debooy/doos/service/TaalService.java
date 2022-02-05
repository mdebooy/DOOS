/**
 * Copyright (c) 2016 Marco de Booij
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
package eu.debooy.doos.service;

import eu.debooy.doos.access.TaalDao;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosTaalService")
@Lock(LockType.WRITE)
public class TaalService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(TaalService.class);

  @Inject
  private TaalDao     taalDao;

  public TaalService() {
    LOGGER.debug("init TaalService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long taalId) {
    var taal  = taalDao.getByPrimaryKey(taalId);
    taalDao.delete(taal);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto iso6391(String iso6391) {
    return taalDao.getTaalIso6391(iso6391);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto iso6392b(String iso6392b) {
    return taalDao.getTaalIso6392b(iso6392b);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto iso6392t(String iso6392t) {
    return taalDao.getTaalIso6392t(iso6392t);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto iso6393(String iso6393) {
    return taalDao.getTaalIso6393(iso6393);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> query() {
    Collection<Taal>  talen = new ArrayList<>();

    taalDao.getAll().forEach(rij ->  talen.add(new Taal(rij)));

    return talen;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> queryIso6391(String iso6391) {
    var               iso6392t  = iso6391(iso6391).getIso6392t();
    Collection<Taal>  talen     = new ArrayList<>();

    taalDao.getTalenIso6391().forEach(rij ->  talen.add(new Taal(rij,
                                                                 iso6392t)));

    return talen;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> queryIso6392b(String iso6392b) {
    var               iso6392t  = iso6392b(iso6392b).getIso6392t();
    Collection<Taal>  talen     = new ArrayList<>();

    taalDao.getTalenIso6392b().forEach(rij ->  talen.add(new Taal(rij,
                                                                  iso6392t)));

    return talen;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> queryIso6392t(String iso6392t) {
    Collection<Taal>  talen = new ArrayList<>();

    taalDao.getTalenIso6392t().forEach(rij ->  talen.add(new Taal(rij,
                                                                  iso6392t)));

    return talen;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> queryIso6393(String iso6393) {
    var               iso6392t  = iso6393(iso6393).getIso6392t();

    Collection<Taal>  talen     = new ArrayList<>();
    taalDao.getTalenIso6393().forEach(rij ->  talen.add(new Taal(rij,
                                                                 iso6392t)));

    return talen;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(TaalDto taal) {
    if (null == taal.getTaalId()) {
      taalDao.create(taal);
      taal.setTaalId(taal.getTaalId());
    } else {
      taalDao.update(taal);
    }
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto taal(Long taalId) {
    return taalDao.getByPrimaryKey(taalId);
  }
}
