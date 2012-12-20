/**
 * Copyright 2009 Marco de Booij
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

import eu.debooy.doos.access.TaalDao;
import eu.debooy.doos.domain.TaalDto;
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
public class TaalManager {
  @EJB
  private TaalDao taalDao;

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public TaalDto createTaal(TaalDto taal) {
    return taalDao.create(taal);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void deleteTaal(TaalDto taal) {
    taalDao.delete(taal);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<TaalDto> getAll() {
    return taalDao.getAll();
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<TaalDto> getAll(DoosFilter<TaalDto> filter) {
    return taalDao.getAll(filter);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public TaalDto updateTaal(TaalDto taal) {
    return taalDao.update(taal);
  }
}
