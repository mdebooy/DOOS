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

import eu.debooy.doos.form.Lijst;
import eu.debooy.doosutils.Aktie;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;

import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.custom.fileupload.UploadedFile;


/**
 * @author Marco de Booij
 */
public final class LijstValidator {
  private LijstValidator() {
  }

  /**
   * Valideer de Lijst.
   */
  public static List<Message> valideer(Lijst lijst, UploadedFile bestand,
                                       Aktie aktie) {
    List<Message> fouten  = new ArrayList<Message>();
    String        waarde  = lijst.getLijstnaam();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.lijstnaam"));
    } else if (waarde.length() > 25) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.MAXLENGTH,
                             "_I18N.label.lijstnaam", 25));
    }

    waarde  = lijst.getOmschrijving();
    if (DoosUtils.isBlankOrNull(waarde)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.omschrijving"));
    } else if (waarde.length() > 100) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.MAXLENGTH,
                             "_I18N.label.omschrijving", 100));
    }

    if (aktie.isNieuw() && DoosUtils.isBlankOrNull(bestand)) {
      fouten.add(new Message(Message.ERROR, PersistenceConstants.REQUIRED,
                             "_I18N.label.selecteerBestand"));
    }

    return fouten;
  }
}
