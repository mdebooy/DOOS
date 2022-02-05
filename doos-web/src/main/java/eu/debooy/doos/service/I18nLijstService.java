/**
 * Copyright (c) 2017 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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
package eu.debooy.doos.service;

import eu.debooy.doos.access.I18nLijstCodeDao;
import eu.debooy.doos.access.I18nLijstDao;
import eu.debooy.doos.access.I18nSelectieDao;
import eu.debooy.doos.domain.I18nLijstCodeDto;
import eu.debooy.doos.domain.I18nLijstCodePK;
import eu.debooy.doos.domain.I18nLijstDto;
import eu.debooy.doos.domain.I18nSelectieDto;
import eu.debooy.doos.form.I18nLijst;
import eu.debooy.doos.form.I18nSelectie;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosI18nLijstService")
@Lock(LockType.WRITE)
public class I18nLijstService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(I18nLijstService.class);

  @Inject
  private I18nLijstDao      i18nLijstDao;
  @Inject
  private I18nLijstCodeDao  i18nLijstCodeDao;
  @Inject
  private I18nSelectieDao   i18nSelectieDao;

  public I18nLijstService() {
    LOGGER.debug("init I18nLijstService");
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nLijstCodeDto getI18nLijstCode(Long codeId, Long lijstId) {
    return i18nLijstCodeDao.getByPrimaryKey(new I18nLijstCodePK(codeId,
                                                                lijstId));
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nSelectieDto getI18nSelectie(String selectie, String code) {
    return i18nSelectieDao.getSelectie(selectie, code);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nSelectie> getI18nSelecties(String selectie) {
    Collection<I18nSelectie>  i18nSelecties = new ArrayList<>();
    i18nSelectieDao.getSelecties(selectie)
                   .forEach(rij ->  i18nSelecties.add(new I18nSelectie(rij)));

    return i18nSelecties;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Map<String, Integer> getI18nSelectItems(String selectie) {
    Map<String, Integer>  i18nSelecties = new HashMap<>();
    try {
      i18nSelectieDao.getSelecties(selectie)
                     .forEach(rij -> i18nSelecties.put(rij.getCode(),
                                                       rij.getVolgorde()));
    } catch (NullPointerException | ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege HashMap gegeven.
    }

    return i18nSelecties;
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nLijstDto i18nLijst(Long codeId) {
    return i18nLijstDao.getByPrimaryKey(codeId);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nLijstCodeDto i18nLijstCode(Long codeId, Long lijstId) {
    return i18nLijstCodeDao.getByPrimaryKey(new I18nLijstCodePK(codeId,
                                                                lijstId));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nLijst i18nLijst) {
    var dto = new I18nLijstDto();
    i18nLijst.persist(dto);

    create(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nLijstDto i18nLijstDto) {
    i18nLijstDao.create(i18nLijstDto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId) {
    var i18nLijst  = i18nLijstDao.getByPrimaryKey(codeId);
    i18nLijstDao.delete(i18nLijst);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId, Long lijstId) {
    var dto =
        i18nLijstCodeDao.getByPrimaryKey(new I18nLijstCodePK(codeId, lijstId));
    i18nLijstCodeDao.delete(dto);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<I18nLijst> query() {
    Collection<I18nLijst>  i18nLijsten  = new ArrayList<>();
    try {
      i18nLijstDao.getAll().forEach(rij -> i18nLijsten.add(new I18nLijst(rij)));
    } catch (NullPointerException | ObjectNotFoundException e) {
      // Er wordt nu gewoon een lege ArrayList gegeven.
    }

    return i18nLijsten;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nLijstDto i18nLijstDto) {
    if (null == i18nLijstDto.getLijstId()) {
      i18nLijstDao.create(i18nLijstDto);
    } else {
      i18nLijstDao.update(i18nLijstDto);
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nLijstCodeDto i18nLijstCodeDto) {
    i18nLijstCodeDao.update(i18nLijstCodeDto);
  }
}
