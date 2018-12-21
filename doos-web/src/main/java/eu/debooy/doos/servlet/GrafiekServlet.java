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
package eu.debooy.doos.servlet;

import eu.debooy.doos.component.Chart;
import eu.debooy.doos.component.servlet.DoosServlet;
import eu.debooy.doos.model.ChartData;
import eu.debooy.doos.model.ChartElement;
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.service.CDI;
import eu.debooy.doosutils.service.JNDI;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Marco de Booij
 */
@WebServlet("/grafiek/*")
public class GrafiekServlet extends DoosServlet {
  private static final  long    serialVersionUID  = 1L;

  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {
    ChartData chartData = new ChartData();
    Gebruiker gebruiker = (Gebruiker) CDI.getBean("gebruiker");

    chartData.setCategorie(getTekst("label.taal"));
    chartData.setCharttype(ChartData.BAR);
    chartData.setChartnaam("TekstenPerTaal");
    chartData.setLabel(getTekst("label.aantal"));
    chartData.setLocale(gebruiker.getLocale());
    chartData.setTitel(getTekst("titel.teksten.per.taal"));

    Collection<ChartElement>  data = ((I18nCodeService)
        new JNDI.JNDINaam().metBean(I18nCodeService.class)
                .locate()).getTekstenPerTaal();
    for (ChartElement entry : data) {
      chartData.addData(entry.getString(), entry.getNumber());
    }

    Chart.maakChart(response, chartData);
  }
}