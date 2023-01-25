/*
 * Copyright (c) 2023 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
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
package eu.debooy.doos.model;

import eu.debooy.doos.TestConstants;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ExportDataTest {
  private static  Object[]            data;
  private static  ExportData          exportData;
  private static  String[]            kolommen;
  private static  Map<String, String> metadata;
  private static  Map<String, String> parameters;
  private static  Map<String, String> velden;

  @BeforeClass
  public static void setUpClass() {
    data        = new Object[] { TestConstants.DATA_1, TestConstants.DATA_2 };
    exportData  = new ExportData();
    kolommen    = new String[] { TestConstants.KOL_1, TestConstants.KOL_2 };
    metadata    = new HashMap<>();
    parameters  = new HashMap<>();
    velden      = new HashMap<>();

    metadata.put(TestConstants.MTD_1, TestConstants.METADATA_1);
    metadata.put(TestConstants.MTD_2, TestConstants.METADATA_2);

    parameters.put(TestConstants.PAR_1, TestConstants.PARAM_1);
    parameters.put(TestConstants.PAR_2, TestConstants.PARAM_2);

    velden.put(TestConstants.VLD_1, TestConstants.VELD_1);
    velden.put(TestConstants.VLD_2, TestConstants.VELD_2);

    exportData.addData(data);
    exportData.setKolommen(kolommen);
    exportData.setMetadata(metadata);
    exportData.setParameters(parameters);
    exportData.setVelden(velden);
  }

  @Test
  public void testAddData() {
    var instance  = new ExportData();
    var dat       = new Object[] { TestConstants.DATA_3 };

    instance.addData(data);

    assertEquals(1, instance.getData().size());
    assertArrayEquals(data, instance.getData().get(0));

    instance.addData(dat);

    assertEquals(2, instance.getData().size());
    assertArrayEquals(dat, instance.getData().get(1));
  }

  @Test
  public void testAddMetadata() {
    var instance  = new ExportData();

    instance.addMetadata(TestConstants.MTD_3, TestConstants.METADATA_3);

    assertTrue(instance.hasMetadata(TestConstants.MTD_3));

    instance.addMetadata(TestConstants.MTD_2, TestConstants.METADATA_2);

    assertTrue(instance.hasMetadata(TestConstants.MTD_2));
    assertTrue(instance.hasMetadata(TestConstants.MTD_3));
  }

  @Test
  public void testAddParameter() {
    var instance  = new ExportData();

    instance.addParameter(TestConstants.PAR_3, TestConstants.PARAM_3);

    assertEquals(1, instance.getParameters().size());
    assertTrue(instance.hasParameter(TestConstants.PAR_3));

    instance.addParameter(TestConstants.PAR_2, TestConstants.PARAM_2);

    assertEquals(2, instance.getParameters().size());
    assertTrue(instance.hasParameter(TestConstants.PAR_2));
    assertTrue(instance.hasParameter(TestConstants.PAR_3));
  }

  @Test
  public void testAddVeld() {
    var instance  = new ExportData();

    instance.addVeld(TestConstants.VLD_3, TestConstants.VELD_3);

    assertEquals(1, instance.getVelden().size());
    assertTrue(instance.hasVeld(TestConstants.VLD_3));

    instance.addVeld(TestConstants.VLD_2, TestConstants.VELD_2);

    assertEquals(2, instance.getVelden().size());
    assertTrue(instance.hasVeld(TestConstants.VLD_2));
    assertTrue(instance.hasVeld(TestConstants.VLD_3));
  }

  @Test
  public void testGetKolommen() {
    assertArrayEquals(kolommen, exportData.getKolommen());
  }

  @Test
  public void testGetMetadata() {
    assertEquals(TestConstants.METADATA_1,
                 exportData.getMetadata(TestConstants.MTD_1));
    assertEquals("", exportData.getMetadata(TestConstants.MTD_9));
  }

  @Test
  public void testGetParameter() {
    assertEquals(TestConstants.PARAM_1,
                 exportData.getParameter(TestConstants.PAR_1));
    assertEquals("", exportData.getParameter(TestConstants.PAR_9));
  }

  @Test
  public void testGetParameters() {
    assertEquals(parameters, exportData.getParameters());
  }

  @Test
  public void testGetType() {
    assertEquals(TestConstants.TYPE_PDF, exportData.getType());
  }

  @Test
  public void testGetVeld() {
    assertEquals(TestConstants.VELD_1, exportData.getVeld(TestConstants.VLD_1));
    assertEquals("", exportData.getVeld(TestConstants.PAR_9));
  }

  @Test
  public void testGetVelden() {
    assertEquals(velden, exportData.getVelden());
  }

  @Test
  public void testGetData() {
    assertEquals(1, exportData.getData().size());
    assertArrayEquals(data, exportData.getData().get(0));
  }

  @Test
  public void testHasKolommen() {
    assertTrue(exportData.hasKolommen());
  }

  @Test
  public void testHasMetadata() {
    assertTrue(exportData.hasMetadata(TestConstants.MTD_1));
    assertFalse(exportData.hasMetadata(TestConstants.MTD_9));
  }

  @Test
  public void testHasParameter() {
    assertTrue(exportData.hasParameter(TestConstants.PAR_1));
    assertFalse(exportData.hasParameter(TestConstants.PAR_9));
  }

  @Test
  public void testHasVeld() {
    assertTrue(exportData.hasVeld(TestConstants.VLD_1));
    assertFalse(exportData.hasVeld(TestConstants.PAR_9));
  }

  @Test
  public void testSetKolommen() {
    var instance  = new ExportData();
    var kols      = new String[]{ TestConstants.KOL_3};

    assertFalse(instance.hasKolommen());

    instance.setKolommen(kolommen);

    assertEquals(2, instance.getKolommen().length);

    instance.setKolommen(kols);

    assertEquals(1, instance.getKolommen().length);
  }

  @Test
  public void testSetParameters() {
    var                 instance = new ExportData();
    Map<String, String> params   = new HashMap<>();

    params.put(TestConstants.PAR_3, TestConstants.PARAM_3);

    assertTrue(instance.getParameters().isEmpty());

    instance.setParameters(parameters);

    assertEquals(2, instance.getParameters().size());

    instance.setParameters(params);

    assertEquals(1, instance.getParameters().size());
    assertTrue(instance.hasParameter(TestConstants.PAR_3));
  }

  @Test
  public void testSetType() {
    var instance  = new ExportData();

    assertEquals(TestConstants.TYPE_PDF, instance.getType());

    instance.setType(TestConstants.TYPE_CSV);

    assertEquals(TestConstants.TYPE_CSV, instance.getType());
  }
}
