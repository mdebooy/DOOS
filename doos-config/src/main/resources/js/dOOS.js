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

const datumOpties = { year: 'numeric', month: '2-digit', day: '2-digit' };
const datumtijdOpties = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second:  '2-digit'};

function formatJsonDatum(datum, taal, metTijd = false) {
  if (metTijd) {
    return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumtijdOpties);
  }

  return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumOpties);
}

function formatDatum(datum, taal, metTijd = false) {
  if (metTijd) {
    return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumtijdOpties);
  }

  return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumOpties);
}
