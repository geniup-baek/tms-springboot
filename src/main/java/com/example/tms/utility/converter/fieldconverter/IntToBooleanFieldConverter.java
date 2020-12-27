package com.example.tms.utility.converter.fieldconverter;

import org.springframework.util.StringUtils;

public class IntToBooleanFieldConverter implements FieldConverter {

    public static final Integer TRUE_VALUE = 1;
    public static final Integer FALSE_VALUE = 0;

    @Override
    public Object convert(Object value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else if (TRUE_VALUE.equals(value)) {
            return true;
        }
        return false;
    }
}
