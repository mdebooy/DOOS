/**
 * Copyright 2009 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.domain;

import eu.debooy.doosutils.ComponentsConstants;
import eu.debooy.doosutils.DoosConstants;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.domain.Dto;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
@Entity
@Table(name="TALEN", schema="DOOS")
@NamedQuery(name="taalIso6391",
            query="select t from TaalDto t where t.iso6391=:iso6391")
@NamedQuery(name="taalIso6392b",
            query="select t from TaalDto t where t.iso6392b=:iso6392b")
@NamedQuery(name="taalIso6392t",
            query="select t from TaalDto t where t.iso6392t=:iso6392t")
@NamedQuery(name="taalIso6393",
            query="select t from TaalDto t where t.iso6393=:iso6393")
@NamedQuery(name="talenIso6391",
            query="select t from TaalDto t where t.iso6391 is not null")
@NamedQuery(name="talenIso6392b",
            query="select t from TaalDto t where t.iso6392b is not null")
@NamedQuery(name="talenIso6392t",
            query="select t from TaalDto t where t.iso6392t is not null")
@NamedQuery(name="talenIso6393",
            query="select t from TaalDto t where t.iso6393 is not null")
@NamedQuery(name="talenLevend",
            query="select t from TaalDto t where t.levend='J'")
@NamedQuery(name="talenNietLevend",
            query="select t from TaalDto t where t.levend='N'")
public class TaalDto extends Dto implements Comparable<TaalDto> {
  private static final  long  serialVersionUID  = 1L;

  public static final String  COL_ISO6391   = "iso6391";
  public static final String  COL_ISO6392B  = "iso6392b";
  public static final String  COL_ISO6392T  = "iso6392t";
  public static final String  COL_ISO6393   = "iso6393";
  public static final String  COL_LEVEND    = "levend";
  public static final String  COL_TAALID    = "taalId";

  public static final String  PAR_ISO6391   = "iso6391";
  public static final String  PAR_ISO6392B  = "iso6392b";
  public static final String  PAR_ISO6392T  = "iso6392t";
  public static final String  PAR_ISO6393   = "iso6393";

  public static final String  QRY_TAAL_ISO6391      = "taalIso6391";
  public static final String  QRY_TAAL_ISO6392B     = "taalIso6392b";
  public static final String  QRY_TAAL_ISO6392T     = "taalIso6392t";
  public static final String  QRY_TAAL_ISO6393      = "taalIso6393";
  public static final String  QRY_TALEN_ISO6391     = "talenIso6391";
  public static final String  QRY_TALEN_ISO6392B    = "talenIso6392b";
  public static final String  QRY_TALEN_ISO6392T    = "talenIso6392t";
  public static final String  QRY_TALEN_ISO6393     = "talenIso6393";
  public static final String  QRY_TALEN_LEVEND      = "talenLevend";
  public static final String  QRY_TALEN_NIETLEVEND  = "talenNietLevend";

  @Column(name="ISO_639_1", length=2)
  private String  iso6391;
  @Column(name="ISO_639_2B", length=3)
  private String  iso6392b;
  @Column(name="ISO_639_2T", length=3, nullable=false)
  private String  iso6392t;
  @Column(name="ISO_639_3", length=3)
  private String  iso6393;
  @Column(name="LEVEND", length=1)
  private String  levend    = DoosConstants.WAAR;
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="TAAL_ID", nullable=false)
  private Long    taalId;

  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=TaalnaamDto.class, orphanRemoval=true)
  @JoinColumn(name="TAAL_ID", referencedColumnName="TAAL_ID", nullable=false, updatable=false, insertable=true)
  @MapKey(name="iso6392t")
  private Map<String, TaalnaamDto>  taalnamen = new HashMap<>();

  public static class NaamComparator
      implements Comparator<TaalDto>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    private String  taal  = ComponentsConstants.DEF_TAAL;

    public void setTaal(String taal) {
      this.taal = taal;
    }

    @Override
    public int compare(TaalDto taal1, TaalDto taal2) {
      return new CompareToBuilder().append(taal1.getNaam(taal),
                                           taal2.getNaam(taal))
                                   .append(taal1.getIso6392t(),
                                           taal2.getIso6392t())
                                   .toComparison();
    }
  }

  public void addNaam(TaalnaamDto taalnaamDto) {
    if (null == taalnaamDto.getTaalId()
        && null != taalId) {
      taalnaamDto.setTaalId(taalId);
    }
    if (!new EqualsBuilder().append(taalId, taalnaamDto.getTaalId())
                            .isEquals()) {
      var message = new StringBuilder().append("TaalId taalnaam (")
                                       .append(taalnaamDto.getTaalId())
                                       .append(") komt niet overeen met ")
                                       .append(taalId).toString();
      throw new IllegalArgumentException(DoosLayer.PERSISTENCE, message);
    }

    taalnamen.put(taalnaamDto.getIso6392t(), taalnaamDto);
  }

  @Override
  public int compareTo(TaalDto taal) {
    return new CompareToBuilder().append(iso6392t, taal.iso6392t)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TaalDto)) {
      return false;
    }
    var andere = (TaalDto) object;
    return new EqualsBuilder().append(taalId, andere.taalId).isEquals();
  }

  @Transient
  public String getEigennaam() {
    return getNaam(iso6392t);
  }

  public String getIso6391() {
    return iso6391;
  }

  public String getIso6392b() {
    return iso6392b;
  }

  public String getIso6392t() {
    return iso6392t;
  }

  public String getIso6393() {
    return iso6393;
  }

  public boolean getLevend() {
    return levend.equals(DoosConstants.WAAR);
  }

  @Transient
  public String getNaam(String iso6392t) {
    if (taalnamen.containsKey(iso6392t)) {
      return taalnamen.get(iso6392t).getNaam();
    } else {
      return "";
    }
  }

  public Long getTaalId() {
    return taalId;
  }

  public TaalnaamDto getTaalnaam(String iso6392t) {
    if (taalnamen.containsKey(iso6392t)) {
      return taalnamen.get(iso6392t);
    } else {
      return new TaalnaamDto();
    }
  }

  public Collection<TaalnaamDto> getTaalnamen() {
    return taalnamen.values();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(taalId).toHashCode();
  }

  @Transient
  public boolean hasTaalnaam(String iso6392t) {
    return taalnamen.containsKey(iso6392t);
  }

  public boolean isLevend() {
    return getLevend();
  }

  public void removeTaalnaam(String iso6392t) {
    if (taalnamen.containsKey(iso6392t)) {
      taalnamen.remove(iso6392t);
    } else {
      throw new ObjectNotFoundException(DoosLayer.PERSISTENCE, iso6392t);
    }
  }

  public void setIso6391(String iso6391) {
    this.iso6391    = DoosUtils.stripToLowerCase(iso6391);
  }

  public void setIso6392b(String iso6392b) {
    this.iso6392b   = DoosUtils.stripToLowerCase(iso6392b);
  }

  public void setIso6392t(String iso6392t) {
    this.iso6392t   = DoosUtils.stripToLowerCase(iso6392t);
  }

  public void setIso6393(String iso6393) {
    this.iso6393    = DoosUtils.stripToLowerCase(iso6393);
  }

  public void setLevend(boolean levend) {
    this.levend     = levend ? DoosConstants.WAAR : DoosConstants.ONWAAR;
  }

  public void setTaalId(Long taalId) {
    this.taalId     = taalId;
  }
}
