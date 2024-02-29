-- Kreatie van alle objecten voor de DOOS database.
--
-- Copyright 2005 Marco de Booij
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

\echo    Passwords
\prompt 'DOOS_APP: ' doos_app_pw
\set q_doos_app_pw '\'':doos_app_pw'\''

-- Gebruikers en rollen.
CREATE ROLE DOOS_APP LOGIN
  PASSWORD :q_doos_app_pw
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

CREATE SCHEMA DOOS;

CREATE ROLE DOOS_SEL NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE DOOS_UPD NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

GRANT USAGE ON SCHEMA DOOS  TO DOOS_SEL;
GRANT USAGE ON SCHEMA DOOS  TO DOOS_UPD;

GRANT DOOS_UPD TO DOOS_APP;

GRANT CONNECT ON DATABASE :q_db_naam TO DOOS_APP;

-- Sequences

-- Tabellen
CREATE TABLE DOOS.I18N_CODES (
  CODE                            VARCHAR(100)    NOT NULL,
  CODE_ID                         INTEGER         NOT NULL  GENERATED ALWAYS AS IDENTITY,
  CONSTRAINT PK_I18N_CODES PRIMARY KEY (CODE_ID)
);

CREATE TABLE DOOS.I18N_CODE_TEKSTEN (
  CODE_ID                         INTEGER         NOT NULL,
  TAAL_KODE                       CHAR(2)         NOT NULL,
  TEKST                           VARCHAR(1024)   NOT NULL,
  CONSTRAINT PK_I18N_CODE_TEKSTEN PRIMARY KEY (CODE_ID, TAAL_KODE)
);

CREATE TABLE DOOS.I18N_LIJSTEN (
  CODE                            VARCHAR(100)    NOT NULL,
  LIJST_ID                        INTEGER         NOT NULL  GENERATED ALWAYS AS IDENTITY,
  OMSCHRIJVING                    VARCHAR(200),
  CONSTRAINT PK_I18N_LIJSTEN PRIMARY KEY (LIJST_ID)
);

CREATE TABLE DOOS.I18N_LIJST_CODES (
  CODE_ID                         INTEGER        NOT NULL,
  LIJST_ID                        INTEGER        NOT NULL,
  VOLGORDE                        NUMERIC(3)     NOT NULL DEFAULT 0,
  CONSTRAINT PK_I18N_LIJST_CODES PRIMARY KEY (LIJST_ID, VOLGORDE, CODE_ID)
);

CREATE TABLE DOOS.LIJSTEN (
  LIJST                           TEXT            NOT NULL,
  LIJSTNAAM                       VARCHAR(25)     NOT NULL,
  OMSCHRIJVING                    VARCHAR(100)    NOT NULL,
  CONSTRAINT PK_LIJSTEN PRIMARY KEY (LIJSTNAAM)
);

CREATE TABLE IF NOT EXISTS DOOS.LOGGING (
  LOGGER                          VARCHAR(100)    NOT NULL,
  LOG_ID                          INTEGER         NOT NULL  GENERATED ALWAYS AS IDENTITY,
  LOGTIME                         TIMESTAMP       NOT NULL,
  LVL                             VARCHAR(15)     NOT NULL,
  MESSAGE                         VARCHAR(1024)   NOT NULL,
  SEQ                             INTEGER         NOT NULL,
  SOURCECLASS                     VARCHAR(100)    NOT NULL,
  SOURCEMETHOD                    VARCHAR(100)    NOT NULL,
  THREAD_ID                       INTEGER         NOT NULL,
  CONSTRAINT PK_LOGGING PRIMARY KEY (LOG_ID)
);

CREATE TABLE IF NOT EXISTS DOOS.LOKALEN (
  CODE                            VARCHAR(100)    NOT NULL,
  EERSTE_TAAL                     CHAR(3)         NOT NULL,
  TWEEDE_TAAL                     CHAR(3),
  CONSTRAINT PK_LOKALEN PRIMARY KEY (CODE)
);

