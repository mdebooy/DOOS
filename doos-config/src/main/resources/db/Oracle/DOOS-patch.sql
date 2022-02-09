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


CREATE TABLE DOOS.TALEN_3_2
AS
SELECT EIGENNAAM, TAAL, TAAL_KODE
FROM DOOS.TALEN;

DELETE FROM DOOS.TALEN WHERE TAAL_KODE='mo';

ALTER TABLE DOOS.TALEN ADD  COLUMN ISO_639_1                       CHAR(2);
ALTER TABLE DOOS.TALEN ADD  COLUMN ISO_639_2B                      CHAR(3);
ALTER TABLE DOOS.TALEN ADD  COLUMN ISO_639_2T                      CHAR(3);
ALTER TABLE DOOS.TALEN ADD  COLUMN ISO_639_3                       CHAR(3);

CREATE TABLE DOOS.TAALNAMEN (
  ISO_639_2T                      CHAR(3)         NOT NULL,
  NAAM                            VARCHAR(100)    NOT NULL,
  TAAL_ID                         INTEGER         NOT NULL,
  CONSTRAINT PK_TAALNAMEN PRIMARY KEY (TAAL_ID, ISO_639_2T)
);

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

GRANT SELECT                         ON TABLE DOOS.TAALNAMEN         TO DOOS_SEL;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE DOOS.TAALNAMEN         TO DOOS_UPD;

COMMENT ON COLUMN DOOS.TALEN.ISO_639_1              IS 'De ISO 639-1 code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_2B             IS 'De ISO 639-2B code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_2T             IS 'De ISO 639-2T code.';
COMMENT ON COLUMN DOOS.TALEN.ISO_639_3              IS 'De ISO 639-3 code.';
COMMENT ON COLUMN DOOS.TALEN.TAAL_ID                IS 'De sleutel van de taal.';
COMMENT ON TABLE  DOOS.TAALNAMEN                    IS 'Deze tabel bevat namen van de talen van de wereld.';
COMMENT ON COLUMN DOOS.TAALNAMEN.ISO_639_2T         IS 'De ISO 639-2T code van de taal van de naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.NAAM               IS 'De naam van de taal.';
COMMENT ON COLUMN DOOS.TAALNAMEN.TAAL_ID            IS 'De De sleutel van de taal.';

UPDATE DOOS.TALEN
  SET ISO_639_1 =TAAL_KODE;

