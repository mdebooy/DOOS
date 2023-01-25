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
package eu.debooy.doos.model;

import static eu.debooy.doos.TestConstants.CRON;
import static eu.debooy.doos.TestConstants.GROEP;
import static eu.debooy.doos.TestConstants.GROEP_G;
import static eu.debooy.doos.TestConstants.GROEP_K;
import static eu.debooy.doos.TestConstants.JAVACLASS;
import static eu.debooy.doos.TestConstants.JOB;
import static eu.debooy.doos.TestConstants.JOB_G;
import static eu.debooy.doos.TestConstants.JOB_K;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import static eu.debooy.doos.TestConstants.QUARTZJOBDATA_HASH;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class QuartzjobDataTest {
  private static  Date          endTime;
  private static  Date          nextFireTime;
  private static  Date          previousFireTime;
  private static  Date          startTime;
  private static  QuartzjobData quartzjobData;

  @BeforeClass
  public static void setUpClass() {
    previousFireTime  = new Date();
    nextFireTime      = new Date(previousFireTime.getTime() + 3600000);
    startTime         = new Date(previousFireTime.getTime() - 3600000);
    endTime           = new Date(nextFireTime.getTime() + 3600000);
    quartzjobData     = new QuartzjobData();

    quartzjobData.setCron(CRON);
    quartzjobData.setEndTime(endTime);
    quartzjobData.setGroep(GROEP);
    quartzjobData.setJavaclass(JAVACLASS);
    quartzjobData.setJob(JOB);
    quartzjobData.setNextFireTime(nextFireTime);
    quartzjobData.setOmschrijving(OMSCHRIJVING);
    quartzjobData.setPreviousFireTime(previousFireTime);
    quartzjobData.setStartTime(startTime);
  }

  @Test
  public void testCompareTo1() {
    var gelijk  = new QuartzjobData();
    var groter  = new QuartzjobData();
    var kleiner = new QuartzjobData();

    gelijk.setGroep(GROEP);
    gelijk.setJob(JOB);
    groter.setGroep(GROEP_G);
    groter.setJob(JOB);
    kleiner.setGroep(GROEP_K);
    kleiner.setJob(JOB);

    assertTrue(quartzjobData.compareTo(groter) < 0);
    assertEquals(0, quartzjobData.compareTo(gelijk));
    assertTrue(quartzjobData.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo2() {
    var gelijk  = new QuartzjobData(quartzjobData);
    var groter  = new QuartzjobData();
    var kleiner = new QuartzjobData();

    groter.setGroep(GROEP);
    groter.setJob(JOB_G);
    kleiner.setGroep(GROEP);
    kleiner.setJob(JOB_K);

    assertTrue(quartzjobData.compareTo(groter) < 0);
    assertEquals(0, quartzjobData.compareTo(gelijk));
    assertTrue(quartzjobData.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new QuartzjobData();

    assertEquals(quartzjobData, quartzjobData);
    assertNotEquals(quartzjobData, null);
    assertNotEquals(quartzjobData, JOB);
    assertNotEquals(quartzjobData, instance);

    instance.setGroep(GROEP);
    instance.setJob(JOB);

    assertEquals(quartzjobData, instance);

    instance  = new QuartzjobData(quartzjobData);

    assertEquals(quartzjobData, instance);
  }

  @Test
  public void testGetCron() {
    assertEquals(CRON, quartzjobData.getCron());
  }

  @Test
  public void testGetEndTime() {
    assertEquals(endTime, quartzjobData.getEndTime());
  }

  @Test
  public void testGetGroep() {
    assertEquals(GROEP, quartzjobData.getGroep());
  }

  @Test
  public void testGetJavaclass() {
    assertEquals(JAVACLASS, quartzjobData.getJavaclass());
  }

  @Test
  public void testGetJob() {
    assertEquals(JOB, quartzjobData.getJob());
  }

  @Test
  public void testGetNextFireTime() {
    assertEquals(nextFireTime, quartzjobData.getNextFireTime());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(OMSCHRIJVING, quartzjobData.getOmschrijving());
  }

  @Test
  public void testGetPreviousFireTime() {
    assertEquals(previousFireTime, quartzjobData.getPreviousFireTime());
  }

  @Test
  public void testGetStartTime() {
    assertEquals(startTime, quartzjobData.getStartTime());
  }

  @Test
  public void testHashCode() {
    assertEquals(QUARTZJOBDATA_HASH, quartzjobData.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new QuartzjobData();

    assertNull(instance.getCron());
    assertNull(instance.getEndTime());
    assertNull(instance.getGroep());
    assertNull(instance.getJavaclass());
    assertNull(instance.getJob());
    assertNull(instance.getNextFireTime());
    assertNull(instance.getOmschrijving());
    assertNull(instance.getPreviousFireTime());
    assertNull(instance.getStartTime());
  }

  @Test
  public void testInit2() {
    var instance  = new QuartzjobData(quartzjobData);

    assertEquals(quartzjobData.getCron(), instance.getCron());
    assertEquals(quartzjobData.getEndTime(), instance.getEndTime());
    assertEquals(quartzjobData.getGroep(), instance.getGroep());
    assertEquals(quartzjobData.getJavaclass(), instance.getJavaclass());
    assertEquals(quartzjobData.getJob(), instance.getJob());
    assertEquals(quartzjobData.getNextFireTime(), instance.getNextFireTime());
    assertEquals(quartzjobData.getOmschrijving(), instance.getOmschrijving());
    assertEquals(quartzjobData.getPreviousFireTime(),
                 instance.getPreviousFireTime());
    assertEquals(quartzjobData.getStartTime(), instance.getStartTime());
  }

  @Test
  public void testInit3() {
    var instance  = new QuartzjobData(CRON, GROEP, JAVACLASS, JOB,
                                      OMSCHRIJVING);

    assertEquals(quartzjobData.getCron(), instance.getCron());
    assertNull(instance.getEndTime());
    assertEquals(quartzjobData.getGroep(), instance.getGroep());
    assertEquals(quartzjobData.getJavaclass(), instance.getJavaclass());
    assertEquals(quartzjobData.getJob(), instance.getJob());
    assertNull(instance.getNextFireTime());
    assertEquals(quartzjobData.getOmschrijving(), instance.getOmschrijving());
    assertNull(instance.getPreviousFireTime());
    assertNull(instance.getStartTime());
  }

  @Test
  public void testSetCron() {
    var instance  = new QuartzjobData();

    assertNotEquals(CRON, instance.getCron());

    instance.setCron(CRON);

    assertEquals(CRON, instance.getCron());
  }

  @Test
  public void testSetEndTime() {
    var instance  = new QuartzjobData();

    assertNotEquals(endTime, instance.getEndTime());

    instance.setEndTime(endTime);

    assertEquals(endTime, instance.getEndTime());

    instance.setEndTime(null);

    assertNull(instance.getEndTime());
  }

  @Test
  public void testSetGroep() {
    var instance  = new QuartzjobData();

    assertNotEquals(GROEP, instance.getGroep());

    instance.setGroep(GROEP);

    assertEquals(GROEP, instance.getGroep());
  }

  @Test
  public void testSetJavaclass() {
    var instance  = new QuartzjobData();

    assertNotEquals(JAVACLASS, instance.getJavaclass());

    instance.setJavaclass(JAVACLASS);

    assertEquals(JAVACLASS, instance.getJavaclass());
  }

  @Test
  public void testSetJob() {
    var instance  = new QuartzjobData();

    assertNotEquals(JOB, instance.getJob());

    instance.setJob(JOB);

    assertEquals(JOB, instance.getJob());
  }

  @Test
  public void testSetNextFireTime() {
    var instance  = new QuartzjobData();

    assertNotEquals(nextFireTime, instance.getNextFireTime());

    instance.setNextFireTime(nextFireTime);

    assertEquals(nextFireTime, instance.getNextFireTime());

    instance.setNextFireTime(null);

    assertNull(instance.getNextFireTime());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new QuartzjobData();

    assertNotEquals(OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(OMSCHRIJVING);

    assertEquals(OMSCHRIJVING, instance.getOmschrijving());
  }

  @Test
  public void testSetPreviousFireTime() {
    var instance  = new QuartzjobData();

    assertNotEquals(previousFireTime, instance.getPreviousFireTime());

    instance.setPreviousFireTime(previousFireTime);

    assertEquals(previousFireTime, instance.getPreviousFireTime());

    instance.setPreviousFireTime(null);

    assertNull(instance.getPreviousFireTime());
  }

  @Test
  public void testSetStartTime() {
    var instance  = new QuartzjobData();

    assertNotEquals(startTime, instance.getStartTime());

    instance.setStartTime(startTime);

    assertEquals(startTime, instance.getStartTime());

    instance.setStartTime(null);

    assertNull(instance.getStartTime());
  }
}
