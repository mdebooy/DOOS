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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_LIJST_CODES", schema="DOOS")
@IdClass(I18nLijstCodePK.class)
public class I18nLijstCodeDto extends Dto
    implements Comparable<I18nLijstCodeDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Id
  @Column(name="CODE_ID", nullable=false)
  private Long    codeId;
  @Id
  @Column(name="LIJST_ID", nullable=false)
  private Long    lijstId;
	@Column(name="VOLGORDE", precision=3, nullable=false)
	private Integer volgorde;

	public I18nLijstCodeDto() {}

  public I18nLijstCodeDto(Long codeId, Long lijstId,
                          Integer volgorde) {
    this.codeId   = codeId;
    this.lijstId  = lijstId;
    this.volgorde = volgorde;
  }

  public Object clone() throws CloneNotSupportedException {
    I18nLijstCodeDto  clone = (I18nLijstCodeDto) super.clone();

    return clone;
  }

  public int compareTo(I18nLijstCodeDto i18nLijstCode) {
    return new CompareToBuilder().append(codeId, i18nLijstCode.codeId)
                                 .append(lijstId, i18nLijstCode.lijstId)
                                 .toComparison();
  }

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