UPDATE DOOS.TALEN SET ISO_639_2T='aar', ISO_639_3='aar' WHERE TAAL_KODE='aa';
UPDATE DOOS.TALEN SET ISO_639_2T='abk', ISO_639_3='abk' WHERE TAAL_KODE='ab';
UPDATE DOOS.TALEN SET ISO_639_2T='ave', ISO_639_3='ave' WHERE TAAL_KODE='ae';
UPDATE DOOS.TALEN SET ISO_639_2T='afr', ISO_639_3='afr' WHERE TAAL_KODE='af';
UPDATE DOOS.TALEN SET ISO_639_2T='aka', ISO_639_3='aka' WHERE TAAL_KODE='ak';
UPDATE DOOS.TALEN SET ISO_639_2T='amh', ISO_639_3='amh' WHERE TAAL_KODE='am';
UPDATE DOOS.TALEN SET ISO_639_2T='arg', ISO_639_3='arg' WHERE TAAL_KODE='an';
UPDATE DOOS.TALEN SET ISO_639_2T='ara', ISO_639_3='ara' WHERE TAAL_KODE='ar';
UPDATE DOOS.TALEN SET ISO_639_2T='asm', ISO_639_3='asm' WHERE TAAL_KODE='as';
UPDATE DOOS.TALEN SET ISO_639_2T='ava', ISO_639_3='ava' WHERE TAAL_KODE='av';
UPDATE DOOS.TALEN SET ISO_639_2T='aym', ISO_639_3='aym' WHERE TAAL_KODE='ay';
UPDATE DOOS.TALEN SET ISO_639_2T='aze', ISO_639_3='aze' WHERE TAAL_KODE='az';
UPDATE DOOS.TALEN SET ISO_639_2T='bak', ISO_639_3='bak' WHERE TAAL_KODE='ba';
UPDATE DOOS.TALEN SET ISO_639_2T='bel', ISO_639_3='bel' WHERE TAAL_KODE='be';
UPDATE DOOS.TALEN SET ISO_639_2T='bul', ISO_639_3='bul' WHERE TAAL_KODE='bg';
UPDATE DOOS.TALEN SET ISO_639_2T='bih' WHERE TAAL_KODE='bh';
UPDATE DOOS.TALEN SET ISO_639_2T='bis', ISO_639_3='bis' WHERE TAAL_KODE='bi';
UPDATE DOOS.TALEN SET ISO_639_2T='bam', ISO_639_3='bam' WHERE TAAL_KODE='bm';
UPDATE DOOS.TALEN SET ISO_639_2T='ben', ISO_639_3='ben' WHERE TAAL_KODE='bn';
UPDATE DOOS.TALEN SET ISO_639_2T='bod', ISO_639_2B='tib', ISO_639_3='bod' WHERE TAAL_KODE='bo';
UPDATE DOOS.TALEN SET ISO_639_2T='bre', ISO_639_3='bre' WHERE TAAL_KODE='br';
UPDATE DOOS.TALEN SET ISO_639_2T='bos', ISO_639_3='bos' WHERE TAAL_KODE='bs';
UPDATE DOOS.TALEN SET ISO_639_2T='cat', ISO_639_3='cat' WHERE TAAL_KODE='ca';
UPDATE DOOS.TALEN SET ISO_639_2T='che', ISO_639_3='che' WHERE TAAL_KODE='ce';
UPDATE DOOS.TALEN SET ISO_639_2T='cha', ISO_639_3='cha' WHERE TAAL_KODE='ch';
UPDATE DOOS.TALEN SET ISO_639_2T='cos', ISO_639_3='cos' WHERE TAAL_KODE='co';
UPDATE DOOS.TALEN SET ISO_639_2T='cre', ISO_639_3='cre' WHERE TAAL_KODE='cr';
UPDATE DOOS.TALEN SET ISO_639_2T='ces', ISO_639_2B='cze', ISO_639_3='ces' WHERE TAAL_KODE='cs';
UPDATE DOOS.TALEN SET ISO_639_2T='chu', ISO_639_3='chu' WHERE TAAL_KODE='cu';
UPDATE DOOS.TALEN SET ISO_639_2T='chv', ISO_639_3='chv' WHERE TAAL_KODE='cv';
UPDATE DOOS.TALEN SET ISO_639_2T='cym', ISO_639_2B='wel', ISO_639_3='cym' WHERE TAAL_KODE='cy';
UPDATE DOOS.TALEN SET ISO_639_2T='dan', ISO_639_3='dan' WHERE TAAL_KODE='da';
UPDATE DOOS.TALEN SET ISO_639_2T='deu', ISO_639_2B='ger', ISO_639_3='deu' WHERE TAAL_KODE='de';
UPDATE DOOS.TALEN SET ISO_639_2T='div', ISO_639_3='div' WHERE TAAL_KODE='dv';
UPDATE DOOS.TALEN SET ISO_639_2T='dzo', ISO_639_3='dzo' WHERE TAAL_KODE='dz';
UPDATE DOOS.TALEN SET ISO_639_2T='ewe', ISO_639_3='ewe' WHERE TAAL_KODE='ee';
UPDATE DOOS.TALEN SET ISO_639_2T='ell', ISO_639_2B='gre', ISO_639_3='ell' WHERE TAAL_KODE='el';
UPDATE DOOS.TALEN SET ISO_639_2T='eng', ISO_639_3='eng' WHERE TAAL_KODE='en';
UPDATE DOOS.TALEN SET ISO_639_2T='epo', ISO_639_3='epo' WHERE TAAL_KODE='eo';
UPDATE DOOS.TALEN SET ISO_639_2T='spa', ISO_639_3='spa' WHERE TAAL_KODE='es';
UPDATE DOOS.TALEN SET ISO_639_2T='est', ISO_639_3='est' WHERE TAAL_KODE='et';
UPDATE DOOS.TALEN SET ISO_639_2T='eus', ISO_639_2B='baq', ISO_639_3='eus' WHERE TAAL_KODE='eu';
UPDATE DOOS.TALEN SET ISO_639_2T='fas', ISO_639_2B='per', ISO_639_3='fas' WHERE TAAL_KODE='fa';
UPDATE DOOS.TALEN SET ISO_639_2T='ful', ISO_639_3='ful' WHERE TAAL_KODE='ff';
UPDATE DOOS.TALEN SET ISO_639_2T='fin', ISO_639_3='fin' WHERE TAAL_KODE='fi';
UPDATE DOOS.TALEN SET ISO_639_2T='fij', ISO_639_3='fij' WHERE TAAL_KODE='fj';
UPDATE DOOS.TALEN SET ISO_639_2T='fao', ISO_639_3='fao' WHERE TAAL_KODE='fo';
UPDATE DOOS.TALEN SET ISO_639_2T='fra', ISO_639_2B='fre', ISO_639_3='fra' WHERE TAAL_KODE='fr';
UPDATE DOOS.TALEN SET ISO_639_2T='fry', ISO_639_3='fry' WHERE TAAL_KODE='fy';
UPDATE DOOS.TALEN SET ISO_639_2T='gle', ISO_639_3='gle' WHERE TAAL_KODE='ga';
UPDATE DOOS.TALEN SET ISO_639_2T='gla', ISO_639_3='gla' WHERE TAAL_KODE='gd';
UPDATE DOOS.TALEN SET ISO_639_2T='glg', ISO_639_3='glg' WHERE TAAL_KODE='gl';
UPDATE DOOS.TALEN SET ISO_639_2T='grn', ISO_639_3='grn' WHERE TAAL_KODE='gn';
UPDATE DOOS.TALEN SET ISO_639_2T='guj', ISO_639_3='guj' WHERE TAAL_KODE='gu';
UPDATE DOOS.TALEN SET ISO_639_2T='glv', ISO_639_3='glv' WHERE TAAL_KODE='gv';
UPDATE DOOS.TALEN SET ISO_639_2T='hau', ISO_639_3='hau' WHERE TAAL_KODE='ha';
UPDATE DOOS.TALEN SET ISO_639_2T='heb', ISO_639_3='heb' WHERE TAAL_KODE='he';
UPDATE DOOS.TALEN SET ISO_639_2T='hin', ISO_639_3='hin' WHERE TAAL_KODE='hi';
UPDATE DOOS.TALEN SET ISO_639_2T='hmo', ISO_639_3='hmo' WHERE TAAL_KODE='ho';
UPDATE DOOS.TALEN SET ISO_639_2T='hrv', ISO_639_3='hrv' WHERE TAAL_KODE='hr';
UPDATE DOOS.TALEN SET ISO_639_2T='hat', ISO_639_3='hat' WHERE TAAL_KODE='ht';
UPDATE DOOS.TALEN SET ISO_639_2T='hun', ISO_639_3='hun' WHERE TAAL_KODE='hu';
UPDATE DOOS.TALEN SET ISO_639_2T='hye', ISO_639_2B='arm', ISO_639_3='hye' WHERE TAAL_KODE='hy';
UPDATE DOOS.TALEN SET ISO_639_2T='her', ISO_639_3='her' WHERE TAAL_KODE='hz';
UPDATE DOOS.TALEN SET ISO_639_2T='ina', ISO_639_3='ina' WHERE TAAL_KODE='ia';
UPDATE DOOS.TALEN SET ISO_639_2T='ind', ISO_639_3='ind' WHERE TAAL_KODE='id';
UPDATE DOOS.TALEN SET ISO_639_2T='ile', ISO_639_3='ile' WHERE TAAL_KODE='ie';
UPDATE DOOS.TALEN SET ISO_639_2T='ibo', ISO_639_3='ibo' WHERE TAAL_KODE='ig';
UPDATE DOOS.TALEN SET ISO_639_2T='iii', ISO_639_3='iii' WHERE TAAL_KODE='ii';
UPDATE DOOS.TALEN SET ISO_639_2T='ipk', ISO_639_3='ipk' WHERE TAAL_KODE='ik';
UPDATE DOOS.TALEN SET ISO_639_2T='ido', ISO_639_3='ido' WHERE TAAL_KODE='io';
UPDATE DOOS.TALEN SET ISO_639_2T='isl', ISO_639_2B='ice', ISO_639_3='isl' WHERE TAAL_KODE='is';
UPDATE DOOS.TALEN SET ISO_639_2T='ita', ISO_639_3='ita' WHERE TAAL_KODE='it';
UPDATE DOOS.TALEN SET ISO_639_2T='iku', ISO_639_3='iku' WHERE TAAL_KODE='iu';
UPDATE DOOS.TALEN SET ISO_639_2T='jpn', ISO_639_3='jpn' WHERE TAAL_KODE='ja';
UPDATE DOOS.TALEN SET ISO_639_1='jv',  ISO_639_2T='jav', ISO_639_3='jav' WHERE TAAL_KODE='jw';
UPDATE DOOS.TALEN SET ISO_639_2T='kat', ISO_639_2B='geo', ISO_639_3='kat' WHERE TAAL_KODE='ka';
UPDATE DOOS.TALEN SET ISO_639_2T='kon', ISO_639_3='kon' WHERE TAAL_KODE='kg';
UPDATE DOOS.TALEN SET ISO_639_2T='kik', ISO_639_3='kik' WHERE TAAL_KODE='ki';
UPDATE DOOS.TALEN SET ISO_639_2T='kua', ISO_639_3='kua' WHERE TAAL_KODE='kj';
UPDATE DOOS.TALEN SET ISO_639_2T='kaz', ISO_639_3='kaz' WHERE TAAL_KODE='kk';
UPDATE DOOS.TALEN SET ISO_639_2T='kal', ISO_639_3='kal' WHERE TAAL_KODE='kl';
UPDATE DOOS.TALEN SET ISO_639_2T='khm', ISO_639_3='khm' WHERE TAAL_KODE='km';
UPDATE DOOS.TALEN SET ISO_639_2T='kan', ISO_639_3='kan' WHERE TAAL_KODE='kn';
UPDATE DOOS.TALEN SET ISO_639_2T='kor', ISO_639_3='kor' WHERE TAAL_KODE='ko';
UPDATE DOOS.TALEN SET ISO_639_2T='kau', ISO_639_3='kau' WHERE TAAL_KODE='kr';
UPDATE DOOS.TALEN SET ISO_639_2T='kas', ISO_639_3='kas' WHERE TAAL_KODE='ks';
UPDATE DOOS.TALEN SET ISO_639_2T='kur', ISO_639_3='kur' WHERE TAAL_KODE='ku';
UPDATE DOOS.TALEN SET ISO_639_2T='kom', ISO_639_3='kom' WHERE TAAL_KODE='kv';
UPDATE DOOS.TALEN SET ISO_639_2T='cor', ISO_639_3='cor' WHERE TAAL_KODE='kw';
UPDATE DOOS.TALEN SET ISO_639_2T='kir', ISO_639_3='kir' WHERE TAAL_KODE='ky';
UPDATE DOOS.TALEN SET ISO_639_2T='lat', ISO_639_3='lat' WHERE TAAL_KODE='la';
UPDATE DOOS.TALEN SET ISO_639_2T='ltz', ISO_639_3='ltz' WHERE TAAL_KODE='lb';
UPDATE DOOS.TALEN SET ISO_639_2T='lug', ISO_639_3='lug' WHERE TAAL_KODE='lg';
UPDATE DOOS.TALEN SET ISO_639_2T='lim', ISO_639_3='lim' WHERE TAAL_KODE='li';
UPDATE DOOS.TALEN SET ISO_639_2T='lin', ISO_639_3='lin' WHERE TAAL_KODE='ln';
UPDATE DOOS.TALEN SET ISO_639_2T='lao', ISO_639_3='lao' WHERE TAAL_KODE='lo';
UPDATE DOOS.TALEN SET ISO_639_2T='lit', ISO_639_3='lit' WHERE TAAL_KODE='lt';
UPDATE DOOS.TALEN SET ISO_639_2T='lub', ISO_639_3='lub' WHERE TAAL_KODE='lu';
UPDATE DOOS.TALEN SET ISO_639_2T='lav', ISO_639_3='lav' WHERE TAAL_KODE='lv';
UPDATE DOOS.TALEN SET ISO_639_2T='mlg', ISO_639_3='mlg' WHERE TAAL_KODE='mg';
UPDATE DOOS.TALEN SET ISO_639_2T='mah', ISO_639_3='mah' WHERE TAAL_KODE='mh';
UPDATE DOOS.TALEN SET ISO_639_2T='mri', ISO_639_2B='mao', ISO_639_3='mri' WHERE TAAL_KODE='mi';
UPDATE DOOS.TALEN SET ISO_639_2T='mkd', ISO_639_2B='mac', ISO_639_3='mkd' WHERE TAAL_KODE='mk';
UPDATE DOOS.TALEN SET ISO_639_2T='mal', ISO_639_3='mal' WHERE TAAL_KODE='ml';
UPDATE DOOS.TALEN SET ISO_639_2T='mon', ISO_639_3='mon' WHERE TAAL_KODE='mn';
UPDATE DOOS.TALEN SET ISO_639_2T='mar', ISO_639_3='mar' WHERE TAAL_KODE='mr';
UPDATE DOOS.TALEN SET ISO_639_2T='msa', ISO_639_2B='may', ISO_639_3='msa' WHERE TAAL_KODE='ms';
UPDATE DOOS.TALEN SET ISO_639_2T='mlt', ISO_639_3='mlt' WHERE TAAL_KODE='mt';
UPDATE DOOS.TALEN SET ISO_639_2T='mya', ISO_639_2B='bur', ISO_639_3='mya' WHERE TAAL_KODE='my';
UPDATE DOOS.TALEN SET ISO_639_2T='nau', ISO_639_3='nau' WHERE TAAL_KODE='na';
UPDATE DOOS.TALEN SET ISO_639_2T='nob', ISO_639_3='nob' WHERE TAAL_KODE='nb';
UPDATE DOOS.TALEN SET ISO_639_2T='nde', ISO_639_3='nde' WHERE TAAL_KODE='nd';
UPDATE DOOS.TALEN SET ISO_639_2T='nep', ISO_639_3='nep' WHERE TAAL_KODE='ne';
UPDATE DOOS.TALEN SET ISO_639_2T='ndo', ISO_639_3='ndo' WHERE TAAL_KODE='ng';
UPDATE DOOS.TALEN SET ISO_639_2T='nld', ISO_639_2B='dut', ISO_639_3='nld' WHERE TAAL_KODE='nl';
UPDATE DOOS.TALEN SET ISO_639_2T='nno', ISO_639_3='nno' WHERE TAAL_KODE='nn';
UPDATE DOOS.TALEN SET ISO_639_2T='nor', ISO_639_3='nor' WHERE TAAL_KODE='no';
UPDATE DOOS.TALEN SET ISO_639_2T='nbl', ISO_639_3='nbl' WHERE TAAL_KODE='nr';
UPDATE DOOS.TALEN SET ISO_639_2T='nav', ISO_639_3='nav' WHERE TAAL_KODE='nv';
UPDATE DOOS.TALEN SET ISO_639_2T='nya', ISO_639_3='nya' WHERE TAAL_KODE='ny';
UPDATE DOOS.TALEN SET ISO_639_2T='oci', ISO_639_3='oci' WHERE TAAL_KODE='oc';
UPDATE DOOS.TALEN SET ISO_639_2T='oji', ISO_639_3='oji' WHERE TAAL_KODE='oj';
UPDATE DOOS.TALEN SET ISO_639_2T='orm', ISO_639_3='orm' WHERE TAAL_KODE='om';
UPDATE DOOS.TALEN SET ISO_639_2T='ori', ISO_639_3='ori' WHERE TAAL_KODE='or';
UPDATE DOOS.TALEN SET ISO_639_2T='oss', ISO_639_3='oss' WHERE TAAL_KODE='os';
UPDATE DOOS.TALEN SET ISO_639_2T='pan', ISO_639_3='pan' WHERE TAAL_KODE='pa';
UPDATE DOOS.TALEN SET ISO_639_2T='pli', ISO_639_3='pli' WHERE TAAL_KODE='pi';
UPDATE DOOS.TALEN SET ISO_639_2T='pol', ISO_639_3='pol' WHERE TAAL_KODE='pl';
UPDATE DOOS.TALEN SET ISO_639_2T='pus', ISO_639_3='pus' WHERE TAAL_KODE='ps';
UPDATE DOOS.TALEN SET ISO_639_2T='por', ISO_639_3='por' WHERE TAAL_KODE='pt';
UPDATE DOOS.TALEN SET ISO_639_2T='que', ISO_639_3='que' WHERE TAAL_KODE='qu';
UPDATE DOOS.TALEN SET ISO_639_2T='roh', ISO_639_3='roh' WHERE TAAL_KODE='rm';
UPDATE DOOS.TALEN SET ISO_639_2T='run', ISO_639_3='run' WHERE TAAL_KODE='rn';
UPDATE DOOS.TALEN SET ISO_639_2T='ron', ISO_639_2B='rum', ISO_639_3='ron' WHERE TAAL_KODE='ro';
UPDATE DOOS.TALEN SET ISO_639_2T='rus', ISO_639_3='rus' WHERE TAAL_KODE='ru';
UPDATE DOOS.TALEN SET ISO_639_2T='kin', ISO_639_3='kin' WHERE TAAL_KODE='rw';
UPDATE DOOS.TALEN SET ISO_639_2T='san', ISO_639_3='san' WHERE TAAL_KODE='sa';
UPDATE DOOS.TALEN SET ISO_639_2T='srd', ISO_639_3='srd' WHERE TAAL_KODE='sc';
UPDATE DOOS.TALEN SET ISO_639_2T='snd', ISO_639_3='snd' WHERE TAAL_KODE='sd';
UPDATE DOOS.TALEN SET ISO_639_2T='sme', ISO_639_3='sme' WHERE TAAL_KODE='se';
UPDATE DOOS.TALEN SET ISO_639_2T='sag', ISO_639_3='sag' WHERE TAAL_KODE='sg';
UPDATE DOOS.TALEN SET ISO_639_2T='scr', ISO_639_3='hbs' WHERE TAAL_KODE='sh';
UPDATE DOOS.TALEN SET ISO_639_2T='sin', ISO_639_3='sin' WHERE TAAL_KODE='si';
UPDATE DOOS.TALEN SET ISO_639_2T='slk', ISO_639_2B='slo', ISO_639_3='slk' WHERE TAAL_KODE='sk';
UPDATE DOOS.TALEN SET ISO_639_2T='slv', ISO_639_3='slv' WHERE TAAL_KODE='sl';
UPDATE DOOS.TALEN SET ISO_639_2T='smo', ISO_639_3='smo' WHERE TAAL_KODE='sm';
UPDATE DOOS.TALEN SET ISO_639_2T='sna', ISO_639_3='sna' WHERE TAAL_KODE='sn';
UPDATE DOOS.TALEN SET ISO_639_2T='som', ISO_639_3='som' WHERE TAAL_KODE='so';
UPDATE DOOS.TALEN SET ISO_639_2T='sqi', ISO_639_2B='alb', ISO_639_3='sqi' WHERE TAAL_KODE='sq';
UPDATE DOOS.TALEN SET ISO_639_2T='srp', ISO_639_3='srp' WHERE TAAL_KODE='sr';
UPDATE DOOS.TALEN SET ISO_639_2T='ssw', ISO_639_3='ssw' WHERE TAAL_KODE='ss';
UPDATE DOOS.TALEN SET ISO_639_2T='sot', ISO_639_3='sot' WHERE TAAL_KODE='st';
UPDATE DOOS.TALEN SET ISO_639_2T='sun', ISO_639_3='sun' WHERE TAAL_KODE='su';
UPDATE DOOS.TALEN SET ISO_639_2T='swe', ISO_639_3='swe' WHERE TAAL_KODE='sv';
UPDATE DOOS.TALEN SET ISO_639_2T='swa', ISO_639_3='swa' WHERE TAAL_KODE='sw';
UPDATE DOOS.TALEN SET ISO_639_2T='tam', ISO_639_3='tam' WHERE TAAL_KODE='ta';
UPDATE DOOS.TALEN SET ISO_639_2T='tel', ISO_639_3='tel' WHERE TAAL_KODE='te';
UPDATE DOOS.TALEN SET ISO_639_2T='tgk', ISO_639_3='tgk' WHERE TAAL_KODE='tg';
UPDATE DOOS.TALEN SET ISO_639_2T='tha', ISO_639_3='tha' WHERE TAAL_KODE='th';
UPDATE DOOS.TALEN SET ISO_639_2T='tir', ISO_639_3='tir' WHERE TAAL_KODE='ti';
UPDATE DOOS.TALEN SET ISO_639_2T='tuk', ISO_639_3='tuk' WHERE TAAL_KODE='tk';
UPDATE DOOS.TALEN SET ISO_639_2T='tgl', ISO_639_3='tgl' WHERE TAAL_KODE='tl';
UPDATE DOOS.TALEN SET ISO_639_2T='tsn', ISO_639_3='tsn' WHERE TAAL_KODE='tn';
UPDATE DOOS.TALEN SET ISO_639_2T='ton', ISO_639_3='ton' WHERE TAAL_KODE='to';
UPDATE DOOS.TALEN SET ISO_639_2T='tur', ISO_639_3='tur' WHERE TAAL_KODE='tr';
UPDATE DOOS.TALEN SET ISO_639_2T='tso', ISO_639_3='tso' WHERE TAAL_KODE='ts';
UPDATE DOOS.TALEN SET ISO_639_2T='tat', ISO_639_3='tat' WHERE TAAL_KODE='tt';
UPDATE DOOS.TALEN SET ISO_639_2T='twi', ISO_639_3='twi' WHERE TAAL_KODE='tw';
UPDATE DOOS.TALEN SET ISO_639_2T='tah', ISO_639_3='tah' WHERE TAAL_KODE='ty';
UPDATE DOOS.TALEN SET ISO_639_2T='uig', ISO_639_3='uig' WHERE TAAL_KODE='ug';
UPDATE DOOS.TALEN SET ISO_639_2T='ukr', ISO_639_3='ukr' WHERE TAAL_KODE='uk';
UPDATE DOOS.TALEN SET ISO_639_2T='urd', ISO_639_3='urd' WHERE TAAL_KODE='ur';
UPDATE DOOS.TALEN SET ISO_639_2T='uzb', ISO_639_3='uzb' WHERE TAAL_KODE='uz';
UPDATE DOOS.TALEN SET ISO_639_2T='ven', ISO_639_3='ven' WHERE TAAL_KODE='ve';
UPDATE DOOS.TALEN SET ISO_639_2T='vie', ISO_639_3='vie' WHERE TAAL_KODE='vi';
UPDATE DOOS.TALEN SET ISO_639_2T='vol', ISO_639_3='vol' WHERE TAAL_KODE='vo';
UPDATE DOOS.TALEN SET ISO_639_2T='wln', ISO_639_3='wln' WHERE TAAL_KODE='wa';
UPDATE DOOS.TALEN SET ISO_639_2T='wol', ISO_639_3='wol' WHERE TAAL_KODE='wo';
UPDATE DOOS.TALEN SET ISO_639_2T='xho', ISO_639_3='xho' WHERE TAAL_KODE='xh';
UPDATE DOOS.TALEN SET ISO_639_2T='yid', ISO_639_3='yid' WHERE TAAL_KODE='yi';
UPDATE DOOS.TALEN SET ISO_639_2T='yor', ISO_639_3='yor' WHERE TAAL_KODE='yo';
UPDATE DOOS.TALEN SET ISO_639_2T='zha', ISO_639_3='zha' WHERE TAAL_KODE='za';
UPDATE DOOS.TALEN SET ISO_639_2T='zho', ISO_639_2B='chi', ISO_639_3='zho' WHERE TAAL_KODE='zh';
UPDATE DOOS.TALEN SET ISO_639_2T='zul', ISO_639_3='zul' WHERE TAAL_KODE='zu';

ALTER TABLE DOOS.TALEN ALTER COLUMN ISO_639_2T SET NOT NULL;

INSERT INTO DOOS.TAALNAMEN (ISO_639_2T, NAAM, TAAL_ID)
SELECT ISO_639_2T, TAAL, TAAL_ID
FROM   DOOS.TALEN

INSERT INTO DOOS.TAALNAMEN (ISO_639_2T, NAAM, TAAL_ID)
SELECT 'nld', TAAL, TAAL_ID
FROM   DOOS.TALEN
WHERE  TAAL_CODE!='nl';

ALTER TABLE DOOS.TALEN DROP  COLUMN EIGENNAAM;
ALTER TABLE DOOS.TALEN DROP  COLUMN TAAL;
ALTER TABLE DOOS.TALEN DROP  COLUMN TAAL_KODE;

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_TAAL_ID FOREIGN KEY (TAAL_ID)
  REFERENCES DOOS.TALEN (TAAL_ID);

ALTER TABLE DOOS.TAALNAMEN
  ADD CONSTRAINT FK_TLN_ISO_639_2T FOREIGN KEY (TAAL)
  REFERENCES DOOS.TALEN (ISO_639_2T);
