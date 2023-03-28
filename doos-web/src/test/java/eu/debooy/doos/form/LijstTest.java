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
  private static  Lijst lijst;

  @BeforeClass
  public static void setUpClass() {
    lijst  = new Lijst();

    lijst.setLijstnaam(TestConstants.LIJSTNAAM);
    lijst.setOmschrijving(TestConstants.OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Lijst();
    var groter  = new Lijst();
    var kleiner = new Lijst();

    gelijk.setLijstnaam(TestConstants.LIJSTNAAM);
    groter.setLijstnaam(TestConstants.LIJSTNAAM_G);
    kleiner.setLijstnaam(TestConstants.LIJSTNAAM_K);

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
    assertNotEquals(lijst, TestConstants.NAAM);
    assertNotEquals(lijst, instance);

    instance.setLijstnaam(TestConstants.LIJSTNAAM);

    assertEquals(lijst, instance);

    instance  = new Lijst(dto);

    assertEquals(lijst, instance);
  }

  @Test
  public void testGetLijstnaam() {
    assertEquals(TestConstants.LIJSTNAAM, lijst.getLijstnaam());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(TestConstants.OMSCHRIJVING, lijst.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.LIJST_HASH, lijst.hashCode());
  }

  @Test
  public void testPersist() {
    var lijstDto = new LijstDto();

    lijst.persist(lijstDto);

    assertEquals(lijst.getLijstnaam(), lijstDto.getLijstnaam());
    assertEquals(lijst.getOmschrijving(), lijstDto.getOmschrijving());
  }

  @Test
  public void testSetLijstnaam1() {
    var instance  = new Lijst();

    assertNotEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(TestConstants.LIJSTNAAM);

    assertEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(null);

    assertNull(instance.getLijstnaam());

    instance.setLijstnaam("");

    assertEquals("", instance.getLijstnaam());
  }

  @Test
  public void testSetLijstnaam2() {
    var instance  = new Lijst();

    assertNotEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(TestConstants.LIJSTNAAM.toUpperCase());

    assertEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new Lijst();

    assertNotEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());
  }
}
