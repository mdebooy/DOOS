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
import static eu.debooy.doos.TestConstants.NAAM_G;
import static eu.debooy.doos.TestConstants.NAAM_K;
import static eu.debooy.doos.TestConstants.TAALID;
import static eu.debooy.doos.TestConstants.TAAL_HASH;
import eu.debooy.doos.domain.TaalDto;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class TaalTest {
  private static  Taal  taal;

  @BeforeClass
  public static void setUpClass() {
    taal  = new Taal();

    taal.setEigennaam(EIGENNAAM);
    taal.setIso6391(ISO6391);
    taal.setIso6392b(ISO6392B);
    taal.setIso6392t(ISO6392T);
    taal.setIso6393(ISO6393);
    taal.setLevend(true);
    taal.setNaam(NAAM);
    taal.setTaalId(TAALID);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Taal(taal);
    var groter  = new Taal();
    var kleiner = new Taal();

    groter.setIso6392t(ISO6392T_G);
    kleiner.setIso6392t(ISO6392T_K);

    assertTrue(taal.compareTo(groter) < 0);
    assertEquals(0, taal.compareTo(gelijk));
    assertTrue(taal.compareTo(kleiner) > 0);
  }

  @Test
  public void testEigennaamComparator() {
    var groter  = new Taal();
    var kleiner = new Taal();

    groter.setEigennaam(EIGENNAAM_G);
    kleiner.setEigennaam(EIGENNAAM_K);

    Set<Taal> talen =
        new TreeSet<>(new Taal.EigennaamComparator());
    talen.add(groter);
    talen.add(taal);
    talen.add(kleiner);

    var tabel = new Taal[talen.size()];
    System.arraycopy(talen.toArray(), 0, tabel, 0, talen.size());
    assertEquals(kleiner.getEigennaam(), tabel[0].getEigennaam());
    assertEquals(taal.getEigennaam(), tabel[1].getEigennaam());
    assertEquals(groter.getEigennaam(), tabel[2].getEigennaam());
  }

  @Test
  public void testEquals() {
    var dto       = new TaalDto();
    var instance  = new Taal();

    taal.persist(dto);

    assertEquals(taal, taal);
    assertNotEquals(taal, null);
    assertNotEquals(taal, EIGENNAAM);
    assertNotEquals(taal, instance);

    instance.setTaalId(TAALID);
    assertEquals(taal, instance);

    instance  = new Taal(taal);
    assertEquals(taal, instance);

    instance  = new Taal(dto);
    assertEquals(taal, instance);

    instance  = new Taal(dto, ISO6391);
    assertEquals(taal, instance);
  }

  @Test
  public void testGetEigennaam() {
    assertEquals(EIGENNAAM, taal.getEigennaam());
  }

  @Test
  public void testGetIso6391() {
    assertEquals(ISO6391, taal.getIso6391());
  }

  @Test
  public void testGetIso6392b() {
    assertEquals(ISO6392B, taal.getIso6392b());
  }

  @Test
  public void testGetIso6392t() {
    assertEquals(ISO6392T, taal.getIso6392t());
  }

  @Test
  public void testGetIso6393() {
    assertEquals(ISO6393, taal.getIso6393());
  }

  @Test
  public void testGetLevend() {
    assertTrue(taal.getLevend());
  }

  @Test
  public void testGetNaam() {
    assertEquals(NAAM, taal.getNaam());
  }

  @Test
  public void testGetTaalId() {
    assertEquals(TAALID, taal.getTaalId());
  }

  @Test
  public void testHashCode() {
    assertEquals(TAAL_HASH, taal.hashCode());
  }

  @Test
  public void testIsLevend() {
    assertTrue(taal.isLevend());
  }

  @Test
  public void testNaamComparator() {
    var groter  = new Taal();
    var kleiner = new Taal();

    groter.setNaam(NAAM_G);
    kleiner.setNaam(NAAM_K);

    Set<Taal> talen =
        new TreeSet<>(new Taal.NaamComparator());
    talen.add(groter);
    talen.add(taal);
    talen.add(kleiner);

    var tabel = new Taal[talen.size()];
    System.arraycopy(talen.toArray(), 0, tabel, 0, talen.size());
    assertEquals(kleiner.getNaam(), tabel[0].getNaam());
    assertEquals(taal.getNaam(), tabel[1].getNaam());
    assertEquals(groter.getNaam(), tabel[2].getNaam());
  }

  @Test
  public void testPersist() {
    var taalDto   = new TaalDto();
    var instance  = new Taal(taal);

    instance.setLevend(false);
    instance.persist(taalDto);

    assertNotEquals(instance.getEigennaam(), taalDto.getEigennaam());
    assertEquals(instance.getIso6391(), taalDto.getIso6391());
    assertEquals(instance.getIso6392b(), taalDto.getIso6392b());
    assertEquals(instance.getIso6392t(), taalDto.getIso6392t());
    assertEquals(instance.getIso6393(), taalDto.getIso6393());
    assertEquals(instance.getLevend(), taalDto.getLevend());
    assertEquals(instance.getTaalId(), taalDto.getTaalId());

    instance.persist(taalDto);

    assertNotEquals(instance.getEigennaam(), taalDto.getEigennaam());
    assertEquals(instance.getIso6391(), taalDto.getIso6391());
    assertEquals(instance.getIso6392b(), taalDto.getIso6392b());
    assertEquals(instance.getIso6392t(), taalDto.getIso6392t());
    assertEquals(instance.getIso6393(), taalDto.getIso6393());
    assertEquals(instance.getLevend(), taalDto.getLevend());
    assertEquals(instance.getTaalId(), taalDto.getTaalId());
  }

  @Test
  public void testSetEigennaam() {
    var instance  = new Taal();

    assertNotEquals(EIGENNAAM, instance.getEigennaam());

    instance.setEigennaam(EIGENNAAM);

    assertEquals(EIGENNAAM, instance.getEigennaam());
  }

  @Test
  public void testSetIso6391_1() {
    var instance  = new Taal();

    assertNotEquals(ISO6391, instance.getIso6391());

    instance.setIso6391(ISO6391);

    assertEquals(ISO6391, instance.getIso6391());
  }

  @Test
  public void testSetIso6391_2() {
    var instance  = new Taal();

    assertNotEquals(ISO6391, instance.getIso6391());

    instance.setIso6391(ISO6391.toUpperCase());

    assertEquals(ISO6391, instance.getIso6391());
  }

  @Test
  public void testSetIso6392b_1() {
    var instance  = new Taal();

    assertNotEquals(ISO6392B, instance.getIso6392b());

    instance.setIso6392b(ISO6392B);

    assertEquals(ISO6392B, instance.getIso6392b());
  }

  @Test
  public void testSetIso6392b_2() {
    var instance  = new Taal();

    assertNotEquals(ISO6392B, instance.getIso6392b());

    instance.setIso6392b(ISO6392B.toUpperCase());

    assertEquals(ISO6392B, instance.getIso6392b());
  }

  @Test
  public void testSetIso6392t_1() {
    var instance  = new Taal();

    assertNotEquals(ISO6392T, instance.getIso6392t());

    instance.setIso6392t(ISO6392T);

    assertEquals(ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6392t_2() {
    var instance  = new Taal();

    assertNotEquals(ISO6392T, instance.getIso6392t());

    instance.setIso6392t(ISO6392T.toUpperCase());

    assertEquals(ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6392t_3() {
    var instance  = new Taal();

    assertNotEquals(ISO6392T, instance.getIso6392t());

    instance.setIso6392t(ISO6392T.toUpperCase());

    assertEquals(ISO6392T, instance.getIso6392t());

    instance.setIso6392t(null);

    assertNull(instance.getIso6392t());
  }

  @Test
  public void testSetIso6393_1() {
    var instance  = new Taal();

    assertNotEquals(ISO6393, instance.getIso6393());
    instance.setIso6393(ISO6393);

    assertEquals(ISO6393, instance.getIso6393());
  }

  @Test
  public void testSetIso6393_2() {
    var instance  = new Taal();

    assertNotEquals(ISO6393, instance.getIso6393());
    instance.setIso6393(ISO6393.toUpperCase());

    assertEquals(ISO6393, instance.getIso6393());
  }

  @Test
  public void testSetLevend() {
    var instance  = new Taal();

    assertTrue(instance.getLevend());
    instance.setLevend(false);

    assertFalse(instance.getLevend());
  }

  @Test
  public void testSetNaam() {
    var instance  = new Taal();

    assertNotEquals(NAAM, instance.getNaam());
    instance.setNaam(NAAM);

    assertEquals(NAAM, instance.getNaam());
  }

  @Test
  public void testSetTaalId() {
    var instance  = new Taal();

    assertNotEquals(TAALID, instance.getTaalId());
    instance.setTaalId(TAALID);

    assertEquals(TAALID, instance.getTaalId());
  }
}
