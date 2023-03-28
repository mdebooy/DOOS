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
package eu.debooy.doos.domain;

import static eu.debooy.doos.TestConstants.CRON;
import static eu.debooy.doos.TestConstants.GROEP;
import static eu.debooy.doos.TestConstants.GROEP_G;
import static eu.debooy.doos.TestConstants.GROEP_K;
import static eu.debooy.doos.TestConstants.JAVACLASS;
import static eu.debooy.doos.TestConstants.JOB;
import static eu.debooy.doos.TestConstants.JOB_G;
import static eu.debooy.doos.TestConstants.JOB_K;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import static eu.debooy.doos.TestConstants.QUARTZJOB_HASH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class QuartzjobDtoTest {
  private static  QuartzjobDto  quartzjobDto;

  @BeforeClass
  public static void setUpClass() {
    quartzjobDto = new QuartzjobDto();

    quartzjobDto.setCron(CRON);
    quartzjobDto.setGroep(GROEP);
    quartzjobDto.setJavaclass(JAVACLASS);
    quartzjobDto.setJob(JOB);
    quartzjobDto.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testCompareTo1() {
    var gelijk  = new QuartzjobDto();
    var groter  = new QuartzjobDto();
    var kleiner = new QuartzjobDto();

    gelijk.setGroep(GROEP);
    gelijk.setJob(JOB);
    groter.setGroep(GROEP_G);
    groter.setJob(JOB);
    kleiner.setGroep(GROEP_K);
    kleiner.setJob(JOB);

    assertTrue(quartzjobDto.compareTo(groter) < 0);
    assertEquals(0, quartzjobDto.compareTo(gelijk));
    assertTrue(quartzjobDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo2() {
    var gelijk  = new QuartzjobDto(quartzjobDto);
    var groter  = new QuartzjobDto();
    var kleiner = new QuartzjobDto();

    groter.setGroep(GROEP);
    groter.setJob(JOB_G);
    kleiner.setGroep(GROEP);
    kleiner.setJob(JOB_K);

    assertTrue(quartzjobDto.compareTo(groter) < 0);
    assertEquals(0, quartzjobDto.compareTo(gelijk));
    assertTrue(quartzjobDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new QuartzjobDto();

    assertEquals(quartzjobDto, quartzjobDto);
    assertNotEquals(quartzjobDto, null);
    assertNotEquals(quartzjobDto, JOB);
    assertNotEquals(quartzjobDto, instance);

    instance.setGroep(GROEP);
    instance.setJob(JOB);
    assertEquals(quartzjobDto, instance);

    instance  = new QuartzjobDto(quartzjobDto);
    assertEquals(quartzjobDto, instance);
  }

  @Test
  public void testGetCron() {
    assertEquals(CRON, quartzjobDto.getCron());
  }

  @Test
  public void testGetGroep() {
    assertEquals(GROEP, quartzjobDto.getGroep());
  }

  @Test
  public void testGetJavaclass() {
    assertEquals(JAVACLASS, quartzjobDto.getJavaclass());
  }

  @Test
  public void testGetJob() {
    assertEquals(JOB, quartzjobDto.getJob());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(OMSCHRIJVING, quartzjobDto.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(QUARTZJOB_HASH, quartzjobDto.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new QuartzjobDto();

    assertNull(instance.getCron());
    assertNull(instance.getGroep());
    assertNull(instance.getJavaclass());
    assertNull(instance.getJob());
    assertNull(instance.getOmschrijving());
  }

  @Test
  public void testInit2() {
    var instance  = new QuartzjobDto(CRON, GROEP, JAVACLASS, JOB, OMSCHRIJVING);

    assertEquals(CRON, instance.getCron());
    assertEquals(GROEP, instance.getGroep());
    assertEquals(JAVACLASS, instance.getJavaclass());
    assertEquals(JOB, instance.getJob());
    assertEquals(OMSCHRIJVING, instance.getOmschrijving());
  }

  @Test
  public void testInit3() {
    var instance  = new QuartzjobDto(quartzjobDto);

    assertEquals(quartzjobDto.getCron(), instance.getCron());
    assertEquals(quartzjobDto.getGroep(), instance.getGroep());
    assertEquals(quartzjobDto.getJavaclass(), instance.getJavaclass());
    assertEquals(quartzjobDto.getJob(), instance.getJob());
    assertEquals(quartzjobDto.getOmschrijving(), instance.getOmschrijving());
  }

  @Test
  public void testSetCron() {
    var instance  = new QuartzjobDto();
    assertNotEquals(CRON, instance.getCron());
    instance.setCron(CRON);

    assertEquals(CRON, instance.getCron());
  }

  @Test
  public void testSetGroep() {
    var instance  = new QuartzjobDto();
    assertNotEquals(GROEP, instance.getGroep());
    instance.setGroep(GROEP);

    assertEquals(GROEP, instance.getGroep());
  }

  @Test
  public void testSetJavaclass() {
    var instance  = new QuartzjobDto();
    assertNotEquals(JAVACLASS, instance.getJavaclass());
    instance.setJavaclass(JAVACLASS);

    assertEquals(JAVACLASS, instance.getJavaclass());
  }

  @Test
  public void testSetJob() {
    var instance  = new QuartzjobDto();
    assertNotEquals(JOB, instance.getJob());
    instance.setJob(JOB);

    assertEquals(JOB, instance.getJob());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new QuartzjobDto();
    assertNotEquals(OMSCHRIJVING, instance.getOmschrijving());
    instance.setOmschrijving(OMSCHRIJVING);

    assertEquals(OMSCHRIJVING, instance.getOmschrijving());
  }
}
