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
import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doos.form.Logging;
import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named("doosLogging")
@SessionScoped
public class LoggingController extends Doos {
  private static final  long  serialVersionUID  = 1L;

  private static final  String  LBL_LOGGING   = "label.logging";
  private static final  String  TIT_RETRIEVE  = "doos.titel.logging.retrieve";

  private Logging logging;

  public Logging getLogging() {
    return logging;
  }

  public void retrieve() {
    if (!isGerechtigd()) {
      addError(ComponentsConstants.GEENRECHTEN);
      return;
    }

    var ec      = FacesContext.getCurrentInstance().getExternalContext();

    if (!ec.getRequestParameterMap().containsKey(LoggingDto.COL_LOGID)) {
      addError(ComponentsConstants.GEENPARAMETER, LoggingDto.COL_LOGID);
      return;
    }

    var logId   = Long.valueOf(ec.getRequestParameterMap()
                               .get(LoggingDto.COL_LOGID));

    try {
      logging   = new Logging(getLoggingService().logging(logId));
      setAktie(PersistenceConstants.RETRIEVE);
      setSubTitel(getTekst(TIT_RETRIEVE));
      redirect(LOG_REDIRECT);
    } catch (ObjectNotFoundException e) {
      addError(PersistenceConstants.NOTFOUND, LBL_LOGGING);
    }
  }
}
