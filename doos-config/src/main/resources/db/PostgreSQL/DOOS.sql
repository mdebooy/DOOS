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

\prompt 'Database: ' db_naam
\echo    Passwords
\prompt 'DOOS    : ' doos_pw
\prompt 'DOOS_APP: ' doos_app_pw
\set q_db_naam     '\"':db_naam'\"'
\set q_doos_pw     '\'':doos_pw'\''
\set q_doos_app_pw '\'':doos_app_pw'\''

-- Gebruikers en rollen.
CREATE ROLE DOOS LOGIN
  PASSWORD :q_doos_pw
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

CREATE ROLE DOOS_APP LOGIN
  PASSWORD :q_doos_app_pw
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

CREATE SCHEMA DOOS AUTHORIZATION DOOS;

CREATE ROLE DOOS_SEL NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
CREATE ROLE DOOS_UPD NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

GRANT USAGE ON SCHEMA DOOS  TO DOOS_SEL;
GRANT USAGE ON SCHEMA DOOS  TO DOOS_UPD;

GRANT DOOS_UPD TO DOOS_APP;

GRANT CONNECT ON DATABASE :q_db_naam TO DOOS;
GRANT CONNECT ON DATABASE :q_db_naam TO DOOS_APP;

-- Connect als DOOS om de objecten te maken
\c :db_naam doos

-- Sequences
CREATE SEQUENCE DOOS.SEQ_I18N_CODES
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

CREATE SEQUENCE DOOS.SEQ_I18N_LIJSTEN
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS DOOS.SEQ_LOGGING
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;

-- Tabellen
CREATE TABLE DOOS.I18N_CODES (
  CODE                            VARCHAR(100)    NOT NULL,
  CODE_ID                         INTEGER         NOT NULL DEFAULT NEXTVAL('DOOS.SEQ_I18N_CODES'::REGCLASS),
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
  LIJST_ID                        INTEGER         NOT NULL DEFAULT NEXTVAL('DOOS.SEQ_I18N_LIJSTEN'::REGCLASS),
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
  JASPER_REPORT                   BYTEA           NOT NULL,
  LIJST                           TEXT            NOT NULL,
  LIJSTNAAM                       VARCHAR(25)     NOT NULL,
  OMSCHRIJVING                    VARCHAR(100)    NOT NULL,
  CONSTRAINT PK_LIJSTEN PRIMARY KEY (LIJSTNAAM)
);
CREATE TABLE IF NOT EXISTS DOOS.LOGGING (
  LOGGER                          VARCHAR(100)    NOT NULL, 
  LOG_ID                          INTEGER         NOT NULL DEFAULT NEXTVAL('DOOS.SEQ_LOGGING'::REGCLASS),
  LOGTIME                         TIMESTAMP       NOT NULL,
  LVL                             VARCHAR(15)     NOT NULL,
  MESSAGE                         VARCHAR(1024)   NOT NULL,
  SEQ                             INTEGER         NOT NULL,
  SOURCECLASS                     VARCHAR(100)    NOT NULL,
  SOURCEMETHOD                    VARCHAR(100)    NOT NULL,
  THREAD_ID                       INTEGER         NOT NULL,
  CONSTRAINT PK_LOGGING PRIMARY KEY (LOG_ID)
);

CREATE TABLE DOOS.PARAMETERS (
  SLEUTEL                         VARCHAR(100)    NOT NULL,
  WAARDE                          VARCHAR(255)    NOT NULL,
  CONSTRAINT PK_PARAMETERS PRIMARY KEY (SLEUTEL)
);

CREATE TABLE DOOS.TALEN (
  EIGENNAAM                       VARCHAR(100)    NOT NULL,
  TAAL                            VARCHAR(100)    NOT NULL,
  TAAL_KODE                       CHAR(2)         NOT NULL,
  CONSTRAINT PK_TALEN PRIMARY KEY (TAAL_KODE)
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

-- Grant rechten
GRANT SELECT                         ON TABLE DOOS.I18N_CODES        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LIJSTEN           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LOGGING           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.PARAMETERS        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.TALEN             TO DOOS_SEL;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODES        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_UPD;
GRANT SELECT                         ON TABLE DOOS.I18N_SELECTIES    TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.LIJSTEN           TO DOOS_UPD;
GRANT SELECT, DELETE                 ON TABLE DOOS.LOGGING           TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.PARAMETERS        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.TALEN             TO DOOS_UPD;

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
