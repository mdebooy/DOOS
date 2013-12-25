/**
 * Copyright 2012 Marco de Booij
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
package eu.debooy.doos.business;

import eu.debooy.doos.component.LijstComponent;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doosutils.components.ExportType;
import eu.debooy.doosutils.conversie.ByteArray;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Stateless
public class ExportManager implements IExport {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ExportManager.class);

  /**
   * Genereer een JasperReport.
   */
  @Override
  public byte[] export(ExportData exportData) {
    String  type    = exportData.getType();
    if (ExportType.toExportType(type) == ExportType.ONBEKEND) {
      LOGGER.error("Onbekend ExportType: " + type);
      throw new IllegalArgumentException(null, type);
    }

    try {
      // Zet de parameters in een Map.
      Map<String, Object> velden    = new HashMap<String, Object>();

      velden.putAll(exportData.getVelden());
      velden.put("ReportType", type);

      // Zet de data om in een CSV.
      StringBuffer  csv = new StringBuffer();
      for (Object[] veld : exportData.getData()) {
        for (int  i = 0; i < veld.length; i++) {
          if (null != veld[i]) {
            if (veld[i] instanceof String) {
              csv.append("\"")
                 .append(((String) veld[i]).replace("\"", "\"\""))
                 .append("\"");
            } else {
              if (veld[i] instanceof Integer) {
                csv.append((Integer) veld[i]);
              } else {
                if (veld[i] instanceof Double) {
                  csv.append((Double) veld[i]);
                } else {
                  if (veld[i] instanceof Byte) {
                    csv.append((Byte) veld[i]);
                  } else {
                    LOGGER.error("Onbekend Type: "
                                 + veld[i].getClass().getName());
                    throw new IllegalArgumentException(null, veld[i].getClass()
                                                                    .getName());
                  }
                }
              }
            }
          }
          if ((i + 1) < veld.length) {
            csv.append(",");
          }
        }
        csv.append("\n");
      }
      JRCsvDataSource data  =
          new JRCsvDataSource(new ByteArrayInputStream(csv.toString()
                                                          .getBytes()));
      data.setRecordDelimiter("\n");
      if (exportData.hasKolommen()) {
        data.setColumnNames(exportData.getKolommen());
      }

      // Bepaal de naam van het rapport.
      String  rapportnaam   = exportData.getMetadata("lijstnaam");
      if (exportData.hasMetadata("rapportnaam")) {
        rapportnaam   = exportData.getMetadata("rapportnaam");
      }

      // Haal de gecompileerde JasperReport op uit de database.
      JasperReport  jasperReport  =
          (JasperReport) ByteArray.byteArrayToObject(new LijstComponent()
                              .getLijst(exportData.getMetadata("application")
                                        + "." + rapportnaam)
                              .getJasperReport());
      jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

      // Zet de juiste background and foreground kleuren.
      JRStyle[]           styles  = jasperReport.getStyles();
      Map<String, String> kleuren = exportData.getKleuren();
      if (null != styles) {
        for (int i = 0; i < styles.length; i++) {
          if ("Column Header".equals(styles[i].getName())) {
            if (kleuren.containsKey("columnheader.background")) {
              styles[i].setBackcolor(
                  maakKleur(kleuren.get("columnheader.background")));
            }
            if (kleuren.containsKey("columnheader.foreground")) {
              styles[i].setForecolor(
                  maakKleur(kleuren.get("columnheader.foreground")));
            }
          }
          if ("Footer".equals(styles[i].getName())) {
            if (kleuren.containsKey("footer.background")) {
              styles[i].setBackcolor(maakKleur(kleuren.get("footer.background")));
            }
            if (kleuren.containsKey("footer.foreground")) {
              styles[i].setForecolor(maakKleur(kleuren.get("footer.foreground")));
            }
          }
          if ("Row".equals(styles[i].getName())) {
            if (kleuren.containsKey("row.background")) {
              styles[i].setBackcolor(maakKleur(kleuren.get("row.background")));
            }
            if (kleuren.containsKey("row.foreground")) {
              styles[i].setForecolor(maakKleur(kleuren.get("row.foreground")));
            }
            JRConditionalStyle[]
                conditionalStyles = styles[i].getConditionalStyles();
            if (null != conditionalStyles) {
              for (int j = 0; j < conditionalStyles.length; j++) {
                if (kleuren.containsKey("row.conditional.background")) {
                  conditionalStyles[j].setBackcolor(
                      maakKleur(kleuren.get("row.conditional.background")));
                }
                if (kleuren.containsKey("row.conditional.foreground")) {
                  conditionalStyles[j].setForecolor(
                      maakKleur(kleuren.get("row.conditional.foreground")));
                }
              }
            }
          }
          if ("Titel".equals(styles[i].getName())) {
            if (kleuren.containsKey("titel.background")) {
              styles[i].setBackcolor(maakKleur(kleuren.get("titel.background")));
            }
            if (kleuren.containsKey("titel.foreground")) {
              styles[i].setForecolor(maakKleur(kleuren.get("titel.foreground")));
            }
          }
        }
      }

      // Genereer de JasperReport.
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                                                             velden, data);

      // Bepaal het type van de export.
      JRExporter  exporter  = null;
      switch (ExportType.toExportType(type)) {
      case CSV:
        exporter  = new JRCsvExporter();
        break;
      case ODS:
        exporter  = new JROdsExporter();
        exporter.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
        break;
      case ODT:
        exporter  = new JROdtExporter();
        break;
      case PDF:
        exporter  = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        if (exportData.hasMetadata("auteur")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR,
                                exportData.getMetadata("auteur"));
        }
        if (exportData.hasMetadata("auteur")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR,
                                exportData.getMetadata("auteur"));
        }
        if (exportData.hasMetadata("application")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_CREATOR,
                                exportData.getMetadata("application"));
        }
        if (exportData.hasMetadata("keywords")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_KEYWORDS,
                                exportData.getMetadata("keywords"));
        }
        if (exportData.hasMetadata("onderwerp")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_SUBJECT,
                                exportData.getMetadata("onderwerp"));
        }
        if (exportData.hasVeld("ReportTitel")) {
          exporter.setParameter(JRPdfExporterParameter.METADATA_TITLE,
                                exportData.getVeld("ReportTitel"));
        }
        break;
      case ONBEKEND:
        break;
      default:
        break;
      }

      // Exporteer de JasperReport.
      ByteArrayOutputStream baos  = new ByteArrayOutputStream();

      if (null != exporter) {
        exporter.setParameter(JRExporterParameter.JASPER_PRINT,   jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,  baos);

        exporter.exportReport();
      }

      return baos.toByteArray();
    } catch (IOException e) {
      LOGGER.error("IO: " + e.getLocalizedMessage(), e);
      throw new TechnicalException(null, null,
                                   "IO: " + e.getLocalizedMessage());
    } catch (JRException e) {
      LOGGER.error("JR: " + e.getLocalizedMessage(), e);
      throw new TechnicalException(null, null,
                                   "JR: " + e.getLocalizedMessage());
    }
  }

  private Color maakKleur(String kleur) {
    String  htmlKleur = kleur;
    char[]  teken     = kleur.toCharArray();
    if (kleur.length() == 3) {
      htmlKleur = "#" + teken[0] + teken[0] + teken[1] + teken[1]
                  + teken[2] + teken[2];
    }
    if (kleur.length() == 4
        && teken[0] == '#') {
      htmlKleur = "#" + teken[1] + teken[1] + teken[2] + teken[2]
                  + teken[3] + teken[3];
    }
    if (kleur.length() == 6) {
      htmlKleur = "#" + kleur;
    }
    if (kleur.length() == 8
        && kleur.startsWith("0x")) {
      htmlKleur = "#" + kleur.substring(2);
    }

    return Color.decode(htmlKleur);
  }
}
