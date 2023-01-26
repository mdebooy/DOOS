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
import eu.debooy.doos.domain.ParameterDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterTest {
  private static  Parameter  parameter;

  @BeforeClass
  public static void setUpClass() {
    parameter  = new Parameter();

    parameter.setSleutel(TestConstants.SLEUTEL);
    parameter.setWaarde(TestConstants.WAARDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Parameter(parameter);
    var groter  = new Parameter();
    var kleiner = new Parameter();

    groter.setSleutel(TestConstants.SLEUTEL_G);
    kleiner.setSleutel(TestConstants.SLEUTEL_K);

    assertTrue(parameter.compareTo(groter) < 0);
    assertEquals(0, parameter.compareTo(gelijk));
    assertTrue(parameter.compareTo(kleiner) > 0);
  }

  @Test
  public void testEquals() {
    var dto       = new ParameterDto();
    var instance  = new Parameter();

    parameter.persist(dto);

    assertEquals(parameter, parameter);
    assertNotEquals(parameter, null);
    assertNotEquals(parameter, TestConstants.NAAM);
    assertNotEquals(parameter, instance);

    instance.setSleutel(TestConstants.SLEUTEL);
    assertEquals(parameter, instance);

    instance  = new Parameter(parameter);
    assertEquals(parameter, instance);

    instance  = new Parameter(dto);
    assertEquals(parameter, instance);
  }

  @Test
  public void testGetSleutel() {
    assertEquals(TestConstants.SLEUTEL, parameter.getSleutel());
  }

  @Test
  public void testGetWaarde() {
    assertEquals(TestConstants.WAARDE, parameter.getWaarde());
  }

  @Test
  public void testHashCode() {
    assertEquals(TestConstants.PARAMETER_HASH, parameter.hashCode());
  }

  @Test
  public void testInit1() {
    var instance  = new Parameter();

    assertNull(instance.getSleutel());
    assertNull(instance.getWaarde());
  }

  @Test
  public void testInit2() {
    var instance  = new Parameter(parameter);

    assertEquals(parameter.getSleutel(), instance.getSleutel());
    assertEquals(parameter.getWaarde(), instance.getWaarde());
  }

  @Test
  public void testInit3() {
    var parameterDto  = new ParameterDto();

    parameter.persist(parameterDto);
    var instance      = new Parameter(parameterDto);

    assertEquals(parameter.getSleutel(), instance.getSleutel());
    assertEquals(parameter.getWaarde(), instance.getWaarde());
  }

  @Test
  public void testInit4() {
    var instance  = new Parameter(TestConstants.SLEUTEL, TestConstants.WAARDE);

    assertEquals(TestConstants.SLEUTEL, instance.getSleutel());
    assertEquals(TestConstants.WAARDE, instance.getWaarde());
  }

  @Test
  public void testPersist() {
    var parameterDto = new ParameterDto();

    parameter.persist(parameterDto);

    assertEquals(parameter.getSleutel(), parameterDto.getSleutel());
    assertEquals(parameter.getWaarde(), parameterDto.getWaarde());

    parameter.persist(parameterDto);

    assertEquals(parameter.getSleutel(), parameterDto.getSleutel());
    assertEquals(parameter.getWaarde(), parameterDto.getWaarde());
  }

  @Test
  public void testSetSleutel() {
    var instance  = new Parameter();

    assertNotEquals(TestConstants.SLEUTEL, instance.getSleutel());

    instance.setSleutel(TestConstants.SLEUTEL);

    assertEquals(TestConstants.SLEUTEL, instance.getSleutel());
  }

  @Test
  public void testSetWaarde() {
    var instance  = new Parameter();

    assertNotEquals(TestConstants.WAARDE, instance.getWaarde());

    instance.setWaarde(TestConstants.WAARDE);

    assertEquals(TestConstants.WAARDE, instance.getWaarde());
  }
}
