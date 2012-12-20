-- Invoeren van alle Parameters voor de DOOS Applicatie.
-- 
-- Copyright 2012 Marco de Booij
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
-- Author: Marco de Booij

INSERT INTO DOOS.Parameters
       (Sleutel, Waarde)
VALUES ('doos.datatable.rows', '15');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.columnheader.background', '#3D665E');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.columnheader.foreground', '#FFFFFF');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.footer.background', '#FFFFFF');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.footer.foreground', '#000000');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.row.background', '#FFFFFF');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.row.foreground', '#000000');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.row.conditional.background', '#B7E0D8');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.row.conditional.foreground', '#000000');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.titel.background', '#3D665E');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.lijst.titel.foreground', '#FFFFFF');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.stylesheet', '/common/css/DOOS_app.css');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('doos.stylesheet.print', '/common/css/DOOS_app_print.css');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('jsf.debug', 'false');

INSERT INTO DOOS.PARAMETERS
       (SLEUTEL, WAARDE)
VALUES ('jsf.default.taal', 'nl');

COMMIT;
