package com.gdn.onestop.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.Date;

public class DateConverterUtil implements Converter<Date, Long> {
    @Override
    public Long convert(Date value) {
        return value.getTime();
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Date.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Long.class);
    }
}
