package com.example.tms.utility.converter.fieldconverter;

import org.springframework.util.StringUtils;

public class IntStringToBooleanFieldConverter implements FieldConverter {

    public static final String STR_TRUE = "1";
    public static final String STR_FALSE = "0";

    @Override
    public Object convert(Object value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else if (STR_TRUE.equals(value)) {
            return true;
        }
        return false;
    }
}
