/**
 * Copyright 2011 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.business;

import eu.debooy.doos.access.I18nCodeDao;
import eu.debooy.doos.access.I18nCodeTekstDao;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.domain.I18nCodeTekstPK;
import eu.debooy.doosutils.domain.DoosFilter;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;


/**
 * @author Marco de Booij
 */
//@Interceptors({PersistenceExceptionHandlerInterceptor.class})
@Stateless
public class I18nCodeManager {
  @Inject
  private I18nCodeDao       i18nCodeDao;
  @Inject
  private I18nCodeTekstDao  i18nCodeTekstDao;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public I18nCodeDto createI18nCode(I18nCodeDto i18nCode) {
    return i18nCodeDao.create(i18nCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public I18nCodeTekstDto createI18nCodeTekst(I18nCodeTekstDto i18nCodeTekst) {
    return i18nCodeTekstDao.create(i18nCodeTekst);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void deleteI18nCode(I18nCodeDto i18nCode) {
    i18nCodeDao.delete(i18nCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void deleteI18nCodeTekst(I18nCodeTekstDto i18nCodeTekst) {
    i18nCodeTekstDao.delete(i18nCodeTekst);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nCodeDto> getAll() {
    return i18nCodeDao.getAll();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nCodeDto> getAll(DoosFilter<I18nCodeDto> filter) {
    return i18nCodeDao.getAll(filter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto getI18nCode(Long codeId) {
    return i18nCodeDao.getByPrimaryKey(codeId);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto getI18nCode(String code) {
    DoosFilter<I18nCodeDto> filter  = new DoosFilter<I18nCodeDto>();
    filter.addFilter("code", code);

    return i18nCodeDao.getUniqueResult(filter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto getI18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public I18nCodeDto updateI18nCode(I18nCodeDto i18nCode) {
    return i18nCodeDao.update(i18nCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public I18nCodeTekstDto updateI18nCodeTekst(I18nCodeTekstDto i18nCodeTekst) {
    return i18nCodeTekstDao.update(i18nCodeTekst);
  }
}
