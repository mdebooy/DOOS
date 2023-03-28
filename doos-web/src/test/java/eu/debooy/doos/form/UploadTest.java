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

import static eu.debooy.doos.TestConstants.ISO6391;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class UploadTest {
  @Test
  public void testAddGelezen() {
    var instance  = new Upload();

    assertEquals(0, instance.getGelezen());

    instance.addGelezen();

    assertEquals(1, instance.getGelezen());

    instance.reset();

    assertEquals(0, instance.getGelezen());
  }

  @Test
  public void testAddGewijzigd() {
    var instance  = new Upload();

    assertEquals(0, instance.getGewijzigd());

    instance.addGewijzigd();

    assertEquals(1, instance.getGewijzigd());

    instance.reset();

    assertEquals(0, instance.getGewijzigd());
  }

  @Test
  public void testAddNieuw() {
    var instance  = new Upload();

    assertEquals(0, instance.getNieuw());

    instance.addNieuw();
    instance.addNieuw();

    assertEquals(2, instance.getNieuw());

    instance.reset();

    assertEquals(0, instance.getNieuw());
  }

  @Test
  public void testAddNieuweWaardes() {
    var instance  = new Upload();

    assertEquals(0, instance.getNieuweWaardes());

    instance.addNieuweWaardes();

    assertEquals(1, instance.getNieuweWaardes());

    instance.reset();

    assertEquals(0, instance.getNieuweWaardes());
  }

  @Test
  public void testGetTaal() {
    var instance  = new Upload();

    assertTrue(instance.getTaal().isEmpty());

    instance.setTaal(ISO6391);

    assertEquals(ISO6391, instance.getTaal());
  }

  @Test
  public void testIsOverschrijven() {
    var instance  = new Upload();

    assertFalse(instance.isOverschrijven());

    instance.setOverschrijven(true);

    assertTrue(instance.isOverschrijven());
  }

  @Test
  public void testIsUtf8() {
    var instance  = new Upload();

    assertFalse(instance.isUtf8());

    instance.setUtf8(true);

    assertTrue(instance.isUtf8());
  }

  @Test
  public void testReset() {
    var instance  = new Upload();

    instance.setGelezen(1);
    instance.setGewijzigd(2);
    instance.setNieuweCodes(3);
    instance.setNieuweWaardes(4);
    instance.setTaal(ISO6391);
    instance.setOverschrijven(true);
    instance.setUtf8(true);

    assertEquals(1, instance.getGelezen());
    assertEquals(2, instance.getGewijzigd());
    assertEquals(3, instance.getNieuw());
    assertEquals(4, instance.getNieuweWaardes());
    assertEquals(ISO6391, instance.getTaal());
    assertTrue(instance.isOverschrijven());
    assertTrue(instance.isUtf8());

    instance.reset();

    assertEquals(0, instance.getGelezen());
    assertEquals(0, instance.getGewijzigd());
    assertEquals(0, instance.getNieuw());
    assertEquals(0, instance.getNieuweWaardes());
    assertEquals(ISO6391, instance.getTaal());
    assertTrue(instance.isOverschrijven());
    assertTrue(instance.isUtf8());
  }
}
