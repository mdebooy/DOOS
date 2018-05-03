/**
 * Copyright 2017 Marco de Booij
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
package eu.debooy.doos.access;

import eu.debooy.doos.domain.I18nSelectieDto;
import eu.debooy.doosutils.access.Dao;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;

import java.util.List;

import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;


/**
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public class I18nSelectieDao extends Dao<I18nSelectieDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public I18nSelectieDao() {
    super(I18nSelectieDto.class);
  } 

  protected EntityManager getEntityManager() {
    return em;
  }

  /**
   * Haal de selectie op.
   * 
   * @return List<I18nSelectieDto>
   */
  @SuppressWarnings("unchecked")
  public I18nSelectieDto getSelectie(String selectie, String code) {
    Query query =
        getEntityManager().createNamedQuery(I18nSelectieDto.QUERY_SELECTIE)
                          .setParameter("selectie", selectie)
                          .setParameter("code", code);

    List<I18nSelectieDto> resultaat = query.getResultList();
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         I18nSelectieDto.QUERY_SELECTIE);
    }

    return resultaat.get(0);
  }

  /**
   * Haal de selectie op.
   * 
   * @return List<I18nSelectieDto>
   */
  @SuppressWarnings("unchecked")
  public List<I18nSelectieDto> getSelecties(String selectie) {
    Query query =
        getEntityManager().createNamedQuery(I18nSelectieDto.QUERY_SELECTIES)
                          .setParameter("selectie", selectie);

    return query.getResultList();
  }
}
