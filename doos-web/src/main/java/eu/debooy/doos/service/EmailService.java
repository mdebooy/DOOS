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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IEmail;
import eu.debooy.doos.component.business.IProperty;
import eu.debooy.doos.model.MailData;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.service.JNDI;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("tempoEmail")
@Lock(LockType.WRITE)
public class EmailService implements IEmail {
  private static final Logger   LOGGER    =
      LoggerFactory.getLogger(EmailService.class);
  private static final String[] MAILPROPS = new String[] {
      "mail.smtp.host", "mail.smtp.port", "mail.smtp.username",
      "mail.smtp.password", "mail.smtp.auth", "mail.smtp.debug"};

  private IProperty propertyService = null;

  public EmailService() {
    LOGGER.debug("init EmailService");
  }

  private InternetAddress[] fillAddresses(Collection<String> addresses)
      throws AddressException {
    int             index     = 0;
    InternetAddress result[]  = new InternetAddress[addresses.size()];
    for (String address : addresses) {
      result[index]  = new InternetAddress(address);
      index++;
    }

    return result;
  }

  private IProperty getProperty() {
    if (null == propertyService) {
      propertyService = (IProperty)
          new JNDI.JNDINaam().metBean(PropertyService.class)
                  .metInterface(IProperty.class).locate();
    }

    return propertyService;
  }

  public void sendMail(MailData mailData) throws TechnicalException {
    LOGGER.info("Start sendMail");

    Properties  props = new Properties();
    for (String key : MAILPROPS) {
      String  prop  = getProperty().getProperty(key);
      if (DoosUtils.isNotBlankOrNull(prop)) {
        LOGGER.debug(key + ": " + prop);
        props.put(key, prop);
      }
    }

    Session session = Session.getInstance(props);
  
    try {
      MimeMessage msg = new MimeMessage(session);
      msg.setHeader("Content-Type", mailData.getContentType());

      // Set the FROM (REPLY-TO) address if present.
      String  deflautFrom = getProperty().getProperty("default.mail.from");
      String  from        = DoosUtils.nullToValue(mailData.getFrom(),
                                                  deflautFrom);
      if (DoosUtils.isBlankOrNull(deflautFrom)) {
        msg.setFrom(new InternetAddress(from));
      } else {
        msg.setFrom(new InternetAddress(deflautFrom));
      }

      String  mailUser  = getProperty().getProperty("default.mail.reply.to");
      if (DoosUtils.isBlankOrNull(mailUser)) {
        msg.setReplyTo(new InternetAddress[] {new InternetAddress(from)});
        LOGGER.debug("ReplyTo        : " + from);
      } else {
        msg.setReplyTo(new InternetAddress[] {new InternetAddress(mailUser)});
        LOGGER.debug("ReplyTo        : " + mailUser);
      }
      LOGGER.debug("From (na)      : " + from);

      if (mailData.getToSize() + mailData.getCcSize()
          + mailData.getBccSize() == 0) {
        mailData.addTo(from);
        mailData.setSubject("No recipients for: " + mailData.getSubject());
        LOGGER.error(mailData.getSubject());
      }

      // Fill the TO addresses.
      if (mailData.getToSize() > 0) {
        LOGGER.debug("TO adressen    : " + mailData.getToSize() + " "
                  + mailData.getTo().values().toString());
        msg.setRecipients(MimeMessage.RecipientType.TO,
                          fillAddresses(mailData.getTo().values()));
      }

      // Fill the CC addresses if present.
      if (mailData.getCcSize() > 0) {
        LOGGER.debug("CC adressen    : " + mailData.getCcSize() + " "
                  + mailData.getCc().values().toString());
        msg.setRecipients(MimeMessage.RecipientType.CC,
                          fillAddresses(mailData.getCc().values()));
      }

      // Fill the BCC addresses if present.
      if (mailData.getBccSize() > 0) {
        LOGGER.debug("BCC adressen   : " + mailData.getBccSize() + " "
                  + mailData.getBcc().values().toString());
        msg.setRecipients(MimeMessage.RecipientType.BCC,
                          fillAddresses(mailData.getBcc().values()));
      }

      msg.setSubject(mailData.getSubject());
      msg.setSentDate(mailData.getSentDate());
      msg.setContent(mailData.getMessage(), mailData.getContentType());

      // Fill the HEADER if present.
      if (mailData.getHeaderSize() > 0) {
        Map<String, String> headers  = mailData.getHeader();
        for (String key : mailData.getHeader().keySet()) {
          msg.setHeader(key, headers.get(key));
        }
      }

      Transport.send(msg);
    } catch (MessagingException e) {
      LOGGER.error("Sending mail error: [" + e.getMessage() + "]");
      LOGGER.error(mailData.toString());
      throw new TechnicalException(DoosError.RUNTIME_EXCEPTION,
                                   DoosLayer.SYSTEM, "sendMail", e);
    }

    LOGGER.info("Einde sendMail");
  }
}