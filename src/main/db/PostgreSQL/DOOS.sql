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
  MINVALUE 0
  MAXVALUE 9223372036854775807
  START 0
  CACHE 1;

CREATE SEQUENCE DOOS.SEQ_I18N_LIJSTEN
  INCREMENT 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  START 0
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

-- Constraints
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

-- Grant rechten
GRANT SELECT                         ON TABLE DOOS.I18N_CODES        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.LIJSTEN           TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.PARAMETERS        TO DOOS_SEL;
GRANT SELECT                         ON TABLE DOOS.TALEN             TO DOOS_SEL;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODES        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_CODE_TEKSTEN TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJSTEN      TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.I18N_LIJST_CODES  TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.LIJSTEN           TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.PARAMETERS        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.TALEN             TO DOOS_UPD;

GRANT SELECT, UPDATE ON SEQUENCE DOOS.SEQ_I18N_CODES   TO DOOS_UPD;
GRANT SELECT, UPDATE ON SEQUENCE DOOS.SEQ_I18N_LIJSTEN TO DOOS_UPD;

