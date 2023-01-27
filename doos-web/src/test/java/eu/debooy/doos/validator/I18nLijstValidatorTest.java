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
import eu.debooy.doos.domain.I18nLijstDto;
import eu.debooy.doos.form.I18nLijst;
import static eu.debooy.doos.validator.I18nLijstValidatorTest.ERR_CODE;
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
public class I18nLijstValidatorTest {
  public static final Message ERR_CODE          =
      new Message.Builder()
                 .setAttribute(I18nLijstDto.COL_CODE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{I18nLijstValidator.LBL_CODE, 100})
                 .build();
  public static final Message ERR_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(I18nLijstDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{I18nLijstValidator.LBL_OMSCHRIJVING,
                                         200})
                 .build();
  public static final Message REQ_CODE          =
      new Message.Builder()
                 .setAttribute(I18nLijstDto.COL_CODE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{I18nLijstValidator.LBL_CODE})
                 .build();
  public static final Message REQ_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(I18nLijstDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{I18nLijstValidator.LBL_OMSCHRIJVING})
                 .build();

  private static  I18nLijst i18nLijst;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_CODE);
    expResult.add(REQ_OMSCHRIJVING);
  }

  @BeforeClass
  public static void setUpClass() {
    i18nLijst  = new I18nLijst();

    i18nLijst.setCode(TestConstants.CODE);
    i18nLijst.setOmschrijving(TestConstants.OMSCHRIJVING);
  }

  @Test
  public void testFouteI18nLijst1() {
    var           instance  = new I18nLijst(i18nLijst);

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 101, "X"));

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_CODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nLijst2() {
    var           instance  = new I18nLijst(i18nLijst);

    instance.setOmschrijving(
        DoosUtils.stringMetLengte(TestConstants.OMSCHRIJVING, 201, "X"));

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_OMSCHRIJVING.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeI18nLijst() {
    var           instance  = new I18nLijst(i18nLijst);

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 100, "X"));
    instance.setOmschrijving(
        DoosUtils.stringMetLengte(TestConstants.OMSCHRIJVING, 200, "X"));

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeI18nLijstDto() {
    var           instance  = new I18nLijstDto();

    i18nLijst.persist(instance);
    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 100, "X"));
    instance.setOmschrijving(
        DoosUtils.stringMetLengte(TestConstants.OMSCHRIJVING, 200, "X"));

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  public void testLegeI18nLijst1() {
    var           instance  = new I18nLijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeI18nLijst2() {
    var           instance  = new I18nLijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeI18nLijst3() {
    var           instance  = new I18nLijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeI18nLijstDto1() {
    var           instance  = new I18nLijstDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    i18nLijst.persist(instance);

    List<Message> result    = I18nLijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }
}
