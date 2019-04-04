/**
 * Copyright 2018 Marco de Booij
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

import eu.debooy.doos.access.LoggingDao;
import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doos.form.Logging;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
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
@Named("doosLoggingService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class LoggingService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(LoggingService.class);

  @Inject
  private LoggingDao  loggingDao;

  public LoggingService() {
    LOGGER.debug("init LoggingService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Long cleanup(Long retention) {
    return loggingDao.cleanup(retention);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LoggingDto logging(Long logId) {
    LoggingDto  logging = loggingDao.getByPrimaryKey(logId);

    return logging;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Logging> query() {
    Collection<Logging> logging = new ArrayList<Logging>();
    try {
      for (LoggingDto rij : loggingDao.getAll()) {
        logging.add(new Logging(rij));
      }
    } catch (NullPointerException | ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return logging;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Logging> query(DoosFilter<LoggingDto> filter) {
    Collection<Logging> logging = new ArrayList<Logging>();
    for (LoggingDto rij : loggingDao.getAll(filter)) {
      logging.add(new Logging(rij));
    }

    return logging;
  }
}
