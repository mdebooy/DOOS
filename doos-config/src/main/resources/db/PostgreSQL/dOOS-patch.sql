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

ALTER TABLE DOOS.LIJSTEN DROP COLUMN JASPER_REPORT;

COMMENT ON COLUMN DOOS.TALEN.LEVEND                 IS 'Is het een levende taal?';
