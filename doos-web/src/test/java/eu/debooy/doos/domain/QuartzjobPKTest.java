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

import static eu.debooy.doos.TestConstants.EIGENNAAM;
import static eu.debooy.doos.TestConstants.GROEP;
import static eu.debooy.doos.TestConstants.JOB;
import static eu.debooy.doos.TestConstants.QUARTZJOBPK_HASH;
import static eu.debooy.doos.TestConstants.QUARTZJOBPK_TOSTRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class QuartzjobPKTest {
  private static final  QuartzjobPK quartzjobPk  =
      new QuartzjobPK(GROEP, JOB);

  @Test
  public void testEquals() {
    var instance  = new QuartzjobPK();

    assertEquals(quartzjobPk, quartzjobPk);
    assertNotEquals(quartzjobPk, null);
    assertNotEquals(quartzjobPk, EIGENNAAM);
    assertNotEquals(quartzjobPk, instance);

    instance.setGroep(GROEP);
    instance.setJob(JOB);
    assertEquals(quartzjobPk, instance);
  }

  @Test
  public void testGetGroep() {
    assertEquals(GROEP, quartzjobPk.getGroep());
  }

  @Test
  public void testGetJob() {
    assertEquals(JOB, quartzjobPk.getJob());
  }

  @Test
  public void testHashCode() {
    assertEquals(QUARTZJOBPK_HASH, quartzjobPk.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new QuartzjobPK();

    assertNull(instance.getGroep());
    assertNull(instance.getJob());
  }

  @Test
  public void testInit2() {
    var instance  = new QuartzjobPK(GROEP, JOB);

    assertEquals(GROEP, instance.getGroep());
    assertEquals(JOB, instance.getJob());
  }

  @Test
  public void testSetIso6392t() {
    var instance  = new QuartzjobPK();

    assertNotEquals(GROEP, instance.getGroep());
    instance.setGroep(GROEP);

    assertEquals(GROEP, instance.getGroep());
  }

  @Test
  public void testSetJob() {
    var instance  = new QuartzjobPK();

    assertNotEquals(JOB, instance.getJob());
    instance.setJob(JOB);

    assertEquals(JOB, instance.getJob());
  }

  @Test
  public void testToString() {
    assertEquals(QUARTZJOBPK_TOSTRING, quartzjobPk.toString());
  }
}
