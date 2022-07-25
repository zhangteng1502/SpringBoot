package com.springboot_mvc.service;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 请求接口名称
     *
     * @return
     */
    String name() default "default名称";
}
