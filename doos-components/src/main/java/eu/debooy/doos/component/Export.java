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
package eu.debooy.doos.component;

import eu.debooy.doos.component.business.IExport;
import eu.debooy.doos.model.ExportData;
import eu.debooy.doosutils.components.ExportType;
import eu.debooy.doosutils.errorhandling.exception.TechnicalException;
import eu.debooy.doosutils.service.ServiceLocator;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Marco de Booij
 */
public final class Export implements Serializable {
  private static final  long  serialVersionUID  = 1L;

  private Export() {}

  public static void export(HttpServletResponse response,
                            ExportData exportData) {
    IExport export  = (IExport) ServiceLocator.getInstance()
                                              .lookup("ExportServiceRemote");

    String  type  = exportData.getType();
    if (ExportType.toExportType(type) == ExportType.ONBEKEND) {
      throw new IllegalArgumentException(type);
    }

    byte[]  report  = export.export(exportData);

    response.reset();
    response.setHeader("Content-Disposition", "attachment; filename=\""
                       + exportData.getMetadata("lijstnaam") + "."
                       + type.toLowerCase() + "\"");
    switch (ExportType.toExportType(type)) {
    case CSV:
      response.setContentType("text/csv");
      break;
    case ODS:
      response
        .setContentType("application/vnd.oasis.opendocument.spreadsheet");
      break;
    case ODT:
      response.setContentType("application/vnd.oasis.opendocument.text");
      break;
    case PDF:
      response.setContentType("application/pdf");
      break;
    case ONBEKEND:
      break;
    default:
      break;
    }

    response.setContentLength(report.length);
    ServletOutputStream ouputStream = null;
    try {
      ouputStream = response.getOutputStream();
      ouputStream.write(report);
      ouputStream.flush();
    } catch (IOException e) {
      throw new TechnicalException(null, null, "ServletOutputStream", e);
    } finally {
      if (null != ouputStream) {
        try {
          ouputStream.close();
        } catch (IOException e) {
          throw new TechnicalException(null, null, "ServletOutputStream", e);
        }
      }
    }
  }
}
