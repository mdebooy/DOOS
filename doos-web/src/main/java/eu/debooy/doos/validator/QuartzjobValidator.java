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
package eu.debooy.doos.validator;

import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.openejb.quartz.CronExpression;


/**
 * @author Marco de Booij
 */
public class QuartzjobValidator {
  private QuartzjobValidator() {}

  public static List<Message> valideer(Quartzjob quartzjob) {
    List<Message> fouten  = new ArrayList<Message>();

    valideerCron(quartzjob.getCron(), fouten);
    valideerGroep(quartzjob.getGroep(), fouten);
    valideerJavaclass(quartzjob.getJavaclass(), fouten);
    valideerJob(quartzjob.getJob(), fouten);
    valideerOmschrijving(quartzjob.getOmschrijving(), fouten);

    return fouten;
  }

  private static void valideerCron(String cron, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(cron)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.cronexpressie"));
    } else {
      @SuppressWarnings("unused")
      CronExpression cronExpression;
      try {
        cronExpression = new CronExpression(cron);
      } catch (ParseException e) {
        fouten.add(new Message(Message.ERROR, PersistenceConstants.INVALID,
                               "_I18N.label.cronexpressie"));
      }
    }
  }

  private static void valideerGroep(String groep, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(groep)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.quartzgroep"));
    }
  }

  private static void valideerJavaclass(String javaclass,
                                        List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(javaclass)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.javaclass"));
    }
  }

  private static void valideerJob(String job, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(job)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.quartzjob"));
    }
  }

  private static void valideerOmschrijving(String omschrijving,
                                           List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(omschrijving)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.omschrijving"));
    }
  }
}
