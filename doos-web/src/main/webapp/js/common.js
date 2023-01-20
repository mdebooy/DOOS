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

function addUploadButton(tabel, form, titel) {
  var btn = document.createElement('label');
  var fltr = document.getElementById(tabel+'_filter');
  var lnk = tabel.substring(0,1).toUpperCase()+tabel.substring(1, tabel.length-5);
  var img = 'imgUpload'+lnk;
  btn.innerHTML = '<img id="'+img+'" src="/common/images/32x32/actions/document-save.png" class="tabelbutton" alt="'+titel+'" title="'+titel+'" />';
  fltr.before(btn);
  $('#'+img).on('click', function() {
    document.getElementById(form+":upload"+lnk).click();
  } );
}

function getTaalnaam(taalnamen, taal) {
  var naam = taalnamen.findIndex(i => i.iso6391 === taal);
  if (naam < 0) {
    return '';
  }
  return(taalnamen[naam].naam);
}
