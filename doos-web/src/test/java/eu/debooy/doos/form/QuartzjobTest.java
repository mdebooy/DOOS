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
package eu.debooy.doos.form;

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
import eu.debooy.doos.domain.QuartzjobDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class QuartzjobTest {
  private static  Quartzjob quartzjob;

  @BeforeClass
  public static void setUpClass() {
    quartzjob = new Quartzjob();

    quartzjob.setCron(CRON);
    quartzjob.setGroep(GROEP);
    quartzjob.setJavaclass(JAVACLASS);
    quartzjob.setJob(JOB);
    quartzjob.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testCompareTo1() {
    var gelijk  = new Quartzjob();
    var groter  = new Quartzjob();
    var kleiner = new Quartzjob();

    gelijk.setGroep(GROEP);
    gelijk.setJob(JOB);
    groter.setGroep(GROEP_G);
    groter.setJob(JOB);
    kleiner.setGroep(GROEP_K);
    kleiner.setJob(JOB);

    assertTrue(quartzjob.compareTo(groter) < 0);
    assertEquals(0, quartzjob.compareTo(gelijk));
    assertTrue(quartzjob.compareTo(kleiner) > 0);
  }

  @Test
  public void testCompareTo2() {
    var gelijk  = new Quartzjob(quartzjob);
    var groter  = new Quartzjob();
    var kleiner = new Quartzjob();

    groter.setGroep(GROEP);
    groter.setJob(JOB_G);
    kleiner.setGroep(GROEP);
    kleiner.setJob(JOB_K);

    assertTrue(quartzjob.compareTo(groter) < 0);
    assertEquals(0, quartzjob.compareTo(gelijk));
    assertTrue(quartzjob.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var dto       = new QuartzjobDto();
    var instance  = new Quartzjob();

    quartzjob.persist(dto);

    assertEquals(quartzjob, quartzjob);
    assertNotEquals(quartzjob, null);
    assertNotEquals(quartzjob, JOB);
    assertNotEquals(quartzjob, instance);

    instance.setGroep(GROEP);
    instance.setJob(JOB);
    assertEquals(quartzjob, instance);

    instance  = new Quartzjob(quartzjob);
    assertEquals(quartzjob, instance);

    instance  = new Quartzjob(dto);
    assertEquals(quartzjob, instance);
  }

  @Test
  public void testGetCron() {
    assertEquals(CRON, quartzjob.getCron());
  }

  @Test
  public void testGetGroep() {
    assertEquals(GROEP, quartzjob.getGroep());
  }

  @Test
  public void testGetJavaclass() {
    assertEquals(JAVACLASS, quartzjob.getJavaclass());
  }

  @Test
  public void testGetJob() {
    assertEquals(JOB, quartzjob.getJob());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(OMSCHRIJVING, quartzjob.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(QUARTZJOB_HASH, quartzjob.hashCode());
  }

  @Test
  public void testPersist() {
    var quartzjobDto  = new QuartzjobDto();

    quartzjob.persist(quartzjobDto);

    assertEquals(quartzjob.getCron(), quartzjobDto.getCron());
    assertEquals(quartzjob.getGroep(), quartzjobDto.getGroep());
    assertEquals(quartzjob.getJavaclass(), quartzjobDto.getJavaclass());
    assertEquals(quartzjob.getJob(), quartzjobDto.getJob());
    assertEquals(quartzjob.getOmschrijving(), quartzjobDto.getOmschrijving());

    quartzjob.persist(quartzjobDto);

    assertEquals(quartzjob.getCron(), quartzjobDto.getCron());
    assertEquals(quartzjob.getGroep(), quartzjobDto.getGroep());
    assertEquals(quartzjob.getJavaclass(), quartzjobDto.getJavaclass());
    assertEquals(quartzjob.getJob(), quartzjobDto.getJob());
    assertEquals(quartzjob.getOmschrijving(), quartzjobDto.getOmschrijving());
  }

  @Test
  public void testSetCron() {
    var instance  = new Quartzjob();
    assertNotEquals(CRON, instance.getCron());
    instance.setCron(CRON);

    assertEquals(CRON, instance.getCron());
  }

  @Test
  public void testSetGroep() {
    var instance  = new Quartzjob();
    assertNotEquals(GROEP, instance.getGroep());
    instance.setGroep(GROEP);

    assertEquals(GROEP, instance.getGroep());
  }

  @Test
  public void testSetJavaclass() {
    var instance  = new Quartzjob();
    assertNotEquals(JAVACLASS, instance.getJavaclass());
    instance.setJavaclass(JAVACLASS);

    assertEquals(JAVACLASS, instance.getJavaclass());
  }

  @Test
  public void testSetJob() {
    var instance  = new Quartzjob();
    assertNotEquals(JOB, instance.getJob());
    instance.setJob(JOB);

    assertEquals(JOB, instance.getJob());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new Quartzjob();
    assertNotEquals(OMSCHRIJVING, instance.getOmschrijving());
    instance.setOmschrijving(OMSCHRIJVING);

    assertEquals(OMSCHRIJVING, instance.getOmschrijving());
  }
}
