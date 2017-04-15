/**
 * Copyright 2012 Marco de Booij
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
package eu.debooy.doos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Marco de Booij
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ExportData", propOrder={"type", "velden", "kolommen", "parameters", "metadata", "data"})
public class ExportData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  @XmlElement(required=true)
  private List<Object[]>      data        = new ArrayList<Object[]>();
  @XmlElement(required=true)
  private Map<String, String> metadata    = new HashMap<String, String>();
  @XmlElement(required=true)
  private Map<String, Object> velden      = new HashMap<String, Object>();
  @XmlElement(required=true)
  private String[]            kolommen    = new String[]{};
  @XmlElement
  private Map<String, String> parameters  = new HashMap<String, String>();
  @XmlElement(required=true)
  private String              type        = "PDF";

  public void addData(Object[] rij) {
    data.add(rij);
  }

  public void addKleur(String sleutel, String kleur) {
    metadata.put(sleutel, kleur);
  }

  public void addMetadata(String sleutel, String data) {
    metadata.put(sleutel, data);
  }

  public void addVeld(String naam, String data) {
    velden.put(naam, data);
  }

  public String[] getKolommen() {
    String[] resultaat  = new String[kolommen.length];
    System.arraycopy(kolommen, 0, resultaat, 0, kolommen.length);

    return resultaat;
  }

  /**
   * @deprecated Gebruik getParameters()
   */
  @Deprecated
  public Map<String, String> getKleuren() {
    return parameters;
  }

  public String getMetadata(String sleutel) {
    if (metadata.containsKey(sleutel)) {
      return metadata.get(sleutel);
    }

    return "";
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public String getType() {
    return type;
  }

  public Object getVeld(String sleutel) {
    if (velden.containsKey(sleutel)) {
      return velden.get(sleutel);
    }

    return "";
  }

  public Map<String, Object> getVelden() {
    return velden;
  }

  public List<Object[]> getData() {
    return data;
  }

  public boolean hasKolommen() {
    return kolommen.length > 0;
  }

  public boolean hasMetadata(String sleutel) {
    return metadata.containsKey(sleutel);
  }

  public boolean hasVeld(String sleutel) {
    return velden.containsKey(sleutel);
  }

  /**
   * @deprecated Gebruik setParameters()
   */
  @Deprecated
  public void setKleuren(Map<String, String> kleuren) {
    parameters  = kleuren;
  }

  public void setKolommen(final String[] kolommen) {
    this.kolommen = new String[kolommen.length];
    System.arraycopy(kolommen, 0, this.kolommen, 0, kolommen.length);
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public void setType(String type) {
    this.type = type.toUpperCase();
  }
}
