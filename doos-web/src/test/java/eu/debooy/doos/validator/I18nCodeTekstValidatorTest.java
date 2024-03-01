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
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.form.I18nCodeTekst;
import static eu.debooy.doos.validator.I18nCodeTekstValidatorTest.ERR_TAALKODE;
import static eu.debooy.doos.validator.I18nCodeTekstValidatorTest.ERR_TEKST;
import static eu.debooy.doos.validator.I18nCodeTekstValidatorTest.REQ_TAALKODE;
import static eu.debooy.doos.validator.I18nCodeTekstValidatorTest.REQ_TEKST;
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
 *
 * @author Marco de Booij
 */
public class I18nCodeTekstValidatorTest {
  public static final Message ERR_TEKST     =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{I18nCodeTekstValidator.LBL_TEKST,
                                         1024})
                 .setAttribute(I18nCodeTekstDto.COL_TEKST)
                 .build();
  public static final Message ERR_TAALKODE  =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{I18nCodeTekstValidator.LBL_TAALKODE,
                                         2})
                 .setAttribute(I18nCodeTekstDto.COL_TAALKODE)
                 .build();
  public static final Message REQ_TAALKODE  =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{I18nCodeTekstValidator.LBL_TAALKODE})
                 .setAttribute(I18nCodeTekstDto.COL_TAALKODE)
                 .build();
  public static final Message REQ_TEKST     =
      new Message.Builder()
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{I18nCodeTekstValidator.LBL_TEKST})
                 .setAttribute(I18nCodeTekstDto.COL_TEKST)
                 .build();

  private static  I18nCodeTekst i18nCodeTekst;

  private void setLeeg(List<Message> expResult) {
    expResult.add(REQ_TAALKODE);
    expResult.add(REQ_TEKST);
  }

  @BeforeClass
  public static void setUpClass() {
    i18nCodeTekst  = new I18nCodeTekst();

    i18nCodeTekst.setCodeId(TestConstants.CODEID);
    i18nCodeTekst.setTaalKode(TestConstants.TAALKODE);
    i18nCodeTekst.setTekst(TestConstants.TEKST);
  }

  @Test
  public void testFouteI18nCodeTekst1() {
    var           instance  = new I18nCodeTekst();

    instance.setTaalKode("X");
    instance.setTekst(TestConstants.TEKST);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TAALKODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeTekst2() {
    var           instance  = new I18nCodeTekst();

    instance.setTaalKode(
        DoosUtils.stringMetLengte(TestConstants.TAALKODE, 3, "X"));
    instance.setTekst(TestConstants.TEKST);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TAALKODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeTekst3() {
    var           instance  = new I18nCodeTekst();

    instance.setTaalKode(TestConstants.TAALKODE);
    instance.setTekst(
        DoosUtils.stringMetLengte(TestConstants.TEKST, 1025, "X"));

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TEKST.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeTekstDto1() {
    var           instance  = new I18nCodeTekstDto();

    i18nCodeTekst.persist(instance);
    instance.setTaalKode("X");
    instance.setTekst(TestConstants.TEKST);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TAALKODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeTekstDto2() {
    var           instance  = new I18nCodeTekstDto();

    i18nCodeTekst.persist(instance);
    instance.setTaalKode(
        DoosUtils.stringMetLengte(TestConstants.TAALKODE, 3, "X"));
    instance.setTekst(TestConstants.TEKST);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TAALKODE.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteI18nCodeTekstDto3() {
    var           instance  = new I18nCodeTekstDto();

    i18nCodeTekst.persist(instance);
    instance.setTaalKode(TestConstants.TAALKODE);
    instance.setTekst(
        DoosUtils.stringMetLengte(TestConstants.TEKST, 1025, "X"));

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_TEKST.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeI18nCodeTekst() {
    var           instance  = new I18nCodeTekst();

    instance.setTaalKode(TestConstants.TAALKODE);
    instance.setTekst(
        DoosUtils.stringMetLengte(TestConstants.TEKST, 1024, "X"));

    List<Message> result  = I18nCodeTekstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeI18nCodeTekstDto() {
    var           instance  = new I18nCodeTekstDto();

    i18nCodeTekst.persist(instance);
    instance.setTaalKode(TestConstants.TAALKODE);
    instance.setTekst(
        DoosUtils.stringMetLengte(TestConstants.TEKST, 1024, "X"));
    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testLegeI18nCodeTekst() {
    var           instance  = new I18nCodeTekst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testLegeI18nCodeTekstDto() {
    var           instance  = new I18nCodeTekstDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult);

    List<Message> result    = I18nCodeTekstValidator.valideer(instance);
    assertEquals(expResult.toString(), result.toString());
  }

  @Test
  public void testNullI18nCodeTekst() {
    I18nCodeTekst instance  = null;
    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }

  @Test
  public void testNullI18nCodeTekstDto() {
    I18nCodeTekstDto
                  instance  = null;
    List<Message> result    = I18nCodeTekstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(PersistenceConstants.NULL, result.get(0).getMessage());
  }
}
