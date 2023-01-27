
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
public class TaalnaamPKTest {
  private static final  TaalnaamPK  taalnaamPk  =
      new TaalnaamPK(TestConstants.TAALID, TestConstants.ISO6392T);

  @Test
  public void testEquals() {
    var instance  = new TaalnaamPK();

    assertEquals(taalnaamPk, taalnaamPk);
    assertNotEquals(taalnaamPk, null);
    assertNotEquals(taalnaamPk, TestConstants.EIGENNAAM);
    assertNotEquals(taalnaamPk, instance);

    instance.setIso6392t(TestConstants.ISO6392T);
    instance.setTaalId(TestConstants.TAALID);
    assertEquals(taalnaamPk, instance);
  }

  @Test
  public void testGetIso6392t() {
    assertEquals(TestConstants.ISO6392T, taalnaamPk.getIso6392t());
  }

  @Test
  public void testGetTaalId() {
    assertEquals(TestConstants.TAALID, taalnaamPk.getTaalId());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.TAALNAAMPK_HASH, taalnaamPk.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new TaalnaamPK();

    assertNull(instance.getIso6392t());
    assertNull(instance.getTaalId());
  }

  @Test
  public void testInit2() {
    var instance  = new TaalnaamPK(TestConstants.TAALID,
                                   TestConstants.ISO6392T);

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
    assertEquals(TestConstants.TAALID, instance.getTaalId());
  }

  @Test
  public void testSetIso6392t() {
    var instance  = new TaalnaamPK();

    assertNotEquals(TestConstants.ISO6392T, instance.getIso6392t());
    instance.setIso6392t(TestConstants.ISO6392T);

    assertEquals(TestConstants.ISO6392T, instance.getIso6392t());
  }

  @Test
  public void testSetTaalId() {
    var instance  = new TaalnaamPK();

    assertNotEquals(TestConstants.TAALID, instance.getTaalId());
    instance.setTaalId(TestConstants.TAALID);

    assertEquals(TestConstants.TAALID, instance.getTaalId());
  }

  @Test
  public void testToString() {
    assertEquals(TestConstants.TAALNAAMPK_TOSTRING, taalnaamPk.toString());
  }
}
