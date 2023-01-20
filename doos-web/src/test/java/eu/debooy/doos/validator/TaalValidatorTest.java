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

import static eu.debooy.doos.TestConstants.EIGENNAAM;
import static eu.debooy.doos.TestConstants.ISO6391;
import static eu.debooy.doos.TestConstants.ISO6392B;
import static eu.debooy.doos.TestConstants.ISO6392T;
import static eu.debooy.doos.TestConstants.ISO6393;
import static eu.debooy.doos.TestConstants.NAAM;
import static eu.debooy.doos.TestConstants.TAALID;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Marco de Booij
 */
public class TaalValidatorTest {
  public static final Message ERR_ISO6391   =
      new Message.Builder()
                 .setAttribute(TaalDto.COL_ISO6391)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{"_I18N.label.iso6391", 2})
                 .build();
  public static final Message ERR_ISO6392B  =
      new Message.Builder()
                 .setAttribute(TaalDto.COL_ISO6392B)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{"_I18N.label.iso6392b", 3})
                 .build();
  public static final Message ERR_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{"_I18N.label.iso6392t", 3})
                 .build();
  public static final Message ERR_ISO6393   =
      new Message.Builder()
                 .setAttribute(TaalDto.COL_ISO6393)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.FIXLENGTH)
                 .setParams(new Object[]{"_I18N.label.iso6393", 3})
                 .build();
  public static final Message REQ_ISO6392T  =
      new Message.Builder()
                 .setAttribute(TaalDto.COL_ISO6392T)
                 .setSeverity(Message.ERROR)
                 .setMessage(PersistenceConstants.REQUIRED)
                 .setParams(new Object[]{"_I18N.label.iso6392t"})
                 .build();
  private static  Taal   taal;

  @BeforeClass
  public static void setUpClass() {
    taal  = new Taal();

    taal.setEigennaam(EIGENNAAM);
    taal.setIso6391(ISO6391);
    taal.setIso6392b(ISO6392B);
    taal.setIso6392t(ISO6392T);
    taal.setIso6393(ISO6393);
    taal.setLevend(true);
    taal.setNaam(NAAM);
    taal.setTaalId(TAALID);
  }

  @Test
  public void testFouteTaal1() {
    var           instance  = new Taal(taal);

    instance.setIso6391("X");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6391.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal2() {
    var           instance  = new Taal(taal);

    instance.setIso6391("XXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6391.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal3() {
    var           instance  = new Taal(taal);

    instance.setIso6392b("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392B.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal4() {
    var           instance  = new Taal(taal);

    instance.setIso6392b("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392B.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal5() {
    var           instance  = new Taal(taal);

    instance.setIso6392t("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal6() {
    var           instance  = new Taal(taal);

    instance.setIso6392t("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal7() {
    var           instance  = new Taal(taal);

    instance.setIso6393("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6393.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaal8() {
    var           instance  = new Taal(taal);

    instance.setIso6393("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6393.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto1() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6391("X");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6391.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto2() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6391("XXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6391.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto3() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6392b("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392B.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto4() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6392b("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392B.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto5() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6392t("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto6() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6392t("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto7() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6393("XX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6393.toString(), result.get(0).toString());
  }

  @Test
  public void testFouteTaalDto8() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6393("XXXX");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(ERR_ISO6393.toString(), result.get(0).toString());
  }

  @Test
  public void testGoedeTaal1() {
    List<Message> result  = TaalValidator.valideer(taal);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaal2() {
    var           instance  = new Taal(taal);

    instance.setIso6391(null);
    instance.setIso6392b(null);
    instance.setIso6393(null);

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaal3() {
    var           instance  = new Taal(taal);

    instance.setIso6391("");
    instance.setIso6392b("");
    instance.setIso6393("");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalDto1() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalDto2() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6391(null);
    instance.setIso6392b(null);
    instance.setIso6393(null);

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testGoedeTaalDto3() {
    var           instance  = new TaalDto();

    taal.persist(instance);
    instance.setIso6391("");
    instance.setIso6392b("");
    instance.setIso6393("");

    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(0, result.size());
  }

  @Test
  public void testLegeTaalnaam() {
    var           instance  = new Taal();
    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(REQ_ISO6392T.toString(), result.get(0).toString());
  }

  @Test
  public void testLegeTaalnaamDto() {
    var           instance  = new TaalDto();
    List<Message> result    = TaalValidator.valideer(instance);

    assertEquals(1, result.size());
    assertEquals(REQ_ISO6392T.toString(), result.get(0).toString());
  }
}
