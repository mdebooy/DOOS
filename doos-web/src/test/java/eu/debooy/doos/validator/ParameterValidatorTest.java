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
package eu.debooy.doos.validator;

import eu.debooy.doos.TestConstants;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterValidatorTest {
  public static final Message ERR_SLEUTEL =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_SLEUTEL)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{ParameterValidator.LBL_SLEUTEL, 100})
                 .build();
  public static final Message ERR_WAARDE  =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_WAARDE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{ParameterValidator.LBL_WAARDE, 255})
                 .build();
  public static final Message REQ_SLEUTEL =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_SLEUTEL)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{ParameterValidator.LBL_SLEUTEL})
                 .build();
  public static final Message REQ_WAARDE  =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_WAARDE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{ParameterValidator.LBL_WAARDE})
                 .build();

  private static  Parameter parameter;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_SLEUTEL);
    expResult.add(REQ_WAARDE);
  }

  @BeforeClass
  public static void setUpClass() {
    parameter  = new Parameter();

    parameter.setSleutel(TestConstants.SLEUTEL);
    parameter.setWaarde(TestConstants.WAARDE);
  }

  @Test
  public void testFouteParameter() {
    var           instance  = new Parameter();

    instance.setSleutel(
        DoosUtils.stringMetLengte(TestConstants.SLEUTEL, 101, "X"));
    instance.setWaarde(
        DoosUtils.stringMetLengte(TestConstants.WAARDE, 256, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_SLEUTEL.toString(), result.get(0).toString());
    assertEquals(ERR_WAARDE.toString(), result.get(1).toString());
  }

  @Test
  public void testFouteParameterDto() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setSleutel(
        DoosUtils.stringMetLengte(TestConstants.SLEUTEL, 101, "X"));
    instance.setWaarde(
        DoosUtils.stringMetLengte(TestConstants.WAARDE, 256, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_SLEUTEL.toString(), result.get(0).toString());
    assertEquals(ERR_WAARDE.toString(), result.get(1).toString());
  }

  @Test
  public void testGoedeParameter() {
    var           instance  = new Parameter();

    instance.setSleutel(
        DoosUtils.stringMetLengte(TestConstants.SLEUTEL, 100, "X"));
    instance.setWaarde(
        DoosUtils.stringMetLengte(TestConstants.WAARDE, 255, "X"));

    List<Message> result  = ParameterValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeParameterDto() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setSleutel(
        DoosUtils.stringMetLengte(TestConstants.SLEUTEL, 100, "X"));
    instance.setWaarde(
        DoosUtils.stringMetLengte(TestConstants.WAARDE, 255, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testLegeParameter() {
    var           instance  = new Parameter();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = ParameterValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testLegeParameterDto() {
    var           instance  = new ParameterDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = ParameterValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }
}
