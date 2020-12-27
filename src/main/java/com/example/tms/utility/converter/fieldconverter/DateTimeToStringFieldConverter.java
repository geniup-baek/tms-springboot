package com.example.tms.utility.converter.fieldconverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeToStringFieldConverter implements FieldConverter {

    // LocalDateTime -> String
    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        } else {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        }
    }
}
