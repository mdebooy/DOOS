/**
 * Copyright (c) 2018 Marco de Booij
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
import eu.debooy.doos.service.I18nCodeService;
import eu.debooy.doosutils.components.bean.Gebruiker;
import eu.debooy.doosutils.service.CDI;
import eu.debooy.doosutils.service.JNDI;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@WebServlet("/grafiek/*")
public class GrafiekServlet extends DoosServlet {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(GrafiekServlet.class);

  @Override
  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {
    var chartData = new ChartData();
    var gebruiker = (Gebruiker) CDI.getBean("gebruiker");

    chartData.setCategorie(getTekst("label.taal"));
    chartData.setCharttype(ChartData.BAR);
    chartData.setChartnaam("TekstenPerTaal");
    chartData.setLabel(getTekst("label.aantal"));
    chartData.setLocale(gebruiker.getLocale());
    chartData.setTitel(getTekst("titel.teksten.per.taal"));

    var data = ((I18nCodeService)
        new JNDI.JNDINaam().metBean(I18nCodeService.class)
                .locate()).getTekstenPerTaal();
    data.forEach(entry -> chartData.addData(entry.getString(),
                                            entry.getNumber()));

    try {
      Chart.maakChart(response, chartData);
    } catch (IOException e) {
      response.setContentType("text/html");
      try (var out = response.getWriter()) {
        out.write("<h3>Exception Details</h3>");
        out.write(String.format("<li>Exception Name: %s</li>",
                                e.getClass().getName()));
        out.write(String.format("<li>Exception Message: %s</li>",
                                e.getLocalizedMessage()));
      } catch (IOException ex) {
        LOGGER.error(ex.getLocalizedMessage());
      }
    }
  }
}
