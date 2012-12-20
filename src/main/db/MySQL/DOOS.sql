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


CREATE DATABASE IF NOT EXISTS DOOS DEFAULT CHARACTER SET UTF8;

USE DOOS;

CREATE TABLE IF NOT EXISTS I18nCodes (
  Code                            VARCHAR(50)     NOT NULL,
  CodeID                          MEDIUMINT(8)    UNSIGNED NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (CodeID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS I18nCodeTeksten (
  CodeID                          MEDIUMINT(8)    UNSIGNED NOT NULL,
  TaalKode                        CHAR(2)         NOT NULL,
  Tekst                           VARCHAR(1024)   NOT NULL,
  PRIMARY KEY (CodeID, TaalKode)
) ENGINE = InnoDB;
    
CREATE TABLE IF NOT EXISTS I18nLijsten (
  Code                            VARCHAR(50)     NOT NULL,
  LijstID                         MEDIUMINT(8)    UNSIGNED NOT NULL AUTO_INCREMENT,
  Omschrijving                    VARCHAR(200),
  PRIMARY KEY (LijstID)
) ENGINE = InnoDB;
 
CREATE TABLE IF NOT EXISTS I18nLijstCodes (
  CodeID                          MEDIUMINT(8)    UNSIGNED NOT NULL,
  LijstID                         MEDIUMINT(8)    UNSIGNED NOT NULL,
  Volgorde                        TINYINT(3)      UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (LijstID, Volgorde, CodeID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Lijsten (
  JasperReport                    BLOB            NOT NULL,
  Lijst                           TEXT            NOT NULL,
  Lijstnaam                       VARCHAR(25)     NOT NULL,
  Omschrijving                    VARCHAR(100)    NOT NULL,
  PRIMARY KEY (Lijstnaam)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Parameters (
  Sleutel                         VARCHAR(100)    NOT NULL,
  Waarde                          VARCHAR(255)    NOT NULL,
  PRIMARY KEY (Sleutel)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS Talen (
  Omschrijving                    VARCHAR(100)    NOT NULL,
  TaalKode                        CHAR(2)         NOT NULL,
  PRIMARY KEY (TaalKode)
) ENGINE = InnoDB;

ALTER TABLE I18nCodeTeksten
  ADD CONSTRAINT FK_ICT_CodeID
  FOREIGN KEY FK_ICT_CodeID (CodeID)
  REFERENCES I18nCodes (CodeID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE I18nLijstCodes
  ADD CONSTRAINT FK_ILC_CodeID
  FOREIGN KEY FK_ILC_CodeID (CodeID)
  REFERENCES I18nCodes (CodeID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;

ALTER TABLE I18nLijstCodes
  ADD CONSTRAINT FK_ILC_LijstID
  FOREIGN KEY FK_ILC_LijstID (LijstID)
  REFERENCES I18nLijsten (LijstID)
  ON DELETE CASCADE
  ON UPDATE RESTRICT;
