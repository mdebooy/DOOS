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

import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.PARAMETER_HASH;
import static eu.debooy.doos.TestConstants.SLEUTEL;
import static eu.debooy.doos.TestConstants.SLEUTEL_G;
import static eu.debooy.doos.TestConstants.SLEUTEL_K;
import static eu.debooy.doos.TestConstants.WAARDE;
import eu.debooy.doos.domain.ParameterDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

    parameter.setSleutel(SLEUTEL);
    parameter.setWaarde(WAARDE);
  }

  @Test
  public void testCompareTo() {
    var gelijk  = new Parameter(parameter);
    var groter  = new Parameter();
    var kleiner = new Parameter();

    groter.setSleutel(SLEUTEL_G);
    kleiner.setSleutel(SLEUTEL_K);

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
    assertNotEquals(parameter, NAAM);
    assertNotEquals(parameter, instance);

    instance.setSleutel(SLEUTEL);
    assertEquals(parameter, instance);

    instance  = new Parameter(parameter);
    assertEquals(parameter, instance);

    instance  = new Parameter(dto);
    assertEquals(parameter, instance);
  }

  @Test
  public void testGetSleutel() {
    assertEquals(SLEUTEL, parameter.getSleutel());
  }

  @Test
  public void testGetWaarde() {
    assertEquals(WAARDE, parameter.getWaarde());
  }

  @Test
  public void testHashCode() {
    assertEquals(PARAMETER_HASH, parameter.hashCode());
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
    assertNotEquals(SLEUTEL, instance.getSleutel());
    instance.setSleutel(SLEUTEL);

    assertEquals(SLEUTEL, instance.getSleutel());
  }

  @Test
  public void testSetWaarde() {
    var instance  = new Parameter();
    assertNotEquals(WAARDE, instance.getWaarde());
    instance.setWaarde(WAARDE);

    assertEquals(WAARDE, instance.getWaarde());
  }
}
