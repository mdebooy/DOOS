/**
 * Copyright 2017 Marco de Booij
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
package eu.debooy.doos.model;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Marco de Booij
 */
public class I18nSelectItem implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private String  code;
  private Integer volgorde;
  private String  waarde;

  public I18nSelectItem(String code, Integer volgorde, String waarde) {
    super();
    this.code     = code;
    this.volgorde = volgorde;
    this.waarde   = waarde;
  }

  public static class CodeComparator
      implements Comparator<I18nSelectItem>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(I18nSelectItem i18nSelectItemDto1,
                       I18nSelectItem i18nSelectItemDto2) {
      return new CompareToBuilder().append(i18nSelectItemDto1.getVolgorde(),
                                           i18nSelectItemDto2.getVolgorde())
                                   .append(i18nSelectItemDto1.getCode(),
                                           i18nSelectItemDto2.getCode())
                                   .toComparison();
    }
  }

  public static class WaardeComparator
      implements Comparator<I18nSelectItem>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(I18nSelectItem i18nSelectItemDto1,
                       I18nSelectItem i18nSelectItemDto2) {
      return new CompareToBuilder().append(i18nSelectItemDto1.getVolgorde(),
                                           i18nSelectItemDto2.getVolgorde())
                                   .append(i18nSelectItemDto1.getWaarde(),
                                           i18nSelectItemDto2.getWaarde())
                                   .append(i18nSelectItemDto1.getCode(),
                                           i18nSelectItemDto2.getCode())
                                   .toComparison();
    }
  }

  public static class VolgordeComparator
      implements Comparator<I18nSelectItem>, Serializable {
    private static final  long  serialVersionUID  = 1L;

    @Override
    public int compare(I18nSelectItem i18nSelectItemDto1,
                       I18nSelectItem i18nSelectItemDto2) {
      return new CompareToBuilder().append(i18nSelectItemDto1.getVolgorde(),
                                           i18nSelectItemDto2.getVolgorde())
                                   .toComparison();
    }
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof I18nSelectItem)) {
      return false;
    }
    if (object == this) {
      return true;
    }

    var andere  = (I18nSelectItem) object;
    return new EqualsBuilder().append(code, andere.code).isEquals();
  }

  public String getCode() {
    return code;
  }

  public Integer getVolgorde() {
    return volgorde;
  }

  public String getWaarde() {
    return waarde;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(code).toHashCode();
  }

  public void setCode(String code) {
    this.code     = code;
  }

  public void setVolgorde(Integer volgorde) {
    this.volgorde = volgorde;
  }

  public void setWaarde(String waarde) {
    this.waarde   = waarde;
  }
}
