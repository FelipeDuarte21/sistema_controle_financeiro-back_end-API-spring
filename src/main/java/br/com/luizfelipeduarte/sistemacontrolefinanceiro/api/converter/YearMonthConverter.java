package br.com.luizfelipeduarte.sistemacontrolefinanceiro.api.converter;

import java.sql.Date;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class YearMonthConverter implements AttributeConverter<YearMonth, Date> {

	@Override
	public Date convertToDatabaseColumn(YearMonth attribute) {
		
		if(attribute == null) return null;
		
		return Date.valueOf(attribute.atDay(1));
		
	}

	@Override
	public YearMonth convertToEntityAttribute(Date dbData) {
		
		if(dbData == null) return null;
		
		return YearMonth.from(
				Instant.ofEpochMilli(dbData.getTime())
					.atZone(ZoneId.systemDefault())
						.toLocalDate());
		
	}

	
}
