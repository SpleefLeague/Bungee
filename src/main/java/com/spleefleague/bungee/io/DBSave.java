/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.bungee.io;

import java.lang.annotation.*;

/**
 *
 * @author Jonas
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface DBSave {

    String fieldName();

    Class<? extends TypeConverter> typeConverter() default TypeConverter.class;

    int priority() default -1;
}
