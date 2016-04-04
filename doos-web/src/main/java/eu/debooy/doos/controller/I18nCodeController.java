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
package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doos.form.I18nCodeTekst;
import eu.debooy.doos.form.Upload;
import eu.debooy.doos.validator.I18nCodeTekstValidator;
import eu.debooy.doos.validator.I18nCodeValidator;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named("doosI18nCode")
@SessionScoped
public class I18nCodeController extends Doos {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(I18nCodeController.class);

  private I18nCode      i18nCode;
  private I18nCodeTekst i18nCodeTekst;
  private Upload        upload;

  /**
   * Prepareer een Upload
   */
  public void batch() {
    upload  = new Upload();
    setSubTitel("doos.titel.i18ncode.upload");
    redirect(I18NUPLOAD_REDIRECT);
  }
  /**
   * Zet het Taxon dat gevraagd is klaar.
   * 
   * @param Long taxonId
   */
  public void bekijk(Long codeId) {
    i18nCode  = new I18nCode(getI18nCodeService().i18nCode(codeId));
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(i18nCode.getCode());
    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Geef de I18nCode.
   * 
   * @return I18nCode
   */
  public I18nCode getI18nCode() {
    return i18nCode;
  }

  /**
   * Geef de lijst met I18nCodes.
   * 
   * @return Collection<I18nCode> met I18nCode objecten.
   */
  public Collection<I18nCode> getI18nCodes() {
    return getI18nCodeService().query();
  }

  /**
   * Geef de Upload informatie.
   * 
   * @return Upload
   */
  public Upload getUpload() {
    return upload;
  }

  /**
   * Laad het propertiesbestand met I18N teksten in en sla ze op in de
   * database. 
   */
  public void uploading() {
    UploadedFile  bestand = upload.getBestand();
    if (null == bestand) {
      addError("errors.nofile");
      return;
    }

    String  split[]   = FilenameUtils.getBaseName(bestand.getName()).split("_");
    String  taalKode  = split[split.length-1].toLowerCase();
    upload.setTaal(taalKode);

    Properties  properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [" + e.getMessage() + "].");
      addError("errors.upload", bestand.getName());
      return;
    }

    upload.reset();

    for (Entry<Object, Object> rij : properties.entrySet()) {
      String  code  = rij.getKey().toString();
      String  tekst = rij.getValue().toString();
      try {
        if (upload.isUtf8()) {
          tekst = new String(tekst.getBytes("ISO-8859-1"), "UTF-8");
        }
      } catch (UnsupportedEncodingException e) {
        LOGGER.error("Tekst " + tekst + " [" + e.getMessage() + "]");
        addError("errors.encoding", code);
      }
      i18nCode  = new I18nCode(code);
      List<Message> messages  = I18nCodeValidator.valideer(i18nCode);
      if (messages.isEmpty()) {
        try {
          @SuppressWarnings("unused")
          I18nCode  aanwezig  =
              new I18nCode(getI18nCodeService().i18nCode(code));
        } catch (ObjectNotFoundException e) {
          upload.addNieuw();
          getI18nCodeService().save(i18nCode);
        }
        Long  codeId  = i18nCode.getCodeId();
        i18nCodeTekst = new I18nCodeTekst(codeId, taalKode, tekst);
        messages      = I18nCodeTekstValidator.valideer(i18nCodeTekst);
        if (messages.isEmpty()) {
          try {
            I18nCodeTekst aanwezig  =
                new I18nCodeTekst(getI18nCodeService().getI18nCodeTekst(codeId,
                    taalKode));
            if (upload.isOverschrijven()
                && !tekst.equals(aanwezig.getTekst())) {
              upload.addGewijzigd();
              getI18nCodeService().save(i18nCodeTekst);
            }
          } catch (ObjectNotFoundException e) {
            upload.addNieuweWaardes();
            getI18nCodeService().save(i18nCodeTekst);
          }
        } else {
          addMessage(messages);
        }
      } else {
        addMessage(messages);
      }
    }

    upload.setGelezen(properties.size());
  }
}
