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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nCodeTest {
  private static  I18nCode  i18nCode;

  @BeforeClass
  public static void setUpClass() {
    i18nCode  = new I18nCode();

    i18nCode.setCode(TestConstants.CODE);
    i18nCode.setCodeId(TestConstants.CODEID);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nCode();
    var groter  = new I18nCode();
    var kleiner = new I18nCode();

    gelijk.setCode(i18nCode.getCode());
    groter.setCode(TestConstants.CODE_G);
    kleiner.setCode(TestConstants.CODE_K);

    assertTrue(i18nCode.compareTo(groter) < 0);
    assertEquals(0, i18nCode.compareTo(gelijk));
    assertTrue(i18nCode.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nCode();

    assertEquals(i18nCode, i18nCode);
    assertNotEquals(i18nCode, null);
    assertNotEquals(i18nCode, NAAM);
    assertNotEquals(i18nCode, instance);

    instance.setCode(i18nCode.getCode());

    assertEquals(i18nCode, instance);
  }

  @Test
  public void testGetCode() {
    assertEquals(TestConstants.CODE, i18nCode.getCode());
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nCode.getCodeId());
  }

  @Test
  public void testGetTeksten() {
    assertNull(i18nCode.getTeksten());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NCODE_HASH, i18nCode.hashCode());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nCode();

    assertNotEquals(TestConstants.CODE, instance.getCode());

    instance.setCode(TestConstants.CODE);

    assertEquals(TestConstants.CODE, instance.getCode());
  }

  @Test
  public void testSetCodeId() {
    var instance  = new I18nCode();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }
}
