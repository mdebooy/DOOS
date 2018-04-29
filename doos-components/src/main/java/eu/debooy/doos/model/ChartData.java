/**
 * Copyright 2018 Marco de Booij
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author Marco de Booij
 */
public class ChartData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  public static final String  PIE   = "pie";

  private Integer             breedte   = 500;
  private String              chartnaam;
  private String              charttype = PIE;
  private Map<String, Number> dataset   = new HashMap<String, Number>();
  private Integer             hoogte    = 500;
  private boolean             legenda   = true;
  private Locale              locale;
  private String              titel;
  private boolean             tooltip   = false;

  public void addData(String sleutel, Number waarde) {
    dataset.put(sleutel, waarde);
  }

  public Integer getBreedte() {
    return breedte;
  }

  public String getChartnaam() {
    return chartnaam;
  }

  public String getCharttype() {
    return charttype;
  }

  public Map<String, Number> getDataset() {
    return dataset;
  }

  public Integer getHoogte() {
    return hoogte;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getTitel() {
    return titel;
  }

  public boolean isLegenda() {
    return legenda;
  }

  public boolean isTooltip() {
    return tooltip;
  }

  public void setBreedte(Integer breedte) {
    this.breedte  = breedte;
  }

  public void setChartnaam(String chartnaam) {
    this.chartnaam  = chartnaam;
  }

  public void setCharttype(String charttype) {
    this.charttype  = charttype;
  }

  public void setHoogte(Integer hoogte) {
    this.hoogte = hoogte;
  }

  public void setLegenda(boolean legenda) {
    this.legenda  = legenda;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public void setTitel(String titel) {
    this.titel  = titel;
  }

  public void setTooltip(boolean tooltip) {
    this.tooltip  = tooltip;
  }
}
