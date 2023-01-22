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

import eu.debooy.doos.domain.ParameterDto;
import eu.debooy.doos.form.Parameter;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public final class ParameterValidator {
  private ParameterValidator() {
  }

  public static List<Message> valideer(ParameterDto parameter) {
    return valideer(new Parameter(parameter));
  }

  public static List<Message> valideer(Parameter parameter) {
    List<Message> fouten  = new ArrayList<>();

    valideerSleutel(parameter.getSleutel(), fouten);
    valideerWaarde(parameter.getWaarde(), fouten);

    return fouten;
  }

  private static void valideerSleutel(String sleutel, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(sleutel)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{"_I18N.label.sleutel"})
                            .setAttribute(ParameterDto.COL_SLEUTEL)
                            .build());
      return;
    }

    if (sleutel.length() > 100) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{"_I18N.label.sleutel",
                                                    100})
                            .setAttribute(ParameterDto.COL_SLEUTEL)
                            .build());
    }
  }

  private static void valideerWaarde(String waarde, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{"_I18N.label.waarde"})
                            .setAttribute(ParameterDto.COL_WAARDE)
                            .build());
      return;
    }

    if (waarde.length() > 255) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{"_I18N.label.waarde",
                                                    255})
                            .setAttribute(ParameterDto.COL_WAARDE)
                            .build());
    }
  }
}
