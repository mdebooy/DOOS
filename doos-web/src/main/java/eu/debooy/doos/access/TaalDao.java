/**
 * Copyright 2010 Marco de Booij
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

import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.access.Dao;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.errorhandling.handler.interceptor.PersistenceExceptionHandlerInterceptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


/**
 * @author Marco de Booij
 */
@Interceptors({PersistenceExceptionHandlerInterceptor.class})
public class TaalDao extends Dao<TaalDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public TaalDao() {
    super(TaalDto.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public TaalDto getTaalIso6391(String iso6391) {
    Map<String, Object> params  = new HashMap<>();
    params.put(TaalDto.PAR_ISO6391, iso6391);

    List<TaalDto> resultaat = namedQuery(TaalDto.QRY_TAAL_ISO6391, params);
    if (resultaat.isEmpty()) {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                        TaalDto.QRY_TAAL_ISO6391 + "="
                                          + iso6391);
    }
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         TaalDto.QRY_TAAL_ISO6391 + "="
                                          + iso6391);
    }

    return resultaat.get(0);
  }

  public TaalDto getTaalIso6392b(String iso6392b) {
    Map<String, Object> params  = new HashMap<>();
    params.put(TaalDto.PAR_ISO6392B, iso6392b);

    List<TaalDto> resultaat = namedQuery(TaalDto.QRY_TAAL_ISO6392B, params);
    if (resultaat.isEmpty()) {
      var iso6392t  = getTaalIso6392t(iso6392b);
      if (DoosUtils.isBlankOrNull(iso6392t.getIso6392b())) {
        return iso6392t;
      }
    }
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         TaalDto.QRY_TAAL_ISO6392B + "="
                                          + iso6392b);
    }

    return resultaat.get(0);
  }

  public TaalDto getTaalIso6392t(String iso6392t) {
    Map<String, Object> params  = new HashMap<>();
    params.put(TaalDto.PAR_ISO6392T, iso6392t);

    List<TaalDto> resultaat = namedQuery(TaalDto.QRY_TAAL_ISO6392T, params);
    if (resultaat.isEmpty()) {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                        TaalDto.QRY_TAAL_ISO6392T + "="
                                          + iso6392t);
    }
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         TaalDto.QRY_TAAL_ISO6392T + "="
                                          + iso6392t);
    }

    return resultaat.get(0);
  }

  public TaalDto getTaalIso6393(String iso6393) {
    Map<String, Object> params  = new HashMap<>();
    params.put(TaalDto.PAR_ISO6393, iso6393);

    List<TaalDto> resultaat = namedQuery(TaalDto.QRY_TAAL_ISO6393, params);
    if (resultaat.isEmpty()) {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                        TaalDto.QRY_TAAL_ISO6393 + "="
                                          + iso6393);
    }
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         TaalDto.QRY_TAAL_ISO6393 + "="
                                          + iso6393);
    }

    return resultaat.get(0);
  }

  public Collection<TaalDto> getTalenIso6391() {
    return namedQuery(TaalDto.QRY_TALEN_ISO6391);
  }

  public Collection<TaalDto> getTalenIso6392b() {
    return namedQuery(TaalDto.QRY_TALEN_ISO6392B);
  }

  public Collection<TaalDto> getTalenIso6392t() {
    return namedQuery(TaalDto.QRY_TALEN_ISO6392T);
  }

  public Collection<TaalDto> getTalenIso6393() {
    return namedQuery(TaalDto.QRY_TALEN_ISO6393);
  }
}
