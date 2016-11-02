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
package eu.debooy.doos.service;

import eu.debooy.doos.component.business.IExport;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doosutils.components.ExportType;
import eu.debooy.doosutils.conversie.ByteArray;
import eu.debooy.doosutils.errorhandling.exception.IllegalArgumentException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.service.JNDI;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/**
 * @author Marco de Booij
 */
@Stateless
public class ExportService implements IExport {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ExportService.class);

  public static final String  COLHDR_BGRND  = "columnheader.background";
  public static final String  COLHDR_FGRND  = "columnheader.foreground";
  public static final String  FTR_BGRND     = "footer.background";
  public static final String  FTR_FGRND     = "footer.foreground";
  public static final String  ROW_BGRND     = "row.background";
  public static final String  ROW_FGRND     = "row.foreground";
  public static final String  ROWCND_BGRND  = "row.conditional.background";
  public static final String  ROWCND_FGRND  = "row.conditional.foreground";
  public static final String  TIT_BGRND     = "titel.background";
  public static final String  TIT_FGRND     = "titel.foreground";

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
      StringBuilder csv = new StringBuilder();
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
      LijstService  lijstService  = (LijstService)
          new JNDI.JNDINaam().metBean(LijstService.class).locate();

      JasperReport  jasperReport  =
          (JasperReport) ByteArray.byteArrayToObject(lijstService
                              .lijst(exportData.getMetadata("application")
                                               .toLowerCase()
                                        + "." + rapportnaam)
                              .getJasperReport());
      jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

      // Zet de juiste background and foreground kleuren.
      JRStyle[]           styles  = jasperReport.getStyles();
      Map<String, String> kleuren = exportData.getKleuren();
      if (null != styles) {
        for (int i = 0; i < styles.length; i++) {
          if ("Column Header".equals(styles[i].getName())) {
            if (kleuren.containsKey(COLHDR_BGRND)) {
              styles[i].setBackcolor(maakKleur(kleuren.get(COLHDR_BGRND)));
            }
            if (kleuren.containsKey(COLHDR_FGRND)) {
              styles[i].setForecolor(maakKleur(kleuren.get(COLHDR_FGRND)));
            }
          }
          if ("Footer".equals(styles[i].getName())) {
            if (kleuren.containsKey(FTR_BGRND)) {
              styles[i].setBackcolor(maakKleur(kleuren.get(FTR_BGRND)));
            }
            if (kleuren.containsKey(FTR_FGRND)) {
              styles[i].setForecolor(maakKleur(kleuren.get(FTR_FGRND)));
            }
          }
          if ("Row".equals(styles[i].getName())) {
            if (kleuren.containsKey(ROW_BGRND)) {
              styles[i].setBackcolor(maakKleur(kleuren.get(ROW_BGRND)));
            }
            if (kleuren.containsKey(ROW_FGRND)) {
              styles[i].setForecolor(maakKleur(kleuren.get(ROW_FGRND)));
            }
            JRConditionalStyle[]
                conditionalStyles = styles[i].getConditionalStyles();
            if (null != conditionalStyles) {
              for (int j = 0; j < conditionalStyles.length; j++) {
                if (kleuren.containsKey(ROWCND_BGRND)) {
                  conditionalStyles[j].setBackcolor(
                      maakKleur(kleuren.get(ROWCND_BGRND)));
                }
                if (kleuren.containsKey(ROWCND_FGRND)) {
                  conditionalStyles[j].setForecolor(
                      maakKleur(kleuren.get(ROWCND_FGRND)));
                }
              }
            }
          }
          if ("Titel".equals(styles[i].getName())) {
            if (kleuren.containsKey(TIT_BGRND)) {
              styles[i].setBackcolor(maakKleur(kleuren.get(TIT_BGRND)));
            }
            if (kleuren.containsKey(TIT_FGRND)) {
              styles[i].setForecolor(maakKleur(kleuren.get(TIT_FGRND)));
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
