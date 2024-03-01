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

import eu.debooy.doos.TestConstants;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class TaalnaamDtoTest {
  private static  TaalnaamDto taalnaamDto;

  @BeforeClass
  public static void setUpClass() {
    taalnaamDto = new TaalnaamDto();

    taalnaamDto.setIso6392t(TestConstants.ISO6392T);
    taalnaamDto.setNaam(TestConstants.NAAM);
    taalnaamDto.setTaalId(TestConstants.TAALID);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new TaalnaamDto();
    var groter  = new TaalnaamDto();
    var kleiner = new TaalnaamDto();

    gelijk.setIso6392t(taalnaamDto.getIso6392t());
    gelijk.setNaam(taalnaamDto.getNaam());
    gelijk.setTaalId(taalnaamDto.getTaalId());
    groter.setIso6392t(TestConstants.ISO6392T_G);
    groter.setTaalId(taalnaamDto.getTaalId());
    kleiner.setIso6392t(TestConstants.ISO6392T_K);
    kleiner.setTaalId(taalnaamDto.getTaalId());

    assertTrue(taalnaamDto.compareTo(groter) < 0);
    assertEquals(0, taalnaamDto.compareTo(gelijk));
    assertTrue(taalnaamDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new TaalnaamDto();

    assertEquals(taalnaamDto, taalnaamDto);
    assertNotEquals(taalnaamDto, null);
    assertNotEquals(taalnaamDto, TestConstants.EIGENNAAM);
    assertNotEquals(taalnaamDto, instance);

    instance.setIso6392t(TestConstants.ISO6392T);
    instance.setTaalId(TestConstants.TAALID);
    assertEquals(taalnaamDto, instance);
  }

  @Test
  public void testGetIso6392t() {
    assertEquals(TestConstants.ISO6392T, taalnaamDto.getIso6392t());
  }

  @Test
  public void testGetNaam() {
    assertEquals(TestConstants.NAAM, taalnaamDto.getNaam());
  }

  @Test
  public void testGetTaalId() {
    assertEquals(TestConstants.TAALID, taalnaamDto.getTaalId());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.TAALNAAMDTO_HASH, taalnaamDto.hashCode());
  }

  @Test
  public void testSetIso6392t1() {
    var instance  = new TaalnaamDto();
    assertNotEquals(TestConstants.ISO6392T, instance.getIso6392t());
    instance.setIso6392t(TestConstants.ISO6392T);

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6392t2() {
    var instance  = new TaalnaamDto();
    assertNotEquals(TestConstants.ISO6392T, instance.getIso6392t());
    instance.setIso6392t(TestConstants.ISO6392T.toUpperCase());

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetNaam() {
    var instance  = new TaalnaamDto();
    assertNotEquals(TestConstants.NAAM, instance.getNaam());
    instance.setNaam(TestConstants.NAAM);

    assertEquals(TestConstants.NAAM, instance.getNaam());
  }

  @Test
  public void testSetNull() {
    var instance  = new TaalnaamDto();

    instance.setIso6392t(TestConstants.ISO6392T);
    instance.setNaam(TestConstants.NAAM);
    instance.setTaalId(TestConstants.TAALID);

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
    assertEquals(TestConstants.NAAM, instance.getNaam());
    assertEquals(TestConstants.TAALID, instance.getTaalId());

    instance.setIso6392t(null);
    instance.setNaam(null);
    instance.setTaalId(null);

    assertNull(instance.getIso6392t());
    assertNull(instance.getNaam());
    assertNull(instance.getTaalId());
  }

  @Test
  public void testSetTaalId() {
    var instance  = new TaalnaamDto();
    assertNotEquals(TestConstants.TAALID, instance.getTaalId());
    instance.setTaalId(TestConstants.TAALID);

    assertEquals(TestConstants.TAALID, instance.getTaalId());
  }
}
