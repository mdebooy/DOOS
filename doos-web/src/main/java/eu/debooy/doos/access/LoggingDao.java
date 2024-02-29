/**
 * Copyright 2018 Marco de Booij
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
package eu.debooy.doos.access;

import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doosutils.access.Dao;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


/**
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public class LoggingDao extends Dao<LoggingDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public LoggingDao() {
    super(LoggingDto.class);
  }

  public Long cleanup(Long retention) {
    Map<String, Object> params  = new HashMap<>();
    var                 cal     = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    cal.add(Calendar.DAY_OF_YEAR, retention.intValue() * -1);
    params.put(LoggingDto.PAR_RETENTIONDATE,
               new Timestamp(cal.getTime().getTime()));
    return namedNonSelect(LoggingDto.QRY_CLEANUP, params);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public List<LoggingDto> getPackageLogging(String pkg) {
    Map<String, Object> params  = new HashMap<>();
    params.put(LoggingDto.PAR_PACKAGE, pkg + ".%");

    return namedQuery(LoggingDto.QRY_PERPACKAGE, params);
  }
}
