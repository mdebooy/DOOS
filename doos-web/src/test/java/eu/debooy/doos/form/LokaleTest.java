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

package eu.debooy.doos.form;

import eu.debooy.doos.TestConstants;
import eu.debooy.doos.domain.LokaleDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class LokaleTest {
  private static  Lokale  lokale;

  @BeforeClass
  public static void setUpClass() {
    lokale    = new Lokale();
    lokale.setCode(TestConstants.LOKALE);
    lokale.setEersteTaal(TestConstants.ISO6392T);
    lokale.setTweedeTaal(TestConstants.ISO6392T_G);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Lokale();
    var groter  = new Lokale();
    var kleiner = new Lokale();

    gelijk.setCode(TestConstants.LOKALE);
    groter.setCode(TestConstants.LOKALE_G);
    kleiner.setCode(TestConstants.LOKALE_K);

    assertTrue(lokale.compareTo(groter) < 0);
    assertEquals(0, lokale.compareTo(gelijk));
    assertTrue(lokale.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new Lokale();

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
    var instance  = new Lokale();

    assertNull(instance.getCode());
    assertNull(instance.getEersteTaal());
    assertNull(instance.getTweedeTaal());
  }

  @Test
  public void testInit2() {
    var lokaleDto = new LokaleDto();

    lokaleDto.setCode(TestConstants.LOKALE);
    lokaleDto.setEersteTaal(TestConstants.ISO6392T);
    lokaleDto.setTweedeTaal(TestConstants.ISO6392T_G);

    var instance  = new Lokale(lokaleDto);

    assertEquals(lokaleDto.getCode(), instance.getCode());
    assertEquals(lokaleDto.getEersteTaal(), instance.getEersteTaal());
    assertEquals(lokaleDto.getTweedeTaal(), instance.getTweedeTaal());
  }

  @Test
  public void testInit3() {
    var instance  = new Lokale(TestConstants.LOKALE, TestConstants.ISO6392T,
                               TestConstants.ISO6392T_G);

    assertEquals(TestConstants.LOKALE, instance.getCode());
    assertEquals(TestConstants.ISO6392T, instance.getEersteTaal());
    assertEquals(TestConstants.ISO6392T_G, instance.getTweedeTaal());
  }

  @Test
  public void testPersist() {
    var parameter = new LokaleDto();

    lokale.persist(parameter);

    assertEquals(lokale.getCode(), parameter.getCode());
    assertEquals(lokale.getEersteTaal(), parameter.getEersteTaal());
    assertEquals(lokale.getTweedeTaal(), parameter.getTweedeTaal());
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
