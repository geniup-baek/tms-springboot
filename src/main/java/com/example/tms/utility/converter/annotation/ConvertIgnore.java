package com.example.tms.utility.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseConverter(@see jp.co.hiics.neuplanet.common.base.BaseConverter) でコンバートする際に対象から除外します。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertIgnore {

}
