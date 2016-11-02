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

import eu.debooy.doos.access.LijstDao;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doos.form.Lijst;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosLijstService")
@Lock(LockType.WRITE)
public class LijstService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(LijstService.class);

  @Inject
  private LijstDao    lijstDao;

  /**
   * Initialisatie.
   */
  public LijstService() {
    LOGGER.debug("init LijstService");
  }

  /**
   * Verwijder een Lijst
   * 
   * @param String lijstnaam
   */
  @Interceptors({PersistenceExceptionHandlerInterceptor.class})
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(String lijstnaam) {
    LijstDto  lijst = lijstDao.getByPrimaryKey(lijstnaam);
    lijstDao.delete(lijst);
  }

  /**
   * Geef een Lijst.
   * 
   * @param String lijstnaam
   * @return LijstDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LijstDto lijst(String lijstnaam) {
    LijstDto  lijst = lijstDao.getByPrimaryKey(lijstnaam);

    return lijst;
  }

  /**
   * Geef alle Lijsten.
   * 
   * @return Collection<Lijst>
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Lijst> query() {
    Collection<Lijst> lijsten = new ArrayList<Lijst>();
    try {
      for (LijstDto rij : lijstDao.getAll()) {
        lijsten.add(new Lijst(rij));
      }
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return lijsten;
  }

  /**
   * Maak of wijzig de Lijst in de database.
   * 
   * @param Lijst lijst
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(LijstDto lijstDto) {
    lijstDao.update(lijstDto);
  }
}
