/**
 * Copyright 2017 Marco de Booij
 *
 * Licensed under de EUPL, Version 1.1 or - as soon they will be approved by
 * de European Commission - subsequent versions of de EUPL (the "Licence");
 * you may not use this work except in compliance with de Licence. You may
 * obtain a copy of de Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under de Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See de Licence for de specific language governing permissions and
 * limitations under de Licence.
 */
package eu.debooy.doos.domain;

import eu.debooy.doosutils.domain.Dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.openjpa.persistence.ReadOnly;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_SELECTIES", schema="DOOS")
@NamedQueries({
  @NamedQuery(name="i18nSelectie", query="select s from I18nSelectieDto s where s.code=:code and s.selectie=:selectie"),
  @NamedQuery(name="i18nSelecties", query="select s from I18nSelectieDto s where s.selectie=:selectie")})
public class I18nSelectieDto extends Dto
    implements Comparable<I18nSelectieDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_CODE      = "code";
  public static final String  COL_CODEID    = "codeId";
  public static final String  COL_SELECTIE  = "selectie";
  public static final String  COL_VOLGORDE  = "volgorde";

  public static final String  PAR_CODE      = "code";
  public static final String  PAR_SELECTIE  = "selectie";

  public static final String  QRY_SELECTIE  = "i18nSelectie";
  public static final String  QRY_SELECTIES = "i18nSelecties";

  @Column(name="CODE", nullable=false)
  @ReadOnly
  private String  code;
  @Id
  @Column(name="CODE_ID", nullable=false)
  @ReadOnly
  private Long    codeId;
  @Column(name="SELECTIE", nullable=false)
  @ReadOnly
  private String  selectie;
  @Column(name="VOLGORDE", nullable=false)
  @ReadOnly
  private Integer volgorde;

  public I18nSelectieDto() {}

  @Override
  public int compareTo(I18nSelectieDto i18nSelectie) {
    return new CompareToBuilder().append(codeId, i18nSelectie.codeId)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nSelectieDto)) {
      return false;
    }
    I18nSelectieDto  i18nSelectie  = (I18nSelectieDto) object;
    return new EqualsBuilder().append(codeId, i18nSelectie.codeId).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getCodeId() {
    return codeId;
  }

  public String getSelectie() {
    return selectie;
  }

  public Integer getVolgorde() {
    return volgorde;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).toHashCode();
  }
}
