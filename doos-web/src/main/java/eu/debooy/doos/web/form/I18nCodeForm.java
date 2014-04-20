/**
 * Copyright 2014 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doos.web.form;

import eu.debooy.doos.domain.I18nCodeDto;

import java.io.Serializable;


/**
 * @author Marco de Booij
 */
public class I18nCodeForm implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private boolean gewijzigd = false;

  private String  code;
  private Long    codeId;

  public I18nCodeForm() {}

  public I18nCodeForm(I18nCodeDto i18nCode) {
    this.code   = i18nCode.getCode();
    this.codeId = i18nCode.getCodeId();
  }

  /**
   * @return de code
   */
  public String getCode() {
    return code;
  }

  /**
   * @return de codeId
   */
  public Long getCodeId() {
    return codeId;
  }

  /**
   * Is er iets gewijzigd?
   * 
   * @return
   */
  public boolean isGewijzigd() {
    return gewijzigd;
  }

  /**
   * Zet de gegevens in de I18nCodeDTO
   *
   * @param i18nCode
   */
  public void persist(I18nCodeDto i18nCode) {
    if (!gewijzigd) {
      return;
    }

    if (null == this.code
        || !this.code.equals(i18nCode.getCode())) {
      i18nCode.setCode(this.code);
    }
    if (null == this.codeId
        || !this.codeId.equals(i18nCode.getCodeId())) {
      i18nCode.setCodeId(this.codeId);
    }
  }

  /**
   * @param code de waarde van code
   */
  public void setCode(String code) {
    if (null == this.code
        || !this.code.equals(code)) {
      gewijzigd = true;
      this.code = code;
    }
  }

  /**
   * @param codeId de waarde van codeId
   */
  public void setCodeId(Long codeId) {
    if (null == this.codeId
        || !this.codeId.equals(codeId)) {
      gewijzigd   = true;
      this.codeId = codeId;
    }
  }
}
