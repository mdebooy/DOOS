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
package eu.debooy.doos.component;

import eu.debooy.doos.component.bean.DoosBean;
import eu.debooy.doos.component.business.IEmail;
import eu.debooy.doos.model.MailData;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class Email extends DoosBean {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(Email.class);

  @EJB
  private IEmail    emailBean;

  public void sendMail(MailData mailData) {
    try {
      emailBean.sendMail(mailData);
    } catch (ClassCastException | TechnicalException e) {
      LOGGER.error("errors.send.email", e.getMessage());
      addError("errors.send.email", e.getLocalizedMessage());
      return;
    }

    addInfo("mail.send.ok", String.join(", ", mailData.getTo().values()));
  }
}
