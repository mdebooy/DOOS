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
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosLayer;
import eu.debooy.doosutils.service.JNDI;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.JRConditionalStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdsExporterConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Stateless
public class ExportService implements IExport {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(ExportService.class);

  protected static  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle("ApplicatieResources");

  private static final  String  ERR_JSPR_ONBEKEND     =
      "jspr.onbekend.exporttype";
  private static final  String  ERR_JSPR_ONBEHANDELD  =
      "jspr.onbekend.exporttype";

  private static final  String  META_AUTHOR   = "auteur";
  private static final  String  META_CREATOR  = "application";
  private static final  String  META_KEYWORDS = "keywords";
  private static final  String  META_SUBJECT  = "onderwerp";
  private static final  String  META_TITLE    = "ReportTitel";

  public static final String  ROWCND_BGRND  = "row.conditional.background";
  public static final String  ROWCND_FGRND  = "row.conditional.foreground";

  public static final Map<String, String> STYLE;
  static {
    STYLE = new HashMap<>();
    STYLE.put("Titel","titel");
    STYLE.put("Column Header","columnheader");
    STYLE.put("Row","row");
    STYLE.put("Footer","footer");
    };

  /**
   * Genereer een JasperReport.
   *
   * @param exportData
   * @return
   */
  @Override
  public byte[] export(ExportData exportData) {
    String  type  = exportData.getType();
    if (ExportType.toExportType(type) == ExportType.ONBEKEND) {
      String  melding =
          MessageFormat.format(resourceBundle.getString(ERR_JSPR_ONBEKEND),
                               type);
      LOGGER.error(melding);
      throw new IllegalArgumentException(DoosLayer.PRESENTATION, melding);
    }

    try {
      // Zet de parameters in een Map.
      Map<String, Object> velden    = new HashMap<>();

      velden.putAll(exportData.getVelden());
      velden.put("ReportType", type);

      // Zet de data om in een CSV.
      String          csv   = maakCsv(exportData);
      JRCsvDataSource data  =
          new JRCsvDataSource(new ByteArrayInputStream(csv.getBytes()));
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
                              .lijst(exportData.getMetadata(META_CREATOR)
                                               .toLowerCase()
                                        + "." + rapportnaam)
                              .getJasperReport());
      jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

      // Zet de juiste background and foreground kleuren.
      JRStyle[]           styles      = jasperReport.getStyles();
      Map<String, String> parameters  = exportData.getParameters();
      zetKleuren(styles, parameters);

      // Genereer de JasperReport.
      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                                                             velden, data);

      // Bepaal het type van de export.
      Exporter<SimpleExporterInput, ?, ?, SimpleOutputStreamExporterOutput>
          exporter  = exportType(type, exportData);

      // Exporteer de JasperReport.
      ByteArrayOutputStream baos  = new ByteArrayOutputStream();

      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));

      exporter.exportReport();

      return baos.toByteArray();
    } catch (ObjectNotFoundException e) {
      throw new TechnicalException(null, null, e.getMessage());
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

  private Exporter exportType(String type, ExportData exportData)
      throws JRException {
    switch (ExportType.toExportType(type)) {
    case CSV:
      return new JRCsvExporter();
    case ODS:
      return ods(exportData);
    case ODT:
      return new JROdtExporter();
    case PDF:
      return pdf(exportData);
    case ONBEKEND:
      String  melding =
          MessageFormat.format(resourceBundle.getString(ERR_JSPR_ONBEKEND),
                               type);
      LOGGER.error(melding);
      throw new JRException(melding);
    default:
      melding =
          MessageFormat.format(resourceBundle.getString(ERR_JSPR_ONBEHANDELD),
                               type);
      LOGGER.error(melding);
      throw new JRException(melding);
    }
  }

  private String maakCsv(ExportData exportData) {
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

    return csv.toString();
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

  private JROdsExporter ods(ExportData exportData) {
    JROdsExporter                   exporter      = new JROdsExporter();
    SimpleOdsExporterConfiguration  configuratie  =
        new SimpleOdsExporterConfiguration();

    if (exportData.hasMetadata(META_AUTHOR)) {
      configuratie.setMetadataAuthor(exportData.getMetadata(META_AUTHOR));
    }
    if (exportData.hasMetadata(META_CREATOR)) {
      configuratie.setMetadataApplication(exportData.getMetadata(META_CREATOR));
    }
    if (exportData.hasMetadata(META_KEYWORDS)) {
      configuratie.setMetadataKeywords(exportData.getMetadata(META_KEYWORDS));
    }
    if (exportData.hasMetadata(META_SUBJECT)) {
      configuratie.setMetadataSubject(exportData.getMetadata(META_SUBJECT));
    }
    if (exportData.hasMetadata(META_TITLE)) {
      configuratie.setMetadataTitle(exportData.getMetadata(META_TITLE));
    }
    exporter.setConfiguration(configuratie);

    return exporter;
  }

  private JRPdfExporter pdf(ExportData exportData) {
    JRPdfExporter                   exporter      = new JRPdfExporter();
    SimplePdfExporterConfiguration  configuratie  =
        new SimplePdfExporterConfiguration();

    if (exportData.hasMetadata(META_AUTHOR)) {
      configuratie.setMetadataAuthor(exportData.getMetadata(META_AUTHOR));
    }
    if (exportData.hasMetadata(META_CREATOR)) {
      configuratie.setMetadataCreator(exportData.getMetadata(META_CREATOR));
    }
    if (exportData.hasMetadata(META_KEYWORDS)) {
      configuratie.setMetadataKeywords(exportData.getMetadata(META_KEYWORDS));
    }
    if (exportData.hasMetadata(META_SUBJECT)) {
      configuratie.setMetadataSubject(exportData.getMetadata(META_SUBJECT));
    }
    if (exportData.hasMetadata(META_TITLE)) {
      configuratie.setMetadataTitle(exportData.getMetadata(META_TITLE));
    }
    exporter.setConfiguration(configuratie);

    return exporter;
  }

  private void zetKleuren(JRStyle[] styles, Map<String, String> parameters) {
    if (null != styles) {
      for (JRStyle style : styles) {
        String  stylenaam = style.getName();
        if (STYLE.containsKey(stylenaam)) {
          String  kleur = STYLE.get(stylenaam) + ".background";
          if (parameters.containsKey(kleur)) {
            style.setBackcolor(maakKleur(parameters.get(kleur)));
            parameters.remove(kleur);
          }
          kleur = STYLE.get(stylenaam) + ".foreground";
          if (parameters.containsKey(kleur)) {
            style.setForecolor(maakKleur(parameters.get(kleur)));
            parameters.remove(kleur);
          }
        }
        if ("Row".equals(style.getName())) {
          JRConditionalStyle[] conditionalStyles = style.getConditionalStyles();
          if (null != conditionalStyles) {
            for (JRConditionalStyle conditionalStyle : conditionalStyles) {
              if (parameters.containsKey(ROWCND_BGRND)) {
                conditionalStyle
                    .setBackcolor(maakKleur(parameters.get(ROWCND_BGRND)));
                parameters.remove(ROWCND_BGRND);
              }
              if (parameters.containsKey(ROWCND_FGRND)) {
                conditionalStyle
                    .setForecolor(maakKleur(parameters.get(ROWCND_FGRND)));
                parameters.remove(ROWCND_FGRND);
              }
            }
          }
        }
      }
    }
  }
}
