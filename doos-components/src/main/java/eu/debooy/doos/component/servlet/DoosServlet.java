/**
 * Copyright 2018 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.component.servlet;

import eu.debooy.doos.component.I18nTeksten;
import eu.debooy.doos.component.Properties;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.CDI;
import javax.servlet.http.HttpServlet;


/**
 * @author Marco de Booij
 */
public class DoosServlet extends HttpServlet {
  private static final  long  serialVersionUID  = 1L;

  private static  I18nTeksten i18nTekst = null;
  private static  Properties  property  = null;

  private static I18nTeksten getI18nTekst() {
    if (null == i18nTekst) {
      i18nTekst = CDI.getBean(I18nTeksten.class);
    }

    return i18nTekst;
  }

  protected String getParameter(String parameter) {
    String  waarde;
    try {
      waarde  = getProperty().value(parameter);
    } catch (ObjectNotFoundException e) {
      return "";
    }

    return waarde;
  }

  private static Properties getProperty() {
    if (null == property) {
      property  = CDI.getBean(Properties.class);
    }

    return property;
  }

  protected String getTekst(String tekst) {
    return getI18nTekst().tekst(tekst);
  }
}
