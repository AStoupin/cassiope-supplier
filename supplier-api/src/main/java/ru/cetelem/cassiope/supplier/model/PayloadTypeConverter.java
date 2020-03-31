package ru.cetelem.cassiope.supplier.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.cetelem.cassiope.supplier.io.PayloadType;

@Converter(autoApply = true)
public class PayloadTypeConverter implements AttributeConverter<PayloadType, String> {

	@Override
	public String convertToDatabaseColumn(PayloadType attribute) {
		return attribute.name();
	}

	@Override
	public PayloadType convertToEntityAttribute(String dbData) {
		return PayloadType.valueOf(dbData);
	}

}
