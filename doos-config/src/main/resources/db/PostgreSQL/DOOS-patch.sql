-- Patch van de objecten voor de DOOS database.
-- 
-- Copyright 2018 Marco de Booij
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

ALTER TABLE DOOS.I18N_LIJST_CODES
  DROP CONSTRAINT PK_I18N_LIJST_CODES;

ALTER TABLE DOOS.I18N_LIJST_CODES
  ADD CONSTRAINT PK_I18N_LIJST_CODES PRIMARY KEY (LIJST_ID, CODE_ID)

DROP  VIEW DOOS.I18N_SELECTIES;

CREATE OR REPLACE VIEW DOOS.I18N_SELECTIES AS
SELECT   COD.CODE_ID, LST.CODE SELECTIE,
         SUBSTRING(COD.CODE FROM LENGTH(LST.CODE)+2) CODE,
         COALESCE(LCD.VOLGORDE, 0) VOLGORDE
FROM     DOOS.I18N_CODES COD
                JOIN DOOS.I18N_LIJSTEN      LST ON COD.CODE LIKE LST.CODE||'%'
           LEFT JOIN DOOS.I18N_LIJST_CODES  LCD ON COD.CODE_ID=LCD.CODE_ID;
           
GRANT SELECT ON TABLE DOOS.I18N_SELECTIES TO DOOS_SEL;
GRANT SELECT ON TABLE DOOS.I18N_SELECTIES TO DOOS_UPD;

