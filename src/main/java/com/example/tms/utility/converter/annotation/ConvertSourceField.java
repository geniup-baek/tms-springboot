package com.example.tms.utility.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Targetと Sourceのフィールド名が異なる場合、 Targetフィールドに付けます。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertSourceField {
    /**
     * Sourceのフィールド名
     * 
     * @return フィールド名
     */
    String name();
}
