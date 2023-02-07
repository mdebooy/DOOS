/**
 * Copyright 2016 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.form;

import eu.debooy.doos.domain.TaalDto;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.form.Formulier;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class Taal extends Formulier implements Comparable<Taal> {
  private static final  long  serialVersionUID  = 1L;

  private String  eigennaam;
  private String  iso6391;
  private String  iso6392b;
  private String  iso6392t;
  private String  iso6393;
  private boolean levend    = true;
  private String  naam;
  private Long    taalId;

  public Taal() {}

  public Taal(Taal taal) {
    eigennaam = taal.getEigennaam();
    iso6391   = taal.getIso6391();
    iso6392b  = taal.getIso6392b();
    iso6392t  = taal.getIso6392t();
    iso6393   = taal.getIso6393();
    levend    = taal.getLevend();
    naam      = taal.getNaam();
    taalId    = taal.getTaalId();
  }

  public Taal(TaalDto taalDto) {
    this(taalDto, null);
  }

  public Taal(TaalDto taalDto, String taal) {
    eigennaam = taalDto.getEigennaam();
    iso6391   = taalDto.getIso6391();
    iso6392b  = taalDto.getIso6392b();
    iso6392t  = taalDto.getIso6392t();
    iso6393   = taalDto.getIso6393();
    levend    = taalDto.getLevend();
    if (DoosUtils.isNotBlankOrNull(taal)) {
      naam        = taalDto.getNaam(taal);
    }
    taalId    = taalDto.getTaalId();
  }

  public static class EigennaamComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Taal taal1, Taal taal2) {
      return new CompareToBuilder().append(taal1.eigennaam, taal2.eigennaam)
                                   .toComparison();
    }
  }

  public static class NaamComparator
      implements Comparator<Taal>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Taal taal1, Taal taal2) {
      return new CompareToBuilder().append(taal1.naam, taal2.naam)
                                   .toComparison();
    }
  }

  @Override
  public int compareTo(Taal andere) {
    return new CompareToBuilder().append(iso6392t, andere.iso6392t)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Taal)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var andere  = (Taal) object;
    return new EqualsBuilder().append(taalId, andere.taalId).isEquals();
  }

  public String getEigennaam() {
    return eigennaam;
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
    return levend;
  }

  public String getNaam() {
    return (DoosUtils.isBlankOrNull(naam) ? iso6392t : naam);
  }

  public Long getTaalId() {
    return taalId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(taalId).toHashCode();
  }

  public boolean isLevend() {
    return getLevend();
  }

  public void persist(TaalDto parameter) {
    if (!new EqualsBuilder().append(iso6391,
                                    parameter.getIso6391()).isEquals()) {
      parameter.setIso6391(iso6391);
    }
    if (!new EqualsBuilder().append(iso6392b,
                                    parameter.getIso6392b()).isEquals()) {
      parameter.setIso6392b(iso6392b);
    }
    if (!new EqualsBuilder().append(iso6392t,
                                    parameter.getIso6392t()).isEquals()) {
      parameter.setIso6392t(iso6392t);
    }
    if (!new EqualsBuilder().append(iso6393,
                                    parameter.getIso6393()).isEquals()) {
      parameter.setIso6393(iso6393);
    }
    if (!new EqualsBuilder().append(levend,
                                    parameter.getLevend()).isEquals()) {
      parameter.setLevend(levend);
    }
    if (!new EqualsBuilder().append(taalId,
                                    parameter.getTaalId()).isEquals()) {
      parameter.setTaalId(taalId);
    }
  }

  public void setEigennaam(String eigennaam) {
    this.eigennaam  = eigennaam;
  }

  public void setIso6391(String iso6391) {
    if (null == iso6391) {
      this.iso6391  = null;
      return;
    }

    this.iso6391    = iso6391.toLowerCase();
  }

  public void setIso6392b(String iso6392b) {
    if (null == iso6392b) {
      this.iso6392b = null;
      return;
    }

    this.iso6392b   = iso6392b.toLowerCase();
  }

  public void setIso6392t(String iso6392t) {
    if (null == iso6392t) {
      this.iso6392t = null;
      return;
    }

    this.iso6392t   = iso6392t.toLowerCase();
  }

  public void setIso6393(String iso6393) {
    if (null == iso6393) {
      this.iso6393  = null;
      return;
    }

    this.iso6393    = iso6393.toLowerCase();
  }

  public void setLevend(boolean levend) {
    this.levend     = levend;
  }

  public void setNaam(String naam) {
    this.naam       = naam;
  }

  public void setTaalId(Long taalId) {
    this.taalId   = taalId;
  }
}
