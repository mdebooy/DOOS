/**
 * Copyright 2019 Marco de Booij
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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IQuartz;
import eu.debooy.doos.form.Quartzjob;
import eu.debooy.doos.model.QuartzjobData;
import eu.debooy.doosutils.service.JNDI;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosQuartzService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class QuartzService implements IQuartz {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(PropertyService.class);

  private QuartzjobService  quartzjobService;

  public QuartzService() {
    LOGGER.debug("init QuartzService");
  }
  public Collection<QuartzjobData> getQuartzjobs(String groep) {
    Collection<QuartzjobData> quartzjobs  = new ArrayList<QuartzjobData>();
    Collection<Quartzjob>     rijen       =
        getQuartzjobService().getPerGroep(groep);
    for (Quartzjob rij : rijen) {
      quartzjobs.add(new QuartzjobData(rij.getCron(), rij.getGroep(),
                                       rij.getJavaclass(), rij.getJob(),
                                       rij.getOmschrijving()));
    }

    return quartzjobs;
  }

  private QuartzjobService getQuartzjobService() {
    if (null == quartzjobService) {
      quartzjobService  = (QuartzjobService)
          new JNDI.JNDINaam().metBean(QuartzjobService.class).locate();
    }

    return quartzjobService;
  }
}
