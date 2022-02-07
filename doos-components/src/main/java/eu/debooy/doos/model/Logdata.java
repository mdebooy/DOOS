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
package eu.debooy.doos.model;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public final class Logdata implements Comparable<Logdata>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private final String    loggerclass;
  private final Long      logId;
  private final Timestamp logtime;
  private final String    lvl;
  private final String    message;
  private final Long      seq;
  private final String    sourceclass;
  private final String    sourcemethod;
  private final Long      threadId;

  private Logdata(Builder builder) {
    loggerclass   = builder.getLoggerclass();
    logId         = builder.getLogId();
    logtime       = builder.getLogtime();
    lvl           = builder.getLvl();
    message       = builder.getMessage();
    seq           = builder.getSeq();
    sourceclass   = builder.getSourceclass();
    sourcemethod  = builder.getSourcemethod();
    threadId      = builder.getThreadId();
  }

  public static final class Builder {
    private String    loggerclass;
    private Long      logId;
    private Timestamp logtime;
    private String    lvl;
    private String    message;
    private Long      seq;
    private String    sourceclass;
    private String    sourcemethod;
    private Long      threadId;

    public Logdata build() {
      return new Logdata(this);
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

    public Builder setLoggerclass(String loggerclass) {
      this.loggerclass  = loggerclass;
      return this;
    }

    public Builder setLogId(Long logId) {
      this.logId  = logId;
      return this;
    }

    public Builder setLogtime(Timestamp logtime) {
      this.logtime  = logtime;
      return this;
    }

    public Builder setLvl(String lvl) {
      this.lvl  = lvl;
      return this;
    }

    public Builder setMessage(String message) {
      this.message  = message;
      return this;
    }

    public Builder setSeq(Long seq) {
      this.seq  = seq;
      return this;
    }

    public Builder setSourceclass(String sourceclass) {
      this.sourceclass  = sourceclass;
      return this;
    }

    public Builder setSourcemethod(String sourcemethod) {
      this.sourcemethod = sourcemethod;
      return this;
    }

    public Builder setThreadId(Long threadId) {
      this.threadId  = threadId;
      return this;
    }
  }

  @Override
  public int compareTo(Logdata logging) {
    return new CompareToBuilder().append(logtime, logging.getLogtime())
                                 .append(loggerclass, logging.getLoggerclass())
                                 .append(seq, logging.getSeq())
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Logdata)) {
      return false;
    }
    var logging = (Logdata) object;
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
}
