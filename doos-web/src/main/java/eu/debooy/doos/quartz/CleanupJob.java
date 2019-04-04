/**
 * Copyright 2019 Marco de Booij
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
package eu.debooy.doos.quartz;

import eu.debooy.doos.component.quartz.QuartzJob;
import eu.debooy.doos.service.LoggingService;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.service.JNDI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class CleanupJob extends QuartzJob {
  private static final Logger LOGGER  =
      LoggerFactory.getLogger(CleanupJob.class);

  private static final  SimpleDateFormat  format  =
      new SimpleDateFormat(DoosConstants.DATUM);

  public void execute(JobExecutionContext context)
      throws JobExecutionException {
    LOGGER.debug("CleanUpJob Started.");

    Long      retention = Long.valueOf(getParameter("retention.logging"));
    Calendar  cal       = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    cal.add(Calendar.DAY_OF_YEAR, retention.intValue() * -1);

    Long  loggings  = ((LoggingService)
        new JNDI.JNDINaam().metBean(LoggingService.class).locate())
            .cleanup(retention);

    LOGGER.info(getTekst("message.retention.date",
        format.format(cal.getTime()), retention));
    LOGGER.info(getTekst("message.retention.loggings", loggings));
  }
}
