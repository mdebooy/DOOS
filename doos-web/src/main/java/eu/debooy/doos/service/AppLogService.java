/**
 * Copyright (c) 2018 Marco de Booij
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

import eu.debooy.doos.component.business.ILogging;
import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doos.model.Logdata;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.service.JNDI;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosAppLogService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class AppLogService implements ILogging {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(AppLogService.class);

  private LoggingService  loggingService;

  public AppLogService() {
    LOGGER.debug("init AppLogService");
  }

  @Lock(LockType.READ)
  @Override
  public Logdata getLogdata(Long logId) {
    var loggingDto  = getLoggingService().logging(logId);

    return new Logdata.Builder()
                      .setLoggerclass(loggingDto.getLoggerclass())
                      .setLogId(loggingDto.getLogId())
                      .setLogtime(loggingDto.getLogtime())
                      .setLvl(loggingDto.getLvl())
                      .setMessage(loggingDto.getMessage())
                      .setSeq(loggingDto.getSeq())
                      .setSourceclass(loggingDto.getSourceclass())
                      .setSourcemethod(loggingDto.getSourcemethod())
                      .setThreadId(loggingDto.getThreadId())
                      .build();
  }

  private LoggingService getLoggingService() {
    if (null == loggingService) {
      loggingService  = (LoggingService)
          new JNDI.JNDINaam().metBean(LoggingService.class).locate();
    }

    return loggingService;
  }

  @Lock(LockType.READ)
  @Override
  public Collection<Logdata> getPackageLogging(String pkg) {
    Collection<Logdata>     logging = new ArrayList<>();
    DoosFilter<LoggingDto>  filter  = new DoosFilter<>();
    filter.addFilter("loggerclass", pkg + ".%");
    getLoggingService().query(filter).forEach(rij ->
      logging.add(new Logdata.Builder()
                             .setLoggerclass(rij.getLoggerclass())
                             .setLogId(rij.getLogId())
                             .setLogtime(rij.getLogtime())
                             .setLvl(rij.getLvl())
                             .setMessage(rij.getMessage())
                             .setSeq(rij.getSeq())
                             .setSourceclass(rij.getSourceclass())
                             .setSourcemethod(rij.getSourcemethod())
                             .setThreadId(rij.getThreadId())
                             .build()));
    LOGGER.debug(String.format("Size: %d", logging.size()));

    return logging;
  }
}
