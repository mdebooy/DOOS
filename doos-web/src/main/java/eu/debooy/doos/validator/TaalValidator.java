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

  /**
   * Valideer de Taal.
   */
  public static List<Message> valideer(Taal taal) {
    List<Message> fouten  = new ArrayList<Message>();
    String        waarde  = taal.getTaalKode();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.code"));
    } else if (waarde.length() != 2) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.FIXLENGTH,
                             "_I18N.label.code", 2));
    }

    waarde  = taal.getTaal();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.taal"));
    } else if (waarde.length() > 100) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.MAXLENGTH,
                             "_I18N.label.taal", 100));
    }

    waarde  = taal.getEigennaam();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.taal.eigennaam"));
    } else if (waarde.length() > 100) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.MAXLENGTH,
                             "_I18N.label.taal.eigennaam", 100));
    }

    return fouten;
  }
}
