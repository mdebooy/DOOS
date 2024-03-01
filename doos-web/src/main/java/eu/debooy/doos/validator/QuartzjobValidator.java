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

import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doosutils.ComponentsUtils;
import eu.debooy.doosutils.DoosConstants;
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
  private static final  String  LBL_CRON          = "_I18N.label.cronexpressie";
  private static final  String  LBL_GROEP         = "_I18N.label.quartzgroep";
  private static final  String  LBL_JAVACLASS     = "_I18N.label.javaclass";
  private static final  String  LBL_JOB           = "_I18N.label.quartzjob";
  private static final  String  LBL_OMSCHRIJVING  = "_I18N.label.omschrijving";

  private QuartzjobValidator() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Message> valideer(QuartzjobDto quartzjob) {
    if (null == quartzjob) {
      return ComponentsUtils.objectIsNull("QuartzjobDto");
    }

    return valideer(new Quartzjob(quartzjob));
  }

  public static List<Message> valideer(Quartzjob quartzjob) {
    if (null == quartzjob) {
      return ComponentsUtils.objectIsNull("Quartzjob");
    }

    List<Message> fouten  = new ArrayList<>();

    valideerCron(quartzjob.getCron(), fouten);
    valideerGroep(quartzjob.getGroep(), fouten);
    valideerJavaclass(quartzjob.getJavaclass(), fouten);
    valideerJob(quartzjob.getJob(), fouten);
    valideerOmschrijving(quartzjob.getOmschrijving(), fouten);

    return fouten;
  }

  private static void valideerCron(String cron, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(cron)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_CRON})
                            .setAttribute(QuartzjobDto.COL_CRON)
                            .build());
      return;
    }

    if (cron.length() > 50) {
      fouten.add(new Message.Builder()
                            .setAttribute(QuartzjobDto.COL_CRON)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_CRON, 50})
                            .build());
      return;
    }

    try {
      var cronExpression = new CronExpression(cron);
      cronExpression.hashCode();
    } catch (ParseException e) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.INVALID)
                            .setParams(new Object[]{LBL_CRON})
                            .setAttribute(QuartzjobDto.COL_CRON)
                            .build());
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(DoosConstants.NOI18N)
                            .setParams(new Object[]{e.getLocalizedMessage()})
                            .setAttribute(QuartzjobDto.COL_CRON)
                            .build());
    }
  }

  private static void valideerGroep(String groep, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(groep)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_GROEP})
                            .setAttribute(QuartzjobDto.COL_GROEP)
                            .build());
      return;
    }

    if (groep.length() > 15) {
      fouten.add(new Message.Builder()
                            .setAttribute(QuartzjobDto.COL_GROEP)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_GROEP, 15})
                            .build());
    }
  }

  private static void valideerJavaclass(String javaclass,
                                        List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(javaclass)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_JAVACLASS})
                            .setAttribute(QuartzjobDto.COL_JAVACLASS)
                            .build());
      return;
    }

    if (javaclass.length() > 100) {
      fouten.add(new Message.Builder()
                            .setAttribute(QuartzjobDto.COL_JAVACLASS)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_JAVACLASS, 100})
                            .build());
    }
  }

  private static void valideerJob(String job, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(job)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_JOB})
                            .setAttribute(QuartzjobDto.COL_JOB)
                            .build());
      return;
    }

    if (job.length() > 15) {
      fouten.add(new Message.Builder()
                            .setAttribute(QuartzjobDto.COL_JOB)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_JOB, 15})
                            .build());
    }
  }

  private static void valideerOmschrijving(String omschrijving,
                                           List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(omschrijving)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_OMSCHRIJVING})
                            .setAttribute(QuartzjobDto.COL_OMSCHRIJVING)
                            .build());
      return;
    }

    if (omschrijving.length() > 100) {
      fouten.add(new Message.Builder()
                            .setAttribute(QuartzjobDto.COL_OMSCHRIJVING)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_OMSCHRIJVING, 100})
                            .build());
    }
  }
}
