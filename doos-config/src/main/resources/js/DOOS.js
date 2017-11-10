function confirmatie(deze, tekst){
  $('#teVerwijderen').text(tekst);
  var elem = $(deze).closest('td').find('.lnkDelete').first();
  $('#btnDelete').one('click',function() {
    $(elem).click();
  });
  $('#bevestiging').modal('show');
}
