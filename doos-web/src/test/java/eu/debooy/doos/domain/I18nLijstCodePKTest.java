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
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nLijstCodePKTest {
  private static final  I18nLijstCodePK i18nLijstCodePk  =
      new I18nLijstCodePK(TestConstants.CODEID, TestConstants.LIJSTID);

  @Test
  public void testEquals() {
    var instance  = new I18nLijstCodePK();

    assertEquals(i18nLijstCodePk, i18nLijstCodePk);
    assertNotEquals(i18nLijstCodePk, null);
    assertNotEquals(i18nLijstCodePk, TestConstants.NAAM);
    assertNotEquals(i18nLijstCodePk, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setLijstId(TestConstants.LIJSTID);
    assertEquals(i18nLijstCodePk, instance);
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nLijstCodePk.getCodeId());
  }

  @Test
  public void testGetLijstId() {
    assertEquals(TestConstants.LIJSTID, i18nLijstCodePk.getLijstId());
  }

  @Test
  public void testHashCodeId() {
    assertEquals(TestConstants.I18NLIJSTCODEPK_HASH,
                 i18nLijstCodePk.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new I18nLijstCodePK();

    assertNull(instance.getCodeId());
    assertNull(instance.getLijstId());
  }

  @Test
  public void testInit2() {
    var instance  = new I18nLijstCodePK(TestConstants.CODEID,
                                        TestConstants.LIJSTID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testSetCodeId() {
    var instance  = new I18nLijstCodePK();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());
    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetLijstId() {
    var instance  = new I18nLijstCodePK();

    assertNotEquals(TestConstants.LIJSTID, instance.getLijstId());
    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testToString() {
    assertEquals(TestConstants.I18NLIJSTCODEPK_TOSTRING,
                 i18nLijstCodePk.toString());
  }
}
