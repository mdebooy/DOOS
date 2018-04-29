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
package eu.debooy.doos.business;


/**
 * @author Marco de Booij
 */
public class StringNumber {
  private Number  number;
  private String  string;

  public StringNumber(Object[] rij) {
    string  = (String) rij[0];
    number  = (Number) rij[1];
  }

  public Number getNumber() {
    return number;
  }

  public String getString() {
    return string;
  }

  public void setNumber(Number number) {
    this.number = number;
  }

  public void setString(String string) {
    this.string = string;
  }
}
