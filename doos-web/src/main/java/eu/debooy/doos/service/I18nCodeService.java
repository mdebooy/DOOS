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
import eu.debooy.doos.form.I18nCodeTekst;
import eu.debooy.doosutils.domain.DoosFilter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

  private Set<I18nCode>     i18nCodes;

  /**
   * Initialisatie.
   */
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
    if (null == i18nCodes) {
      i18nCodes = new HashSet<I18nCode>();
      Collection<I18nCodeDto> rijen = i18nCodeDao.getAll();
      for (I18nCodeDto rij : rijen) {
        i18nCodes.add(new I18nCode(rij));
      }
    }

    return i18nCodes;
  }

  /**
   * Maak of wijzig de I18nCode in de database.
   * 
   * @param I18nCode i18nCode
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCode i18nCode) {
    I18nCodeDto dto = new I18nCodeDto();
    i18nCode.persist(dto);

    i18nCodeDao.update(dto);

    if (null != i18nCodes) {
      i18nCodes.remove(i18nCode);
      i18nCodes.add(i18nCode);
    }
  }

  /**
   * Maak of wijzig de I18nCode in de database.
   * 
   * @param I18nCodeDto i18nCodeDto
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeDto i18nCodeDto) {
    save(new I18nCode(i18nCodeDto));
  }

  /**
   * Maak of wijzig de I18nCodeTekst in de database.
   * 
   * @param I18nCodeTekst i18nCodeTekst
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeTekst i18nCodeTekst) {
    I18nCodeTekstDto dto = new I18nCodeTekstDto();
    i18nCodeTekst.persist(dto);

    i18nCodeTekstDao.update(dto);
  }

  /**
   * Maak of wijzig de I18nTekstCode in de database.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeTekstDto i18nCodeTekstDto) {
    save(new I18nCodeTekst(i18nCodeTekstDto));
  }
}