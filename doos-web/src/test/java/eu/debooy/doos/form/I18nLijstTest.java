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
import eu.debooy.doos.domain.I18nLijstDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nLijstTest {
  private static  I18nLijst  i18nLijst;

  @BeforeClass
  public static void setUpClass() {
    i18nLijst  = new I18nLijst();

    i18nLijst.setCode(TestConstants.CODE);
    i18nLijst.setLijstId(TestConstants.LIJSTID);
    i18nLijst.setOmschrijving(TestConstants.OMSCHRIJVING);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nLijst();
    var groter  = new I18nLijst();
    var kleiner = new I18nLijst();

    gelijk.setLijstId(TestConstants.LIJSTID);
    groter.setLijstId(TestConstants.LIJSTID_G);
    kleiner.setLijstId(TestConstants.LIJSTID_K);

    assertTrue(i18nLijst.compareTo(groter) < 0);
    assertEquals(0, i18nLijst.compareTo(gelijk));
    assertTrue(i18nLijst.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var dto       = new I18nLijstDto();
    var instance  = new I18nLijst();

    i18nLijst.persist(dto);

    assertEquals(i18nLijst, i18nLijst);
    assertNotEquals(i18nLijst, null);
    assertNotEquals(i18nLijst, TestConstants.NAAM);
    assertNotEquals(i18nLijst, instance);

    instance.setLijstId(TestConstants.LIJSTID);
    assertEquals(i18nLijst, instance);

    instance  = new I18nLijst(dto);
    assertEquals(i18nLijst, instance);
  }

  @Test
  public void testGetCode() {
    assertEquals(TestConstants.CODE, i18nLijst.getCode());
  }

  @Test
  public void testGetLijstId() {
    assertEquals(TestConstants.LIJSTID, i18nLijst.getLijstId());
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(TestConstants.OMSCHRIJVING, i18nLijst.getOmschrijving());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NLIJST_HASH, i18nLijst.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new I18nLijst();

    assertNull(instance.getCode());
    assertNull(instance.getLijstId());
    assertNull(instance.getOmschrijving());
  }

  @Test
  public void testInit2() {
    var i18nLijstDto  = new I18nLijstDto();

    i18nLijst.persist(i18nLijstDto);
    var instance      = new I18nLijst(i18nLijstDto);

    assertEquals(i18nLijst.getCode(), instance.getCode());
    assertEquals(i18nLijst.getLijstId(), instance.getLijstId());
    assertEquals(i18nLijst.getOmschrijving(), instance.getOmschrijving());
  }

  @Test
  public void testPersist() {
    var i18nLijstDto = new I18nLijstDto();

    i18nLijst.persist(i18nLijstDto);

    assertEquals(i18nLijst.getCode(), i18nLijst.getCode());
    assertEquals(i18nLijst.getLijstId(), i18nLijst.getLijstId());
    assertEquals(i18nLijst.getOmschrijving(), i18nLijst.getOmschrijving());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nLijst();

    assertNotEquals(TestConstants.CODE, instance.getCode());

    instance.setCode(TestConstants.CODE);

    assertEquals(TestConstants.CODE, instance.getCode());
  }

  @Test
  public void testSetLijstId() {
    var instance  = new I18nLijst();

    assertNotEquals(TestConstants.LIJSTID, instance.getLijstId());

    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testSetNull() {
    var instance  = new I18nLijst();

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
    var instance  = new I18nLijst();

    assertNotEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());

    instance.setOmschrijving(TestConstants.OMSCHRIJVING);

    assertEquals(TestConstants.OMSCHRIJVING, instance.getOmschrijving());
  }
}
