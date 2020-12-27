package com.example.tms.utility.converter.fieldconverter;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

public class StringToDecimalFieldConverter implements FieldConverter {

    @Override
    public Object convert(Object value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return new BigDecimal(Double.parseDouble(value.toString()));
        }
    }
}
