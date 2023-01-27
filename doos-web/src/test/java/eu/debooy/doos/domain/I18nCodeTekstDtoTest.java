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
public class I18nCodeTekstDtoTest {
  private static  I18nCodeTekstDto  i18nCodeTekstDto;

  @BeforeClass
  public static void setUpClass() {
    i18nCodeTekstDto  = new I18nCodeTekstDto();

    i18nCodeTekstDto.setCodeId(TestConstants.CODEID);
    i18nCodeTekstDto.setTaalKode(TestConstants.TAALKODE);
    i18nCodeTekstDto.setTekst(TestConstants.TEKST);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nCodeTekstDto();
    var groter  = new I18nCodeTekstDto();
    var kleiner = new I18nCodeTekstDto();

    gelijk.setCodeId(i18nCodeTekstDto.getCodeId());
    gelijk.setTaalKode(i18nCodeTekstDto.getTaalKode());
    groter.setCodeId(TestConstants.CODEID_G);
    kleiner.setCodeId(TestConstants.CODEID_K);

    assertTrue(i18nCodeTekstDto.compareTo(groter) < 0);
    assertEquals(0, i18nCodeTekstDto.compareTo(gelijk));
    assertTrue(i18nCodeTekstDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nCodeTekstDto();


    assertEquals(i18nCodeTekstDto, i18nCodeTekstDto);
    assertNotEquals(i18nCodeTekstDto, null);
    assertNotEquals(i18nCodeTekstDto, TestConstants.NAAM);
    assertNotEquals(i18nCodeTekstDto, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setTaalKode(TestConstants.TAALKODE);

    assertEquals(i18nCodeTekstDto, instance);
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nCodeTekstDto.getCodeId());
  }

  @Test
  public void testGetTaalkode() {
    assertEquals(TestConstants.TAALKODE, i18nCodeTekstDto.getTaalKode());
  }

  @Test
  public void testGetTekst() {
    assertEquals(TestConstants.TEKST, i18nCodeTekstDto.getTekst());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NCODETEKST_HASH, i18nCodeTekstDto.hashCode());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nCodeTekstDto();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetTaalKode() {
    var instance  = new I18nCodeTekstDto();

    assertNotEquals(TestConstants.TAALKODE, instance.getTaalKode());

    instance.setTaalKode(TestConstants.TAALKODE);

    assertEquals(TestConstants.TAALKODE, instance.getTaalKode());
  }

  @Test
  public void testSetTekst() {
    var instance  = new I18nCodeTekstDto();

    assertNotEquals(TestConstants.TEKST, instance.getTekst());

    instance.setTekst(TestConstants.TEKST);

    assertEquals(TestConstants.TEKST, instance.getTekst());
  }
}
