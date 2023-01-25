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

import eu.debooy.doos.domain.LijstDto;
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
  public static final String  COL_BESTAND       = "bestand";
  public static final String  LBL_BESTAND       =
      "_I18N.label.selecteerBestand";
  public static final String  LBL_LIJSTNAAM     = "_I18N.label.lijstnaam";
  public static final String  LBL_OMSCHRIJVING  = "_I18N.label.omschrijving";

  private LijstValidator() {}

  public static List<Message> valideer(Lijst lijst) {
    return valideer(lijst, null, new Aktie(PersistenceConstants.UPDATE));
  }

  public static List<Message> valideer(LijstDto lijst) {
    return valideer(new Lijst(lijst),
                    null, new Aktie(PersistenceConstants.UPDATE));
  }

  public static List<Message> valideer(Lijst lijst, UploadedFile bestand,
                                       Aktie aktie) {
    List<Message> fouten  = new ArrayList<>();

    valideerLijstnaam(lijst.getLijstnaam(), fouten);
    valideerOmschrijving(lijst.getOmschrijving(), fouten);
    valideerBestand(bestand, aktie, fouten);

    return fouten;
  }

  private static void valideerBestand(UploadedFile bestand, Aktie aktie,
                                        List<Message> fouten) {
    if (aktie.isNieuw() && DoosUtils.isBlankOrNull(bestand)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(
                                new Object[]{LBL_BESTAND})
                            .setAttribute(COL_BESTAND)
                            .build());
    }
  }

  private static void valideerLijstnaam(String lijstnaam,
                                        List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(lijstnaam)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_LIJSTNAAM})
                            .setAttribute(LijstDto.COL_LIJSTNAAM)
                            .build());
      return;
    }

    if (lijstnaam.length() > 25) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_LIJSTNAAM, 25})
                            .setAttribute(LijstDto.COL_LIJSTNAAM)
                            .build());
    }
  }

  private static void valideerOmschrijving(String omschrijving,
                                        List<Message> fouten) {
    if (DoosUtils.isBlankOrNull(omschrijving)) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.REQUIRED)
                            .setParams(new Object[]{LBL_OMSCHRIJVING})
                            .setAttribute(LijstDto.COL_OMSCHRIJVING)
                            .build());
      return;
    }

    if (omschrijving.length() > 100) {
      fouten.add(new Message.Builder()
                            .setSeverity(Message.ERROR)
                            .setMessage(PersistenceConstants.MAXLENGTH)
                            .setParams(new Object[]{LBL_OMSCHRIJVING, 100})
                            .setAttribute(LijstDto.COL_OMSCHRIJVING)
                            .build());
    }
  }
}
