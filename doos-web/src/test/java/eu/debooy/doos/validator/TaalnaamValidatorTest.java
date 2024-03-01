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
import eu.debooy.doos.domain.TaalnaamDto;
import eu.debooy.doos.form.Taalnaam;
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
public class TaalnaamValidatorTest {
  public static final Message ERR_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{TaalnaamValidator.LBL_ISO6392T, 3})
                 .build();
  public static final Message ERR_NAAM      =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_NAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{TaalnaamValidator.LBL_NAAM, 100})
                 .build();
  public static final Message REQ_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{TaalnaamValidator.LBL_ISO6392T})
                 .build();
  public static final Message REQ_NAAM      =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_NAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{TaalnaamValidator.LBL_NAAM})
                 .build();

  private static  Taalnaam  taalnaam;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_ISO6392T);
    expResult.add(REQ_NAAM);
  }

  @BeforeClass
  public static void setUpClass() {
    taalnaam  = new Taalnaam();

    taalnaam.setIso6392t(TestConstants.ISO6392T);
    taalnaam.setNaam(TestConstants.NAAM);
    taalnaam.setTaalId(TestConstants.TAALID);
  }

  @Test
  public void testFouteTaalnaam1() {
    var           instance  = new Taalnaam();

    instance.setIso6392t("XX");
    instance.setNaam(TestConstants.NAAM);

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalnaam2() {
    var           instance  = new Taalnaam();

    instance.setIso6392t("XXXX");
    instance.setNaam(DoosUtils.stringMetLengte(TestConstants.NAAM, 101, "X"));

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
    assertEquals(ERR_NAAM.toString(), result.get(1).toString());
  }

  @Test
  public void testFouteTaalnaamDto1() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    instance.setIso6392t("XX");

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalnaamDto2() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    instance.setIso6392t("XXXX");
    instance.setNaam(DoosUtils.stringMetLengte(TestConstants.NAAM, 101, "X"));

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(2, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
    assertEquals(ERR_NAAM.toString(), result.get(1).toString());
  }

  @Test
  public void testGoedeTaalnaam() {
    List<Message> result  = TaalnaamValidator.valideer(taalnaam);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeTaalnaamDto() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testLegeTaalnaam() {
    var           instance  = new Taalnaam();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = TaalnaamValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testLegeTaalnaamDto() {
    var           instance  = new TaalnaamDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = TaalnaamValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testNullTaalnaam() {
    Taalnaam      instance  = null;
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }

  @Test
  public void testNullTaalnaamDto() {
    TaalnaamDto   instance  = null;
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }
}
