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

import eu.debooy.doos.domain.LokaleDto;
import eu.debooy.doos.form.Lokale;
import eu.debooy.doosutils.ComponentsUtils;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public class LokaleValidator {
  protected static final  String  LBL_CODE        = "_I18N.label.lokale";
  protected static final  String  LBL_EERSTETAAL  = "_I18N.label.eerstetaal";
  protected static final  String  LBL_TWEEDETAAL  = "_I18N.label.tweedetaal";

  private LokaleValidator() {
   throw new IllegalStateException("Utility class");
  }

  public static List<Message> valideer(LokaleDto lokale) {
    if (null == lokale) {
      return ComponentsUtils.objectIsNull("LokaleDto");
    }

    return valideer(new Lokale(lokale));
  }

  public static List<Message> valideer(Lokale lokale) {
    if (null == lokale) {
      return ComponentsUtils.objectIsNull("Lokale");
    }

    List<Message> fouten  = new ArrayList<>();

    valideerCode(lokale.getCode(), fouten);
    valideerEersteTaal(lokale.getEersteTaal(), fouten);
    valideerTweedeTaal(lokale.getTweedeTaal(), fouten);

    return fouten;
  }

  private static void valideerCode(String code, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(code)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_CODE})
                            .setAttribute(LokaleDto.COL_CODE)
                            .build());
      return;
    }

    if (code.length() > 50) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_CODE, 50})
                            .setAttribute(LokaleDto.COL_CODE)
                            .build());
    }
  }

  private static void valideerEersteTaal(String eersteTaal,
                                         List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(eersteTaal)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_EERSTETAAL})
                            .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                            .build());
      return;
    }

    if (!eersteTaal.toLowerCase().equals(eersteTaal)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.NIETLCASE)
                            .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                            .build());
    }

    if (eersteTaal.length() != 3) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{LBL_EERSTETAAL, 3})
                            .setAttribute(LokaleDto.COL_EERSTE_TAAL)
                            .build());
    }
  }

  private static void valideerTweedeTaal(String tweedeTaal,
                                         List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(tweedeTaal)) {
      return;
    }

    if (!tweedeTaal.toLowerCase().equals(tweedeTaal)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.NIETLCASE)
                            .setAttribute(LokaleDto.COL_TWEEDE_TAAL)
                            .build());
    }

    if (tweedeTaal.length() != 3) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{LBL_TWEEDETAAL, 3})
                            .setAttribute(LokaleDto.COL_TWEEDE_TAAL)
                            .build());
    }
  }
}
