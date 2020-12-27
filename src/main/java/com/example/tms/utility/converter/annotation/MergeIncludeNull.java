package com.example.tms.utility.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DataConverter(@see jp.co.hiics.neuplanet.common.converter.DataConverter) でマージする際に Sourceフィールドの値が
 * Nullであってもマージします。 基本は、Sourceフィールドの値が Nullの場合、マージ対象から除外されます。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeIncludeNull {

}
