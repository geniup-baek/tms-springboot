package com.example.tms.utility.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sourceフィールドオブジェクトの内部フィールドを持って来る場合、 Targetフィールドに付けます。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertSourceNestField {

    /**
     * Sourceのフィールド名
     * 
     * @return フィールド名
     */
    String name();

    /**
     * Sourceのフィールドのクラスタイプ
     * 
     * @return クラスタイプ
     */
    Class<?> nestClass();

    /**
     * Sourceフィールドクラス内部のフィールド名
     * 
     * @return
     */
    String nestFieldName();
}
