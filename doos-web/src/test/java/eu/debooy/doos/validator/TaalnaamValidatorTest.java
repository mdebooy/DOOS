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

import static eu.debooy.doos.TestConstants.ISO6392T;
import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.TAALID;
import eu.debooy.doos.domain.TaalnaamDto;
import eu.debooy.doos.form.Taalnaam;
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
public class TaalnaamValidatorTest {
  public static final Message ERR_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{"_I18N.label.iso6392t", 3})
                 .build();
  public static final Message ERR_NAAM      =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_NAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{"_I18N.label.naam", 100})
                 .build();
  public static final Message REQ_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.iso6392t"})
                 .build();
  public static final Message REQ_NAAM      =
      new Message.Builder()
                 .setAttribute(TaalnaamDto.COL_NAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.naam"})
                 .build();

  private static  Taalnaam  taalnaam;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_ISO6392T);
    expResult.add(REQ_NAAM);
  }

  @BeforeClass
  public static void setUpClass() {
    taalnaam  = new Taalnaam();

    taalnaam.setIso6392t(ISO6392T);
    taalnaam.setNaam(NAAM);
    taalnaam.setTaalId(TAALID);
  }

  @Test
  public void testFouteTaalnaam1() {
    var           instance  = new Taalnaam(taalnaam);

    instance.setIso6392t("XX");

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalnaam2() {
    var           instance  = new Taalnaam(taalnaam);

    instance.setIso6392t("XXXX");

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalnaam3() {
    var           instance  = new Taalnaam(taalnaam);

    instance.setNaam(DoosUtils.stringMetLengte(NAAM, 101, "X"));

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_NAAM.toString(), result.get(0).toString());
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

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalnaamDto3() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    instance.setNaam(DoosUtils.stringMetLengte(NAAM, 101, "X"));

    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_NAAM.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeTaalnaam1() {
    List<Message> result  = TaalnaamValidator.valideer(taalnaam);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalnaam2() {
    var           instance  = new Taalnaam(taalnaam);

    instance.setNaam(DoosUtils.stringMetLengte(NAAM, 100, "X"));
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalnaamDto1() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalnaamDto2() {
    var           instance  = new TaalnaamDto();

    taalnaam.persist(instance);
    instance.setNaam(DoosUtils.stringMetLengte(NAAM, 100, "X"));
    List<Message> result    = TaalnaamValidator.valideer(instance);

    assertEquals(0, result.size());
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
}
