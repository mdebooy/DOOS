/*
 * Copyright (c) 2022 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
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

import eu.debooy.doos.domain.TaalnaamDto;
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
public class Taalnaam
    extends Formulier implements Comparable<Taalnaam>, Serializable {
  private static final  long  serialVersionUID  = 1L;

  private String  iso6392t;
  private String  naam;
  private Long    taalId;

  public Taalnaam() {}

  public Taalnaam(TaalnaamDto taalnaamDto) {
    iso6392t  = taalnaamDto.getIso6392t();
    naam      = taalnaamDto.getNaam();
    taalId    = taalnaamDto.getTaalId();
  }

  public static class NaamComparator
      implements Comparator<Taalnaam>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(Taalnaam naam1, Taalnaam naam2) {
      return naam1.naam.compareTo(naam2.naam);
    }
  }

  @Override
  public int compareTo(Taalnaam taalnaam) {
    return new CompareToBuilder().append(iso6392t, taalnaam.iso6392t)
                                 .append(taalId, taalnaam.taalId)
                                 .toComparison();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Taalnaam)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var taalnaam = (Taalnaam) object;
    return new EqualsBuilder().append(taalId, taalnaam.taalId)
                              .append(iso6392t, taalnaam.iso6392t).isEquals();
  }

  public String getIso6392t() {
    return iso6392t;
  }

  public String getNaam() {
    return naam;
  }

  public Long getTaalId() {
    return taalId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(taalId).append(iso6392t).toHashCode();
  }

  public void persist(TaalnaamDto parameter) {
    parameter.setIso6392t(iso6392t);
    parameter.setNaam(naam);
    parameter.setTaalId(taalId);
  }

  public void setIso6392t(String iso6392t) {
    if (null == iso6392t) {
      this.iso6392t = null;
    } else {
      this.iso6392t = iso6392t.strip().toLowerCase();
    }
  }

  public void setNaam(String naam) {
    this.naam       = DoosUtils.strip(naam);
  }

  public void setTaalId(Long taalId) {
    this.taalId     = taalId;
  }
}
