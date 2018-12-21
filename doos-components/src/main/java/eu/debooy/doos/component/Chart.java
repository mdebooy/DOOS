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
package eu.debooy.doos.component;

import eu.debooy.doos.component.business.IChart;
import eu.debooy.doos.model.ChartData;
import eu.debooy.doosutils.service.ServiceLocator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;


/**
 * @author Marco de Booij
 */
public final class Chart implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private Chart() {}
  
  public static void maakChart(HttpServletResponse response,
                               ChartData chartData) throws IOException {
    IChart  chart = (IChart) ServiceLocator.getInstance()
                                           .lookup("ChartServiceRemote");

    byte[]  graph = chart.maakChart(chartData);
    
    response.setContentLength(graph.length);
    response.setHeader("Content-Disposition", "inline; filename=\""
                       + chartData.getChartnaam() + ".png\"");

    BufferedInputStream   input   = null;
    BufferedOutputStream  output  = null;

    try {
        input   = new BufferedInputStream(new ByteArrayInputStream(graph));
        output  = new BufferedOutputStream(response.getOutputStream());
        byte[] buffer = new byte[8192];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
    } finally {
        if (output != null) {
          output.close();
        }
        if (input != null) {
          input.close();
        }
    }
  }
}
