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

import static eu.debooy.doos.TestConstants.LIJSTNAAM;
import static eu.debooy.doos.TestConstants.LIJSTNAAM_G;
import static eu.debooy.doos.TestConstants.LIJSTNAAM_K;
import static eu.debooy.doos.TestConstants.LIJST_HASH;
import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import eu.debooy.doos.domain.LijstDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class LijstTest {
  private static  Lijst  lijst;

  @BeforeClass
  public static void setUpClass() {
    lijst  = new Lijst();

    lijst.setLijstnaam(LIJSTNAAM);
    lijst.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Lijst(lijst);
    var groter  = new Lijst();
    var kleiner = new Lijst();

    groter.setLijstnaam(LIJSTNAAM_G);
    kleiner.setLijstnaam(LIJSTNAAM_K);

    assertTrue(lijst.compareTo(groter) < 0);
    assertEquals(0, lijst.compareTo(gelijk));
    assertTrue(lijst.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var dto       = new LijstDto();
    var instance  = new Lijst();

    lijst.persist(dto);

    assertEquals(lijst, lijst);
    assertNotEquals(lijst, null);
    assertNotEquals(lijst, NAAM);
    assertNotEquals(lijst, instance);

    instance.setLijstnaam(LIJSTNAAM);

    assertEquals(lijst, instance);

    instance  = new Lijst(lijst);

    assertEquals(lijst, instance);

    instance  = new Lijst(dto);

    assertEquals(lijst, instance);
  }

  @Test
  public void testGetLijstnaam() {
    assertEquals(LIJSTNAAM, lijst.getLijstnaam());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(OMSCHRIJVING, lijst.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(LIJST_HASH, lijst.hashCode());
  }

  @Test
  public void testPersist() {
    var lijstDto = new LijstDto();

    lijst.persist(lijstDto);

    assertEquals(lijst.getLijstnaam(), lijstDto.getLijstnaam());
    assertEquals(lijst.getOmschrijving(), lijstDto.getOmschrijving());

    lijst.persist(lijstDto);

    assertEquals(lijst.getLijstnaam(), lijstDto.getLijstnaam());
    assertEquals(lijst.getOmschrijving(), lijstDto.getOmschrijving());
  }

  @Test
  public void testSetLijstnaam1() {
    var instance  = new Lijst();

    assertNotEquals(LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(LIJSTNAAM);

    assertEquals(LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(null);

    assertNull(instance.getLijstnaam());

    instance.setLijstnaam("");

    assertEquals("", instance.getLijstnaam());
  }

  @Test
  public void testSetLijstnaam2() {
    var instance  = new Lijst();

    assertNotEquals(LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(LIJSTNAAM.toUpperCase());

    assertEquals(LIJSTNAAM, instance.getLijstnaam());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new Lijst();

    assertNotEquals(OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(OMSCHRIJVING);

    assertEquals(OMSCHRIJVING, instance.getOmschrijving());
  }
}
