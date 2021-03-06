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
package eu.debooy.doos.service;

import eu.debooy.doos.access.QuartzjobDao;
import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doos.domain.QuartzjobPK;
import eu.debooy.doos.form.Quartzjob;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosQuartzjobService")
@Lock(LockType.WRITE)
public class QuartzjobService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(QuartzjobService.class);

  @Inject
  private QuartzjobDao  quartzjobDao;

  public QuartzjobService() {
    LOGGER.debug("init QuartzjobService");
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(QuartzjobPK sleutel) {
    QuartzjobDto  quartzjob = quartzjobDao.getByPrimaryKey(sleutel);
    quartzjobDao.delete(quartzjob);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public Collection<Quartzjob> getPerGroep(String groep) {
    Collection<Quartzjob> quartzjobs  = new ArrayList<Quartzjob>();
    for (QuartzjobDto rij : quartzjobDao.getPerGroep(groep)) {
      quartzjobs.add(new Quartzjob(rij));
    }

    return quartzjobs;
  }

  public QuartzjobDto quartzjob(QuartzjobPK sleutel) {
    QuartzjobDto  quartzjob = quartzjobDao.getByPrimaryKey(sleutel);

    return quartzjob;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<Quartzjob> query() {
    Collection<Quartzjob>  quartzjobs = new ArrayList<Quartzjob>();
    for (QuartzjobDto rij : quartzjobDao.getAll()) {
      quartzjobs.add(new Quartzjob(rij));
    }

    return quartzjobs;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(Quartzjob quartzjob) {
    QuartzjobDto  dto = new QuartzjobDto();
    quartzjob.persist(dto);

    quartzjobDao.update(dto);
  }
}
