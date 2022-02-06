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
import eu.debooy.doos.component.business.ILogging;
import eu.debooy.doos.model.Logdata;
import eu.debooy.doosutils.PersistenceConstants;
import java.util.Collection;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named
@SessionScoped
public class Loggings extends DoosBean {
  private static final  long    serialVersionUID  = 1L;

  @EJB
  private ILogging  loggingBean;

  private Logdata   logdata;

  public Logdata getLogging() {
    return logdata;
  }

  public Collection<Logdata> getPackageLogging(String pkg) {
    return loggingBean.getPackageLogging(pkg);
  }

  public void retrieve(Long logId) {
    logdata = loggingBean.getLogdata(logId);
    setAktie(PersistenceConstants.RETRIEVE);
    setSubTitel("doos.titel.logging.retrieve");
    redirect(APP_LOG_REDIRECT);
  }
}
