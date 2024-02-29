/*
 * Copyright (c) 2024 Marco de Booij
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

import eu.debooy.doos.TestConstants;
import eu.debooy.doos.form.Lokale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class LokaleDtoTest {
  private static  LokaleDto lokale;

  @BeforeClass
  public static void setUpClass() {
    lokale    = new LokaleDto();
    lokale.setCode(TestConstants.LOKALE);
    lokale.setEersteTaal(TestConstants.ISO6392T);
    lokale.setTweedeTaal(TestConstants.ISO6392T_G);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new LokaleDto();
    var groter  = new LokaleDto();
    var kleiner = new LokaleDto();

    gelijk.setCode(TestConstants.LOKALE);
    groter.setCode(TestConstants.LOKALE_G);
    kleiner.setCode(TestConstants.LOKALE_K);

    assertTrue(lokale.compareTo(groter) < 0);
    assertEquals(0, lokale.compareTo(gelijk));
    assertTrue(lokale.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new LokaleDto();

    assertEquals(lokale, lokale);
    assertNotEquals(lokale, null);
    assertNotEquals(lokale, TestConstants.LOKALE);
    assertNotEquals(lokale, instance);

    instance.setCode(TestConstants.LOKALE);
    assertEquals(lokale, instance);
  }

  @Test
  public void testGetCode() {
    assertEquals(TestConstants.LOKALE, lokale.getCode());
  }

  @Test
  public void testGetEersteTaal() {
    assertEquals(TestConstants.ISO6392T, lokale.getEersteTaal());
  }

  @Test
  public void testGetTweedeTaal() {
    assertEquals(TestConstants.ISO6392T_G, lokale.getTweedeTaal());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.LOKALE_HASH, lokale.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new LokaleDto();

    assertNull(instance.getCode());
    assertNull(instance.getEersteTaal());
    assertNull(instance.getTweedeTaal());
  }

  @Test
  public void testSetCode() {
    var instance  = new Lokale();

    assertNotEquals(TestConstants.LOKALE, instance.getCode());

    instance.setCode(TestConstants.LOKALE);

    assertEquals(TestConstants.LOKALE, instance.getCode());
  }

  @Test
  public void testSetEersteTaal() {
    var instance  = new Lokale();

    assertNotEquals(TestConstants.ISO6392T, instance.getEersteTaal());

    instance.setEersteTaal(TestConstants.ISO6392T);

    assertEquals(TestConstants.ISO6392T, instance.getEersteTaal());
  }

  @Test
  public void testSetTweedeTaal() {
    var instance  = new Lokale();

    assertNotEquals(TestConstants.ISO6392T_G, instance.getTweedeTaal());

    instance.setTweedeTaal(TestConstants.ISO6392T_G);

    assertEquals(TestConstants.ISO6392T_G, instance.getTweedeTaal());
  }
}