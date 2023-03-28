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

import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.PARAMETER_HASH;
import static eu.debooy.doos.TestConstants.SLEUTEL;
import static eu.debooy.doos.TestConstants.SLEUTEL_G;
import static eu.debooy.doos.TestConstants.SLEUTEL_K;
import static eu.debooy.doos.TestConstants.WAARDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterDtoTest {
  private static  ParameterDto  parameterDto;

  @BeforeClass
  public static void setUpClass() {
    parameterDto  = new ParameterDto();

    parameterDto.setSleutel(SLEUTEL);
    parameterDto.setWaarde(WAARDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new ParameterDto(SLEUTEL, WAARDE);
    var groter  = new ParameterDto();
    var kleiner = new ParameterDto();

    groter.setSleutel(SLEUTEL_G);
    kleiner.setSleutel(SLEUTEL_K);

    assertTrue(parameterDto.compareTo(groter) < 0);
    assertEquals(0, parameterDto.compareTo(gelijk));
    assertTrue(parameterDto.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var instance  = new ParameterDto();

    assertEquals(parameterDto, parameterDto);
    assertNotEquals(parameterDto, null);
    assertNotEquals(parameterDto, NAAM);
    assertNotEquals(parameterDto, instance);

    instance.setSleutel(SLEUTEL);
    assertEquals(parameterDto, instance);

    instance  = new ParameterDto(SLEUTEL);
    assertEquals(parameterDto, instance);

    instance  = new ParameterDto(SLEUTEL, WAARDE);
    assertEquals(parameterDto, instance);
  }

  @Test
  public void testGetSleutel() {
    assertEquals(SLEUTEL, parameterDto.getSleutel());
  }

  @Test
  public void testGetWaarde() {
    assertEquals(WAARDE, parameterDto.getWaarde());
  }

  @Test
  public void testHashCode() {
    assertEquals(PARAMETER_HASH, parameterDto.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new ParameterDto();

    assertNull(instance.getSleutel());
    assertNull(instance.getWaarde());
  }

  @Test
  public void testInit2() {
    var instance  = new ParameterDto(SLEUTEL);

    assertEquals(SLEUTEL, instance.getSleutel());
    assertNull(instance.getWaarde());
  }

  @Test
  public void testInit3() {
    var instance  = new ParameterDto(SLEUTEL, WAARDE);

    assertEquals(SLEUTEL, instance.getSleutel());
    assertEquals(WAARDE, instance.getWaarde());
  }

  @Test
  public void testSetSleutel() {
    var instance  = new ParameterDto();
    assertNotEquals(SLEUTEL, instance.getSleutel());
    instance.setSleutel(SLEUTEL);

    assertEquals(SLEUTEL, instance.getSleutel());
  }

  @Test
  public void testSetWaarde() {
    var instance  = new ParameterDto();
    assertNotEquals(WAARDE, instance.getWaarde());
    instance.setWaarde(WAARDE);

    assertEquals(WAARDE, instance.getWaarde());
  }
}
