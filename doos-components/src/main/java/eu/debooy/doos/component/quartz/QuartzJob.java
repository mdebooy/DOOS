/**
 * Copyright 2019 Marco de Booij
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
package eu.debooy.doos.component.quartz;

import eu.debooy.doos.component.business.IEmail;
import eu.debooy.doos.component.business.II18nTekst;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doosutils.components.Applicatieparameter;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.service.JNDI;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.openejb.quartz.Job;
import org.apache.openejb.quartz.JobExecutionContext;
import org.apache.openejb.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class QuartzJob implements Job {
  private static final Logger LOGGER  =
      LoggerFactory.getLogger(QuartzJob.class);

  private IEmail      email     = null;
  private II18nTekst  i18nTekst = null;
  private IProperty   property  = null;
  private String      language  = "";

  public QuartzJob() {
    language  = getParameter("default.taal");
  }

  public void execute(JobExecutionContext context)
      throws JobExecutionException {
    throw new JobExecutionException("Implement execute method.");
  }

  protected IEmail getEmailService() {
    if (null == email) {
      email = (IEmail) new JNDI.JNDINaam()
                               .metBeanNaam("EmailService")
                               .metInterface(IEmail.class)
                               .metAppNaam("doos")
                               .locate();
    }

    return email;
  }

  private II18nTekst getI18nTekst() {
    if (null == i18nTekst) {
      i18nTekst = (II18nTekst) new JNDI.JNDINaam()
                                       .metBeanNaam("I18nTekstManager")
                                       .metInterface(II18nTekst.class)
                                       .metAppNaam("doos")
                                       .locate();
    }

    return i18nTekst;
  }

  protected String getParameter(String parameter) {
    String  waarde;
    try {
      waarde  = getProperty().getProperty(parameter);
    } catch (ObjectNotFoundException e) {
      LOGGER.error("errors.notfound.parameter", parameter);
      return "";
    }

    return waarde;
  }

  protected Map<String, String> getParameters(String prefix) {
    Map<String, String>       parameters  = new HashMap<String, String>();
    List<Applicatieparameter> rows;
    try {
      rows  = getProperty().getProperties(prefix);
      for (Applicatieparameter row : rows) {
        parameters.put(row.getSleutel(), row.getWaarde());
      }
    } catch (ObjectNotFoundException e) {
      // Just return the empty ArrayList.
    }

    return parameters;
  }

  private IProperty getProperty() {
    if (null == property) {
      property  = (IProperty) new JNDI.JNDINaam()
                                      .metBeanNaam("PropertyService")
                                      .metInterface(IProperty.class)
                                      .metAppNaam("doos")
                                      .locate();
    }

    return property;
  }

  protected String getTekst(String code, Object... params) {
    String  tekst = getI18nTekst().getI18nTekst(code, language);

    if (null == params) {
      return tekst;
    }

    MessageFormat formatter = new MessageFormat(tekst);

    return formatter.format(params);
  }
}
