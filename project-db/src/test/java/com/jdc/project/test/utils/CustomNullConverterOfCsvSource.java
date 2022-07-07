package com.jdc.project.test.utils;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public final class CustomNullConverterOfCsvSource extends SimpleArgumentConverter{

	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		// TODO Auto-generated method stub
		
		// for int type parameters
		if (source instanceof Integer) {
			return 0;
		}
		
		if ("".equals(source)) {
			return null;
		}

		return DefaultArgumentConverter.INSTANCE.convert(source, targetType);
	}

	
}
