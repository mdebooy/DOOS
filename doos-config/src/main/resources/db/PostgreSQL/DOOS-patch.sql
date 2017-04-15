-- Patch van de objecten voor de DOOS database.
-- 
-- Copyright 2017 Marco de Booij
--
-- Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
-- the European Commission - subsequent versions of the EUPL (the "Licence");
-- you may not use this work except in compliance with the Licence. You may
-- obtain a copy of the Licence at:
--
-- http://www.osor.eu/eupl
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
-- WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the Licence for the specific language governing permissions and
-- limitations under the Licence.
--
-- Project: DOOS
-- Author : Marco de Booij

\prompt 'Database: ' db_naam

-- Connect als DOOS om de uit te voeren.
\c :db_naam doos

ALTER TABLE DOOS.I18N_LIJSTEN
  ADD CONSTRAINT UK_ILS_CODE UNIQUE (CODE);

CREATE OR REPLACE VIEW DOOS.I18N_SELECTIES AS
SELECT   COD.CODE_ID, LST.CODE SELECTIE,
         SUBSTRING(COD.CODE FROM LENGTH(LST.CODE)+2) CODE,
         COALESCE(LCD.VOLGORDE, 0) VOLGORDE
FROM     DOOS.I18N_CODES COD
                JOIN DOOS.I18N_LIJSTEN      LST ON COD.CODE LIKE LST.CODE||'%'
           LEFT JOIN DOOS.I18N_LIJST_CODES  LCD ON COD.CODE_ID=LCD.CODE_ID;

GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_UPD;

COMMENT ON TABLE  DOOS.I18N_CODES                   IS 'Deze tabel bevat alle I18N codes die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.I18N_CODES.CODE              IS 'De code die in de applicatie gebruikt wordt.';
COMMENT ON COLUMN DOOS.I18N_CODES.CODE_ID           IS 'De sleutel van de I18N code.';
COMMENT ON TABLE  DOOS.I18N_CODE_TEKSTEN            IS 'Deze tabel bevat de teksten die bij de I18N codes horen in verschllende talen.';
COMMENT ON COLUMN DOOS.I18N_CODE_TEKSTEN.CODE_ID    IS 'De sleutel van de I18N code.';
COMMENT ON COLUMN DOOS.I18N_CODE_TEKSTEN.TAAL_KODE  IS 'De sleutel van de taal.';
COMMENT ON COLUMN DOOS.I18N_CODE_TEKSTEN.TEKST      IS 'De I18N tekst in een taal.';
COMMENT ON TABLE  DOOS.I18N_LIJSTEN                 IS 'Deze tabel bevat alle selectie lijsten die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.I18N_LIJSTEN.CODE            IS 'De prefix van de I18N codes van waaruit de I18N lijst bestaat.';
COMMENT ON COLUMN DOOS.I18N_LIJSTEN.LIJST_ID        IS 'De sleutel van de I18N lijst.';
COMMENT ON COLUMN DOOS.I18N_LIJSTEN.OMSCHRIJVING    IS 'De omschrijving van de I18N lijst.';
COMMENT ON TABLE  DOOS.I18N_LIJST_CODES             IS 'Deze tabel bevat de I18N codes die in een speciale volgorde in de selecties moeten staan.';
COMMENT ON COLUMN DOOS.I18N_LIJST_CODES.CODE_ID     IS 'De sleutel van de I18N code.';
COMMENT ON COLUMN DOOS.I18N_LIJST_CODES.LIJST_ID    IS 'De sleutel van de I18N lijsten.';
COMMENT ON COLUMN DOOS.I18N_LIJST_CODES.VOLGORDE    IS 'De volgorde waarin de I18N codes moeten staan.';
COMMENT ON VIEW   DOOS.I18N_SELECTIES               IS 'Deze view bevat alle selectie opties die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.CODE          IS 'De I18N code zonder de prefix van de I18N codes van waaruit de I18N lijst bestaat.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.CODE_ID       IS 'De sleutel van de I18N code.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.SELECTIE      IS 'De prefix van de I18N codes van waaruit de I18N lijst bestaat.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.VOLGORDE      IS 'De volgorde waarin de I18N codes moeten staan.';
COMMENT ON TABLE  DOOS.LIJSTEN                      IS 'Deze tabel bevat alle lijsten die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.LIJSTEN.JASPER_REPORT        IS 'De gecompileerde lijst die gebruikt wordt in de applicatie.';
COMMENT ON COLUMN DOOS.LIJSTEN.LIJST                IS 'De broncode van de lijst die gebruikt wordt in de applicatie.';
COMMENT ON COLUMN DOOS.LIJSTEN.LIJSTNAAM            IS 'De sleutel van de lijst.';
COMMENT ON COLUMN DOOS.LIJSTEN.OMSCHRIJVING         IS 'De omschrijving van de lijst.';
COMMENT ON TABLE  DOOS.PARAMETERS                   IS 'Deze tabel bevat alle parameters die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.PARAMETERS.SLEUTEL           IS 'De sleutel van de parameter.';
COMMENT ON COLUMN DOOS.PARAMETERS.WAARDE            IS 'De waarde van de parameter.';
COMMENT ON TABLE  DOOS.TALEN                        IS 'Deze tabel bevat de talen van de wereld.';
COMMENT ON COLUMN DOOS.TALEN.EIGENNAAM              IS 'De naam van de taal in die taal.';
COMMENT ON COLUMN DOOS.TALEN.TAAL                   IS 'De naam van de taal.';
COMMENT ON COLUMN DOOS.TALEN.TAAL_KODE              IS 'De sleutel van de taal (ISO2).';

