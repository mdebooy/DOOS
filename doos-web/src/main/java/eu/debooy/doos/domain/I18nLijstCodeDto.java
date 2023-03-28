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
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_LIJST_CODES", schema="DOOS")
@IdClass(I18nLijstCodePK.class)
public class I18nLijstCodeDto extends Dto
    implements Comparable<I18nLijstCodeDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_CODEID    = "codeId";
  public static final String  COL_LIJSTID   = "lijstId";
  public static final String  COL_VOLGORDE  = "volgorde";

  @Id
  @Column(name="CODE_ID", nullable=false)
  private Long    codeId;
  @Id
  @Column(name="LIJST_ID", nullable=false)
  private Long    lijstId;
	@Column(name="VOLGORDE", precision=3, nullable=false)
	private Integer volgorde;

  @Override
  public int compareTo(I18nLijstCodeDto i18nLijstCode) {
    return new CompareToBuilder().append(codeId, i18nLijstCode.codeId)
                                 .append(lijstId, i18nLijstCode.lijstId)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nLijstCodeDto)) {
      return false;
    }
    I18nLijstCodeDto  i18nLijstCode = (I18nLijstCodeDto) object;
    return new EqualsBuilder().append(codeId, i18nLijstCode.codeId)
                              .append(lijstId, i18nLijstCode.lijstId)
                              .isEquals();
  }

  public Long getCodeId() {
    return codeId;
  }

  public Long getLijstId() {
    return lijstId;
  }

  public Integer getVolgorde() {
    return volgorde;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(lijstId).toHashCode();
  }

  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }

  public void setLijstId(Long lijstId) {
    this.lijstId  = lijstId;
  }

  public void setVolgorde(Integer volgorde) {
    this.volgorde = volgorde;
  }
}