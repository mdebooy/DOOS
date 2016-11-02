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
package eu.debooy.doos.service;

import eu.debooy.doos.access.I18nCodeDao;
import eu.debooy.doos.access.I18nCodeTekstDao;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.domain.I18nCodeTekstPK;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosI18nCodeService")
@Lock(LockType.WRITE)
public class I18nCodeService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(I18nCodeService.class);

  @Inject
  private I18nCodeDao       i18nCodeDao;
  @Inject
  private I18nCodeTekstDao  i18nCodeTekstDao;

  public I18nCodeService() {
    LOGGER.debug("init I18nCodeService");
  }

  /**
   * Geef een I18nCode.
   * 
   * @param Long codeId
   * @return I18nCodeDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(Long codeId) {
    I18nCodeDto i18nCode  = i18nCodeDao.getByPrimaryKey(codeId);

    return i18nCode;
  }

  /**
   * Geef een I18nCode.
   * 
   * @param String code
   * @return I18nCodeDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(String code) {
    DoosFilter<I18nCodeDto> filter  = new DoosFilter<I18nCodeDto>();
    filter.addFilter("code", code);
    I18nCodeDto i18nCode  = i18nCodeDao.getUniqueResult(filter);

    return i18nCode;
  }

  /**
   * Geef een I18nCodeTekst.
   * 
   * @param Long codeId
   * @param String taalKode
   * @return
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto i18nCodeTekst(Long codeId, String taalKode) {
    I18nCodeTekstDto  i18nCodeTekst =
        i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId, taalKode));

    return i18nCodeTekst;
  }

  /**
   * Maak de I18nTekstCode in de database.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  public void create(I18nCode i18nCode) {
    I18nCodeDto dto = new I18nCodeDto();
    i18nCode.persist(dto);

    create(dto);
  }

  /**
   * Maak de I18nTekstCode in de database.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  public void create(I18nCodeDto i18nCodeDto) {
    i18nCodeDao.create(i18nCodeDto);
  }

  /**
   * Verwijder de I18nCode.
   * 
   * @param Long codeId
   */
  public void delete(Long codeId) {
    I18nCodeDto i18nCode  = i18nCodeDao.getByPrimaryKey(codeId);
    i18nCodeDao.delete(i18nCode);
  }

  /**
   * Verwijder de I18nCodeTekst.
   * 
   * @param Long codeId
   * @param String taalKode
   */
  public void delete(Long codeId, String taalKode) {
    I18nCodeTekstDto  i18nCodeTekst =
        i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId, taalKode));
    i18nCodeTekstDao.delete(i18nCodeTekst);
  }

  /**
   * Geef een I18nCode.
   * 
   * @param Long codeId
   * @param String taalKode
   * @return I18nCodeDto.
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto getI18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  /**
   * Geef alle I18nCodes.
   * 
   * @return Collection<I18nCode>
   */
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nCode> query() {
    Collection<I18nCode>  i18nCodes = new ArrayList<I18nCode>();
    try {
      for (I18nCodeDto rij : i18nCodeDao.getAll()) {
        i18nCodes.add(new I18nCode(rij));
      }
    } catch (ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return i18nCodes;
  }

  /**
   * Maak of wijzig de I18nCode in de database.
   * 
   * @param I18nCodeDto i18nCodeDto
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeDto i18nCodeDto) {
    if (null == i18nCodeDto.getCodeId()) {
      i18nCodeDao.create(i18nCodeDto);
    } else {
      i18nCodeDao.update(i18nCodeDto);
    }
  }

  /**
   * Wijzig de I18nTekstCode in de database.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeTekstDto i18nCodeTekstDto) {
    i18nCodeTekstDao.update(i18nCodeTekstDto);
  }
}
