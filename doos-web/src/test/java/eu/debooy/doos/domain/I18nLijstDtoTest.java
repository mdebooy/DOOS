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
public class I18nLijstDtoTest {
  private static  I18nLijstDto i18nLijstDto;

  @BeforeClass
  public static void setUpClass() {
    i18nLijstDto  = new I18nLijstDto();

    i18nLijstDto.setCode(TestConstants.CODE);
    i18nLijstDto.setLijstId(TestConstants.LIJSTID);
    i18nLijstDto.setOmschrijving(TestConstants.OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nLijstDto();
    var groter  = new I18nLijstDto();
    var kleiner = new I18nLijstDto();

    gelijk.setLijstId(i18nLijstDto.getLijstId());
    groter.setLijstId(TestConstants.LIJSTID_G);
    kleiner.setLijstId(TestConstants.LIJSTID_K);

    assertTrue(i18nLijstDto.compareTo(groter) < 0);
    assertEquals(0, i18nLijstDto.compareTo(gelijk));
    assertTrue(i18nLijstDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nLijstDto();

    assertEquals(i18nLijstDto, i18nLijstDto);
    assertNotEquals(i18nLijstDto, null);
    assertNotEquals(i18nLijstDto, TestConstants.NAAM);
    assertNotEquals(i18nLijstDto, instance);

    instance.setLijstId(TestConstants.LIJSTID);
    assertEquals(i18nLijstDto, instance);

    assertEquals(i18nLijstDto, instance);
  }

  @Test
  public void testGetCode() {
    assertEquals(TestConstants.CODE, i18nLijstDto.getCode());
  }

  @Test
  public void testGetLijstId() {
    assertEquals(TestConstants.LIJSTID, i18nLijstDto.getLijstId());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(TestConstants.OMSCHRIJVING, i18nLijstDto.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NLIJST_HASH, i18nLijstDto.hashCode());
  }

  @Test
  public void testInit() {
    var instance  = new I18nLijstDto();

    assertNull(instance.getCode());
    assertNull(instance.getLijstId());
    assertNull(instance.getOmschrijving());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nLijstDto();

    assertNotEquals(TestConstants.CODE, instance.getCode());

    instance.setCode(TestConstants.CODE);

    assertEquals(TestConstants.CODE, instance.getCode());
  }

  @Test
  public void testSetLijstId() {
    var instance  = new I18nLijstDto();

    assertNotEquals(TestConstants.LIJSTID, instance.getLijstId());

    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testSetNull() {
    var instance  = new I18nLijstDto();

    instance.setCode(TestConstants.CODE);
    instance.setLijstId(TestConstants.LIJSTID);
    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.CODE, instance.getCode());
    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setCode(null);
    instance.setLijstId(null);
    instance.setOmschrijving(null);

    assertNull(instance.getCode());
    assertNull(instance.getLijstId());
    assertNull(instance.getOmschrijving());
  }

  @Test
  public void testSetOmschrijving() {
    var instance  = new I18nLijstDto();

    assertNotEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());
  }
}
