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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_CODE_TEKSTEN", schema="DOOS")
public class I18nCodeTekstDto extends Dto
    implements Comparable<I18nCodeTekstDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @EmbeddedId
	private I18nCodeTekstPK id;
	@Column(name="TEKST", length=1024, nullable=false)
	private String          tekst;
  @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="CODE_ID", insertable=false, updatable=false)
  private I18nCodeDto   i18nCode;

	public I18nCodeTekstDto () {
		super();
		id  = new I18nCodeTekstPK();
	}

  /**
   * Constructor voor required fields
   */
  public I18nCodeTekstDto (I18nCodeDto i18nCode, String taalKode,
                           String tekst) {
    this.i18nCode = i18nCode;
    this.id       = new I18nCodeTekstPK(i18nCode.getCodeId(), taalKode);
    this.tekst    = tekst;
    this.i18nCode.getTeksten().add(this);
  }

  /**
   * @return de i18nCode
   */
  public I18nCodeDto getI18nCode() {
    return i18nCode;
  }

  /**
   * @return de id
   */
  public I18nCodeTekstPK getId() {
    return id;
  }

  /**
   * @return de tekst
   */
  public String getTekst() {
    return tekst;
  }

  /**
   * @param i18nCode de i18nCode
   */
  public void setI18nCode(I18nCodeDto i18nCode) {
    this.i18nCode = i18nCode;
  }

  /**
   * @param id de id
   */
  public void setId(I18nCodeTekstPK id) {
    this.id = id;
  }

  /**
   * @param tekst de tekst
   */
  public void setTekst(String tekst) {
    this.tekst  = tekst;
  }

  @Override
  public int compareTo(I18nCodeTekstDto i18nCodeWaarde) {
    return new CompareToBuilder().append(id.getTaalKode(),
                                         i18nCodeWaarde.id.getTaalKode())
                                 .append(tekst, i18nCodeWaarde.tekst)
                                 .toComparison();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    I18nCodeTekstDto  clone = (I18nCodeTekstDto) super.clone();

    return clone;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeTekstDto)) {
      return false;
    }
    I18nCodeTekstDto  i18nCodeWaarde  = (I18nCodeTekstDto) object;
    return new EqualsBuilder().append(id.getTaalKode(),
                                      i18nCodeWaarde.id.getTaalKode())
                              .append(tekst, i18nCodeWaarde.tekst)
                              .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id.getCodeId())
                                .append(id.getTaalKode())
                                .append(tekst).toHashCode();
  }
}