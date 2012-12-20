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
@XmlType(name="ExportData", propOrder={"type", "velden", "kolommen", "kleuren", "metadata", "data"})
public class ExportData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  @XmlElement(required=true)
  private List<Object[]>      data      = new ArrayList<Object[]>();
  @XmlElement(required=true)
  private Map<String, String> metadata  = new HashMap<String, String>();
  @XmlElement(required=true)
  private Map<String, Object> velden    = new HashMap<String, Object>();
  @XmlElement
  private Map<String, String> kleuren   = new HashMap<String, String>();
  @XmlElement(required=true)
  private String[]            kolommen  = new String[]{};
  @XmlElement(required=true)
  private String              type      = "PDF";

  /**
   * Voeg een rij toe aan de List.
   * 
   * @param rij
   */
  public void addData(Object[] rij) {
    data.add(rij);
  }

  /**
   * Voeg een kleur toe.
   * 
   * @param sleutel
   * @param kleur
   */
  public void addKleur(String sleutel, String kleur) {
    metadata.put(sleutel, kleur);
  }

  /**
   * Voeg een metadata toe.
   * 
   * @param sleutel
   * @param data
   */
  public void addMetadata(String sleutel, String data) {
    metadata.put(sleutel, data);
  }

  /**
   * Voeg een rij toe aan de List.
   * 
   * @param rij
   */
  public void addVeld(String naam, String data) {
    velden.put(naam, data);
  }

  /**
   * @return
   */
  public String[] getKolommen() {
    return kolommen;
  }

  /**
   * @return
   */
  public Map<String, String> getKleuren() {
    return kleuren;
  }

  /**
   * @param sleutel
   * @return
   */
  public String getMetadata(String sleutel) {
    if (metadata.containsKey(sleutel)) {
      return metadata.get(sleutel);
    }

    return "";
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param sleutel
   * @return
   */
  public Object getVeld(String sleutel) {
    if (velden.containsKey(sleutel)) {
      return velden.get(sleutel);
    }

    return "";
  }

  /**
   * @return
   */
  public Map<String, Object> getVelden() {
    return velden;
  }

  /**
   * @return
   */
  public List<Object[]> getData() {
    return data;
  }

  /**
   * @return
   */
  public boolean hasKolommen() {
    return kolommen.length > 0;
  }

  /**
   * @param sleutel
   * @return
   */
  public boolean hasMetadata(String sleutel) {
    return metadata.containsKey(sleutel);
  }

  /**
   * @param sleutel
   * @return
   */
  public boolean hasVeld(String sleutel) {
    return velden.containsKey(sleutel);
  }

  /**
   * @param kleuren
   */
  public void setKleuren(Map<String, String> kleuren) {
    this.kleuren  = kleuren;
  }

  /**
   * @param kolommen
   */
  public void setKolommen(final String[] kolommen) {
    this.kolommen = new String[kolommen.length];
    for (int  i = 0; i < kolommen.length; i++) {
      this.kolommen[i]  = kolommen[i];
    }
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type.toUpperCase();
  }
}
