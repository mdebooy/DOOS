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

import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public final class I18nCodeValidator {
  protected static final  String  LBL_CODE  = "_I18N.label.code";

  private I18nCodeValidator() {
  }

  public static List<Message> valideer(I18nCodeDto i18nCode) {
    return valideer(new I18nCode(i18nCode));
  }

  public static List<Message> valideer(I18nCode i18nCode) {
    List<Message> fouten  = new ArrayList<>();

    valideerCode(i18nCode.getCode(), fouten);

    return fouten;
  }

  private static void valideerCode(String code, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(code)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_CODE})
                            .setAttribute(I18nCodeDto.COL_CODE)
                            .build());
      return;
    }

    if (code.length() > 100) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_CODE, 100})
                            .setAttribute(I18nCodeDto.COL_CODE)
                            .build());
    }
  }
}
