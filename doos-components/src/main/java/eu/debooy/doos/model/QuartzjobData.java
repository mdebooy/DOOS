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
package eu.debooy.doos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class QuartzjobData implements Comparable<QuartzjobData>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private String  cron;
  private Date    endTime;
  private String  groep;
  private String  javaclass;
  private String  job;
  private Date    nextFireTime;
  private String  omschrijving;
  private Date    previousFireTime;
  private Date    startTime;

  public QuartzjobData() {}

  public QuartzjobData(QuartzjobData quartzjobData) {
    cron              = quartzjobData.getCron();
    endTime           = quartzjobData.getEndTime();
    groep             = quartzjobData.getGroep();
    javaclass         = quartzjobData.getJavaclass();
    job               = quartzjobData.getJob();
    nextFireTime      = quartzjobData.getNextFireTime();
    omschrijving      = quartzjobData.getOmschrijving();
    previousFireTime  = quartzjobData.getPreviousFireTime();
    startTime         = quartzjobData.getStartTime();
  }

  public QuartzjobData(String cron, String groep, String javaclass, String job,
                       String omschrijving) {
    this.cron         = cron;
    this.groep        = groep;
    this.javaclass    = javaclass;
    this.job          = job;
    this.omschrijving = omschrijving;
  }

  @Override
  public int compareTo(QuartzjobData quartzjob) {
    return new CompareToBuilder().append(groep, quartzjob.groep)
                                 .append(job, quartzjob.job)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof QuartzjobData)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var andere  = (QuartzjobData) object;
    return new EqualsBuilder().append(groep, andere.groep)
                              .append(job, andere.job).isEquals();
  }

  public String getCron() {
    return cron;
  }

  public Date getEndTime() {
    if (null == endTime) {
      return null;
    }

    return new Date(endTime.getTime());
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

  public Date getNextFireTime() {
    if (null == nextFireTime) {
      return null;
    }

    return new Date(nextFireTime.getTime());
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public Date getPreviousFireTime() {
    if (null == previousFireTime) {
      return null;
    }

    return new Date(previousFireTime.getTime());
  }

  public Date getStartTime() {
    if (null == startTime) {
      return null;
    }

    return new Date(startTime.getTime());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(groep).append(job).toHashCode();
  }

  public void setCron(String cron) {
    this.cron               = cron;
  }

  public void setEndTime(Date endTime) {
    if (null == endTime) {
      this.endTime          = null;
    } else {
      this.endTime          = new Date(endTime.getTime());
    }
  }

  public void setGroep(String groep) {
    this.groep              = groep;
  }

  public void setJavaclass(String javaclass) {
    this.javaclass          = javaclass;
  }

  public void setJob(String job) {
    this.job                = job;
  }

  public void setNextFireTime(Date nextFireTime) {
    if (null == nextFireTime) {
      this.nextFireTime     = null;
    } else {
      this.nextFireTime     = new Date(nextFireTime.getTime());
    }
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving       = omschrijving;
  }

  public void setPreviousFireTime(Date previousFireTime) {
    if (null == previousFireTime) {
      this.previousFireTime = null;
    } else {
      this.previousFireTime = new Date(previousFireTime.getTime());
    }
  }

  public void setStartTime(Date startTime) {
    if (null == startTime) {
      this.startTime        = null;
    } else {
      this.startTime        = new Date(startTime.getTime());
    }
  }
}
