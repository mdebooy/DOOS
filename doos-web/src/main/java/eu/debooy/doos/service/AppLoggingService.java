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

import eu.debooy.doos.component.bean.DoosBean;
import eu.debooy.doos.component.business.ILogging;
import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doos.form.Logging;
import eu.debooy.doos.model.Logdata;
import eu.debooy.doosutils.PersistenceConstants;
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
@Named("doosAppLoggingService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class AppLoggingService extends DoosBean implements ILogging {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(AppLoggingService.class);

  private Logdata         logdata;
  private LoggingService  loggingService;

  public AppLoggingService() {
    LOGGER.debug("init AppLoggingService");
  }

  @Lock(LockType.READ)
  public Logdata  getLogdata() {
    if (null == logdata) {
      LOGGER.error("null == logdata");
      return new Logdata();
    }

    return logdata;
  }

  private LoggingService getLoggingService() {
    if (null == loggingService) {
      loggingService  = (LoggingService)
          new JNDI.JNDINaam().metBean(LoggingService.class).locate();
    }

    return loggingService;
  }

  @Lock(LockType.READ)
  public Collection<Logdata> getPackageLogging(String pkg) {
    Collection<Logdata>     logging = new ArrayList<Logdata>();
    DoosFilter<LoggingDto>  filter  = new DoosFilter<LoggingDto>();
    filter.addFilter("loggerclass", pkg + ".%");
    for (Logging rij : getLoggingService().query(filter)) {
      logging.add(new Logdata(rij.getLoggerclass(), rij.getLogId(),
                              rij.getLogtime(), rij.getLvl(), rij.getMessage(),
                              rij.getSeq(), rij.getSourceclass(),
                              rij.getSourcemethod(), rij.getThreadId()));
    }

    return logging;
  }

  @Lock(LockType.READ)
  public void retrieve(Long logId) {
    LoggingDto  loggingDto  = getLoggingService().logging(logId);
    logdata     = new Logdata(loggingDto.getLoggerclass(),
                              loggingDto.getLogId(), loggingDto.getLogtime(),
                              loggingDto.getLvl(), loggingDto.getMessage(),
                              loggingDto.getSeq(), loggingDto.getSourceclass(),
                              loggingDto.getSourcemethod(),
                              loggingDto.getThreadId());

    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel("doos.titel.applogging.retrieve");
    redirect("/admin/log.xhtml");
  }
}
