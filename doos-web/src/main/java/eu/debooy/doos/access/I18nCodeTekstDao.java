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
package eu.debooy.doos.access;

import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.model.ChartElement;
import eu.debooy.doosutils.access.Dao;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


/**
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public class I18nCodeTekstDao extends Dao<I18nCodeTekstDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public I18nCodeTekstDao() {
    super(I18nCodeTekstDto.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Collection<ChartElement> getTekstenPerTaal() {
    var query = getEntityManager().createNamedQuery(
                    I18nCodeTekstDto.QRY_TEKSTENPERTAAL);

    List<Object[]>    rijen     = query.getResultList();
    Set<ChartElement> resultaat = new HashSet<>();
    if (null != rijen) {
      for (Object[] rij : rijen) {
        resultaat.add(new ChartElement(rij));
      }
    }

    return resultaat;
  }
}
