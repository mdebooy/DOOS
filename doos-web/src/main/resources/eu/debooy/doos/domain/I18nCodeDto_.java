package eu.debooy.doos.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-12-30T14:23:13.930+0100")
@StaticMetamodel(I18nCodeDto.class)
public class I18nCodeDto_ {
	public static volatile SingularAttribute<I18nCodeDto, Long> codeId;
	public static volatile SingularAttribute<I18nCodeDto, String> code;
	public static volatile ListAttribute<I18nCodeDto, I18nCodeTekstDto> teksten;
}