CREATE TABLE DOOS.PARAMS (
  SLEUTEL                         VARCHAR(100)    NOT NULL,
  WAARDE                          VARCHAR(255)    NOT NULL,
  CONSTRAINT PK_PARAMS PRIMARY KEY (SLEUTEL)
);

CREATE TABLE DOOS.QUARTZJOBS (
  CRON                            VARCHAR(50)     NOT NULL,
  GROEP                           VARCHAR(15)     NOT NULL,
  JAVACLASS                       VARCHAR(100)    NOT NULL,
  JOB                             VARCHAR(15)     NOT NULL,
  OMSCHRIJVING                    VARCHAR(100)    NOT NULL,
  CONSTRAINT PK_QUARTZJOBS PRIMARY KEY (GROEP, JOB)
);

CREATE TABLE DOOS.TALEN (
  ISO_639_1                       CHAR(2),
  ISO_639_2B                      CHAR(3),
  ISO_639_2T                      CHAR(3)         NOT NULL,
  ISO_639_3                       CHAR(3),
  LEVEND                          CHAR(1)         NOT NULL  DEFAULT 'J',
  TAAL_ID                         INTEGER         NOT NULL  GENERATED ALWAYS AS IDENTITY,
  CONSTRAINT PK_TALEN PRIMARY KEY (TAAL_ID)
);

CREATE TABLE DOOS.TAALNAMEN (
  ISO_639_2T                      CHAR(3)         NOT NULL,
  NAAM                            VARCHAR(100)    NOT NULL,
  TAAL_ID                         INTEGER         NOT NULL,
  CONSTRAINT PK_TAALNAMEN PRIMARY KEY (TAAL_ID, ISO_639_2T)
);

-- Views
CREATE OR REPLACE VIEW DOOS.I18N_SELECTIES AS
SELECT   COD.CODE_ID, LST.CODE SELECTIE,
         SUBSTRING(COD.CODE FROM LENGTH(LST.CODE)+2) CODE,
         COALESCE(LCD.VOLGORDE, 0) VOLGORDE
FROM     DOOS.I18N_CODES COD
                JOIN DOOS.I18N_LIJSTEN      LST ON COD.CODE LIKE LST.CODE||'%'
           LEFT JOIN DOOS.I18N_LIJST_CODES  LCD ON COD.CODE_ID=LCD.CODE_ID;

-- Constraints
ALTER TABLE DOOS.I18N_CODES
  ADD CONSTRAINT UK_ICD_CODE UNIQUE (CODE);

