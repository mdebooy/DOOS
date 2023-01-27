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

import static eu.debooy.doos.TestConstants.LIJSTNAAM;
import static eu.debooy.doos.TestConstants.OMSCHRIJVING;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doos.form.Lijst;
import eu.debooy.doosutils.Aktie;
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
public class LijstValidatorTest {
  public static final Message ERR_LIJSTNAAM     =
      new Message.Builder()
                 .setAttribute(LijstDto.COL_LIJSTNAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{LijstValidator.LBL_LIJSTNAAM, 25})
                 .build();
  public static final Message ERR_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(LijstDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.MAXLENGTH)
                 .setParams(new Object[]{LijstValidator.LBL_OMSCHRIJVING, 100})
                 .build();
  public static final Message REQ_BESTAND       =
      new Message.Builder()
                 .setAttribute(LijstValidator.COL_BESTAND)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{LijstValidator.LBL_BESTAND})
                 .build();
  public static final Message REQ_LIJSTNAAM     =
      new Message.Builder()
                 .setAttribute(LijstDto.COL_LIJSTNAAM)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{LijstValidator.LBL_LIJSTNAAM})
                 .build();
  public static final Message REQ_OMSCHRIJVING  =
      new Message.Builder()
                 .setAttribute(LijstDto.COL_OMSCHRIJVING)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{LijstValidator.LBL_OMSCHRIJVING})
                 .build();

  private static  Aktie aktieF;
  private static  Aktie aktieT;
  private static  Lijst lijst;

  private void setLeeg(List<Message> expResult, boolean nieuw) {
    if (nieuw) {
      expResult.add(REQ_BESTAND);
    }
    expResult.add(REQ_LIJSTNAAM);
    expResult.add(REQ_OMSCHRIJVING);
  }

  @BeforeClass
  public static void setUpClass() {
    aktieF  = new Aktie(PersistenceConstants.UPDATE);
    aktieT  = new Aktie(PersistenceConstants.CREATE);
    lijst  = new Lijst();

    lijst.setLijstnaam(LIJSTNAAM);
    lijst.setOmschrijving(OMSCHRIJVING);
  }

  @Test
  public void testFouteLijst1() {
    var           instance  = new Lijst(lijst);

    instance.setLijstnaam(DoosUtils.stringMetLengte(LIJSTNAAM, 26, "X"));

    List<Message> result    = LijstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_LIJSTNAAM.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteLijst2() {
    var           instance  = new Lijst(lijst);

    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 101, "X"));

    List<Message> result    = LijstValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_OMSCHRIJVING.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteLijst3() {
    var           instance  = new Lijst(lijst);

    instance.setLijstnaam(DoosUtils.stringMetLengte(LIJSTNAAM, 26, "X"));

    List<Message> result    = LijstValidator.valideer(instance, null, aktieF);

    assertEquals(1, result.size());
    assertEquals(ERR_LIJSTNAAM.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteLijst4() {
    var           instance  = new Lijst(lijst);

    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 101, "X"));

    List<Message> result    = LijstValidator.valideer(instance, null, aktieF);

    assertEquals(1, result.size());
    assertEquals(ERR_OMSCHRIJVING.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeLijst1() {
    var           instance  = new Lijst(lijst);

    instance.setLijstnaam(DoosUtils.stringMetLengte(LIJSTNAAM, 25, "X"));
    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 100, "X"));

    List<Message> result    = LijstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeLijst2() {
    var           instance  = new Lijst(lijst);

    instance.setLijstnaam(DoosUtils.stringMetLengte(LIJSTNAAM, 25, "X"));
    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 100, "X"));

    List<Message> result    = LijstValidator.valideer(instance, null, aktieF);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testGoedeLijstDto() {
    var           instance  = new LijstDto();

    lijst.persist(instance);
    instance.setLijstnaam(DoosUtils.stringMetLengte(LIJSTNAAM, 25, "X"));
    instance.setOmschrijving(DoosUtils.stringMetLengte(OMSCHRIJVING, 100, "X"));

    List<Message> result    = LijstValidator.valideer(instance);

    assertTrue(result.isEmpty());
  }

  public void testLegeLijst1() {
    var           instance  = new Lijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult, true);

    List<Message> result    = LijstValidator.valideer(instance, null, aktieT);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeLijst2() {
    var           instance  = new Lijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult, true);

    List<Message> result    = LijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeLijst3() {
    var           instance  = new Lijst();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult, false);

    List<Message> result    = LijstValidator.valideer(instance, null, aktieF);

    assertEquals(expResult.toString(), result.toString());
  }

  public void testLegeLijstDto1() {
    var           instance  = new LijstDto();
    List<Message> expResult = new ArrayList<>();

    setLeeg(expResult, true);

    lijst.persist(instance);

    List<Message> result    = LijstValidator.valideer(instance);

    assertEquals(expResult.toString(), result.toString());
  }
}
