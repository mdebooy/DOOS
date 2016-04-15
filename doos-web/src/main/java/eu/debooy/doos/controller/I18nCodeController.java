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
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doos.form.I18nCodeTekst;
import eu.debooy.doos.form.Upload;
import eu.debooy.doos.validator.I18nCodeTekstValidator;
import eu.debooy.doos.validator.I18nCodeValidator;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.components.Message;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
  private I18nCodeDto   i18nCodeDto;
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
   * Prepareer een nieuwe I18nCode.
   */
  public void create() {
    i18nCode    = new I18nCode();
    i18nCodeDto = new I18nCodeDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.i18ncode.create");
    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Prepareer een nieuwe I18nCodeTekst.
   */
  public void createI18nCodeTekst() {
    i18nCodeTekst = new I18nCodeTekst();
    setDetailAktie(PersistenceConstants.CREATE);
    setDetailSubTitel("doos.titel.i18ncodetekst.create");
    redirect(I18NCODETEKST_REDIRECT);
  }

  /**
   * Verwijder de I18nCode
   * 
   * @param Long codeId
   * @param String code
   */
  public void delete(Long codeId, String code) {
    try {
      getI18nCodeService().delete(codeId);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.DELETED, code);
  }

  /**
   * Verwijder de I18nCodeTekst.
   * 
   * @param String taalKode
   */
  public void deleteI18nCodeTekst(String taalKode) {
    try {
      i18nCodeDto.removeTekst(taalKode);
      getI18nCodeService().save(i18nCodeDto);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taalKode);
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }
    addInfo(PersistenceConstants.DELETED, "'" + taalKode + "'");
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
   * Geef de I18nCodeTekst.
   * 
   * @return I18nCodeTekst
   */
  public I18nCodeTekst getI18nCodeTekst() {
    return i18nCodeTekst;
  }

  /**
   * Geef de lijst met I18nCodeTeksten.
   * 
   * @return Collection<I18nCodeTekst>
   */
  public Collection<I18nCodeTekst> getI18nCodeTeksten() {
    Collection<I18nCodeTekst> teksten = new ArrayList<I18nCodeTekst>();
    for (I18nCodeTekstDto i18nCodeTekst : i18nCodeDto.getTeksten()) {
      teksten.add(new I18nCodeTekst(i18nCodeTekst));
    }

    return teksten;
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
   * Zet het I18nCode dat gevraagd is klaar.
   * 
   * @param Long codeId
   */
  public void retrieve(Long codeId) {
    i18nCodeDto = getI18nCodeService().i18nCode(codeId);
    i18nCode    = new I18nCode(i18nCodeDto);
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(i18nCode.getCode());
    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Zet het I18nCodeTekst dat gevraagd is klaar.
   * 
   * @param String taalKode
   */
  public void retrieveI18nCodeTekst(String taalKode) {
    i18nCodeTekst = new I18nCodeTekst(i18nCodeDto.getTekst(taalKode));
    setDetailAktie(PersistenceConstants.RETRIEVE);
    setDetailSubTitel(i18nCode.getCode());
    redirect(I18NCODETEKST_REDIRECT);
  }

  /**
   * Persist de I18nCode
   * 
   * @param I18nCode
   */
  public void save() {
    List<Message> messages  = I18nCodeValidator.valideer(i18nCode);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      i18nCode.persist(i18nCodeDto);
      getI18nCodeService().save(i18nCodeDto);
      if (getAktie().equals(PersistenceConstants.CREATE)) {
        addInfo(PersistenceConstants.CREATED, i18nCode.getCode());
      }
      if (getAktie().equals(PersistenceConstants.UPDATE)) {
        addInfo(PersistenceConstants.UPDATED, i18nCode.getCode());
      }
      setAktie(PersistenceConstants.RETRIEVE);
      setSubTitel(i18nCode.getCode());
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nCode.getCode());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nCode.getCode());
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }

    return;
  }

  /**
   * Persist de I18nCodeTekst
   * 
   * @param I18nCodeTekst
   */
  public void saveI18nCodeTekst() {
    List<Message> messages  = I18nCodeTekstValidator.valideer(i18nCodeTekst);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      I18nCodeTekstDto  i18nCodeTekstDto  = new I18nCodeTekstDto();
      i18nCodeTekst.persist(i18nCodeTekstDto);
      i18nCodeDto.addTekst(i18nCodeTekstDto);
      getI18nCodeService().save(i18nCodeDto);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nCodeTekst.getTaalKode());
      return;
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nCodeTekst.getTaalKode());
      return;
    } catch (DoosRuntimeException e) {
      LOGGER.error("RT: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
      return;
    }

    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Zet de I18nCode die gewijzigd gaat worden klaar.
   * 
   * @param Long codeId
   */
  public void update(Long codeId) {
    i18nCodeDto = getI18nCodeService().i18nCode(codeId);
    i18nCode    = new I18nCode(i18nCodeDto);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.i18ncode.update");
    redirect(I18NCODE_REDIRECT);
  }

  /**
   * Zet de I18nCodeTekst die gewijzigd gaat worden klaar.
   * 
   * @param String taalKode
   */
  public void updateI18nCodeTekst(String taalKode) {
    i18nCodeTekst =
        new I18nCodeTekst(getI18nCodeService()
            .i18nCodeTekst(i18nCode.getCodeId(), taalKode));
    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel("doos.titel.i18ncodetekst.update");
    redirect(I18NCODETEKST_REDIRECT);
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
          i18nCodeDto = getI18nCodeService().i18nCode(code);
        } catch (ObjectNotFoundException e) {
          i18nCodeDto = new I18nCodeDto(code);
          getI18nCodeService().create(i18nCodeDto);
          upload.addNieuw();
        }
        Long  codeId  = i18nCodeDto.getCodeId();
        i18nCodeTekst = new I18nCodeTekst(codeId, taalKode, tekst);
        messages      = I18nCodeTekstValidator.valideer(i18nCodeTekst);
        if (messages.isEmpty()) {
          I18nCodeTekstDto  i18nCodeTekstDto  = new I18nCodeTekstDto();
          i18nCodeTekst.persist(i18nCodeTekstDto);
          if (i18nCodeDto.containsTekst(taalKode)) {
            if (upload.isOverschrijven()
                && !tekst.equals(i18nCodeDto.getTekst(taalKode).getTekst())) {
              i18nCodeDto.addTekst(i18nCodeTekstDto);
              getI18nCodeService().save(i18nCodeDto);
              upload.addGewijzigd();
            }
          } else {
            i18nCodeDto.addTekst(i18nCodeTekstDto);
            getI18nCodeService().save(i18nCodeDto);
            upload.addNieuweWaardes();
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
