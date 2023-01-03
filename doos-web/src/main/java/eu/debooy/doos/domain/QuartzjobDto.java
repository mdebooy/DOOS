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
package eu.debooy.doos.domain;

import eu.debooy.doosutils.domain.Dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="QUARTZJOBS", schema="DOOS")
@IdClass(QuartzjobPK.class)
@NamedQuery(name="quartzjobPerGroep", query="select q from QuartzjobDto q where q.groep=:groep")
public class QuartzjobDto extends Dto implements Comparable<QuartzjobDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_CRON          = "cron";
  public static final String  COL_GROEP         = "groep";
  public static final String  COL_JAVACLASS     = "javaclass";
  public static final String  COL_JOB           = "job";
  public static final String  COL_OMSCHRIJVING  = "omschrijving";

  public static final String  PAR_GROEP = "groep";

  public static final String  QRY_GROEP = "quartzjobPerGroep";

  @Column(name="CRON", length=50, nullable=false)
  private String  cron;
  @Id
  @Column(name="GROEP", length=15, nullable=false)
  private String  groep;
  @Column(name="JAVACLASS", length=100, nullable=false)
  private String  javaclass;
  @Id
  @Column(name="JOB", length=15, nullable=false)
  private String  job;
  @Column(name="OMSCHRIJVING", length=100, nullable=false)
  private String  omschrijving;

  public QuartzjobDto() {}

  public QuartzjobDto(String cron, String groep, String javaclass, String job,
                      String omschrijving) {
    this.cron         = cron;
    this.groep        = groep;
    this.javaclass    = javaclass;
    this.job          = job;
    this.omschrijving = omschrijving;
  }

  public QuartzjobDto(QuartzjobDto quartzjob) {
    cron          = quartzjob.getCron();
    groep         = quartzjob.getGroep();
    javaclass     = quartzjob.getJavaclass();
    job           = quartzjob.getJob();
    omschrijving  = quartzjob.getOmschrijving();
  }

  @Override
  public int compareTo(QuartzjobDto quartzjob) {
    return new CompareToBuilder().append(groep, quartzjob.groep)
                                 .append(job, quartzjob.job)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof QuartzjobDto)) {
      return false;
    }
    QuartzjobDto  andere = (QuartzjobDto) object;
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
