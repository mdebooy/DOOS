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
public class LijstDtoTest {
  private static  LijstDto lijstDto;

  @BeforeClass
  public static void setUpClass() {
    lijstDto  = new LijstDto();

    lijstDto.setLijst(TestConstants.LIJST);
    lijstDto.setLijstnaam(TestConstants.LIJSTNAAM);
    lijstDto.setOmschrijving(TestConstants.OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new LijstDto();
    var groter  = new LijstDto();
    var kleiner = new LijstDto();

    gelijk.setLijstnaam(lijstDto.getLijstnaam());
    groter.setLijstnaam(TestConstants.LIJSTNAAM_G);
    kleiner.setLijstnaam(TestConstants.LIJSTNAAM_K);

    assertTrue(lijstDto.compareTo(groter) < 0);
    assertEquals(0, lijstDto.compareTo(gelijk));
    assertTrue(lijstDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new LijstDto();


    assertEquals(lijstDto, lijstDto);
    assertNotEquals(lijstDto, null);
    assertNotEquals(lijstDto, TestConstants.NAAM);
    assertNotEquals(lijstDto, instance);

    instance.setLijstnaam(TestConstants.LIJSTNAAM);

    assertEquals(lijstDto, instance);
  }

  @Test
  public void testGetLijst() {
    assertEquals(TestConstants.LIJST, lijstDto.getLijst());
  }

  @Test
  public void testGetLijstnaam() {
    assertEquals(TestConstants.LIJSTNAAM, lijstDto.getLijstnaam());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(TestConstants.OMSCHRIJVING, lijstDto.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.LIJST_HASH, lijstDto.hashCode());
  }

  @Test
  public void testSetLijst() {
    var instance  = new LijstDto();

    assertNotEquals(TestConstants.LIJST, instance.getLijst());

    instance.setLijst(TestConstants.LIJST);

    assertEquals(TestConstants.LIJST, instance.getLijst());
  }

  @Test
  public void testSetLijstnaam1() {
    var instance  = new LijstDto();

    assertNotEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(TestConstants.LIJSTNAAM);

    assertEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());
  }

  @Test
  public void testSetLijstnaam2() {
    var instance  = new LijstDto();

    assertNotEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(TestConstants.LIJSTNAAM.toUpperCase());

    assertEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());
  }

  @Test
  public void testSetNull() {
    var instance  = new LijstDto();

    instance.setLijst(TestConstants.LIJST);
    instance.setLijstnaam(TestConstants.LIJSTNAAM);
    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.LIJST, instance.getLijst());
    assertEquals(TestConstants.LIJSTNAAM, instance.getLijstnaam());
    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setLijst(null);
    instance.setLijstnaam(null);
    instance.setOmschrijving(null);

    assertNull(instance.getLijst());
    assertNull(instance.getLijstnaam());
    assertNull(instance.getOmschrijving());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new LijstDto();

    assertNotEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());
  }
}
