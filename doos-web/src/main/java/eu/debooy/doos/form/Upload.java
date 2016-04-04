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

import eu.debooy.doosutils.form.Formulier;

import org.apache.myfaces.custom.fileupload.UploadedFile;


/**
 * @author Marco de Booij
 */
public class Upload extends Formulier {
  private static final  long  serialVersionUID  = 1L;

  private UploadedFile  bestand;
  private boolean       overschrijven = false;
  private int           gelezen       = 0;
  private int           gewijzigd     = 0;
  private int           nieuw         = 0;
  private int           nieuweWaardes = 0;
  private String        taal          = "";
  private boolean       utf8          = false;

  /**
   * Tel 1 op bij gelezen.
   */
  public void addGelezen() {
    gelezen++;
  }

  /**
   * Tel 1 op bij gewijzigd.
   */
  public void addGewijzigd() {
    gewijzigd++;
  }

  /**
   * Tel 1 op bij nieuw.
   */
  public void addNieuw() {
    nieuw++;
  }

  /**
   * Tel 1 op bij nieuweWaardes.
   */
  public void addNieuweWaardes() {
    nieuweWaardes++;
  }

  /**
   * @return de bestand
   */
  public UploadedFile getBestand() {
    return bestand;
  }

  /**
   * @return de overschrijven
   */
  public boolean isOverschrijven() {
    return overschrijven;
  }

  /**
   * @return de gelezen
   */
  public int getGelezen() {
    return gelezen;
  }

  /**
   * @return de gewijzigd
   */
  public int getGewijzigd() {
    return gewijzigd;
  }

  /**
   * @return de nieuweCodes
   */
  public int getNieuw() {
    return nieuw;
  }

  /**
   * @return de nieuweWaardes
   */
  public int getNieuweWaardes() {
    return nieuweWaardes;
  }

  /**
   * @return de taal
   */
  public String getTaal() {
    return taal;
  }

  /**
   * @return de utf8
   */
  public boolean isUtf8() {
    return utf8;
  }

  /**
   * Zet de totalen op 0.
   */
  public void reset() {
    gelezen       = 0;
    nieuw         = 0;
    nieuweWaardes = 0;
    gewijzigd     = 0;
  }

  /**
   * @param bestand de waarde van bestand
   */
  public void setBestand(UploadedFile bestand) {
    this.bestand = bestand;
  }

  /**
   * @param overschrijven de waarde van overschrijven
   */
  public void setOverschrijven(boolean overschrijven) {
    this.overschrijven = overschrijven;
  }

  /**
   * @param gelezen de waarde van gelezen
   */
  public void setGelezen(int gelezen) {
    this.gelezen  = gelezen;
  }

  /**
   * @param gewijzigd de waarde van gewijzigd
   */
  public void setGewijzigdeWaardes(int gewijzigd) {
    this.gewijzigd  = gewijzigd;
  }

  /**
   * @param nieuweCodes de waarde van nieuweCodes
   */
  public void setNieuweCodes(int nieuw) {
    this.nieuw  = nieuw;
  }

  /**
   * @param nieuweWaardes de waarde van nieuweWaardes
   */
  public void setNieuweWaardes(int nieuweWaardes) {
    this.nieuweWaardes  = nieuweWaardes;
  }

  /**
   * @param taal de waarde van taal
   */
  public void setTaal(String taal) {
    this.taal = taal;
  }

  /**
   * @param utf8 de waarde van utf8
   */
  public void setUtf8(boolean utf8) {
    this.utf8 = utf8;
  }
}
