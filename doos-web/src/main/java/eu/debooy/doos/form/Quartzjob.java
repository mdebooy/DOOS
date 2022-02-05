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
package eu.debooy.doos.form;

import eu.debooy.doos.domain.QuartzjobDto;
import eu.debooy.doosutils.form.Formulier;
import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Quartzjob
    extends Formulier implements Comparable<Quartzjob>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private String  cron;
  private String  groep;
  private String  javaclass;
  private String  job;
  private String  omschrijving;

  public Quartzjob() {}

  public Quartzjob(QuartzjobDto quartzjobDto) {
    cron          = quartzjobDto.getCron();
    groep         = quartzjobDto.getGroep();
    javaclass     = quartzjobDto.getJavaclass();
    job           = quartzjobDto.getJob();
    omschrijving  = quartzjobDto.getOmschrijving();
  }

  @Override
  public int compareTo(Quartzjob quartzjob) {
    return new CompareToBuilder().append(groep, quartzjob.groep)
                                 .append(job, quartzjob.job)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Quartzjob)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var andere  = (Quartzjob) object;
    return new EqualsBuilder().append(groep, andere.groep)
                              .append(job, andere.job).isEquals();
  }

  public String getCron() {
    return cron;
  }

  public String getGroep() {
    return groep;
  }

  public String getJavaclass() {
    return javaclass;
  }

  public String getJob() {
    return job;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(groep).append(job).toHashCode();
  }

  public void persist(QuartzjobDto parameter) {
    if (!new EqualsBuilder().append(cron,
                                    parameter.getCron()).isEquals()) {
      parameter.setCron(cron);
    }
    if (!new EqualsBuilder().append(groep,
                                    parameter.getGroep()).isEquals()) {
      parameter.setGroep(groep);
    }
    if (!new EqualsBuilder().append(javaclass,
                                    parameter.getJavaclass()).isEquals()) {
      parameter.setJavaclass(javaclass);
    }
    if (!new EqualsBuilder().append(job,
                                    parameter.getJob()).isEquals()) {
      parameter.setJob(job);
    }
    if (!new EqualsBuilder().append(omschrijving,
                                    parameter.getOmschrijving()).isEquals()) {
      parameter.setOmschrijving(omschrijving);
    }
  }

  public void setCron(String cron) {
    this.cron         = cron;
  }

  public void setGroep(String groep) {
    this.groep        = groep;
  }

  public void setJavaclass(String javaclass) {
    this.javaclass    = javaclass;
  }

  public void setJob(String job) {
    this.job          = job;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }
}
