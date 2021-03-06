package com.example.tms.utility.converter.fieldconverter;

import org.springframework.util.StringUtils;

public class StringToIntegerFieldConverter implements FieldConverter {

    @Override
    public Object convert(Object value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return Integer.parseInt(value.toString());
        }
    }
}
