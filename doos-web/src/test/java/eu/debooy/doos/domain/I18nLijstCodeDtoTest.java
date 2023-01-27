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
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nLijstCodeDtoTest {
  private static  I18nLijstCodeDto  i18nLijstCodeDto;

  @BeforeClass
  public static void setUpClass() {
    i18nLijstCodeDto = new I18nLijstCodeDto();

    i18nLijstCodeDto.setLijstId(TestConstants.LIJSTID);
    i18nLijstCodeDto.setCodeId(TestConstants.CODEID);
    i18nLijstCodeDto.setVolgorde(TestConstants.VOLGORDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nLijstCodeDto();
    var groter  = new I18nLijstCodeDto();
    var kleiner = new I18nLijstCodeDto();

    gelijk.setCodeId(i18nLijstCodeDto.getCodeId());
    gelijk.setLijstId(i18nLijstCodeDto.getLijstId());
    groter.setCodeId(TestConstants.CODEID_G);
    kleiner.setCodeId(TestConstants.CODEID_K);

    assertTrue(i18nLijstCodeDto.compareTo(groter) < 0);
    assertEquals(0, i18nLijstCodeDto.compareTo(gelijk));
    assertTrue(i18nLijstCodeDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nLijstCodeDto();


    assertEquals(i18nLijstCodeDto, i18nLijstCodeDto);
    assertNotEquals(i18nLijstCodeDto, null);
    assertNotEquals(i18nLijstCodeDto, TestConstants.NAAM);
    assertNotEquals(i18nLijstCodeDto, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(i18nLijstCodeDto, instance);
  }

  @Test
  public void testGetLijstId() {
    assertEquals(TestConstants.LIJSTID, i18nLijstCodeDto.getLijstId());
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nLijstCodeDto.getCodeId());
  }

  @Test
  public void testGetVolgorde() {
    assertEquals(TestConstants.VOLGORDE, i18nLijstCodeDto.getVolgorde());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NLIJSTCODE_HASH, i18nLijstCodeDto.hashCode());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nLijstCodeDto();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetLijstId() {
    var instance  = new I18nLijstCodeDto();

    assertNotEquals(TestConstants.LIJSTID, instance.getLijstId());

    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testSetVolgorde() {
    var instance  = new I18nLijstCodeDto();

    assertNotEquals(TestConstants.VOLGORDE, instance.getVolgorde());

    instance.setVolgorde(TestConstants.VOLGORDE);

    assertEquals(TestConstants.VOLGORDE, instance.getVolgorde());
  }
}
