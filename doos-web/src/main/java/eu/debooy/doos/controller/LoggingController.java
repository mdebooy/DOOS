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
package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doos.form.Logging;
import eu.debooy.doosutils.PersistenceConstants;
import java.util.Collection;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named("doosLogging")
@SessionScoped
public class LoggingController extends Doos {
  private static final  long  serialVersionUID  = 1L;

  private Logging logging;

  public Logging getLogging() {
    return logging;
  }

  public Collection<Logging> getLoggings() {
    return getLoggingService().query();
  }

  public void retrieve(Long logId) {
    logging    = new Logging(getLoggingService().logging(logId));
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel("doos.titel.logging.retrieve");
    redirect(LOG_REDIRECT);
  }
}
