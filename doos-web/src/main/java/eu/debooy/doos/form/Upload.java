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

import java.io.Serializable;

import org.apache.myfaces.custom.fileupload.UploadedFile;


/**
 * @author Marco de Booij
 */
public class Upload implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private UploadedFile  bestand;
  private boolean       overschrijven = false;
  private int           gelezen       = 0;
  private int           gewijzigd     = 0;
  private int           nieuw         = 0;
  private int           nieuweWaardes = 0;
  private String        taal          = "";
  private boolean       utf8          = false;

  public void addGelezen() {
    gelezen++;
  }

  public void addGewijzigd() {
    gewijzigd++;
  }

  public void addNieuw() {
    nieuw++;
  }

  public void addNieuweWaardes() {
    nieuweWaardes++;
  }

  public UploadedFile getBestand() {
    return bestand;
  }

  public int getGelezen() {
    return gelezen;
  }

  public int getGewijzigd() {
    return gewijzigd;
  }

  public int getNieuw() {
    return nieuw;
  }

  public int getNieuweWaardes() {
    return nieuweWaardes;
  }

  public String getTaal() {
    return taal;
  }

  public boolean isOverschrijven() {
    return overschrijven;
  }

  public boolean isUtf8() {
    return utf8;
  }

  public void reset() {
    gelezen       = 0;
    nieuw         = 0;
    nieuweWaardes = 0;
    gewijzigd     = 0;
  }

  public void setBestand(UploadedFile bestand) {
    this.bestand = bestand;
  }

  public void setOverschrijven(boolean overschrijven) {
    this.overschrijven = overschrijven;
  }

  public void setGelezen(int gelezen) {
    this.gelezen  = gelezen;
  }

  public void setGewijzigdeWaardes(int gewijzigd) {
    this.gewijzigd  = gewijzigd;
  }

  public void setNieuweCodes(int nieuw) {
    this.nieuw  = nieuw;
  }

  public void setNieuweWaardes(int nieuweWaardes) {
    this.nieuweWaardes  = nieuweWaardes;
  }

  public void setTaal(String taal) {
    this.taal = taal;
  }

  public void setUtf8(boolean utf8) {
    this.utf8 = utf8;
  }
}
