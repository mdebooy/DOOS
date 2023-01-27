/*
 * Copyright (c) 2022 Marco de Booij
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

import eu.debooy.doos.domain.TaalnaamDto;
import eu.debooy.doos.form.Taalnaam;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public class TaalnaamValidator {
  protected static final  String  LBL_ISO6392T  = "_I18N.label.iso6392t";
  protected static final  String  LBL_NAAM      = "_I18N.label.naam";

  private TaalnaamValidator() {}

  public static List<Message> valideer(TaalnaamDto taalnaam) {
    return valideer(new Taalnaam(taalnaam));
  }

  public static List<Message> valideer(Taalnaam taalnaam) {
    List<Message> fouten  = new ArrayList<>();

    valideerIso6392t(DoosUtils.nullToEmpty(taalnaam.getIso6392t()), fouten);
    valideerNaam(DoosUtils.nullToEmpty(taalnaam.getNaam()), fouten);

    return fouten;
  }

  private static void valideerIso6392t(String iso6392t, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(iso6392t)) {
      fouten.add(new Message.Builder()
                            .setAttribute(TaalnaamDto.COL_ISO6392T)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_ISO6392T})
                            .build());
      return;
    }

    if (iso6392t.length() != 3) {
      fouten.add(new Message.Builder()
                            .setAttribute(TaalnaamDto.COL_ISO6392T)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{LBL_ISO6392T, 3})
                            .build());
    }
  }

  private static void valideerNaam(String naam, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(naam)) {
      fouten.add(new Message.Builder()
                            .setAttribute(TaalnaamDto.COL_NAAM)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_NAAM})
                            .build());
      return;
    }

    if (naam.length() > 100) {
      fouten.add(new Message.Builder()
                            .setAttribute(TaalnaamDto.COL_NAAM)
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_NAAM, 100})
                            .build());
    }
  }
}
