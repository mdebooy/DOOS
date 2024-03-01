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

import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.form.I18nCodeTekst;
import eu.debooy.doosutils.ComponentsUtils;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public final class I18nCodeTekstValidator {
  protected static final  String  LBL_TAALKODE  = "_I18N.label.code";
  protected static final  String  LBL_TEKST     = "_I18N.label.tekst";

  private I18nCodeTekstValidator() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Message> valideer(I18nCodeTekstDto i18nCodeTekst) {
    if (null == i18nCodeTekst) {
      return ComponentsUtils.objectIsNull("I18nCodeTekstDto");
    }

    return valideer(new I18nCodeTekst(i18nCodeTekst));
  }

  public static List<Message> valideer(I18nCodeTekst i18nCodeTekst) {
    if (null == i18nCodeTekst) {
      return ComponentsUtils.objectIsNull("I18nCodeTekst");
    }

    List<Message> fouten  = new ArrayList<>();

    valideerTaalKode(i18nCodeTekst.getTaalKode(), fouten);
    valideerTekst(i18nCodeTekst.getTekst(), fouten);

    return fouten;
  }

  private static void valideerTaalKode(String taalKode, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(taalKode)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_TAALKODE})
                            .setAttribute(I18nCodeTekstDto.COL_TAALKODE)
                            .build());
      return;
    }

    if (taalKode.length() != 2) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.FIXLENGTH)
                            .setParams(new Object[]{LBL_TAALKODE, 2})
                            .setAttribute(I18nCodeTekstDto.COL_TAALKODE)
                            .build());
    }
  }

  private static void valideerTekst(String tekst, List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(tekst)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_TEKST})
                            .setAttribute(I18nCodeTekstDto.COL_TEKST)
                            .build());
      return;
    }

    if (tekst.length() > 1024) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_TEKST, 1024})
                            .setAttribute(I18nCodeTekstDto.COL_TEKST)
                            .build());
    }
  }
}
