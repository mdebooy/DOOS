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
import static eu.debooy.doos.TestConstants.EIGENNAAM_G;
import static eu.debooy.doos.TestConstants.EIGENNAAM_K;
import static eu.debooy.doos.TestConstants.ISO6391;
import static eu.debooy.doos.TestConstants.ISO6392B;
import static eu.debooy.doos.TestConstants.ISO6392T;
import static eu.debooy.doos.TestConstants.ISO6392T_G;
import static eu.debooy.doos.TestConstants.ISO6392T_K;
import static eu.debooy.doos.TestConstants.ISO6393;
import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.TAALID;
import static eu.debooy.doos.TestConstants.TAAL_HASH;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Marco de Booij
 */
public class TaalDtoTest {
  private static  TaalDto taalDto;

  @BeforeClass
  public static void setUpClass() {
    taalDto = new TaalDto();

    taalDto.setIso6391(ISO6391);
    taalDto.setIso6392b(ISO6392B);
    taalDto.setIso6392t(ISO6392T);
    taalDto.setIso6393(ISO6393);
    taalDto.setLevend(true);
    taalDto.setTaalId(TAALID);

    var taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T);
    taalnaamDto.setNaam(EIGENNAAM);
    taalnaamDto.setTaalId(TAALID);
    taalDto.addNaam(taalnaamDto);

    taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T_K);
    taalnaamDto.setNaam(EIGENNAAM_K);
    taalnaamDto.setTaalId(TAALID);
    taalDto.addNaam(taalnaamDto);
  }

  @Test
  public void testAddNaam1() {
    var instance  = new TaalDto();
    var taalnaam  = new TaalnaamDto();

    instance.setTaalId(TAALID);
    taalnaam.setIso6392t(ISO6392T);
    taalnaam.setNaam(NAAM);
    taalnaam.setTaalId(instance.getTaalId());
    instance.addNaam(taalnaam);

    assertEquals(NAAM, instance.getTaalnaam(ISO6392T).getNaam());
    assertEquals(instance.getTaalId(),
                 instance.getTaalnaam(ISO6392T).getTaalId());
    assertNull(instance.getTaalnaam(ISO6392T_K).getTaalId());
  }

  @Test
  public void testAddNaam2() {
    var instance  = new TaalDto();
    var taalnaam  = new TaalnaamDto();

    instance.setTaalId(TAALID);
    taalnaam.setIso6392t(ISO6392T);
    taalnaam.setNaam(NAAM);
    instance.addNaam(taalnaam);

    assertEquals(NAAM, instance.getTaalnaam(ISO6392T).getNaam());
    assertEquals(instance.getTaalId(),
                 instance.getTaalnaam(ISO6392T).getTaalId());
    assertNull(instance.getTaalnaam(ISO6392T_K).getTaalId());
  }

  @Test
  public void testAddNaam3() {
    var instance  = new TaalDto();
    var taalnaam  = new TaalnaamDto();

    instance.setTaalId(TAALID);
    taalnaam.setIso6392t(ISO6392T);
    taalnaam.setNaam(NAAM);
    taalnaam.setTaalId(TAALID + 1);
    try {
      instance.addNaam(taalnaam);
      fail("Geen IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // OK
    }
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new TaalDto();
    var groter  = new TaalDto();
    var kleiner = new TaalDto();

    gelijk.setTaalId(TAALID);
    gelijk.setIso6392t(ISO6392T);
    groter.setTaalId(TAALID - 1);
    groter.setIso6392t(ISO6392T_G);
    kleiner.setTaalId(TAALID + 1);
    kleiner.setIso6392t(ISO6392T_K);

    assertTrue(taalDto.compareTo(groter) < 0);
    assertEquals(0, taalDto.compareTo(gelijk));
    assertTrue(taalDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new TaalDto();

    assertEquals(taalDto, taalDto);
    assertNotEquals(taalDto, null);
    assertNotEquals(taalDto, EIGENNAAM);
    assertNotEquals(taalDto, instance);

    instance.setTaalId(TAALID);
    assertEquals(taalDto, instance);
  }

  @Test
  public void testGetEigennaam() {
    assertEquals(EIGENNAAM, taalDto.getEigennaam());
  }

  @Test
  public void testGetIso6391() {
    assertEquals(ISO6391, taalDto.getIso6391());
  }

  @Test
  public void testGetIso6392b() {
    assertEquals(ISO6392B, taalDto.getIso6392b());
  }

  @Test
  public void testGetIso6392t() {
    assertEquals(ISO6392T, taalDto.getIso6392t());
  }

  @Test
  public void testGetIso6393() {
    assertEquals(ISO6393, taalDto.getIso6393());
  }

  @Test
  public void testGetLevend() {
    assertTrue(taalDto.getLevend());
  }

  @Test
  public void testGetNaam1() {
    assertEquals(EIGENNAAM, taalDto.getNaam(ISO6392T));
  }

  @Test
  public void testGetNaam2() {
    assertEquals(EIGENNAAM_K, taalDto.getNaam(ISO6392T_K));
  }

  @Test
  public void testGetNaam3() {
    assertEquals("", taalDto.getNaam(ISO6392T_G));
  }

  @Test
  public void testGetTaalId() {
    assertEquals(TAALID, taalDto.getTaalId());
  }

  @Test
  public void testHashCode() {
    assertEquals(TAAL_HASH, taalDto.hashCode());
  }

  @Test
  public void testIsLevend() {
    assertTrue(taalDto.isLevend());
  }

  @Test
  public void testNaamComparator() {
    var groter  = new TaalDto();
    var kleiner = new TaalDto();

    var taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T);
    taalnaamDto.setNaam(EIGENNAAM_G);
    groter.setTaalId(TAALID - 1);
    groter.addNaam(taalnaamDto);

    taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T);
    taalnaamDto.setNaam(EIGENNAAM_K);
    kleiner.setTaalId(TAALID + 1);
    kleiner.addNaam(taalnaamDto);

    var           comparator  = new TaalDto.NaamComparator();
    comparator.setTaal(ISO6392T);
    Set<TaalDto>  talen       = new TreeSet<>(comparator);
    talen.add(groter);
    talen.add(taalDto);
    talen.add(kleiner);

    var tabel = new TaalDto[talen.size()];
    System.arraycopy(talen.toArray(), 0, tabel, 0, talen.size());
    assertEquals(kleiner.getEigennaam(), tabel[0].getEigennaam());
    assertEquals(taalDto.getEigennaam(), tabel[1].getEigennaam());
    assertEquals(groter.getEigennaam(), tabel[2].getEigennaam());
  }

  @Test
  public  void testRemoveTaalnaam1() {
    var instance = new TaalDto();

    instance.setIso6391(ISO6391);
    instance.setIso6392b(ISO6392B);
    instance.setIso6392t(ISO6392T);
    instance.setIso6393(ISO6393);
    instance.setLevend(true);
    instance.setTaalId(TAALID);

    var taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T);
    taalnaamDto.setNaam(EIGENNAAM);
    taalnaamDto.setTaalId(TAALID);
    instance.addNaam(taalnaamDto);

    taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T_K);
    taalnaamDto.setNaam(EIGENNAAM_K);
    taalnaamDto.setTaalId(TAALID);
    instance.addNaam(taalnaamDto);

    assertEquals(2, instance.getTaalnamen().size());

    try {
      instance.removeTaalnaam(ISO6392T_G);
      fail("Geen ObjectNotFoundException");
    } catch (ObjectNotFoundException e) {
      // OK
    }

    assertEquals(2, instance.getTaalnamen().size());
  }

  @Test
  public  void testRemoveTaalnaam2() {
    var instance = new TaalDto();

    instance.setIso6391(ISO6391);
    instance.setIso6392b(ISO6392B);
    instance.setIso6392t(ISO6392T);
    instance.setIso6393(ISO6393);
    instance.setLevend(true);
    instance.setTaalId(TAALID);

    var taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T);
    taalnaamDto.setNaam(EIGENNAAM);
    taalnaamDto.setTaalId(TAALID);
    instance.addNaam(taalnaamDto);

    taalnaamDto = new TaalnaamDto();
    taalnaamDto.setIso6392t(ISO6392T_K);
    taalnaamDto.setNaam(EIGENNAAM_K);
    taalnaamDto.setTaalId(TAALID);
    instance.addNaam(taalnaamDto);

    assertEquals(2, instance.getTaalnamen().size());

    instance.removeTaalnaam(ISO6392T_K);

    assertEquals(1, instance.getTaalnamen().size());
  }

  @Test
  public void testSetIso6391_1() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6391, instance.getIso6391());
    instance.setIso6391(ISO6391);

    assertEquals(ISO6391, instance.getIso6391());
  }

  @Test
  public void testSetIso6391_2() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6391, instance.getIso6391());
    instance.setIso6391(ISO6391.toUpperCase());

    assertEquals(ISO6391, instance.getIso6391());
  }

  @Test
  public void testSetIso6392b_1() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6392B, instance.getIso6392b());
    instance.setIso6392b(ISO6392B);

    assertEquals(ISO6392B, instance.getIso6392b());
  }

  @Test
  public void testSetIso6392b_2() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6392B, instance.getIso6392b());
    instance.setIso6392b(ISO6392B.toUpperCase());

    assertEquals(ISO6392B, instance.getIso6392b());
  }

  @Test
  public void testSetIso6392t_1() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6392T, instance.getIso6392t());
    instance.setIso6392t(ISO6392T);

    assertEquals(ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6392t_2() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6392T, instance.getIso6392t());
    instance.setIso6392t(ISO6392T.toUpperCase());

    assertEquals(ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6393_1() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6393, instance.getIso6393());
    instance.setIso6393(ISO6393);

    assertEquals(ISO6393, instance.getIso6393());
  }

  @Test
  public void testSetIso6393_2() {
    var instance  = new TaalDto();

    assertNotEquals(ISO6393, instance.getIso6393());
    instance.setIso6393(ISO6393.toUpperCase());

    assertEquals(ISO6393, instance.getIso6393());
  }

  @Test
  public void testSetLevend() {
    var instance  = new TaalDto();

    assertTrue(instance.getLevend());
    instance.setLevend(false);

    assertFalse(instance.getLevend());
  }

  @Test
  public void testSetTaalId() {
    var instance  = new TaalDto();

    assertNotEquals(TAALID, instance.getTaalId());
    instance.setTaalId(TAALID);

    assertEquals(TAALID, instance.getTaalId());
  }
}
