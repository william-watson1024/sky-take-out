package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ==================================================
 *
 * @Project : sky-take-out
 * @Package : com.sky.annotation
 * @Class Name  : AutoFill
 * --------------------------------------------------
 * @Author : William
 * @Date : 2025-09-27 03:51:12
 * @Description : TODO
 * ==================================================
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
