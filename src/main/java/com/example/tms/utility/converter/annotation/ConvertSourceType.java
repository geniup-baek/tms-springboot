package com.example.tms.utility.converter.annotation;

import com.example.tms.utility.converter.fieldconverter.FieldConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sourceのフィールド値のタイプを変換して持って来る場合、Targetフィールドに付けます。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertSourceType {

    /**
     * コンバーターのクラスタイプ
     * 
     * @return クラスタイプ
     */
    Class<? extends FieldConverter> converterClass() default Default.class;

    static final class Default implements FieldConverter {

        @Override
        public Object convert(Object value) {
            return value;
        }
    }
}
