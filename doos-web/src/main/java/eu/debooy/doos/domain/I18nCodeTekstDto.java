/**
 * Copyright 2011 Marco de Booij
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_CODE_TEKSTEN", schema="DOOS")
@IdClass(I18nCodeTekstPK.class)
public class I18nCodeTekstDto extends Dto
    implements Comparable<I18nCodeTekstDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Id
  @Column(name="CODE_ID", nullable=false)
  private Long    codeId;
  @Id
  @OrderColumn
  @Column(name="TAAL_KODE", length=2, nullable=false)
  private String  taalKode;
	@Column(name="TEKST", length=1024, nullable=false)
	private String  tekst;

	public I18nCodeTekstDto() {}

  /**
   * Constructor voor required fields
   */
  public I18nCodeTekstDto(Long codeId, String taalKode,
                          String tekst) {
    this.codeId   = codeId;
    this.taalKode = taalKode;
    this.tekst    = tekst;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    I18nCodeTekstDto  clone = (I18nCodeTekstDto) super.clone();

    return clone;
  }

  @Override
  public int compareTo(I18nCodeTekstDto i18nCodeTekst) {
    return new CompareToBuilder().append(codeId, i18nCodeTekst.codeId)
                                 .append(taalKode, i18nCodeTekst.taalKode)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeTekstDto)) {
      return false;
    }
    I18nCodeTekstDto  i18nCodeWaarde  = (I18nCodeTekstDto) object;
    return new EqualsBuilder().append(codeId, i18nCodeWaarde.codeId)
                              .append(taalKode, i18nCodeWaarde.taalKode)
                              .isEquals();
  }

  /**
   * @return de codeId
   */
  public Long getCodeId() {
    return codeId;
  }

  /**
   * @return de id
   */
  public I18nCodeTekstPK getId() {
    return new I18nCodeTekstPK(codeId, taalKode);
  }

  /**
   * @return de taalKode
   */
  public String getTaalKode() {
    return taalKode;
  }

  /**
   * @return de tekst
   */
  public String getTekst() {
    return tekst;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(codeId).append(taalKode).toHashCode();
  }

  /**
   * @param codeId de codeId
   */
  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }

  /**
   * @param id de id
   */
  public void setId(I18nCodeTekstPK id) {
    codeId    = id.getCodeId();
    taalKode  = id.getTaalKode();
  }

  /**
   * @param taalKode de taalKode
   */
  public void setTaalKode(String taalKode) {
    this.taalKode = taalKode;
  }

  /**
   * @param tekst de tekst
   */
  public void setTekst(String tekst) {
    this.tekst  = tekst;
  }
}