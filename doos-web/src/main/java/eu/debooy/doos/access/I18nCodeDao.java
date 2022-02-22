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

import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doosutils.access.Dao;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


/**
 * @author Marco de Booij
 */
public class I18nCodeDao extends Dao<I18nCodeDto> {
  @PersistenceContext(unitName="doos", type=PersistenceContextType.TRANSACTION)
  private EntityManager em;

  public I18nCodeDao() {
    super(I18nCodeDto.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public I18nCodeDto getI18nCode(String code) {
    Map<String, Object> params  = new HashMap<>();
    params.put(I18nCodeDto.PAR_CODE, code);

    List<I18nCodeDto> resultaat = namedQuery(I18nCodeDto.QRY_CODE, params);
    if (resultaat.isEmpty()) {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE,
                                        I18nCodeDto.QRY_CODE + "="
                                          + code);
    }
    if (resultaat.size() != 1) {
      throw new DuplicateObjectException(DoosLayer.PERSISTENCE,
                                         I18nCodeDto.QRY_CODE + "="
                                          + code);
    }

    return resultaat.get(0);
  }
}
