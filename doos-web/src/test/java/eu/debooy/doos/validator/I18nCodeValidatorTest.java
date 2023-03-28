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
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class I18nCodeValidatorTest {
  public static final Message ERR_CODE  =
      new Message.Builder()
                 .setAttribute(I18nCodeDto.COL_CODE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{I18nCodeValidator.LBL_CODE, 100})
                 .build();
  public static final Message REQ_CODE  =
      new Message.Builder()
                 .setAttribute(I18nCodeDto.COL_CODE)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{I18nCodeValidator.LBL_CODE})
                 .build();

  private static  I18nCode  i18nCode;

  @BeforeClass
  public static void setUpClass() {
    i18nCode  = new I18nCode();

    i18nCode.setCode(TestConstants.CODE);
    i18nCode.setCodeId(TestConstants.CODEID);
  }

  @Test
  public void testFouteI18nCode() {
    var           instance  = new I18nCode();

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 101, "X"));

    List<Message> result    = I18nCodeValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_CODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeDto() {
    var           instance  = new I18nCodeDto();

    i18nCode.persist(instance);
    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 101, "X"));

    List<Message> result    = I18nCodeValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_CODE.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeI18nCode() {
    var           instance  = new I18nCode();

    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 100, "X"));

    List<Message> result  = I18nCodeValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeI18nCodeDto() {
    var           instance  = new I18nCodeDto();

    i18nCode.persist(instance);
    instance.setCode(DoosUtils.stringMetLengte(TestConstants.CODE, 100, "X"));
    List<Message> result    = I18nCodeValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testLegeI18nCode() {
    var           instance  = new I18nCode();
    List<Message> result    = I18nCodeValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(REQ_CODE.toString(), result.get(0).toString());
  }

  @Test
  public void testLegeI18nCodeDto() {
    var           instance  = new I18nCodeDto();
    List<Message> result    = I18nCodeValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(REQ_CODE.toString(), result.get(0).toString());
  }
}
