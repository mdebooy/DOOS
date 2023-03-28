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
 *
 * @author Marco de Booij
 */
public class I18nCodeTekstPKTest {
  private static final  I18nCodeTekstPK i18nCodeTekstPk  =
      new I18nCodeTekstPK(TestConstants.CODEID, TestConstants.TAALKODE);

  @Test
  public void testEquals() {
    var instance  = new I18nCodeTekstPK();

    assertEquals(i18nCodeTekstPk, i18nCodeTekstPk);
    assertNotEquals(i18nCodeTekstPk, null);
    assertNotEquals(i18nCodeTekstPk, TestConstants.EIGENNAAM);
    assertNotEquals(i18nCodeTekstPk, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setTaalKode(TestConstants.TAALKODE);
    assertEquals(i18nCodeTekstPk, instance);
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nCodeTekstPk.getCodeId());
  }

  @Test
  public void testGetTaalKode() {
    assertEquals(TestConstants.TAALKODE, i18nCodeTekstPk.getTaalKode());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NCODETEKSTPK_HASH,
                 i18nCodeTekstPk.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new I18nCodeTekstPK();

    assertNull(instance.getCodeId());
    assertNull(instance.getTaalKode());
  }

  @Test
  public void testInit2() {
    var instance  = new I18nCodeTekstPK(TestConstants.CODEID,
                                        TestConstants.TAALKODE);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
    assertEquals(TestConstants.TAALKODE, instance.getTaalKode());
  }

  @Test
  public void testSetCodeId() {
    var instance  = new I18nCodeTekstPK();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());
    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetTaalKode() {
    var instance  = new I18nCodeTekstPK();

    assertNotEquals(TestConstants.TAALKODE, instance.getTaalKode());
    instance.setTaalKode(TestConstants.TAALKODE);

    assertEquals(TestConstants.TAALKODE, instance.getTaalKode());
  }

  @Test
  public void testToString() {
    assertEquals(TestConstants.I18NCODETEKSTPK_TOSTRING,
                 i18nCodeTekstPk.toString());
  }
}
