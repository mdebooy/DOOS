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
import eu.debooy.doos.domain.I18nCodeTekstDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nCodeTekstTest {
  private static  I18nCodeTekst i18nCodeTekst;

  @BeforeClass
  public static void setUpClass() {
    i18nCodeTekst = new I18nCodeTekst();

    i18nCodeTekst.setCodeId(TestConstants.CODEID);
    i18nCodeTekst.setTaalKode(TestConstants.TAALKODE);
    i18nCodeTekst.setTekst(TestConstants.TEKST);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nCodeTekst();
    var groter  = new I18nCodeTekst();
    var kleiner = new I18nCodeTekst();

    gelijk.setCodeId(i18nCodeTekst.getCodeId());
    gelijk.setTaalKode(i18nCodeTekst.getTaalKode());
    groter.setCodeId(TestConstants.CODEID_G);
    kleiner.setCodeId(TestConstants.CODEID_K);

    assertTrue(i18nCodeTekst.compareTo(groter) < 0);
    assertEquals(0, i18nCodeTekst.compareTo(gelijk));
    assertTrue(i18nCodeTekst.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nCodeTekst();


    assertEquals(i18nCodeTekst, i18nCodeTekst);
    assertNotEquals(i18nCodeTekst, null);
    assertNotEquals(i18nCodeTekst, TestConstants.NAAM);
    assertNotEquals(i18nCodeTekst, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setTaalKode(TestConstants.TAALKODE);

    assertEquals(i18nCodeTekst, instance);
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nCodeTekst.getCodeId());
  }

  @Test
  public void testGetTaalkode() {
    assertEquals(TestConstants.TAALKODE, i18nCodeTekst.getTaalKode());
  }

  @Test
  public void testGetTekst() {
    assertEquals(TestConstants.TEKST, i18nCodeTekst.getTekst());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NCODETEKST_HASH, i18nCodeTekst.hashCode());
  }

  @Test
  public void testPersist() {
    var i18nCodeTekstDto  = new I18nCodeTekstDto();

    i18nCodeTekst.persist(i18nCodeTekstDto);

    assertEquals(i18nCodeTekst.getCodeId(), i18nCodeTekstDto.getCodeId());
    assertEquals(i18nCodeTekst.getTaalKode(), i18nCodeTekstDto.getTaalKode());
    assertEquals(i18nCodeTekst.getTekst(), i18nCodeTekstDto.getTekst());

    i18nCodeTekst.persist(i18nCodeTekstDto);

    assertEquals(i18nCodeTekst.getCodeId(), i18nCodeTekstDto.getCodeId());
    assertEquals(i18nCodeTekst.getTaalKode(), i18nCodeTekstDto.getTaalKode());
    assertEquals(i18nCodeTekst.getTekst(), i18nCodeTekstDto.getTekst());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nCodeTekst();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetTaalKode() {
    var instance  = new I18nCodeTekst();

    assertNotEquals(TestConstants.TAALKODE, instance.getTaalKode());

    instance.setTaalKode(TestConstants.TAALKODE);

    assertEquals(TestConstants.TAALKODE, instance.getTaalKode());
  }

  @Test
  public void testSetTekst() {
    var instance  = new I18nCodeTekst();

    assertNotEquals(TestConstants.TEKST, instance.getTekst());

    instance.setTekst(TestConstants.TEKST);

    assertEquals(TestConstants.TEKST, instance.getTekst());
  }
}
