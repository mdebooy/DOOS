/**
 * Copyright (c) 2018 Marco de Booij
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
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.MailData;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosError;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.service.JNDI;
import java.util.Collection;
import java.util.Properties;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.mail.Message.RecipientType;
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
    var index   = 0;
    var result  = new InternetAddress[addresses.size()];
    for (var address : addresses) {
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

  @Override
  public void sendMail(MailData mailData) throws TechnicalException {
    LOGGER.info("Start sendMail");

    var props = new Properties();
    for (var key : MAILPROPS) {
      var prop  = getProperty().getProperty(key);
      if (DoosUtils.isNotBlankOrNull(prop)) {
        LOGGER.debug(String.format("%s: %s", key, prop));
        props.put(key, prop);
      }
    }

    var session = Session.getInstance(props);

    try {
      var msg = new MimeMessage(session);
      msg.setHeader("Content-Type", mailData.getContentType());

      // Set the FROM (REPLY-TO) address if present.
      var deflautFrom = getProperty().getProperty("default.mail.from");
      var from        = DoosUtils.nullToValue(mailData.getFrom(), deflautFrom);
      if (DoosUtils.isBlankOrNull(deflautFrom)) {
        msg.setFrom(new InternetAddress(from));
      } else {
        msg.setFrom(new InternetAddress(deflautFrom));
      }

      var mailUser  = getProperty().getProperty("default.mail.reply.to");
      if (DoosUtils.isBlankOrNull(mailUser)) {
        msg.setReplyTo(new InternetAddress[] {new InternetAddress(from)});
        LOGGER.debug(String.format("ReplyTo        : %s", from));
      } else {
        msg.setReplyTo(new InternetAddress[] {new InternetAddress(mailUser)});
        LOGGER.debug(String.format("ReplyTo        : %s", mailUser));
      }
      LOGGER.debug(String.format("From (na)      : %s", from));

      if (mailData.getToSize() + mailData.getCcSize()
          + mailData.getBccSize() == 0) {
        mailData.addTo(from);
        mailData.setSubject(String.format("No recipients for: %s",
                                          mailData.getSubject()));
        LOGGER.error(mailData.getSubject());
      }

      // Fill the TO addresses.
      if (mailData.getToSize() > 0) {
        LOGGER.debug(String.format("TO adressen    : %3d %s",
                                   mailData.getToSize(),
                                   mailData.getTo().values().toString()));
        msg.setRecipients(RecipientType.TO,
                          fillAddresses(mailData.getTo().values()));
      }

      // Fill the CC addresses if present.
      if (mailData.getCcSize() > 0) {
        LOGGER.debug(String.format("CC adressen    : %3d %s",
                                   mailData.getCcSize(),
                                   mailData.getCc().values().toString()));
        msg.setRecipients(RecipientType.CC,
                          fillAddresses(mailData.getCc().values()));
      }

      // Fill the BCC addresses if present.
      if (mailData.getBccSize() > 0) {
        LOGGER.debug(String.format("BCC adressen   : %3d %s",
                                   mailData.getBccSize(),
                                   mailData.getBcc().values().toString()));
        msg.setRecipients(RecipientType.BCC,
                          fillAddresses(mailData.getBcc().values()));
      }

      msg.setSubject(mailData.getSubject());
      msg.setSentDate(mailData.getSentDate());
      msg.setContent(mailData.getMessage(), mailData.getContentType());

      // Fill the HEADER if present.
      if (mailData.getHeaderSize() > 0) {
        var headers  = mailData.getHeader();
        for (String key : mailData.getHeader().keySet()) {
          msg.setHeader(key, headers.get(key));
        }
      }

      Transport.send(msg);
    } catch (MessagingException e) {
      throw new TechnicalException(DoosError.RUNTIME_EXCEPTION,
                                   DoosLayer.SYSTEM, "sendMail", e);
    }

    LOGGER.info("Einde sendMail");
  }
}