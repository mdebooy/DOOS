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
package eu.debooy.doos.form;

import eu.debooy.doos.domain.LoggingDto;
import eu.debooy.doosutils.form.Formulier;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Logging extends Formulier implements Comparable<Logging> {
  private static final  long  serialVersionUID  = 1L;

  private String    loggerclass;
  private Long      logId;
  private Timestamp logtime;
  private String    lvl;
  private String    message;
  private Long      seq;
  private String    sourceclass;
  private String    sourcemethod;
  private Long      threadId;

  public Logging() {}

  public Logging(LoggingDto loggingDto) {
    loggerclass   = loggingDto.getLoggerclass();
    logId         = loggingDto.getLogId();
    logtime       = loggingDto.getLogtime();
    lvl           = loggingDto.getLvl();
    message       = loggingDto.getMessage();
    seq           = loggingDto.getSeq();
    sourceclass   = loggingDto.getSourceclass();
    sourcemethod  = loggingDto.getSourcemethod();
    threadId      = loggingDto.getThreadId();
  }

  @Override
  public int compareTo(Logging logging) {
    return new CompareToBuilder().append(logtime, logging.getLogtime())
                                 .append(loggerclass, logging.getLoggerclass())
                                 .append(seq, logging.getSeq())
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LoggingDto)) {
      return false;
    }
    LoggingDto  logging = (LoggingDto) object;
    return new EqualsBuilder().append(logtime, logging.getLogtime())
                              .append(loggerclass, logging.getLoggerclass())
                              .append(seq, logging.getSeq()).isEquals();
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
    this.threadId  = threadId;
  }
}
