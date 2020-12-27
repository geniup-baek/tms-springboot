package com.example.tms.utility.converter.fieldconverter;

public class BooleanToIntStringFieldConverter implements FieldConverter {

    public static final String STR_TRUE = "1";
    public static final String STR_FALSE = "0";

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        } else if (Boolean.TRUE.equals(value)) {
            return STR_TRUE;
        }
        return STR_FALSE;
    }
}
