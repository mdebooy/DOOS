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

import eu.debooy.doos.form.I18nCodeTekst;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marco de Booij
 */
public final class I18nCodeTekstValidator {
  private I18nCodeTekstValidator() {
  }

  /**
   * Valideer de Lijst.
   */
  public static List<Message> valideer(I18nCodeTekst i18nCodeTekst) {
    List<Message> fouten  = new ArrayList<Message>();
    String  waarde  = i18nCodeTekst.getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.code"));
    } else if (waarde.length() != 2) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.FIXLENGTH,
                             "_I18N.label.code", 2));
    }

    waarde  = i18nCodeTekst.getTekst();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.tekst"));
    } else if (waarde.length() > 1024) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.MAXLENGTH,
                             "_I18N.label.tekst", 1024));
    }

    return fouten;
  }
}
