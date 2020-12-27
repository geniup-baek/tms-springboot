package com.example.tms.utility.converter.fieldconverter;

import org.springframework.util.StringUtils;

public class StringToDoubleFieldConverter implements FieldConverter {

    @Override
    public Object convert(Object value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return Double.parseDouble(value.toString());
        }
    }
}
