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
package eu.debooy.doos.web.controller;

import eu.debooy.doos.component.LijstComponent;
import eu.debooy.doos.domain.LijstDto;
import eu.debooy.doos.web.model.Lijst;
import eu.debooy.doosutils.DoosUtils;
import eu.debooy.doosutils.PersistenceConstants;
import eu.debooy.doosutils.conversie.ByteArray;
import eu.debooy.doosutils.errorhandling.exception.DuplicateObjectException;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import eu.debooy.doosutils.errorhandling.exception.base.DoosRuntimeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
public class LijstBean extends DoosController {
  private static final  long    serialVersionUID  = 1L;
  private static final  Logger  LOGGER            =
      LoggerFactory.getLogger(LijstBean.class);

  public static final String BEAN_NAME = "lijstBean";

  private List<Lijst>     lijsten;
  private Lijst           lijst   = null;
  private UploadedFile    bestand;

  public LijstBean() {
    try {
      Collection<LijstDto> rows  = new LijstComponent().getAll();
      lijsten = new ArrayList<Lijst>(rows.size());
      for (LijstDto lijstDto : rows) {
        lijsten.add(new Lijst(lijstDto));
      }
    } catch (ObjectNotFoundException e) {
      lijsten = null;
      addInfo("info.norows",
          new Object[] {getTekst("doos.title.lijsten").toLowerCase()});
    } catch (DoosRuntimeException e) {
      LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
      generateExceptionMessage(e);
    }
  }

  /**
   * Cancel een CRUD aktie.
   */
  public void cancel() {
    setAktie(PersistenceConstants.RETRIEVE);
    lijst = null;
  }

  /**
   * Schrijf de nieuwe Lijst in de database.
   */
  public void createLijst() {
    if (null != lijst
        && isNieuw()) {
      if (valideerForm() ) {
        LijstComponent  lijstComponent  = new LijstComponent();
        try {
          vulLijst();
          lijstComponent.insert(lijst.getLijst());
          if (null == lijsten) {
            lijsten = new ArrayList<Lijst>(1);
          }
          lijsten.add(lijst);
          setAktie(PersistenceConstants.RETRIEVE);
          lijst = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        } catch (IOException e) {
          LOGGER.error("IO: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        } catch (JRException e) {
          LOGGER.error("JR: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("createLijst() niet toegestaan.");
    }
  }

  /**
   * Verwijder de Lijst uit de database en de List.
   */
  public void deleteLijst() {
    if (null != lijst
        && isVerwijder()) {
      LijstComponent lijstComponent = new LijstComponent();
      try {
        lijstComponent.delete(lijst.getLijst());
        lijsten.remove(lijst);
        setAktie(PersistenceConstants.RETRIEVE);
        lijst  = null;
      } catch (ObjectNotFoundException e) {
        addError("persistence.duplicate");
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    } else {
      LOGGER.error("deleteLijst() niet toegestaan.");
    }
  }

  /**
   * Geef alle Lijsten.
   * 
   * @return
   */
  public List<Lijst> getLijsten() {
    return lijsten;
  }

  /**
   * @return het bestand
   */
  public UploadedFile getBestand() {
    return bestand;
  }

  /**
   * Geef de geselecteerde/nieuwe Lijst.
   * 
   * @return
   */
  public Lijst getLijst() {
    return lijst;
  }

  /**
   * Start nieuwe lijst
   */
  public void nieuw() {
    setAktie(PersistenceConstants.CREATE);
    lijst     = new Lijst(new LijstDto());
    setSubTitel("title.lijst.create");
  }

  /**
   * Bewaar de Lijst in de database en in de List.
   */
  public void saveLijst() {
    if (null != lijst
        && isWijzig()) {
      if (valideerForm()) {
        LijstComponent  lijstComponent  = new LijstComponent();
        try {
          vulLijst();
          lijstComponent.update(lijst.getLijst());
          lijsten.remove(lijst);
          lijsten.add(lijst);
          setAktie(PersistenceConstants.RETRIEVE);
          lijst = null;
        } catch (DuplicateObjectException e) {
          addError("persistence.duplicate");
        } catch (ObjectNotFoundException e) {
          addError("persistence.notfound");
        } catch (DoosRuntimeException e) {
          LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        } catch (IOException e) {
          LOGGER.error("IO: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        } catch (JRException e) {
          LOGGER.error("JR: " + e.getLocalizedMessage(), e);
          generateExceptionMessage(e);
        }
      }
    } else {
      LOGGER.error("saveLijst() niet toegestaan.");
    }
  }

  /**
   * Zoek de Lijst(en) in de database.
   */
  public void searchLijst() {
    if (null != lijst
        && isZoek()) {
      try {
        Collection<LijstDto> rows  =
            new LijstComponent().getAll(lijst.getLijst()
                                             .<LijstDto>makeFilter());
        lijsten  = new ArrayList<Lijst>(rows.size());
        for (LijstDto lijstDto : rows) {
          lijsten.add(new Lijst(lijstDto));
        }
        setGefilterd(true);
        lijst     = null;
      } catch (ObjectNotFoundException e) {
        lijsten = null;
        addInfo("info.norows",
            new Object[] {getTekst("doos.title.lijsten").toLowerCase()});
      } catch (DoosRuntimeException e) {
        LOGGER.error("Runtime: " + e.getLocalizedMessage(), e);
        generateExceptionMessage(e);
      }
    }
  }

  /**
   * @param bestand het bestand
   */
  public void setBestand(UploadedFile bestand) {
    this.bestand  = bestand;
  }

  /**
   * Valideer de invoer.
   */
  @Override
  public boolean valideerForm() {
    boolean correct = true;
    String  waarde  = lijst.getLijst().getLijstnaam();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.lijstnaam"));
    }
    waarde  = lijst.getLijst().getOmschrijving();
    if (DoosUtils.isBlankOrNull(waarde)) {
      correct = false;
      addError("errors.required", getTekst("label.omschrijving"));
    }
    if (isNieuw()
        && DoosUtils.isBlankOrNull(bestand)) {
      correct = false;
      addError("errors.required", getTekst("label.selectFile"));
    }

    return correct;
  }

  /**
   * @param lijst
   */
  public void verwijder(Lijst lijst) {
    setAktie(PersistenceConstants.DELETE);
    this.lijst  = lijst;
    setSubTitel("title.lijst.delete");
  }

  /**
   * Vul de Lijst met de tekst en gecompileerde versie.
   * 
   * @throws IOException
   * @throws JRException 
   */
  private void vulLijst() throws IOException, JRException {
    String        report        =
        new Scanner(bestand.getInputStream()).useDelimiter("\\A").next();
    JasperReport  jasperReport  =
        JasperCompileManager
          .compileReport(new ByteArrayInputStream(report.getBytes()));

    lijst.getLijst().setLijst(report);
    lijst.getLijst().setJasperReport(ByteArray.toByteArray(jasperReport));
  }

  /**
   * @param lijst
   */
  public void wijzig(Lijst lijst) {
    setAktie(PersistenceConstants.UPDATE);
    this.lijst  = lijst;
    setSubTitel("title.lijst.update");
  }

  /**
   * Start zoek Lijst(en)
   */
  public void zoek() {
    setAktie(PersistenceConstants.SEARCH);
    lijst     = new Lijst(new LijstDto());
    setSubTitel("title.lijst.search");
  }
}
