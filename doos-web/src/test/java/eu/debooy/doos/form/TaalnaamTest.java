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

import eu.debooy.doos.TestConstants;
import eu.debooy.doos.domain.TaalnaamDto;
import java.util.Set;
import java.util.TreeSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class TaalnaamTest {
  private static  Taalnaam  taalnaam;

  @BeforeClass
  public static void setUpClass() {
    taalnaam  = new Taalnaam();

    taalnaam.setIso6392t(TestConstants.ISO6392T);
    taalnaam.setNaam(TestConstants.NAAM);
    taalnaam.setTaalId(TestConstants.TAALID);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Taalnaam();
    var groter  = new Taalnaam();
    var kleiner = new Taalnaam();

    gelijk.setIso6392t(TestConstants.ISO6392T);
    gelijk.setTaalId(TestConstants.TAALID);
    groter.setIso6392t(TestConstants.ISO6392T_G);
    kleiner.setIso6392t(TestConstants.ISO6392T_K);

    assertTrue(taalnaam.compareTo(groter) < 0);
    assertEquals(0, taalnaam.compareTo(gelijk));
    assertTrue(taalnaam.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var dto       = new TaalnaamDto();
    var instance  = new Taalnaam();

    taalnaam.persist(dto);

    assertEquals(taalnaam, taalnaam);
    assertNotEquals(taalnaam, null);
    assertNotEquals(taalnaam, TestConstants.NAAM);
    assertNotEquals(taalnaam, instance);

    instance.setIso6392t(TestConstants.ISO6392T);
    instance.setTaalId(TestConstants.TAALID);
    assertEquals(taalnaam, instance);

    instance  = new Taalnaam(dto);
    assertEquals(taalnaam, instance);
  }

  @Test
  public void testGetIso6392t() {
    assertEquals(TestConstants.ISO6392T, taalnaam.getIso6392t());
  }

  @Test
  public void testGetNaam() {
    assertEquals(TestConstants.NAAM, taalnaam.getNaam());
  }

  @Test
  public void testGetTaalId() {
    assertEquals(TestConstants.TAALID, taalnaam.getTaalId());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.TAALNAAM_HASH, taalnaam.hashCode());
  }

  @Test
  public void testNaamComparator() {
    var groter  = new Taalnaam();
    var kleiner = new Taalnaam();

    groter.setNaam(TestConstants.NAAM_G);
    kleiner.setNaam(TestConstants.NAAM_K);

    Set<Taalnaam> taalnamen =
        new TreeSet<>(new Taalnaam.NaamComparator());
    taalnamen.add(groter);
    taalnamen.add(taalnaam);
    taalnamen.add(kleiner);

    var tabel = new Taalnaam[taalnamen.size()];
    System.arraycopy(taalnamen.toArray(), 0, tabel, 0, taalnamen.size());
    assertEquals(kleiner.getNaam(), tabel[0].getNaam());
    assertEquals(taalnaam.getNaam(), tabel[1].getNaam());
    assertEquals(groter.getNaam(), tabel[2].getNaam());
  }

  @Test
  public void testPersist() {
    var taalnaamDto = new TaalnaamDto();

    taalnaam.persist(taalnaamDto);

    assertEquals(taalnaam.getIso6392t(), taalnaamDto.getIso6392t());
    assertEquals(taalnaam.getNaam(), taalnaamDto.getNaam());
    assertEquals(taalnaam.getTaalId(), taalnaamDto.getTaalId());
  }

  @Test
  public void testSetIso6392t1() {
    var instance  = new Taalnaam();

    assertNotEquals(TestConstants.ISO6392T, instance.getIso6392t());

    instance.setIso6392t(TestConstants.ISO6392T);

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetIso6392t2() {
    var instance  = new Taalnaam();

    assertNotEquals(TestConstants.ISO6392T, instance.getIso6392t());

    instance.setIso6392t(TestConstants.ISO6392T.toUpperCase());

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetNaam() {
    var instance  = new Taalnaam();

    assertNotEquals(TestConstants.NAAM, instance.getNaam());

    instance.setNaam(TestConstants.NAAM);

    assertEquals(TestConstants.NAAM, instance.getNaam());
  }

  @Test
  public void testSetNull() {
    var instance  = new Taalnaam();

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
    var instance  = new Taalnaam();

    assertNotEquals(TestConstants.TAALID, instance.getTaalId());

    instance.setTaalId(TestConstants.TAALID);

    assertEquals(TestConstants.TAALID, instance.getTaalId());
  }
}
