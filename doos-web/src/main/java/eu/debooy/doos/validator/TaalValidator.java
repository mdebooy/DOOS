/**
 * Copyright 2016 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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

import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doos.form.Taal;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public final class TaalValidator {
  private TaalValidator() {
  }

  public static List<Message> valideer(TaalDto taal) {
    return valideer(new Taal(taal));
  }

  public static List<Message> valideer(Taal taal) {
    List<Message> fouten  = new ArrayList<>();

    valideerIso6391(taal.getIso6391(), fouten);
    valideerIso6392b(taal.getIso6392b(), fouten);
    valideerIso6392t(taal.getIso6392t(), fouten);
    valideerIso6393(taal.getIso6393(), fouten);

    return fouten;
  }

  private static void valideerIso6391(String iso6391, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(iso6391)) {
      return;
    }

    if (iso6391.length() != 2) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{"_I18N.label.iso6391", 2})
                            .setAttribute(TaalDto.COL_ISO6391)
                            .build());
    }
  }

  private static void valideerIso6392b(String iso6392b, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(iso6392b)) {
      return;
    }

    if (iso6392b.length() != 3) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{"_I18N.label.iso6392b", 3})
                            .setAttribute(TaalDto.COL_ISO6392B)
                            .build());
    }
  }

  private static void valideerIso6392t(String iso6392t, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(iso6392t)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{"_I18N.label.iso6392t"})
                            .setAttribute(TaalDto.COL_ISO6392T)
                            .build());
      return;
    }

    if (iso6392t.length() != 3) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{"_I18N.label.iso6392t", 3})
                            .setAttribute(TaalDto.COL_ISO6392T)
                            .build());
    }
  }

  private static void valideerIso6393(String iso6393, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(iso6393)) {
      return;
    }

    if (iso6393.length() != 3) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{"_I18N.label.iso6393", 3})
                            .setAttribute(TaalDto.COL_ISO6393)
                            .build());
    }
  }
}
