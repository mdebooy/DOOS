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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


/**
 * @author Marco de Booij
 */
public class I18nSelectieDao extends Dao<I18nSelectieDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public I18nSelectieDao() {
    super(I18nSelectieDto.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public Collection<I18nSelectieDto> getSelectie(String selectie) {
    var query =
      getEntityManager().createNamedQuery(I18nSelectieDto.QRY_SELECTIE)
                        .setParameter(I18nSelectieDto.PAR_SELECTIE, selectie);

    return query.getResultList();
  }

  public I18nSelectieDto getSelectie(String selectie, String code) {
    Map<String, Object> params  = new HashMap<>();
    params.put(I18nSelectieDto.PAR_SELECTIE, selectie);
    params.put(I18nSelectieDto.PAR_CODE, code);

    List<I18nSelectieDto> resultaat = namedQuery(I18nSelectieDto.QRY_SELECTIE,
                                                 params);
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         I18nSelectieDto.QRY_SELECTIE);
    }

    return resultaat.get(0);
  }

  public I18nSelectieDto getSelectie(String selectie, Long codeId) {
    Map<String, Object> params  = new HashMap<>();
    params.put(I18nSelectieDto.PAR_CODEID, codeId);
    params.put(I18nSelectieDto.PAR_SELECTIE, selectie);

    List<I18nSelectieDto> resultaat = namedQuery(I18nSelectieDto.QRY_METCODEID,
                                                 params);
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         I18nSelectieDto.QRY_METCODEID);
    }

    return resultaat.get(0);
  }

  public List<I18nSelectieDto> getSelecties(String selectie) {
    Map<String, Object> params  = new HashMap<>();
    params.put(I18nSelectieDto.PAR_SELECTIE, selectie);

    return namedQuery(I18nSelectieDto.QRY_SELECTIES, params);
  }
}
