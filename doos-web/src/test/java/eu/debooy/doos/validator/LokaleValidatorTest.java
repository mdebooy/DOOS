/*
 * Copyright (c) 2024 Marco de Booij
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
import eu.debooy.doos.domain.LokaleDto;
import eu.debooy.doos.form.Lokale;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class LokaleValidatorTest {
  public static final Message ERR_CODE        =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{LokaleValidator.LBL_CODE, 50})
                 .setAttribute(LokaleDto.COL_CODE)
                 .build();
  public static final Message ERR_EERSTETAAL1 =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{LokaleValidator.LBL_EERSTETAAL, 3})
                 .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                 .build();
  public static final Message ERR_EERSTETAAL2 =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.NIETLCASE)
                 .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                 .build();
  public static final Message ERR_TWEEDETAAL1 =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{LokaleValidator.LBL_TWEEDETAAL, 3})
                 .setAttribute(LokaleDto.COL_TWEEDE_TAAL)
                 .build();
  public static final Message ERR_TWEEDETAAL2 =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.NIETLCASE)
                 .setAttribute(LokaleDto.COL_TWEEDE_TAAL)
                 .build();
  public static final Message REQ_CODE        =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{LokaleValidator.LBL_CODE})
                 .setAttribute(LokaleDto.COL_CODE)
                 .build();
  public static final Message REQ_EERSTETAAL  =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{LokaleValidator.LBL_EERSTETAAL})
                 .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                 .build();

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_CODE);
    expResult.add(REQ_EERSTETAAL);
  }

  @Test
  public void testFouteLokale1() {
    var           instance  = new Lokale();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6391);
    instance.setTweedeTaal(TestConstants.ISO6391);

    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_EERSTETAAL1.toString(), result.get(0).toString());
    assertEquals(ERR_TWEEDETAAL1.toString(), result.get(1).toString());
  }

  @Test
  public void testFouteLokale2() {
     var           instance  = new Lokale();

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.LOKALE, 51, "X"));
    instance.setEersteTaal(
        DoosUtils.stringMetLengte(TestConstants.ISO6392T, 4, "X"));
    instance.setTweedeTaal(
        DoosUtils.stringMetLengte(TestConstants.ISO6392T, 4, "X"));

    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(3, result.size());
    assertEquals(ERR_CODE.toString(), result.get(0).toString());
    assertEquals(ERR_EERSTETAAL1.toString(), result.get(1).toString());
    assertEquals(ERR_TWEEDETAAL1.toString(), result.get(2).toString());
  }

  @Test
  public void testFouteLokaleDto1() {
    var           instance  = new LokaleDto();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6391);
    instance.setTweedeTaal(TestConstants.ISO6391);

    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_EERSTETAAL1.toString(), result.get(0).toString());
    assertEquals(ERR_TWEEDETAAL1.toString(), result.get(1).toString());
  }

  @Test
  public void testFouteLokaleDto2() {
    var           instance  = new LokaleDto();

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.LOKALE, 51, "X"));
    instance.setEersteTaal(
        DoosUtils.stringMetLengte(TestConstants.ISO6392T, 4, "X"));
    instance.setTweedeTaal(
        DoosUtils.stringMetLengte(TestConstants.ISO6392T, 4, "X"));

    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(3, result.size());
    assertEquals(ERR_CODE.toString(), result.get(0).toString());
    assertEquals(ERR_EERSTETAAL1.toString(), result.get(1).toString());
    assertEquals(ERR_TWEEDETAAL1.toString(), result.get(2).toString());
  }

  @Test
  public void testGoedeLokale1() {
    var           instance  = new Lokale();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6392T);

    List<Message> result    = LokaleValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeLokale2() {
    var           instance  = new Lokale();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6392T.toUpperCase());
    instance.setTweedeTaal(TestConstants.ISO6392T.toUpperCase());

    List<Message> result    = LokaleValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeLokaleDto1() {
    var           instance  = new LokaleDto();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6392T);

    List<Message> result    = LokaleValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeLokaleDto2() {
    var           instance  = new LokaleDto();

    instance.setCode(TestConstants.LOKALE);
    instance.setEersteTaal(TestConstants.ISO6392T.toUpperCase());
    instance.setTweedeTaal(TestConstants.ISO6392T.toUpperCase());

    List<Message> result    = LokaleValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testLegeLokale() {
    var           instance  = new Lokale();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = LokaleValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testLegeLokaleDto() {
    var           instance  = new LokaleDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = LokaleValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testNullLokale() {
    Lokale        instance  = null;
    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }

  @Test
  public void testNullLokaleDto() {
    LokaleDto     instance  = null;
    List<Message> result    = LokaleValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }
}
