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
@XmlType(name="ExportData",
         propOrder={"type", "velden", "kolommen", "parameters", "metadata",
                    "data"})
public class ExportData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  public static final String  MTD_LIJSTNAAM   = "lijstnaam";
  public static final String  MTD_RAPPORTNAAM = "rapportnaam";

  @XmlElement(required=true)
  private String[]            kolommen  = new String[]{};
  @XmlElement(required=true)
  private String              type      = "PDF";

  @XmlElement(required=true)
  private final List<Object[]>      data        = new ArrayList<>();
  @XmlElement(required=true)
  private final Map<String, String> metadata    = new HashMap<>();
  @XmlElement
  private final Map<String, String> parameters  = new HashMap<>();
  @XmlElement(required=true)
  private final Map<String, Object> velden      = new HashMap<>();

  public void addData(Object[] rij) {
    data.add(rij);
  }

  public void addMetadata(String sleutel, String data) {
    metadata.put(sleutel, data);
  }

  public void addParameter(String sleutel, String data) {
    parameters.put(sleutel, data);
  }

  public void addVeld(String naam, String data) {
    velden.put(naam, data);
  }

  public String[] getKolommen() {
     return kolommen;
  }

  public String getMetadata(String sleutel) {
    return metadata.containsKey(sleutel) ? metadata.get(sleutel) : "";
  }

  public String getParameter(String sleutel) {
    return parameters.containsKey(sleutel) ? parameters.get(sleutel) : "";
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public String getType() {
    return type;
  }

  public Object getVeld(String sleutel) {
    return velden.containsKey(sleutel) ? velden.get(sleutel) : "";
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

  public boolean hasParameter(String sleutel) {
    return parameters.containsKey(sleutel);
  }

  public boolean hasVeld(String sleutel) {
    return velden.containsKey(sleutel);
  }

  public void setKolommen(final String[] kolommen) {
    this.kolommen = kolommen;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata.clear();
    this.metadata.putAll(metadata);
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters.clear();
    this.parameters.putAll(parameters);
  }

  public void setVelden(Map<String, String> velden) {
    this.velden.clear();
    this.velden.putAll(velden);
  }

  public void setType(String type) {
    this.type = type.toUpperCase();
  }
}
