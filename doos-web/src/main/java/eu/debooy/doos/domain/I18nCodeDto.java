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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_CODES", schema="DOOS")
// TODO Wanneer de bug OpenJPA-2196 is opgelost kan de allocationSize weer
// naar 1 en kan DOOS weer de owner van de sequence worden.
@SequenceGenerator(name="SEQ_CODE_ID", sequenceName="DOOS.SEQ_I18N_CODES", allocationSize=2)
public class I18nCodeDto extends Dto
    implements Comparable<I18nCodeDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CODE_ID")
  @Column(name="CODE_ID", nullable=false)
  private Long codeId;
  @Column(name="CODE", length=100, nullable=false, unique=true)
  private String  code;

  @OneToMany(fetch=FetchType.EAGER)
  @JoinColumn(name="CODE_ID")
  @OrderBy("id.taalKode ASC")
  private List<I18nCodeTekstDto>  teksten = new ArrayList<I18nCodeTekstDto>();

  // constructors
  public I18nCodeDto() {
  }

  /**
   * @param code
   */
  public I18nCodeDto(String code) {
    this.code = code;
  }

  /**
   * Voeg een I18nCodeTekst toe.
   * 
   * @param tekst
   */
  public void add(I18nCodeTekstDto tekst) {
    if (!getTeksten().contains(tekst)) {
      getTeksten().add(tekst);
    }
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    I18nCodeDto  clone = (I18nCodeDto) super.clone();

    return clone;
  }

  @Override
  public int compareTo(I18nCodeDto i18nCode) {
    return new CompareToBuilder().append(this.code, i18nCode.code)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeDto)) {
      return false;
    }
    I18nCodeDto  i18nCode  = (I18nCodeDto) object;
    return new EqualsBuilder().append(this.code, i18nCode.code).isEquals();
  }

  /**
   * @return het aantal I18nCodeTeksten van deze I18nCode
   */
  public int getAantalTalen() {
    return teksten.size();
  }

  /**
   * @return de code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return de codeId
   */
  public Long getCodeId() {
    return codeId;
  }

  /**
   * @return de teksten
   */
  public List<I18nCodeTekstDto> getTeksten() {
    return teksten;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.code).toHashCode();
  }

  /**
   * @param code de code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @param codeId de codeId
   */
  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }

  /**
   * @param teksten de teksten
   */
  public void setTeksten(List<I18nCodeTekstDto> teksten) {
    this.teksten = teksten;
  }
}