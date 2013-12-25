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

import eu.debooy.doos.business.I18nCodeManager;
import eu.debooy.doos.business.I18nTekstManager;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doosutils.components.business.II18nTekst;
import eu.debooy.doosutils.domain.DoosFilter;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;


/**
 * @author Marco de Booij
 */
public class I18nCodeComponent {
  private I18nCodeManager   i18nCodeManager;
  private II18nTekst        i18nTekstManager;

  public void delete(I18nCodeDto i18nCode) {
    getI18nCodeManager().deleteI18nCode(i18nCode);
  }

  public void delete(I18nCodeTekstDto i18nCodeTekst) {
    getI18nCodeManager().deleteI18nCodeTekst(i18nCodeTekst);
  }

  public Collection<I18nCodeDto> getAll() {
    return getI18nCodeManager().getAll();
  }

  public Collection<I18nCodeDto> getAll(DoosFilter<I18nCodeDto> filter) {
    return getI18nCodeManager().getAll(filter);
  }

  public I18nCodeDto getI18nCode(String sleutel) {
    return getI18nCodeManager().getI18nCode(sleutel);
  }

  public int getI18nTeksten() {
    return getI18nTekstManager().size();
  }

  public I18nCodeTekstDto getI18nCodeTekst(Long codeId, String taalKode) {
    return getI18nCodeManager().getI18nCodeTekst(codeId, taalKode);
  }

  private I18nCodeManager getI18nCodeManager() {
    if (null == i18nCodeManager) {
      i18nCodeManager = (I18nCodeManager)
          new JNDI.JNDINaam().metBean(I18nCodeManager.class).locate();
    }

    return i18nCodeManager;
  }

  private II18nTekst getI18nTekstManager() {
    if (null == i18nTekstManager) {
      i18nTekstManager  = (II18nTekst)
          new JNDI.JNDINaam().metBean(I18nTekstManager.class)
                             .metInterface(II18nTekst.class).locate();
    }

    return i18nTekstManager;
  }

  public void insert(I18nCodeDto i18nCode) {
    getI18nCodeManager().createI18nCode(i18nCode);
  }

  public void insert(I18nCodeTekstDto i18nCodeTekst) {
    getI18nCodeManager().createI18nCodeTekst(i18nCodeTekst);
  }

  public void clearI18nTeksten() {
    getI18nTekstManager().clear();
  }

  public void update(I18nCodeDto i18nCode) {
    getI18nCodeManager().updateI18nCode(i18nCode);
  }

  public void update(I18nCodeTekstDto i18nCodeTekst) {
    getI18nCodeManager().updateI18nCodeTekst(i18nCodeTekst);
  }
}
