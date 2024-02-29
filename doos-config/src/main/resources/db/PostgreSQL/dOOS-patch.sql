/*
 * Copyright (c) 2022 Marco de Booij
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

CREATE TABLE IF NOT EXISTS DOOS.LOKALEN (
  CODE                            VARCHAR(50)    NOT NULL,
  EERSTE_TAAL                     CHAR(3)         NOT NULL,
  TWEEDE_TAAL                     CHAR(3),
  CONSTRAINT PK_LOKALEN PRIMARY KEY (CODE)
);

ALTER TABLE DOOS.LIJSTEN DROP COLUMN JASPER_REPORT;

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
  ADD COLUMN LEVEND CHAR(1) NOT NULL  DEFAULT 'J';

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_1  CHECK (ISO_639_1 = LOWER(ISO_639_1));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_2B CHECK (ISO_639_2B = LOWER(ISO_639_2B));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_2T CHECK (ISO_639_2T = LOWER(ISO_639_2T));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_ISO_639_3  CHECK (ISO_639_3 = LOWER(ISO_639_3));

ALTER TABLE DOOS.TALEN
  ADD CONSTRAINT CHK_TLN_LEVEND CHECK(LEVEND = ANY (ARRAY['J', 'N']));

GRANT SELECT                         ON TABLE DOOS.LOKALEN           TO DOOS_SEL;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.LOKALEN           TO DOOS_UPD;

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
COMMENT ON COLUMN DOOS.TALEN.LEVEND                 IS 'Is het een levende taal?';
