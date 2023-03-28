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
import static eu.debooy.doos.TestConstants.NAAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nSelectieTest {
  private static  I18nSelectie  i18nSelectie;

  @BeforeClass
  public static void setUpClass() {
    i18nSelectie  = new I18nSelectie();

    i18nSelectie.setCode(TestConstants.CODE);
    i18nSelectie.setCodeId(TestConstants.CODEID);
    i18nSelectie.setSelectie(TestConstants.SELECTIE);
    i18nSelectie.setVolgorde(TestConstants.VOLGORDE);
    i18nSelectie.setWaarde(TestConstants.WAARDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nSelectie();
    var groter  = new I18nSelectie();
    var kleiner = new I18nSelectie();

    gelijk.setCodeId(i18nSelectie.getCodeId());
    groter.setCodeId(TestConstants.CODEID_G);
    kleiner.setCodeId(TestConstants.CODEID_K);

    assertTrue(i18nSelectie.compareTo(groter) < 0);
    assertEquals(0, i18nSelectie.compareTo(gelijk));
    assertTrue(i18nSelectie.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nSelectie();

    assertEquals(i18nSelectie, i18nSelectie);
    assertNotEquals(i18nSelectie, null);
    assertNotEquals(i18nSelectie, NAAM);
    assertNotEquals(i18nSelectie, instance);

    instance.setCodeId(i18nSelectie.getCodeId());

    assertEquals(i18nSelectie, instance);
  }

  @Test
  public void testGetCode() {
    assertEquals(TestConstants.CODE, i18nSelectie.getCode());
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nSelectie.getCodeId());
  }

  @Test
  public void testGetSelectie() {
    assertEquals(TestConstants.SELECTIE, i18nSelectie.getSelectie());
  }

  @Test
  public void testGetVolgorde() {
    assertEquals(TestConstants.VOLGORDE, i18nSelectie.getVolgorde());
  }

  @Test
  public void testGetWaarde() {
    assertEquals(TestConstants.WAARDE, i18nSelectie.getWaarde());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NSELECTIE_HASH, i18nSelectie.hashCode());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nSelectie();

    assertNotEquals(TestConstants.CODE, instance.getCode());

    instance.setCode(TestConstants.CODE);

    assertEquals(TestConstants.CODE, instance.getCode());
  }

  @Test
  public void testSetCodeId() {
    var instance  = new I18nSelectie();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetSelectie() {
    var instance  = new I18nSelectie();

    assertNotEquals(TestConstants.SELECTIE, instance.getSelectie());

    instance.setSelectie(TestConstants.SELECTIE);

    assertEquals(TestConstants.SELECTIE, instance.getSelectie());
  }

  @Test
  public void testSetVolgorde() {
    var instance  = new I18nSelectie();

    assertNotEquals(TestConstants.VOLGORDE, instance.getVolgorde());

    instance.setVolgorde(TestConstants.VOLGORDE);

    assertEquals(TestConstants.VOLGORDE, instance.getVolgorde());
  }

  @Test
  public void testSetWaarde() {
    var instance  = new I18nSelectie();

    assertNotEquals(TestConstants.WAARDE, instance.getWaarde());

    instance.setWaarde(TestConstants.WAARDE);

    assertEquals(TestConstants.WAARDE, instance.getWaarde());
  }
}
