package com.example.tms.utility.converter.fieldconverter;

public class BooleanToIntFieldConverter implements FieldConverter {

    public static final Integer TRUE_VALUE = 1;
    public static final Integer FALSE_VALUE = 0;

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        } else if (Boolean.TRUE.equals(value)) {
            return TRUE_VALUE;
        }
        return FALSE_VALUE;
    }
}
