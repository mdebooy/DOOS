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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
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

  public void batch() {
    upload  = new Upload();
    setSubTitel("doos.titel.i18ncode.upload");
    redirect(I18NUPLOAD_REDIRECT);
  }

  public void create() {
    i18nCode    = new I18nCode();
    i18nCodeDto = new I18nCodeDto();
    setAktie(PersistenceConstants.CREATE);
    setSubTitel("doos.titel.i18ncode.create");
    redirect(I18NCODE_REDIRECT);
  }

  public void createI18nCodeTekst() {
    i18nCodeTekst = new I18nCodeTekst();
    setDetailAktie(PersistenceConstants.CREATE);
    setDetailSubTitel("doos.titel.i18ncodetekst.create");
    redirect(I18NCODETEKST_REDIRECT);
  }

  public void delete(Long codeId, String code) {
    try {
      getI18nCodeService().delete(codeId);
      addInfo(PersistenceConstants.DELETED, code);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, code);
    } catch (DoosRuntimeException e) {
      LOGGER.error(ComponentsConstants.ERR_RUNTIME, e.getLocalizedMessage());
      generateExceptionMessage(e);
    }
  }

  public void deleteI18nCodeTekst(String taalKode) {
    try {
      i18nCodeDto.removeTekst(taalKode);
      getI18nCodeService().save(i18nCodeDto);
      addInfo(PersistenceConstants.DELETED, "'" + taalKode + "'");
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

  public Collection<I18nCode> getI18nCodes() {
    return getI18nCodeService().query();
  }

  public I18nCodeTekst getI18nCodeTekst() {
    return i18nCodeTekst;
  }

  public Collection<I18nCodeTekst> getI18nCodeTeksten() {
    Collection<I18nCodeTekst> teksten = new ArrayList<>();

    i18nCodeDto.getTeksten()
               .forEach(i18nCodeTekstDto ->
                            teksten.add(new I18nCodeTekst(i18nCodeTekstDto)));

    return teksten;
  }

  public Upload getUpload() {
    return upload;
  }

  public void retrieve(Long codeId) {
    i18nCodeDto = getI18nCodeService().i18nCode(codeId);
    i18nCode    = new I18nCode(i18nCodeDto);
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel(i18nCode.getCode());
    redirect(I18NCODE_REDIRECT);
  }

  public void retrieveI18nCodeTekst(String taalKode) {
    i18nCodeTekst = new I18nCodeTekst(i18nCodeDto.getTekst(taalKode));
    setDetailAktie(PersistenceConstants.RETRIEVE);
    setDetailSubTitel(i18nCode.getCode());
    redirect(I18NCODETEKST_REDIRECT);
  }

  public void save() {
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

  public void saveI18nCodeTekst() {
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

  public void update(Long codeId) {
    i18nCodeDto = getI18nCodeService().i18nCode(codeId);
    i18nCode    = new I18nCode(i18nCodeDto);
    setAktie(PersistenceConstants.UPDATE);
    setSubTitel("doos.titel.i18ncode.update");
    redirect(I18NCODE_REDIRECT);
  }

  public void updateI18nCodeTekst(String taalKode) {
    i18nCodeTekst =
        new I18nCodeTekst(getI18nCodeService()
            .i18nCodeTekst(i18nCode.getCodeId(), taalKode));
    setDetailAktie(PersistenceConstants.UPDATE);
    setDetailSubTitel("doos.titel.i18ncodetekst.update");
    redirect(I18NCODETEKST_REDIRECT);
  }

  public void uploading() {
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

    for (Entry<Object, Object> rij : properties.entrySet()) {
      var code  = rij.getKey().toString();
      var tekst = rij.getValue().toString();
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
      if (!messages.isEmpty()) {
        addMessage(messages);
        continue;
      }

      var i18nCodeTekstDto  = new I18nCodeTekstDto();
      i18nCodeTekst.persist(i18nCodeTekstDto);
      if (i18nCodeDto.containsTekst(uploadTaal)) {
        if (upload.isOverschrijven()
            && !tekst.equals(i18nCodeDto.getTekst(uploadTaal)
                                        .getTekst())) {
          i18nCodeDto.addTekst(i18nCodeTekstDto);
          getI18nCodeService().save(i18nCodeDto);
          upload.addGewijzigd();
        }
      } else {
        i18nCodeDto.addTekst(i18nCodeTekstDto);
        getI18nCodeService().save(i18nCodeDto);
        upload.addNieuweWaardes();
      }
    }

    addInfo("message.upload", bestand.getName());

    upload.setGelezen(properties.size());
  }
}
