/**
 * Copyright (c) 2016 Marco de Booij
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

import eu.debooy.doos.access.I18nCodeDao;
import eu.debooy.doos.access.I18nCodeTekstDao;
import eu.debooy.doos.domain.I18nCodeDto;
import eu.debooy.doos.domain.I18nCodeTekstDto;
import eu.debooy.doos.domain.I18nCodeTekstPK;
import eu.debooy.doos.form.I18nCode;
import eu.debooy.doos.model.ChartElement;
import eu.debooy.doosutils.errorhandling.exception.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Marco de Booij
 */
@Singleton
@Named("doosI18nCodeService")
@Path("/i18nCodes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Lock(LockType.WRITE)
public class I18nCodeService {
  private static final  Logger  LOGGER  =
      LoggerFactory.getLogger(I18nCodeService.class);

  @Inject
  private I18nCodeDao       i18nCodeDao;
  @Inject
  private I18nCodeTekstDao  i18nCodeTekstDao;

  public I18nCodeService() {
    LOGGER.debug("init I18nCodeService");
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(Long codeId) {
    return i18nCodeDao.getByPrimaryKey(codeId);
  }

  @GET
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getCodes() {
    try {
      return Response.ok().entity(i18nCodeDao.getAll()).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new ArrayList<>()).build();
    }
  }

  @GET
  @Path("/{code}")
  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Response getCode (@PathParam(I18nCodeDto.COL_CODE) String code) {
    try {
      return Response.ok().entity(i18nCode(code)).build();
    } catch (ObjectNotFoundException e) {
      return Response.ok().entity(new I18nCodeDto()).build();
    }
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public Collection<ChartElement> getTekstenPerTaal() {
    try {
      return i18nCodeTekstDao.getTekstenPerTaal();
    } catch (ObjectNotFoundException e) {
      return new ArrayList<>();
    }
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeDto i18nCode(String code) {
    return i18nCodeDao.getI18nCode(code);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto i18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nCode i18nCode) {
    var dto = new I18nCodeDto();
    i18nCode.persist(dto);

    create(dto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void create(I18nCodeDto i18nCodeDto) {
    i18nCodeDao.create(i18nCodeDto);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId) {
    var i18nCode  = i18nCodeDao.getByPrimaryKey(codeId);
    i18nCodeDao.delete(i18nCode);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(Long codeId, String taalKode) {
    var  i18nCodeTekst =
        i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId, taalKode));
    i18nCodeTekstDao.delete(i18nCodeTekst);
  }

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public I18nCodeTekstDto getI18nCodeTekst(Long codeId, String taalKode) {
    return i18nCodeTekstDao.getByPrimaryKey(new I18nCodeTekstPK(codeId,
                                                                taalKode));
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeDto i18nCodeDto) {
    if (null == i18nCodeDto.getCodeId()) {
      i18nCodeDao.create(i18nCodeDto);
    } else {
      i18nCodeDao.update(i18nCodeDto);
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void save(I18nCodeTekstDto i18nCodeTekstDto) {
    i18nCodeTekstDao.update(i18nCodeTekstDto);
  }
}
