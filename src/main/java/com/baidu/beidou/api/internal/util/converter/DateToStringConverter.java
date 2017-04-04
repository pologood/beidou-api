package com.baidu.beidou.api.internal.util.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.DozerConverter;

/**
 * ClassName: DateToStringConverter
 * Function: TODO ADD FUNCTION
 *
 * @author genglei
 * @version 2.0.0
 * @since cpweb357
 * @date 2011-12-20
 */
public class DateToStringConverter extends DozerConverter<Date, String> {
	private static final String DATE_PATTERN = "yyyyMMdd";

	public DateToStringConverter() {
		super(Date.class, String.class);
	}

	@Override
	public Date convertFrom(String source, Date destination) {
		if (source == null) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		try {
			destination = dateFormat.parse(source);
		} catch (ParseException e) {
			destination = null;
		}
		return destination;
	}

	@Override
	public String convertTo(Date source, String destination) {
		if (source == null) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		destination = dateFormat.format(source);
		return destination;
	}

}
