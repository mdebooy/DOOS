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
const timestampOpties = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second:  '2-digit', fractionalSecondDigits: '3'};

function addTabelButton(tabel, form, titel, type, button) {
  var btn = document.createElement('label');
  var fltr = document.getElementById(tabel+'_filter');
  var lnk = tabel.substring(0,1).toUpperCase()+tabel.substring(1, tabel.length-5);
  var typecc = type.substring(0,1).toUpperCase()+type.substring(1);
  var typel = type.toLowerCase();
  var img = 'img'+typecc+lnk;
  btn.innerHTML = '<img id="'+img+'" src="'+button+'" class="tabelbutton" alt="'+titel+'" title="'+titel+'" />';
  fltr.before(btn);
  $('#'+img).on('click', function() {
    document.getElementById(form+":"+typel+lnk).click();
  } );
}

function addCalcButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Ods', '/common/images/32x32/apps/libreoffice-calc.png');
}

function addInsertButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Add', '/common/images/32x32/actions/document-new.png');
}

function addPdfButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Pdf', '/common/images/32x32/apps/evince.png');
}

function addRandomButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Rnd', '/common/images/32x32/status/dialog-information.png');
}

function addUploadButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Upload', '/common/images/32x32/actions/document-save.png');
}

function addWriterButton(tabel, form, titel) {
  addTabelButton(tabel, form, titel, 'Odt', '/common/images/32x32/apps/libreoffice-writer.png');
}

function alterParam(element, value, param = 'XX') {
  var param = element.getAttribute('onclick').replace(param, value);
  element.setAttribute('onclick', param);
}

function confirmatie(form, tekst){
  $('#teVerwijderen').text(tekst);
  var elem = document.getElementById(form+':lnkDelete');

  $('#btnDelete').one('click',function() {
    $(elem).click();
  });
}

function formatDatum(datum, taal, metTijd = false) {
  if (metTijd) {
    return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumtijdOpties);
  }

  return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumOpties);
}

function formatJsonDatum(datum, taal, metTijd = false) {
  if (metTijd) {
    return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumtijdOpties);
  }

  return (new Date(datum.substring(0,20))).toLocaleDateString(taal, datumOpties);
}

function formatJsonTimestamp(datum, taal) {
  var tz = datum.indexOf("[");
  return (new Date(datum.substring(0,tz))).toLocaleDateString(taal, timestampOpties);
}

function formatNumber(getal, decimalen = null) {
  if (null === decimalen) {
    uitvoer = getal.toLocaleString();
  } else {
    uitvoer = getal.toLocaleString(navigator.language || navigator.languages[0], {
      minimumFractionDigits: decimalen,
      maximumFractionDigits: decimalen
    });
  }

  if (getal < 0) {
    return '<span class="negatief">' + uitvoer + '</span>';
  } else {
    return uitvoer;
  }
}

function initTabs() {
  tabs = document.getElementsByClassName('tab-detail');
  for (i=0 ; i<tabs.length; i++) {
    tables = tabs[i].getElementsByTagName('table');
    for (j=0; j<tables.length; j++) {
      tables[j].style.width = '100%';
    }
  }
}

function switchToTab(tabId) {
  tabDetail = document.getElementsByClassName('tab-detail');
  for (i=0; i<tabDetail.length; i++) {
    tabDetail[i].classList.add('is-hidden');
  }

  tabLink = document.getElementsByClassName('tab-link');
  for (i=0; i<tabLink.length; i++) {
    tabLink[i].classList.remove('is-active');
  }

  document.getElementById('con'+tabId).classList.remove('is-hidden');
  document.getElementById('tab'+tabId).classList.add('is-active');
  $($.fn.dataTable.tables(true)).DataTable().columns.adjust();
}

function taalvlag(taal, align = 'centered') {
  return '<img alt="'+taal+'" align="'+align+'" hspace="10px" src="/common/images/taal/'+(taal === '??' ? 'unk' : taal)+'.png" title="'+taal+'" height="9px" />';
}
