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

import static eu.debooy.doos.TestConstants.SLEUTEL;
import static eu.debooy.doos.TestConstants.WAARDE;
import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
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
                 .setParams(new Object[]{"_I18N.label.sleutel", 100})
                 .build();
  public static final Message ERR_WAARDE  =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_WAARDE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.waarde", 255})
                 .build();
  public static final Message REQ_SLEUTEL =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_SLEUTEL)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.sleutel"})
                 .build();
  public static final Message REQ_WAARDE  =
      new Message.Builder()
                 .setAttribute(ParameterDto.COL_WAARDE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.waarde"})
                 .build();

  private static  Parameter parameter;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_SLEUTEL);
    expResult.add(REQ_WAARDE);
  }

  @BeforeClass
  public static void setUpClass() {
    parameter  = new Parameter();

    parameter.setSleutel(SLEUTEL);
    parameter.setWaarde(WAARDE);
  }

  @Test
  public void testFouteParameter1() {
    var           instance  = new Parameter(parameter);

    instance.setSleutel(DoosUtils.stringMetLengte(SLEUTEL, 101, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_SLEUTEL.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteParameter2() {
    var           instance  = new Parameter(parameter);

    instance.setWaarde(DoosUtils.stringMetLengte(WAARDE, 256, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_WAARDE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteParameterDto1() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setSleutel(DoosUtils.stringMetLengte(SLEUTEL, 101, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_SLEUTEL.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteParameterDto2() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setWaarde(DoosUtils.stringMetLengte(WAARDE, 256, "X"));

    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_WAARDE.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeParameter1() {
    var           instance  = new ParameterDto();

    instance.setSleutel(DoosUtils.stringMetLengte(SLEUTEL, 100, "X"));

    List<Message> result  = ParameterValidator.valideer(parameter);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeParameter2() {
    var           instance  = new Parameter(parameter);

    instance.setWaarde(DoosUtils.stringMetLengte(WAARDE, 255, "X"));
    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeParameterDto1() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setSleutel(DoosUtils.stringMetLengte(SLEUTEL, 100, "X"));
    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeParameterDto2() {
    var           instance  = new ParameterDto();

    parameter.persist(instance);
    instance.setWaarde(DoosUtils.stringMetLengte(WAARDE, 255, "X"));
    List<Message> result    = ParameterValidator.valideer(instance);

    assertEquals(0, result.size());
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
