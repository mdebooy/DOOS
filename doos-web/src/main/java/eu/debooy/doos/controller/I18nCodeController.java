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
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
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

  private static final  String  DTIT_CREATE   =
      "doos.titel.i18nCodeTekst.create";
  private static final  String  DTIT_UPDATE   =
      "doos.titel.i18nCodeTekst.update";
  private static final  String  TIT_CREATE    = "doos.titel.i18nCode.create";
  private static final  String  TIT_RETRIEVE  = "doos.titel.i18nCode.retrieve";
  private static final  String  TIT_UPDATE    = "doos.titel.i18nCode.update";
  private static final  String  TIT_UPLOAD    = "doos.titel.i18nCode.upload";

  private I18nCode      i18nCode;
  private I18nCodeDto   i18nCodeDto;
  private I18nCodeTekst i18nCodeTekst;
  private Upload        upload;

  public void batch() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    upload  = new Upload();

    setSubTitel(getTekst(TIT_UPLOAD));
    redirect(I18NUPLOAD_REDIRECT);
  }

  public void create() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    i18nCode    = new I18nCode();
    i18nCodeDto = new I18nCodeDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel(getTekst(TIT_CREATE));
    redirect(I18NCODE_REDIRECT);
  }

  public void createDetail() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    i18nCodeTekst = new I18nCodeTekst();
    i18nCodeTekst.setTaalKode(getGebruikersTaal());
    setDetailAktie(PersistenceConstants.CREATE);
    setDetailSubTitel(getTekst(DTIT_CREATE));
    redirect(I18NCODETEKST_REDIRECT);
  }

  public void delete() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var codeId  = i18nCode.getCodeId();
    var code    = i18nCode.getCode();
    try {
      getI18nCodeService().delete(codeId);
      addInfo(PersistenceConstants.DELETED, code);
      redirect(I18NCODES_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void deleteDetail() {
    if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var taalKode  = i18nCodeTekst.getTaalKode();
    try {
      i18nCodeDto.removeTekst(taalKode);
      getI18nCodeService().save(i18nCodeDto);
      addInfo(PersistenceConstants.DELETED, "'" + taalKode + "'");
      redirect(I18NCODE_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, taalKode);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public I18nCode getI18nCode() {
    return i18nCode;
  }

  public I18nCodeTekst getI18nCodeTekst() {
    return i18nCodeTekst;
  }

  public JSONArray getI18nCodeTeksten() {
    var teksten = new JSONArray();

    i18nCodeDto.getTeksten().forEach(rij -> teksten.add(rij.toJSON()));

    return teksten;
  }

  public Upload getUpload() {
    return upload;
  }

  public void retrieve() {
     if (!isUser() && !isView()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec      = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(I18nCodeDto.COL_CODEID)) {
      addError(ComponentsConstants.GEENPARAMETER, I18nCodeDto.COL_CODEID);
      return;
    }

    var codeId  =
        Long.valueOf(ec.getRequestParameterMap()
                       .get(I18nCodeDto.COL_CODEID));

    i18nCodeDto = getI18nCodeService().i18nCode(codeId);
    i18nCode    = new I18nCode(i18nCodeDto);
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(getTekst(TIT_RETRIEVE));
    redirect(I18NCODE_REDIRECT);
  }

  public void retrieveDetail() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec  = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap()
           .containsKey(I18nCodeTekstDto.COL_TAALKODE)) {
      addError(ComponentsConstants.GEENPARAMETER,
               I18nCodeTekstDto.COL_TAALKODE);
      return;
    }

    i18nCodeTekst =
        new I18nCodeTekst(
            i18nCodeDto.getTekst(ec.getRequestParameterMap()
                                   .get(I18nCodeTekstDto.COL_TAALKODE)));
    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel(DTIT_UPDATE);
    redirect(I18NCODETEKST_REDIRECT);
  }

  public void save() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = I18nCodeValidator.valideer(i18nCode);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    try {
      i18nCode.persist(i18nCodeDto);
      getI18nCodeService().save(i18nCodeDto);
      switch (getAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          i18nCode.setCodeId(i18nCodeDto.getCodeId());
          addInfo(PersistenceConstants.CREATED, i18nCode.getCode());
          setAktie(PersistenceConstants.UPDATE);
          setSubTitel(i18nCode.getCode());
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, i18nCode.getCode());
          break;
        default:
          addError("error.aktie.wrong", getAktie().getAktie());
          break;
      }
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nCode.getCode());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nCode.getCode());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void saveDetail() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var messages  = I18nCodeTekstValidator.valideer(i18nCodeTekst);
    if (!messages.isEmpty()) {
      addMessage(messages);
      return;
    }

    if (getDetailAktie().getAktie() == PersistenceConstants.CREATE
        && i18nCodeDto.hasTekst(i18nCodeTekst.getTaalKode())) {
      addError(PersistenceConstants.DUPLICATE, i18nCodeTekst.getTaalKode());
      return;
    }

    try {
      var i18nCodeTekstDto  = new I18nCodeTekstDto();
      i18nCodeTekst.persist(i18nCodeTekstDto);
      i18nCodeDto.addTekst(i18nCodeTekstDto);
      getI18nCodeService().save(i18nCodeDto);
      switch (getDetailAktie().getAktie()) {
        case PersistenceConstants.CREATE:
          i18nCode.setCodeId(i18nCodeDto.getCodeId());
          addInfo(PersistenceConstants.CREATED, i18nCodeTekst.getTaalKode());
          break;
        case PersistenceConstants.UPDATE:
          addInfo(PersistenceConstants.UPDATED, i18nCodeTekst.getTaalKode());
          break;
        default:
          addError(ComponentsConstants.WRONGREDIRECT, getAktie().getAktie());
          break;
      }
      redirect(I18NCODE_REDIRECT);
    } catch (DuplicateObjectException e) {
      addError(PersistenceConstants.DUPLICATE, i18nCodeTekst.getTaalKode());
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, i18nCodeTekst.getTaalKode());
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void update() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    setAktie(PersistenceConstants.UPDATE);
    setSubTitel(getTekst(TIT_UPDATE));
  }

  public void uploading() {
     if (!isUser()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var bestand = upload.getBestand();
    if (null == bestand) {
      addError("errors.nofile");
      return;
    }

    var split       = FilenameUtils.getBaseName(bestand.getName()).split("_");
    var uploadTaal  = split[split.length-1].toLowerCase();
    upload.setTaal(uploadTaal);

    var properties  = new Properties();
    try {
      properties.load(bestand.getInputStream());
    } catch (IOException e) {
      LOGGER.error("Properties Load error [{}].", e.getLocalizedMessage());
      addError("errors.upload", bestand.getName());
      return;
    }

    upload.reset();

    for (var entry : properties.entrySet()) {
      var code      = entry.getKey().toString();
      var tekst     = entry.getValue().toString();
      if (upload.isUtf8()) {
        tekst = new String(tekst.getBytes(StandardCharsets.ISO_8859_1),
                           StandardCharsets.UTF_8);
      }

      i18nCode      = new I18nCode(code);
      var messages  = I18nCodeValidator.valideer(i18nCode);
      if (!messages.isEmpty()) {
        addMessage(messages);
        continue;
      }

      try {
        i18nCodeDto = getI18nCodeService().i18nCode(code);
      } catch (ObjectNotFoundException e) {
        i18nCodeDto = new I18nCodeDto(code);
        getI18nCodeService().create(i18nCodeDto);
        upload.addNieuw();
      }
      var codeId    = i18nCodeDto.getCodeId();
      i18nCodeTekst = new I18nCodeTekst(codeId, uploadTaal, tekst);
      messages      = I18nCodeTekstValidator.valideer(i18nCodeTekst);
      if (messages.isEmpty()) {
        verwerkUploadedTekst();
      } else {
        addMessage(messages);
      }
    }

    upload.setGelezen(properties.size());

    addInfo("message.upload", bestand.getName());
  }

  private void verwerkUploadedTekst() {
    var i18nCodeTekstDto  = new I18nCodeTekstDto();
    i18nCodeTekst.persist(i18nCodeTekstDto);

    if (!i18nCodeDto.containsTekst(i18nCodeTekst.getTaalKode())) {
      i18nCodeDto.addTekst(i18nCodeTekstDto);
      getI18nCodeService().save(i18nCodeDto);
      upload.addNieuweWaardes();

      return;
    }

    if (upload.isOverschrijven()
        && !i18nCodeTekst.getTekst()
                         .equals(
                              i18nCodeDto.getTekst(i18nCodeTekst.getTaalKode())
                                         .getTekst())) {
      i18nCodeDto.addTekst(i18nCodeTekstDto);
      getI18nCodeService().save(i18nCodeDto);
      upload.addGewijzigd();
    }
  }
}
