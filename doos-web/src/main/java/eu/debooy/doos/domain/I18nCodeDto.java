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
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


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
  @Column(name="CODE", length=75, nullable=false, unique=true)
  private String  code;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=I18nCodeTekstDto.class, orphanRemoval=true)
  @JoinColumn(name="CODE_ID", referencedColumnName="CODE_ID", nullable=false, updatable=false, insertable=true)
  @MapKey(name="taalKode")
  private Map<String, I18nCodeTekstDto> teksten =
      new HashMap<String, I18nCodeTekstDto>();

  public I18nCodeDto() {}

  public I18nCodeDto(String code) {
    this.code = code;
  }

  /**
   * Voeg een I18nCodeTekst toe. Als de I18nCodeTekst al bestaat dan wordt die
   * overschreven.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  public void addTekst(I18nCodeTekstDto i18nCodeTekstDto) {
    //TODO Kijken voor 'de' JPA manier.
    if (null == i18nCodeTekstDto.getCodeId()) {
      i18nCodeTekstDto.setCodeId(codeId);
    }
    teksten.put(i18nCodeTekstDto.getTaalKode(), i18nCodeTekstDto);
  }

  public Object clone() throws CloneNotSupportedException {
    I18nCodeDto clone = (I18nCodeDto) super.clone();

    return clone;
  }

  public int compareTo(I18nCodeDto i18nCode) {
    return new CompareToBuilder().append(code, i18nCode.code)
                                 .toComparison();
  }

  public boolean containsTekst(String taalKode) {
    return teksten.containsKey(taalKode);
  }

  public boolean equals(Object object) {
    if (!(object instanceof I18nCodeDto)) {
      return false;
    }

    I18nCodeDto  i18nCode  = (I18nCodeDto) object;
    return new EqualsBuilder().append(code, i18nCode.code).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Long getCodeId() {
    return codeId;
  }

  public I18nCodeTekstDto getTekst(String taalKode) {
    if (teksten.containsKey(taalKode)) {
      return teksten.get(taalKode);
    } else {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE, taalKode);
    }
  }

  public Collection<I18nCodeTekstDto> getTeksten() {
    return teksten.values();
  }

  public int hashCode() {
    return new HashCodeBuilder().append(code).toHashCode();
  }

  /**
   * Verwijder een I18nCodeTekst.
   * 
   * @param I18nCodeTekstDto i18nCodeTekstDto
   */
  public void removeTekst(I18nCodeTekstDto i18nCodeTekstDto) {
    removeTekst(i18nCodeTekstDto.getTaalKode());
  }

  /**
   * Verwijder een I18nCodeTekst.
   * 
   * @param String taalKode
   */
  public void removeTekst(String taalKode) {
    if (teksten.containsKey(taalKode)) {
      teksten.remove(taalKode);
    } else {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE, taalKode);
    }
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setCodeId(Long codeId) {
    this.codeId = codeId;
  }

  public void setTeksten(Collection<I18nCodeTekstDto> teksten) {
    for (I18nCodeTekstDto tekst : teksten) {
      this.teksten.put(tekst.getTaalKode(), tekst);
    }
  }
}