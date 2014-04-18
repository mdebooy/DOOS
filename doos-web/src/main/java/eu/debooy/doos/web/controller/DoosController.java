/**
 * Copyright 2009 Marco de Booij
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
package eu.debooy.doos.web.controller;

import eu.debooy.doosutils.components.controller.DataController;

import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named("doos")
@SessionScoped
public class DoosController extends DataController {
  private static final  long  serialVersionUID  = 1L;

  public static final String  BEAN_NAME                 = "doos";
  public static final String  CACHE_REDIRECT            = "/admin/cache.jsf";
  public static final String  APP_PARAMS_REDIRECT       = "/admin/parameters.jsf";
  public static final String  I18NCODE_REDIRECT         = "/i18n/i18nCode.jsf";
  public static final String  I18NCODES_REDIRECT        = "/i18n/i18nCodes.jsf";
  public static final String  I18NUPLOAD_REDIRECT       =
      "/i18n/i18nUpload.jsf";
  public static final String  LIJSTEN_REDIRECT          =
      "/lijsten/lijsten.jsf";
  public static final String  PARAMETERS_REDIRECT       =
      "/parameters/parameters.jsf";
  public static final String  PARAMETERUPLOAD_REDIRECT  =
      "/parameters/parameterUpload.jsf";
  public static final String  TALEN_REDIRECT            = "/talen/talen.jsf";

  protected Map<String, String> getLijstKleuren() {
    return getLijstKleuren(BEAN_NAME);
  }

  public void home() {
    processActionWithCaution(BEAN_NAME + ".homeRedirect");
  }

  public void homeRedirect() {
    setPageDirty(Boolean.FALSE);
    redirect();
  }

  public void cache() {
    processActionWithCaution(BEAN_NAME + ".cacheRedirect");
  }

  public void cacheRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(CacheBean.BEAN_NAME);
    redirect(CACHE_REDIRECT);
  }

  public void applicatieparameters() {
    processActionWithCaution(BEAN_NAME + ".applicatieparametersRedirect");
  }

  public void applicatieparametersRedirect() {
    redirect(APP_PARAMS_REDIRECT);
  }

  public void i18nCode() {
    processActionWithCaution(BEAN_NAME + ".i18nCodeRedirect");
  }

  public void i18nCodes() {
    processActionWithCaution(BEAN_NAME + ".i18nCodesRedirect");
  }

  public void i18nCodeRedirect() {
    setPageDirty(Boolean.FALSE);
    redirect(I18NCODE_REDIRECT);
  }

  public void i18nCodesRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(I18nCodeBean.BEAN_NAME);
    redirect(I18NCODES_REDIRECT);
  }

  public void i18nUpload() {
    processActionWithCaution(BEAN_NAME + ".i18nUploadRedirect");
  }

  public void i18nUploadRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(I18nUploadBean.BEAN_NAME);
    redirect(I18NUPLOAD_REDIRECT);
  }

  public void lijsten() {
    processActionWithCaution(BEAN_NAME + ".lijstenRedirect");
  }

  public void lijstenRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(LijstBean.BEAN_NAME);
    redirect(LIJSTEN_REDIRECT);
  }

  public void parameters() {
    processActionWithCaution(BEAN_NAME + ".parametersRedirect");
  }

  public void parametersRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(ParameterBean.BEAN_NAME);
    redirect(PARAMETERS_REDIRECT);
  }

  public void parameterUpload() {
    processActionWithCaution(BEAN_NAME + ".parameterUploadRedirect");
  }

  public void parameterUploadRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(ParameterUploadBean.BEAN_NAME);
    redirect(PARAMETERUPLOAD_REDIRECT);
  }

  public void talen() {
    processActionWithCaution(BEAN_NAME + ".talenRedirect");
  }

  public void talenRedirect() {
    setPageDirty(Boolean.FALSE);
    destroyBean(TaalBean.BEAN_NAME);
    redirect(TALEN_REDIRECT);
  }
}
