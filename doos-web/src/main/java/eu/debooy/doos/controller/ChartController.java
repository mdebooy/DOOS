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
package eu.debooy.doos.controller;

import eu.debooy.doos.Doos;
import eu.debooy.doos.business.StringNumber;
import eu.debooy.doos.component.Chart;
import eu.debooy.doos.model.ChartData;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.service.CDI;

import java.io.IOException;
import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * @author Marco de Booij
 */
@Named("doosChart")
@SessionScoped
public class ChartController extends Doos {
  private static final  long    serialVersionUID  = 1L;

  public void chart() {
    ChartData chartData = new ChartData();

    chartData.setChartnaam("TekstenPerTaal");
    chartData.setTitel("Teksten Per Taal");

    Collection<StringNumber>  data  = getI18nCodeService().getTekstenPerTaal();
    for (StringNumber entry : data) {
      chartData.addData(entry.getString(), entry.getNumber());
    }

    Gebruiker           gebruiker = (Gebruiker) CDI.getBean("gebruiker");
    chartData.setLocale(gebruiker.getLocale());

    try {
      Chart.maakChart(null, chartData);
    } catch (IOException e) {
      generateExceptionMessage(e);
    }
  }
}
