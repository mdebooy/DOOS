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

import static eu.debooy.doos.TestConstants.LIJSTNAAM;
import static eu.debooy.doos.TestConstants.LIJSTNAAM_G;
import static eu.debooy.doos.TestConstants.LIJSTNAAM_K;
import static eu.debooy.doos.TestConstants.LIJST_HASH;
import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import eu.debooy.doos.form.Lijst;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

    lijstDto.setLijstnaam(LIJSTNAAM);
    lijstDto.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new LijstDto();
    var groter  = new LijstDto();
    var kleiner = new LijstDto();

    gelijk.setLijstnaam(lijstDto.getLijstnaam());
    groter.setLijstnaam(LIJSTNAAM_G);
    kleiner.setLijstnaam(LIJSTNAAM_K);

    assertTrue(lijstDto.compareTo(groter) < 0);
    assertEquals(0, lijstDto.compareTo(gelijk));
    assertTrue(lijstDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new LijstDto();


    assertEquals(lijstDto, lijstDto);
    assertNotEquals(lijstDto, null);
    assertNotEquals(lijstDto, NAAM);
    assertNotEquals(lijstDto, instance);

    instance.setLijstnaam(LIJSTNAAM);

    assertEquals(lijstDto, instance);
  }

  @Test
  public void testGetLijstnaam() {
    assertEquals(LIJSTNAAM, lijstDto.getLijstnaam());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(OMSCHRIJVING, lijstDto.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(LIJST_HASH, lijstDto.hashCode());
  }

  @Test
  public void testSetLijstnaam1() {
    var instance  = new Lijst();

    assertNotEquals(LIJSTNAAM, instance.getLijstnaam());

    instance.setLijstnaam(LIJSTNAAM);

    assertEquals(LIJSTNAAM, instance.getLijstnaam());
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
