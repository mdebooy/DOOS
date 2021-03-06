-- Patch van de objecten voor de DOOS database.
-- 
-- Copyright 2019 Marco de Booij
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

CREATE TABLE DOOS.QUARTZJOBS (
  CRON                            VARCHAR2(50)    NOT NULL,
  GROEP                           VARCHAR2(15)    NOT NULL,
  JAVACLASS                       VARCHAR2(100)   NOT NULL,
  JOB                             VARCHAR2(15)    NOT NULL,
  OMSCHRIJVING                    VARCHAR2(100)   NOT NULL,
  CONSTRAINT PK_QUARTZJOBS PRIMARY KEY (GROEP, JOB)
);

-- Grant rechten
GRANT SELECT                         ON DOOS.QUARTZJOBS        TO DOOS_SEL;

GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.QUARTZJOBS        TO DOOS_UPD;

COMMENT ON TABLE  DOOS.QUARTZJOBS                   IS 'Deze tabel bevat alle Quartz jobs die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.CRON              IS 'De cron expressie van de job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.GROEP             IS 'De groep (applicatie) waartoe deze job behoort.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.JAVACLASS         IS 'De Java class van de Quartz job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.JOB               IS 'De ''ID'' van de Quartz job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.OMSCHRIJVING      IS 'De omschrijving van de Quartz job.';
