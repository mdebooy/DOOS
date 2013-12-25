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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="I18N_CODES", schema="DOOS")
public class I18nCodeDto extends Dto
    implements Comparable<I18nCodeDto>, Cloneable {
  private static final  long  serialVersionUID  = 1L;

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="CODE_ID", nullable=false)
  private Long    codeId;
  @Column(name="CODE", length=100, nullable=false, unique=true)
  private String  code;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=I18nCodeTekstDto.class)
  @ElementJoinColumn(name="CODE_ID", nullable=false, updatable=false)
  private List<I18nCodeTekstDto>  teksten = new ArrayList<I18nCodeTekstDto>();

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
    return new CompareToBuilder().append(code, i18nCode.code)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeDto)) {
      return false;
    }

    I18nCodeDto  i18nCode  = (I18nCodeDto) object;
    return new EqualsBuilder().append(code, i18nCode.code).isEquals();
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
    return new HashCodeBuilder().append(code).toHashCode();
  }

  /**
   * Verwijder een I18nCodeTekst.
   * 
   * @param tekst
   */
  public void remove(I18nCodeTekstDto tekst) {
    if (getTeksten().contains(tekst)) {
      getTeksten().remove(tekst);
    }
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