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
package eu.debooy.doos.service;

import eu.debooy.doos.access.TaalDao;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;

import java.util.Collection;
import java.util.HashSet;

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
  private TaalDao   taalDao;

  /**
   * Initialisatie.
   */
  public TaalService() {
    LOGGER.debug("init TaalService");
  }

  /**
   * Verwijder een Taal
   * 
   * @param String taalKode
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String taalKode) {
    TaalDto taal  = taalDao.getByPrimaryKey(taalKode);
    taalDao.delete(taal);
  }

  /**
   * Geef alle Talen.
   * 
   * @return Collection<Taal>
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Taal> query() {
    Collection<Taal>  talen = new HashSet<Taal>();
    Collection<TaalDto>  rijen = taalDao.getAll();
    for (TaalDto rij : rijen) {
      talen.add(new Taal(rij));
    }

    return talen;
  }

  /**
   * Maak of wijzig de Taal in de database.
   * 
   * @param Taal taal
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(Taal taal) {
    TaalDto dto = new TaalDto();
    taal.persist(dto);

    taalDao.update(dto);
  }

  /**
   * Geef een Taal.
   * 
   * @param String taalKode
   * @return TaalDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public TaalDto taal(String taalKode) {
    TaalDto taal  = taalDao.getByPrimaryKey(taalKode);

    return taal;
  }
}
