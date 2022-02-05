/**
 * Copyright (c) 2016 Marco de Booij
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
import eu.debooy.doos.model.ChartElement;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
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

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(Long codeId) {
    return i18nCodeDao.getByPrimaryKey(codeId);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<ChartElement> getTekstenPerTaal() {
    return i18nCodeTekstDao.getTekstenPerTaal();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(String code)
      throws DuplicateObjectException, ObjectNotFoundException {
    DoosFilter<I18nCodeDto> filter  = new DoosFilter<>();
    filter.addFilter(I18nCodeDto.COL_CODE, code);

    return i18nCodeDao.getUniqueResult(filter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto i18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nCode i18nCode) {
    var dto = new I18nCodeDto();
    i18nCode.persist(dto);

    create(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nCodeDto i18nCodeDto) {
    i18nCodeDao.create(i18nCodeDto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId) {
    var i18nCode  = i18nCodeDao.getByPrimaryKey(codeId);
    i18nCodeDao.delete(i18nCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId, String taalKode) {
    var  i18nCodeTekst =
        i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId, taalKode));
    i18nCodeTekstDao.delete(i18nCodeTekst);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto getI18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nCode> query() {
    Collection<I18nCode>  i18nCodes = new ArrayList<>();
    i18nCodeDao.getAll().forEach(rij -> i18nCodes.add(new I18nCode(rij)));

    return i18nCodes;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeDto i18nCodeDto) {
    if (null == i18nCodeDto.getCodeId()) {
      i18nCodeDao.create(i18nCodeDto);
    } else {
      i18nCodeDao.update(i18nCodeDto);
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeTekstDto i18nCodeTekstDto) {
    i18nCodeTekstDao.update(i18nCodeTekstDto);
  }
}
