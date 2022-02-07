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

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class QuartzjobPK implements Serializable {
  private static final  long  serialVersionUID  = 1L;

	private String groep;
	private String job;

  public QuartzjobPK() {}

  public QuartzjobPK(String groep, String job) {
    this.groep  = groep;
    this.job    = job;
  }

  @Override
   public boolean equals(Object object) {
     if (!(object instanceof QuartzjobPK)) {
       return false;
     }
     var quartzjobPK = (QuartzjobPK) object;
     return new EqualsBuilder().append(groep, quartzjobPK.groep)
                               .append(job, quartzjobPK.job)
                               .isEquals();
   }

  public String getCodeId() {
    return groep;
  }

  public String getTaalKode() {
    return job;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(groep).append(job).toHashCode();
  }

  public void setCodeId(String groep) {
    this.groep  = groep;
  }

  public void setTaalKode(String job) {
    this.job    = job.toLowerCase();
  }

  @Override
  public String toString() {
    return new StringBuilder().append("QuartzjobPK")
                              .append(" (groep=").append(groep)
                              .append(", job=").append(job)
                              .append(")").toString();
  }
}