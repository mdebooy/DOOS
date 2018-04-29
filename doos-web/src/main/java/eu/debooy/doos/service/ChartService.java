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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IChart;
import eu.debooy.doos.model.ChartData;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Stateless
public class ChartService implements IChart {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ChartService.class);

  public byte[] maakChart(ChartData chartData) throws IOException {
    JFreeChart chart;
    String  charttype = chartData.getCharttype();

    switch (charttype) {
    case ChartData.PIE:
      DefaultPieDataset dataset = new DefaultPieDataset();
      for (Entry<String, Number> entry : chartData.getDataset().entrySet()) {
        dataset.setValue(entry.getKey(), entry.getValue());
      }
      chart = ChartFactory.createPieChart(chartData.getTitel(),
                                          dataset,
                                          chartData.isLegenda(), 
                                          chartData.isTooltip(),
                                          chartData.getLocale());
      break;
    default:
      LOGGER.error("Onbekend Type: " + charttype);
      throw new IllegalArgumentException(DoosLayer.PRESENTATION, charttype);
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedImage bufferedImage =
        chart.createBufferedImage(chartData.getBreedte(),
                                  chartData.getHoogte());
    ImageIO.write(bufferedImage, "jpg", baos);
    baos.flush();
    byte[] imageInByte = baos.toByteArray();
    baos.close();

    return imageInByte;
  }
}
