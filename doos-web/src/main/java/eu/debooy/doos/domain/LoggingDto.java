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
package eu.debooy.doos.domain;

import eu.debooy.doosutils.domain.Dto;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="LOGGING", schema="DOOS")
@NamedQuery(name="loggingCleanup",
            query="delete from LoggingDto l where l.logtime < :retentionDate")
@NamedQuery(name="loggingPerPackage", query="select l from LoggingDto l where l.loggerclass like :pkg")
public class LoggingDto extends Dto implements Comparable<LoggingDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_LOGGERCLASS   = "loggerclass";
  public static final String  COL_LOGID         = "logId";
  public static final String  COL_LOGTIME       = "logtime";
  public static final String  COL_LVL           = "lvl";
  public static final String  COL_MESSAGE       = "message";
  public static final String  COL_SEQ           = "seq";
  public static final String  COL_SOURCECLASS   = "sourceclass";
  public static final String  COL_SOURCEMETHOD  = "sourcemethod";
  public static final String  COL_THREADID      = "threadId";

  public static final String  PAR_PACKAGE       = "pkg";
  public static final String  PAR_RETENTIONDATE = "retentionDate";

  public static final String  QRY_CLEANUP       = "loggingCleanup";
  public static final String  QRY_PERPACKAGE    = "loggingPerPackage";

  @Column(name="LOGGER", length=100, nullable=false)
  private String    loggerclass;
  @Id
  @Column(name="LOG_ID", nullable=false)
  private Long      logId;
  @Column(name="LOGTIME", nullable=false)
  private Timestamp logtime;
  @Column(name="LVL", length=15, nullable=false)
  private String    lvl;
  @Column(name="MESSAGE", length=1024, nullable=false)
  private String    message;
  @Column(name="SEQ", nullable=false)
  private Long      seq;
  @Column(name="SOURCECLASS", length=100, nullable=false)
  private String    sourceclass;
  @Column(name="SOURCEMETHOD", length=100, nullable=false)
  private String    sourcemethod;
  @Column(name="THREAD_ID", nullable=false)
  private Long      threadId;

  @Override
  public int compareTo(LoggingDto logging) {
    return new CompareToBuilder().append(logtime, logging.logtime)
                                 .append(loggerclass, logging.loggerclass)
                                 .append(seq, logging.seq)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LoggingDto)) {
      return false;
    }
    LoggingDto  logging = (LoggingDto) object;
    return new EqualsBuilder().append(logtime, logging.logtime)
                              .append(loggerclass, logging.loggerclass)
                              .append(seq, logging.seq).isEquals();
  }

  public String getLoggerclass() {
    return loggerclass;
  }

  public Long getLogId() {
    return logId;
  }

  public Timestamp getLogtime() {
    return logtime;
  }

  public String getLvl() {
    return lvl;
  }

  public String getMessage() {
    return message;
  }

  public Long getSeq() {
    return seq;
  }

  public String getSourceclass() {
    return sourceclass;
  }

  public String getSourcemethod() {
    return sourcemethod;
  }

  public Long getThreadId() {
    return threadId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(logtime).append(loggerclass)
                                .append(seq).toHashCode();
  }

  public void setLoggerclass(String loggerclass) {
    this.loggerclass  = loggerclass;
  }

  public void setLogId(Long logId) {
    this.logId  = logId;
  }

  public void setLogtime(Timestamp logtime) {
    this.logtime  = logtime;
  }

  public void setLvl(String lvl) {
    this.lvl  = lvl;
  }

  public void setMessage(String message) {
    this.message  = message;
  }

  public void setSeq(Long seq) {
    this.seq  = seq;
  }

  public void setSourceclass(String sourceclass) {
    this.sourceclass  = sourceclass;
  }

  public void setSourcemethod(String sourcemethod) {
    this.sourcemethod = sourcemethod;
  }

  public void setThreadId(Long threadId) {
    this.threadId = threadId;
  }
}
