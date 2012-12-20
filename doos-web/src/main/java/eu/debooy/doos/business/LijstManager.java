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

import eu.debooy.doos.access.LijstDao;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doosutils.domain.DoosFilter;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;


/**
 * @author Marco de Booij
 */
//@Interceptors({PersistenceExceptionHandlerInterceptor.class})
@Stateless
public class LijstManager {
  @EJB
  private LijstDao  lijstDao;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public LijstDto createLijst(LijstDto lijst) {
    return lijstDao.create(lijst);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void deleteLijst(LijstDto lijst) {
    lijstDao.delete(lijst);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<LijstDto> getAll() {
    return lijstDao.getAll();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<LijstDto> getAll(DoosFilter<LijstDto> filter) {
    return lijstDao.getAll(filter);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public LijstDto getLijst(String sleutel) {
    DoosFilter<LijstDto>  filter  = new DoosFilter<LijstDto>();
    filter.addFilter("lijstnaam", sleutel);

    return lijstDao.getUniqueResult(filter);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public LijstDto updateLijst(LijstDto lijst) {
    return lijstDao.update(lijst);
  }
}
