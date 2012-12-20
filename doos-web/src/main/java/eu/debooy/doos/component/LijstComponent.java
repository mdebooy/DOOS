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

import eu.debooy.doos.business.LijstManager;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;


/**
 * @author Marco de Booij
 */
public class LijstComponent {
  private LijstManager   lijstManager;

  public void delete(LijstDto lijst) {
    getLijstManager().deleteLijst(lijst);
  }

  public Collection<LijstDto> getAll() {
    return getLijstManager().getAll();
  }

  public Collection<LijstDto> getAll(DoosFilter<LijstDto> filter) {
    return getLijstManager().getAll(filter);
  }

  public LijstDto getLijst(String sleutel) {
    return getLijstManager().getLijst(sleutel);
  }

  private LijstManager getLijstManager() {
    if (null == lijstManager) {
      lijstManager = (LijstManager)
          new JNDI.JNDINaam().metBean(LijstManager.class).locate();
    }

    return lijstManager;
  }

  public void insert(LijstDto lijst) {
    getLijstManager().createLijst(lijst);
  }

  public void update(LijstDto lijst) {
    getLijstManager().updateLijst(lijst);
  }
}