ALTER TABLE DOOS.I18N_CODE_TEKSTEN
  ADD CONSTRAINT FK_ICT_CODE_ID FOREIGN KEY (CODE_ID)
  REFERENCES DOOS.I18N_CODES (CODE_ID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.I18N_LIJST_CODES
  ADD CONSTRAINT FK_ILC_CODE_ID FOREIGN KEY (CODE_ID)
  REFERENCES DOOS.I18N_CODES (CODE_ID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.I18N_LIJST_CODES
  ADD CONSTRAINT FK_ILC_LIJST_ID FOREIGN KEY (LIJST_ID)
  REFERENCES DOOS.I18N_LIJSTEN (LIJST_ID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.I18N_LIJSTEN
  ADD CONSTRAINT UK_ILS_CODE UNIQUE (CODE);

ALTER TABLE DOOS.LIJSTEN
  ADD CONSTRAINT CHK_LST_LIJSTNAAM  CHECK (LIJSTNAAM = LOWER(LIJSTNAAM));

ALTER TABLE DOOS.LOKALEN
  ADD CONSTRAINT FK_ĹOK_EERSTE_TAAL FOREIGN KEY (EERSTE_TAAL)
  REFERENCES DOOS.TALEN (ISO_639_2T)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.LOKALEN
  ADD CONSTRAINT FK_ĹOK_TWEEDE_TAAL FOREIGN KEY (TWEEDE_TAAL)
  REFERENCES DOOS.TALEN (ISO_639_2T)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_1  CHECK (ISO_639_1 = LOWER(ISO_639_1));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_1   UNIQUE (ISO_639_1);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_2B CHECK (ISO_639_2B = LOWER(ISO_639_2B));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_2B  UNIQUE (ISO_639_2B);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_2T CHECK (ISO_639_2T = LOWER(ISO_639_2T));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_2T  UNIQUE (ISO_639_2T);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_3  CHECK (ISO_639_3 = LOWER(ISO_639_3));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_3   UNIQUE (ISO_639_3);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_LEVEND     CHECK(LEVEND = ANY (ARRAY['J', 'N']));

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_TAAL_ID FOREIGN KEY (TAAL_ID)
  REFERENCES DOOS.TALEN (TAAL_ID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_ISO_639_2T FOREIGN KEY (ISO_639_2T)
  REFERENCES DOOS.TALEN (ISO_639_2T)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

-- Grant rechten
GRANT SELECT                         ON TABLE DOOS.I18N_CODES        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LIJSTEN           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LOGGING           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LOKALEN           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.PARAMS            TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.QUARTZJOBS        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.TALEN             TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.TAALNAMEN         TO DOOS_SEL;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODES        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_UPD;
GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.LIJSTEN           TO DOOS_UPD;
GRANT SELECT, DELETE                 ON TABLE DOOS.LOGGING           TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.LOKALEN           TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.PARAMS            TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.QUARTZJOBS        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.TALEN             TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.TAALNAMEN         TO DOOS_UPD;

GRANT SELECT, UPDATE ON SEQUENCE DOOS.SEQ_I18N_CODES   TO DOOS_UPD;
GRANT SELECT, UPDATE ON SEQUENCE DOOS.SEQ_I18N_LIJSTEN TO DOOS_UPD;

-- Commentaren
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
COMMENT ON TABLE  DOOS.LOGGING                      IS 'Deze tabel bevat de log messages van de applicaties.';
COMMENT ON COLUMN DOOS.LOGGING.LOGGER               IS 'De naam van de logger.';
COMMENT ON COLUMN DOOS.LOGGING.LOG_ID               IS 'De sleutel van de logging.';
COMMENT ON COLUMN DOOS.LOGGING.LOGTIME              IS 'Het tijdstip waarop de message is ge''throw''d.';
COMMENT ON COLUMN DOOS.LOGGING.LVL                  IS 'Loglevel van de message.';
COMMENT ON COLUMN DOOS.LOGGING.MESSAGE              IS 'De message.';
COMMENT ON COLUMN DOOS.LOGGING.SEQ                  IS 'De sequence van de message.';
COMMENT ON COLUMN DOOS.LOGGING.SOURCECLASS          IS 'De class die de message heeft ge''throw''d.';
COMMENT ON COLUMN DOOS.LOGGING.SOURCEMETHOD         IS 'De method die de message heeft ge''throw''d.';
COMMENT ON COLUMN DOOS.LOGGING.THREAD_ID            IS 'De ID van thread die de message heeft ge''throw''d.';
COMMENT ON VIEW   DOOS.I18N_SELECTIES               IS 'Deze view bevat alle selectie opties die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.CODE          IS 'De I18N code zonder de prefix van de I18N codes van waaruit de I18N lijst bestaat.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.CODE_ID       IS 'De sleutel van de I18N code.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.SELECTIE      IS 'De prefix van de I18N codes van waaruit de I18N lijst bestaat.';
COMMENT ON COLUMN DOOS.I18N_SELECTIES.VOLGORDE      IS 'De volgorde waarin de I18N codes moeten staan.';
COMMENT ON TABLE  DOOS.LIJSTEN                      IS 'Deze tabel bevat alle lijsten die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.LIJSTEN.LIJST                IS 'De broncode van de lijst die gebruikt wordt in de applicatie.';
COMMENT ON COLUMN DOOS.LIJSTEN.LIJSTNAAM            IS 'De sleutel van de lijst.';
COMMENT ON COLUMN DOOS.LIJSTEN.OMSCHRIJVING         IS 'De omschrijving van de lijst.';
COMMENT ON TABLE  DOOS.LOGGING                      IS 'Deze tabel bevat de logging van de applicaties.';
COMMENT ON COLUMN DOOS.LOGGING.LOGGER               IS 'De naam van de logger.';
COMMENT ON COLUMN DOOS.LOGGING.LOG_ID               IS 'De sleutel van de log.';
COMMENT ON COLUMN DOOS.LOGGING.LOGTIME              IS 'Het tijdstip van de log.';
COMMENT ON COLUMN DOOS.LOGGING.LVL                  IS 'Het log niveau van de log.';
COMMENT ON COLUMN DOOS.LOGGING.MESSAGE              IS 'De melding.';
COMMENT ON COLUMN DOOS.LOGGING.SEQ                  IS 'Het volgnummer van de log..';
COMMENT ON COLUMN DOOS.LOGGING.SOURCECLASS          IS 'De class die de log heeft gedaan.';
COMMENT ON COLUMN DOOS.LOGGING.SOURCEMETHOD         IS 'De mithod die de log heeft gedaan.';
COMMENT ON COLUMN DOOS.LOGGING.THREAD_ID            IS 'De thread die de log heeft gedaan.';
COMMENT ON TABLE  DOOS.LOKALEN                      IS 'Deze tabel bevat alle lokalen (Locales) die er bestaan.';
COMMENT ON COLUMN DOOS.LOKALEN.CODE                 IS 'De lokale code. Het is de sleutel van de lokale.';
COMMENT ON COLUMN DOOS.LOKALEN.EERSTE_TAAL          IS 'De taal die bij de lokale behoort.';
COMMENT ON COLUMN DOOS.LOKALEN.TWEEDE_TAAL          IS 'De taal die gebruikt mag worden als de eerste taal niet beschikbaar is.';
COMMENT ON TABLE  DOOS.PARAMS                       IS 'Deze tabel bevat alle parameters die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.PARAMS.SLEUTEL               IS 'De sleutel van de parameter.';
COMMENT ON COLUMN DOOS.PARAMS.WAARDE                IS 'De waarde van de parameter.';
COMMENT ON TABLE  DOOS.QUARTZJOBS                   IS 'Deze tabel bevat alle Quartz jobs die in de applicaties gebruikt worden.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.CRON              IS 'De cron expressie van de job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.GROEP             IS 'De groep (applicatie) waartoe deze job behoort.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.JAVACLASS         IS 'De Java class van de Quartz job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.JOB               IS 'De ''ID'' van de Quartz job.';
COMMENT ON COLUMN DOOS.QUARTZJOBS.OMSCHRIJVING      IS 'De omschrijving van de Quartz job.';
COMMENT ON TABLE  DOOS.TALEN                        IS 'Deze tabel bevat de talen van de wereld.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_1              IS 'De ISO 639-1 code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_2B             IS 'De ISO 639-2B code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_2T             IS 'De ISO 639-2T code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_3              IS 'De ISO 639-3 code.';
COMMENT ON COLUMN DOOS.TALEN.LEVEND                 IS 'Is het een levende taal?';
COMMENT ON COLUMN DOOS.TALEN.TAAL_ID                IS 'De sleutel van de taal.';
COMMENT ON TABLE  DOOS.TAALNAMEN                    IS 'Deze tabel bevat namen van de talen van de wereld.';
COMMENT ON COLUMN DOOS.TAALNAMEN.ISO_639_2T         IS 'De ISO 639-2T code van de taal van de naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.NAAM               IS 'De naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.TAAL_ID            IS 'De De sleutel van de taal.';
