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
package eu.debooy.doos.component;

import eu.debooy.doos.business.TaalManager;
import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;


/**
 * @author Marco de Booij
 */
public class TaalComponent {
  private TaalManager taalManager;

  public void delete(TaalDto taal) {
    getTaalManager().deleteTaal(taal);
  }

  public Collection<TaalDto> getAll() {
    return getTaalManager().getAll();
  }

  public Collection<TaalDto> getAll(DoosFilter<TaalDto> filter) {
    return getTaalManager().getAll(filter);
  }

  private TaalManager getTaalManager() {
    if (null == taalManager) {
      taalManager = (TaalManager)
          new JNDI.JNDINaam().metBean(TaalManager.class).locate();
    }

    return taalManager;
  }

  public void insert(TaalDto taal) {
    getTaalManager().createTaal(taal);
  }

  public void update(TaalDto taal) {
    getTaalManager().updateTaal(taal);
  }
}
