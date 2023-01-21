/*
 * Copyright (c) 2023 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
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

import static eu.debooy.doos.TestConstants.CRON;
import static eu.debooy.doos.TestConstants.GROEP;
import static eu.debooy.doos.TestConstants.JAVACLASS;
import static eu.debooy.doos.TestConstants.JOB;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class QuartzjobValidatorTest {
  public static final Message ERR_CRON0         =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(DoosConstants.NOI18N)
                 .setParams(new Object[]{""})
                 .setAttribute(QuartzjobDto.COL_CRON)
                 .build();
  public static final Message ERR_CRON1         =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.INVALID)
                 .setParams(new Object[]{"_I18N.label.cronexpressie"})
                 .setAttribute(QuartzjobDto.COL_CRON)
                 .build();
  public static final Message ERR_CRON2         =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_CRON)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.cronexpressie", 50})
                 .build();
  public static final Message ERR_GROEP         =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_GROEP)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.quartzgroep", 15})
                 .build();
  public static final Message ERR_JAVACLASS     =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_JAVACLASS)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.javaclass", 100})
                 .build();
  public static final Message ERR_JOB           =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_JOB)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.quartzjob", 15})
                 .build();
  public static final Message ERR_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.omschrijving", 100})
                 .build();
  public static final Message REQ_CRON          =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_CRON)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.cronexpressie"})
                 .build();
  public static final Message REQ_GROEP         =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_GROEP)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.quartzgroep"})
                 .build();
  public static final Message REQ_JAVACLASS     =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_JAVACLASS)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.javaclass"})
                 .build();
  public static final Message REQ_JOB           =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_JOB)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.quartzjob"})
                 .build();
  public static final Message REQ_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(QuartzjobDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.omschrijving"})
                 .build();

  private static  Quartzjob quartzjob;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_CRON);
    expResult.add(REQ_GROEP);
    expResult.add(REQ_JAVACLASS);
    expResult.add(REQ_JOB);
    expResult.add(REQ_OMSCHRIJVING);
  }

  @BeforeClass
  public static void setUpClass() {
    quartzjob  = new Quartzjob();

    quartzjob.setCron(CRON);
    quartzjob.setGroep(GROEP);
    quartzjob.setJavaclass(JAVACLASS);
    quartzjob.setJob(JOB);
    quartzjob.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testFouteQuartzjob1() {
    var           instance  = new Quartzjob(quartzjob);

    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 101, "X"));

    List<Message> result    = QuartzjobValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_OMSCHRIJVING.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteQuartzjobDto1() {
    var           instance  = new QuartzjobDto();

    quartzjob.persist(instance);
    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 101, "X"));

    List<Message> result    = QuartzjobValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_OMSCHRIJVING.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeQuartzjob1() {
    List<Message> result  = QuartzjobValidator.valideer(quartzjob);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeQuartzjob2() {
    var           instance  = new Quartzjob(quartzjob);

    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 100, "X"));
    List<Message> result    = QuartzjobValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeQuartzjobDto1() {
    var           instance  = new QuartzjobDto();

    quartzjob.persist(instance);
    List<Message> result    = QuartzjobValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeQuartzjobDto2() {
    var           instance  = new QuartzjobDto();

    quartzjob.persist(instance);
    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 100, "X"));
    List<Message> result    = QuartzjobValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testLegeQuartzjob() {
    var           instance  = new Quartzjob();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = QuartzjobValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testLegeQuartzjobDto() {
    var           instance  = new QuartzjobDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = QuartzjobValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }
}
