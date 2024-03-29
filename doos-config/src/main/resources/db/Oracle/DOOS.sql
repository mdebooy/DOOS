-- Kreatie van alle objecten voor de DOOS database.
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

ACCEPT DOOS_PW      PROMPT 'DOOS Password             : '
ACCEPT DOOS_APP_PW  PROMPT 'DOOS app Password         : '
ACCEPT DEFTS        PROMPT 'Default tablespace [USERS]: ' default USERS

-- Gebruikers en rollen.
CREATE USER DOOS IDENTIFIED BY &&DOOS_PW
DEFAULT   TABLESPACE &&DEFTS
TEMPORARY TABLESPACE TEMP;

ALTER USER DOOS QUOTA UNLIMITED ON &&DEFTS ;
GRANT RESOURCE TO DOOS;
GRANT CREATE SESSION TO DOOS;
GRANT CREATE VIEW TO DOOS;
GRANT CREATE SEQUENCE TO DOOS;
GRANT CREATE TABLE TO DOOS;
GRANT CREATE TRIGGER TO DOOS;
GRANT UNLIMITED TABLESPACE TO DOOS;

CREATE USER DOOS_APP IDENTIFIED BY &&DOOS_APP_PW
DEFAULT   TABLESPACE USERS
TEMPORARY TABLESPACE TEMP;

UNDEF DEFTS
UNDEF DOOS_PW
UNDEF DOOS_APP_PW

GRANT CREATE SESSION TO DOOS_APP;

CREATE ROLE DOOS_SEL;
CREATE ROLE DOOS_UPD;

GRANT DOOS_UPD TO DOOS_APP;

-- Sequences
CREATE SEQUENCE DOOS.SEQ_I18N_CODES
  INCREMENT BY 1
  MINVALUE 1
  START WITH 1;

CREATE SEQUENCE DOOS.SEQ_I18N_LIJSTEN
  INCREMENT BY 1
  MINVALUE 1
  START WITH 1;

CREATE SEQUENCE DOOS.SEQ_TALEN
  INCREMENT BY 1
  MINVALUE 1
  START WITH 1;

-- Tabellen
CREATE TABLE DOOS.I18N_CODES (
  CODE                            VARCHAR2(100)   NOT NULL,
  CODE_ID                         NUMBER          NOT NULL,
  CONSTRAINT PK_I18N_CODES PRIMARY KEY (CODE_ID)
);

CREATE TABLE DOOS.I18N_CODE_TEKSTEN (
  CODE_ID                         NUMBER          NOT NULL,
  TAAL_KODE                       CHAR(2)         NOT NULL,
  TEKST                           VARCHAR2(1024)  NOT NULL,
  CONSTRAINT PK_I18N_CODE_TEKSTEN PRIMARY KEY (CODE_ID, TAAL_KODE)
);

CREATE TABLE DOOS.I18N_LIJSTEN (
  CODE                            VARCHAR2(100)   NOT NULL,
  LIJST_ID                        NUMBER          NOT NULL,
  OMSCHRIJVING                    VARCHAR2(200),
  CONSTRAINT PK_I18N_LIJSTEN PRIMARY KEY (LIJST_ID)
);

CREATE TABLE DOOS.I18N_LIJST_CODES (
  CODE_ID                         NUMBER         NOT NULL,
  LIJST_ID                        NUMBER         NOT NULL,
  VOLGORDE                        NUMBER(3)      NOT NULL DEFAULT 0,
  CONSTRAINT PK_I18N_LIJST_CODES PRIMARY KEY (LIJST_ID, VOLGORDE, CODE_ID)
);

CREATE TABLE DOOS.LIJSTEN (
  JASPER_REPORT                   BLOB            NOT NULL,
  LIJST                           CLOB            NOT NULL,
  LIJSTNAAM                       VARCHAR2(25)    NOT NULL,
  OMSCHRIJVING                    VARCHAR2(100)   NOT NULL,
  CONSTRAINT PK_LIJSTEN PRIMARY KEY (LIJSTNAAM)
);

CREATE TABLE DOOS.PARAMS (
  SLEUTEL                         VARCHAR2(100)   NOT NULL,
  WAARDE                          VARCHAR2(255)   NOT NULL,
  CONSTRAINT PK_PARAMS PRIMARY KEY (SLEUTEL)
);

CREATE TABLE DOOS.QUARTZJOBS (
  CRON                            VARCHAR2(50)    NOT NULL,
  GROEP                           VARCHAR2(15)    NOT NULL,
  JAVACLASS                       VARCHAR2(100)   NOT NULL,
  JOB                             VARCHAR2(15)    NOT NULL,
  OMSCHRIJVING                    VARCHAR2(100)   NOT NULL,
  CONSTRAINT PK_QUARTZJOBS PRIMARY KEY (GROEP, JOB)
);

ALTER TABLE DOOS.TALEN (
  ISO_639_1                       CHAR(2),
  ISO_639_2B                      CHAR(3)         NOT NULL,
  ISO_639_2T                      CHAR(3),
  ISO_639_3                       CHAR(3),
  TAAL_ID                         NUMBER          NOT NULL,
  CONSTRAINT PK_TALEN PRIMARY KEY (TAAL_ID)
);

CREATE TABLE DOOS.TAALNAMEN (
  ISO_639_2T                      CHAR(3)         NOT NULL,
  NAAM                            VARCHAR(100)    NOT NULL,
  TAAL_ID                         NUMBER          NOT NULL,
  CONSTRAINT PK_TALEN PRIMARY KEY (TAAL_ID, ISO_639_2T)
);

