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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IChart;
import eu.debooy.doos.model.ChartData;
import eu.debooy.doos.model.ChartElement;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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

  @Override
  public byte[] maakChart(ChartData chartData) throws IOException {
    JFreeChart  chart;
    var         charttype = chartData.getCharttype();

    switch (charttype) {
      case ChartData.BAR:
        chart = ChartFactory.createBarChart(chartData.getTitel(),
                                            chartData.getCategorie(),
                                            chartData.getLabel(),
                                            getCategoryDataset(
                                                chartData.getDataset()),
                                            getOrientation(
                                                chartData.getOrientation()),
                                            chartData.isLegenda(),
                                            chartData.isTooltip(),
                                            false);
        break;
      case ChartData.BAR_3D:
        chart = ChartFactory.createBarChart3D(chartData.getTitel(),
                                              chartData.getCategorie(),
                                              chartData.getLabel(),
                                              getCategoryDataset(
                                                  chartData.getDataset()),
                                              getOrientation(
                                                  chartData.getOrientation()),
                                              chartData.isLegenda(),
                                              chartData.isTooltip(),
                                              false);
        break;
      case ChartData.LINE:
        chart = ChartFactory.createLineChart(chartData.getTitel(),
                                              chartData.getCategorie(),
                                              chartData.getLabel(),
                                              getCategoryDataset(
                                                  chartData.getDataset()),
                                              getOrientation(
                                                  chartData.getOrientation()),
                                              chartData.isLegenda(),
                                              chartData.isTooltip(),
                                              false);
        break;
      case ChartData.LINE_3D:
        chart = ChartFactory.createLineChart3D(chartData.getTitel(),
                                                chartData.getCategorie(),
                                                chartData.getLabel(),
                                                getCategoryDataset(
                                                    chartData.getDataset()),
                                                getOrientation(
                                                    chartData.getOrientation()),
                                                chartData.isLegenda(),
                                                chartData.isTooltip(),
                                                false);
        break;
      case ChartData.PIE:
        chart = ChartFactory.createPieChart(chartData.getTitel(),
                                            getPieDataset(
                                                chartData.getDataset()),
                                            chartData.isLegenda(),
                                            chartData.isTooltip(),
                                            chartData.getLocale());
        break;
      case ChartData.PIE_3D:
        chart = ChartFactory.createPieChart3D(chartData.getTitel(),
                                              getPieDataset(
                                                  chartData.getDataset()),
                                              chartData.isLegenda(),
                                              chartData.isTooltip(),
                                              chartData.getLocale());
        break;
      default:
        LOGGER.error("Onbekend Type: " + charttype);
        throw new IllegalArgumentException(DoosLayer.PRESENTATION, charttype);
    }

    if (chartData.hasParameters()) {
      setParameters(chart, chartData);
    }

    byte[] imageInByte;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      BufferedImage bufferedImage =
              chart.createBufferedImage(chartData.getBreedte(),
                      chartData.getHoogte());
      ImageIO.write(bufferedImage, "png", baos);
      baos.flush();
      imageInByte = baos.toByteArray();
    }

    return imageInByte;
  }

  private DefaultCategoryDataset
      getCategoryDataset(Collection<ChartElement> collection) {
    var dataset = new DefaultCategoryDataset();
    collection.forEach(entry ->  dataset.setValue(entry.getNumber(),
                                                  entry.getString(),
                                                  entry.getCategorie()));

    return dataset;
  }

  private DefaultPieDataset
      getPieDataset(Collection<ChartElement> collection) {
    var dataset = new DefaultPieDataset();
    collection.forEach(entry ->  dataset.setValue(entry.getString(),
                                                  entry.getNumber()));

    return dataset;
  }

  private PlotOrientation getOrientation(String orientation) {
    if (ChartData.HORIZONTAAL.equals(orientation)) {
      return PlotOrientation.HORIZONTAL;
    }

    return PlotOrientation.VERTICAL;
  }

  private void setParameters(JFreeChart chart, ChartData chartData) {
    var           plot  = (CategoryPlot)chart.getPlot();
    CategoryAxis  xAxis;
    if (chartData.hasParameter("CategoryLabelPositions")) {
      xAxis = (CategoryAxis)plot.getDomainAxis();
      var hoek  = chartData.getParameter("CategoryLabelPositions");
      CategoryLabelPositions  clps;
      switch (hoek) {
        case "-90":
          clps  = CategoryLabelPositions.UP_90;
          break;
        case "90":
          clps  = CategoryLabelPositions.DOWN_90;
          break;
        default:
          Double  graden  = Math.abs(Double.valueOf(hoek)/180) * Math.PI;
          if (hoek.startsWith("-")){
            clps  =
                CategoryLabelPositions.createUpRotationLabelPositions(graden);
          } else {
            clps  =
                CategoryLabelPositions.createDownRotationLabelPositions(graden);
          }
          break;
      }
      xAxis.setCategoryLabelPositions(clps);
    }
  }
}
