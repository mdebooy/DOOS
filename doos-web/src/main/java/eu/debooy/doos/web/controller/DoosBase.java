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

import eu.debooy.doosutils.components.bean.JsfBean;



/**
 * @author Marco de Booij
 */
//@Named("jsf")
//@SessionScoped
public class DoosBase extends JsfBean {
  private static final  long  serialVersionUID  = 1L;

  public static final String  APPLICATION_NAME  = "DOOS";
  public static final String  BEAN_NAME         = "doos";
  public static final String  USER_ROLE         = "DOOS-User";
  public static final String  ADMIN_ROLE        = "DOOS-Admin";

  public String getAdminRole() {
    return ADMIN_ROLE;
  }

  public String getApplicationName() {
    return APPLICATION_NAME;
  }

  public String getUserRole() {
    return USER_ROLE;
  }

  public void home() {
    processActionWithCaution(BEAN_NAME + ".homeRedirect");
  }

  public boolean isAdministrator() {
    return getExternalContext().isUserInRole(ADMIN_ROLE);
  }

  public boolean isUser() {
    return getExternalContext().isUserInRole(USER_ROLE);
  }
}
