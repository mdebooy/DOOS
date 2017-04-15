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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_LIJSTEN", schema="DOOS")
public class I18nLijstDto extends Dto
    implements Comparable<I18nLijstDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Column(name="CODE", length=100, nullable=false)
  private String  code;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="LIJST_ID", nullable=false)
  private Long    lijstId;
	@Column(name="OMSCHRIJVING", length=200, nullable=false)
	private String  omschrijving;

	public I18nLijstDto() {}

  /**
   * Constructor voor required fields
   */
  public I18nLijstDto(String code,Long lijstId, String omschrijving) {
    this.code         = code;
    this.lijstId      = lijstId;
    this.omschrijving = omschrijving;
  }

  public Object clone() throws CloneNotSupportedException {
    I18nLijstDto  clone = (I18nLijstDto) super.clone();

    return clone;
  }

  public int compareTo(I18nLijstDto i18nCodeTekst) {
    return new CompareToBuilder().append(lijstId, i18nCodeTekst.lijstId)
                                 .toComparison();
  }

  public boolean equals(Object object) {
    if (!(object instanceof I18nLijstDto)) {
      return false;
    }
    I18nLijstDto  i18nCodeWaarde  = (I18nLijstDto) object;
    return new EqualsBuilder().append(lijstId, i18nCodeWaarde.lijstId)
                              .isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getLijstId() {
    return lijstId;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public int hashCode() {
    return new HashCodeBuilder().append(lijstId).toHashCode();
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setLijstId(Long lijstId) {
    this.lijstId = lijstId;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }
}