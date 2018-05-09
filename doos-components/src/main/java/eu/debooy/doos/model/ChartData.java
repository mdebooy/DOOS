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
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * @author Marco de Booij
 */
public class ChartData implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  public static final String  BAR         = "bar";
  public static final String  BAR_3D      = "3Dbar";
  public static final String  HORIZONTAAL = "horizontaal";
  public static final String  LINE        = "line";
  public static final String  LINE_3D     = "3Dline";
  public static final String  PIE         = "pie";
  public static final String  PIE_3D      = "3Dpie";
  public static final String  VERTICAAL   = "verticaal";

  private Integer             breedte     = 500;
  private String              categorie;
  private String              chartnaam;
  private String              charttype   = PIE;
  private Set<ChartElement>   dataset     = new TreeSet<ChartElement>();
  private Integer             hoogte      = 500;
  private String              label;
  private boolean             legenda     = true;
  private Locale              locale;
  private String              orientation = VERTICAAL;
  private Map<String, String> parameters  = new HashMap<String, String>();
  private String              titel;
  private boolean             tooltip     = false;

  public void addData(ChartElement chartElement) {
    dataset.add(chartElement);
  }

  public void addData(Collection<ChartElement> chartElements) {
    dataset.addAll(chartElements);
  }

  public void addData(String sleutel, Number waarde) {
    dataset.add(new ChartElement(sleutel, waarde));
  }

  public void addData(String sleutel, Number waarde, String categorie) {
    dataset.add(new ChartElement(sleutel, waarde, categorie));
  }

  public void addParameter(String parameter, String waarde) {
    parameters.put(parameter, waarde);
  }

  public Integer getBreedte() {
    return breedte;
  }

  public String getCategorie() {
    return categorie;
  }

  public String getChartnaam() {
    return chartnaam;
  }

  public String getCharttype() {
    return charttype;
  }

  public Collection<ChartElement> getDataset() {
    return dataset;
  }

  public Integer getHoogte() {
    return hoogte;
  }

  public String getLabel() {
    return label;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getOrientation() {
    return orientation;
  }

  public String getParameter(String parameter) {
    return parameters.get(parameter);
  }

  public String getTitel() {
    return titel;
  }

  public boolean hasParameter(String parameter) {
    return parameters.containsKey(parameter);
  }

  public boolean hasParameters() {
    return !parameters.isEmpty();
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

  public void setCategorie(String categorie) {
    this.categorie = categorie;
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

  public void setLabel(String label) {
    this.label = label;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public void setOrientation(String orientation) {
    this.orientation  = orientation;
  }

  public void setTitel(String titel) {
    this.titel  = titel;
  }

  public void setTooltip(boolean tooltip) {
    this.tooltip  = tooltip;
  }
}
