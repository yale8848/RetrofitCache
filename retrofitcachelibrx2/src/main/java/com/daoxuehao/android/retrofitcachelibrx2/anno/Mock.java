package com.daoxuehao.android.retrofitcachelibrx2.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yale on 2017/7/5.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mock {
    String value() default "";
    String url() default "";
    String assets() default "";
}
