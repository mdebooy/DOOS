/*
 * Copyright (c) 2023 Marco de Booij
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

package eu.debooy.doos;

/**
 * @author Marco de Booij
 */
public final class TestConstants {
  public static final String  CODE                      = "code";
  public static final String  CODE_G                    = "z-code";
  public static final String  CODE_K                    = "a-code";
  public static final Long    CODEID                    = 21L;
  public static final Long    CODEID_G                  = 123L;
  public static final Long    CODEID_K                  = 13L;
  public static final String  CRON                      = "0 18 0/2 ? * *";
  public static final String  EIGENNAAM                 = "eigennaam";
  public static final String  EIGENNAAM_G               = "mijnnaam";
  public static final String  EIGENNAAM_K               = "dienaam";
  public static final String  GROEP                     = "groep";
  public static final String  GROEP_G                   = "z-groep";
  public static final String  GROEP_K                   = "a-groep";
  public static final int     I18NCODE_HASH             = 3059810;
  public static final int     I18NCODETEKST_HASH        = 27508;
  public static final int     I18NCODETEKSTPK_HASH      = 27508;
  public static final String  I18NCODETEKSTPK_TOSTRING  =
      "I18nCodeTekstPK (codeId=21, taalKode=ln)";
  public static final int     I18NLIJST_HASH            = 639;
  public static final int     I18NLIJSTCODE_HASH        = 24060;
  public static final int     I18NLIJSTCODEPK_HASH      = 24060;
  public static final String  I18NLIJSTCODEPK_TOSTRING  =
      "I18nLijstCodePK (codeId=21, lijstId=10)";
  public static final int     I18NLIJSTPK_HASH          = 639;
  public static final int     I18NSELECTIE_HASH         = 650;
  public static final String  ISO6391                   = "is";
  public static final String  ISO6392B                  = "isb";
  public static final String  ISO6392T                  = "ist";
  public static final String  ISO6392T_G                = "isu";
  public static final String  ISO6392T_K                = "iss";
  public static final String  ISO6393                   = "iso";
  public static final String  JAVACLASS                 =
      "TestConstants.doos.debooy.eu";
  public static final String  JOB                       = "job";
  public static final String  JOB_G                     = "jz-ob";
  public static final String  JOB_K                     = "a-job";
  public static final String  LIJST                     =
      "Een hele, hele, hele lange string.";
  public static final Long    LIJSTID                   = 10L;
  public static final Long    LIJSTID_G                 = 100L;
  public static final Long    LIJSTID_K                 = 1L;
  public static final int     LIJST_HASH                = -631097790;
  public static final String  LIJSTNAAM                 = "lijstnaam";
  public static final String  LIJSTNAAM_G               = "z-lijstnaam";
  public static final String  LIJSTNAAM_K               = "a-lijstnaam";
  public static final String  NAAM                      = "Naam";
  public static final String  NAAM_G                    = "Paam";
  public static final String  NAAM_K                    = "Maam";
  public static final String  OMSCHRIJVING              = "Omschrijving";
  public static final int     PARAMETER_HASH            = -2122434265;
  public static final int     QUARTZJOB_HASH            = -645574831;
  public static final int     QUARTZJOBPK_HASH          = -645574831;
  public static final String  QUARTZJOBPK_TOSTRING      =
      "QuartzjobPK (groep=groep, job=job)";
  public static final String  SELECTIE                  = "selectie";
  public static final String  SLEUTEL                   = "sleutel";
  public static final String  SLEUTEL_G                 = "z-sleutel";
  public static final String  SLEUTEL_K                 = "a-sleutel";
  public static final Long    TAALID                    = 1L;
  public static final int     TAAL_HASH                 = 630;
  public static final String  TAALKODE                  = "ln";
  public static final int     TAALNAAM_HASH             = 127896;
  public static final int     TAALNAAMDTO_HASH          = 127896;
  public static final int     TAALNAAMPK_HASH           = 23311;
  public static final String  TAALNAAMPK_TOSTRING       =
      "TaalnaamPK (taalId=1, iso6392t=ist)";
  public static final String  TEKST                     =
      "Dit is een tekst.";
  public static final Integer VOLGORDE                  = 90;
  public static final String  WAARDE                    = "waarde";

  private TestConstants() {
    throw new IllegalStateException("Utility class");
  }
}
