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
import eu.debooy.doos.domain.I18nLijstCodeDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nLijstCodeTest {
  private static  I18nLijstCode i18nLijstCode;

  @BeforeClass
  public static void setUpClass() {
    i18nLijstCode = new I18nLijstCode();

    i18nLijstCode.setLijstId(TestConstants.LIJSTID);
    i18nLijstCode.setCodeId(TestConstants.CODEID);
    i18nLijstCode.setVolgorde(TestConstants.VOLGORDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new I18nLijstCode();
    var groter  = new I18nLijstCode();
    var kleiner = new I18nLijstCode();

    gelijk.setCodeId(i18nLijstCode.getCodeId());
    gelijk.setLijstId(i18nLijstCode.getLijstId());
    groter.setCodeId(TestConstants.CODEID_G);
    kleiner.setCodeId(TestConstants.CODEID_K);

    assertTrue(i18nLijstCode.compareTo(groter) < 0);
    assertEquals(0, i18nLijstCode.compareTo(gelijk));
    assertTrue(i18nLijstCode.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new I18nLijstCode();


    assertEquals(i18nLijstCode, i18nLijstCode);
    assertNotEquals(i18nLijstCode, null);
    assertNotEquals(i18nLijstCode, TestConstants.NAAM);
    assertNotEquals(i18nLijstCode, instance);

    instance.setCodeId(TestConstants.CODEID);
    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(i18nLijstCode, instance);
  }

  @Test
  public void testGetCodeId() {
    assertEquals(TestConstants.CODEID, i18nLijstCode.getCodeId());
  }

  @Test
  public void testGetLijstId() {
    assertEquals(TestConstants.LIJSTID, i18nLijstCode.getLijstId());
  }

  @Test
  public void testGetVolgorde() {
    assertEquals(TestConstants.VOLGORDE, i18nLijstCode.getVolgorde());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.I18NLIJSTCODE_HASH, i18nLijstCode.hashCode());
  }

  @Test
  public void testPersist() {
    var i18nLijstCodeDto  = new I18nLijstCodeDto();

    i18nLijstCode.persist(i18nLijstCodeDto);

    assertEquals(i18nLijstCode.getCodeId(), i18nLijstCodeDto.getCodeId());
    assertEquals(i18nLijstCode.getLijstId(), i18nLijstCodeDto.getLijstId());
    assertEquals(i18nLijstCode.getVolgorde(), i18nLijstCodeDto.getVolgorde());

    i18nLijstCode.persist(i18nLijstCodeDto);

    assertEquals(i18nLijstCode.getCodeId(), i18nLijstCodeDto.getCodeId());
    assertEquals(i18nLijstCode.getLijstId(), i18nLijstCodeDto.getLijstId());
    assertEquals(i18nLijstCode.getVolgorde(), i18nLijstCodeDto.getVolgorde());
  }

  @Test
  public void testSetCode() {
    var instance  = new I18nLijstCode();

    assertNotEquals(TestConstants.CODEID, instance.getCodeId());

    instance.setCodeId(TestConstants.CODEID);

    assertEquals(TestConstants.CODEID, instance.getCodeId());
  }

  @Test
  public void testSetLijstId() {
    var instance  = new I18nLijstCode();

    assertNotEquals(TestConstants.LIJSTID, instance.getLijstId());

    instance.setLijstId(TestConstants.LIJSTID);

    assertEquals(TestConstants.LIJSTID, instance.getLijstId());
  }

  @Test
  public void testSetVolgorde() {
    var instance  = new I18nLijstCode();

    assertNotEquals(TestConstants.VOLGORDE, instance.getVolgorde());

    instance.setVolgorde(TestConstants.VOLGORDE);

    assertEquals(TestConstants.VOLGORDE, instance.getVolgorde());
  }
}