-- Views
CREATE OR REPLACE VIEW DOOS.I18N_SELECTIES AS
SELECT   COD.CODE_ID, LST.CODE SELECTIE,
         SUBSTR(COD.CODE,LENGTH(LST.CODE)+2) CODE,
         COALESCE(LCD.VOLGORDE, 0) VOLGORDE
FROM     DOOS.I18N_CODES COD
                JOIN DOOS.I18N_LIJSTEN      LST ON COD.CODE LIKE LST.CODE||'%'
           LEFT JOIN DOOS.I18N_LIJST_CODES  LCD ON COD.CODE_ID=LCD.CODE_ID;

-- Triggers
CREATE OR REPLACE TRIGGER DOOS.INS_I18N_CODES
  BEFORE INSERT ON DOOS.I18N_CODES
  FOR EACH ROW
BEGIN
  SELECT DOOS.SEQ_I18N_CODES.NEXTVAL
  INTO :NEW.CODE_ID
  FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER DOOS.INS_I18N_LIJSTEN
  BEFORE INSERT ON DOOS.I18N_LIJSTEN
  FOR EACH ROW
BEGIN
  SELECT DOOS.SEQ_I18N_LIJSTEN.NEXTVAL
  INTO :NEW.LIJST_ID
  FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER DOOS.INS_TALEN
  BEFORE INSERT ON DOOS.TALEN
  FOR EACH ROW
BEGIN
  SELECT DOOS.TALEN.NEXTVAL
  INTO :NEW.TAAL_ID
  FROM DUAL;
END;
/

-- Constraints
ALTER TABLE DOOS.I18N_CODES
  ADD CONSTRAINT UK_ICD_CODE UNIQUE (CODE);

ALTER TABLE DOOS.I18N_CODE_TEKSTEN
  ADD CONSTRAINT FK_ICT_CODE_ID FOREIGN KEY (CODE_ID)
  REFERENCES DOOS.I18N_CODES (CODE_ID);

ALTER TABLE DOOS.I18N_LIJST_CODES
  ADD CONSTRAINT FK_ILC_CODE_ID FOREIGN KEY (CODE_ID)
  REFERENCES DOOS.I18N_CODES (CODE_ID);

ALTER TABLE DOOS.I18N_LIJST_CODES
  ADD CONSTRAINT FK_ILC_LIJST_ID FOREIGN KEY (LIJST_ID)
  REFERENCES DOOS.I18N_LIJSTEN (LIJST_ID);

ALTER TABLE DOOS.I18N_LIJSTEN
  ADD CONSTRAINT UK_ILS_CODE UNIQUE (CODE);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_1   UNIQUE (ISO_639_1);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_2B  UNIQUE (ISO_639_2B);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_2T  UNIQUE (ISO_639_2T);

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT UK_TLN_ISO_639_3   UNIQUE (ISO_639_3);

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_TAAL_ID FOREIGN KEY (TAAL_ID)
  REFERENCES DOOS.TALEN (TAAL_ID);

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_ISO_639_2T FOREIGN KEY (ISO_639_2T)
  REFERENCES DOOS.TALEN (ISO_639_2T);

-- Grant rechten
GRANT SELECT                         ON DOOS.I18N_CODES        TO DOOS_SEL;
GRANT SELECT                         ON DOOS.I18N_CODE_TEKSTEN TO DOOS_SEL;
GRANT SELECT                         ON DOOS.I18N_LIJSTEN      TO DOOS_SEL;
GRANT SELECT                         ON DOOS.I18N_LIJST_CODES  TO DOOS_SEL;
GRANT SELECT                         ON DOOS.I18N_SELECTIES    TO DOOS_SEL;
GRANT SELECT                         ON DOOS.LIJSTEN           TO DOOS_SEL;
GRANT SELECT                         ON DOOS.PARAMS            TO DOOS_SEL;
GRANT SELECT                         ON DOOS.QUARTZJOBS        TO DOOS_SEL;
GRANT SELECT                         ON DOOS.TALEN             TO DOOS_SEL;
GRANT SELECT                         ON DOOS.TAALNAMEN         TO DOOS_SEL;

GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.I18N_CODES        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.I18N_CODE_TEKSTEN TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.I18N_LIJSTEN      TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.I18N_LIJST_CODES  TO DOOS_UPD;
GRANT SELECT                         ON DOOS.I18N_SELECTIES    TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.LIJSTEN           TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.PARAMS            TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.QUARTZJOBS        TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.TALEN             TO DOOS_UPD;
GRANT SELECT, UPDATE, INSERT, DELETE ON DOOS.TAALNAMEN         TO DOOS_UPD;

GRANT SELECT, UPDATE ON DOOS.SEQ_I18N_CODES   TO DOOS_UPD;
GRANT SELECT, UPDATE ON DOOS.SEQ_I18N_LIJSTEN TO DOOS_UPD;

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
COMMENT ON COLUMN DOOS.TALEN.TAAL_ID                IS 'De sleutel van de taal.';
COMMENT ON TABLE  DOOS.TAALNAMEN                    IS 'Deze tabel bevat namen van de talen van de wereld.';
COMMENT ON COLUMN DOOS.TAALNAMEN.ISO_639_2T         IS 'De ISO 639-3 code van de taal van de naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.NAAM               IS 'De naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.TAAL_ID            IS 'De De sleutel van de taal.';
