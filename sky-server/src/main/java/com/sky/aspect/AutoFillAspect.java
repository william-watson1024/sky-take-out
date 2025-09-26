package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ==================================================
 *
 * @Project : sky-take-out
 * @Package : com.sky.aspect
 * @Class Name  : AutoFillAspect
 * --------------------------------------------------
 * @Author : William
 * @Date : 2025-09-27 03:52:33
 * @Description : TODO
 * ==================================================
 */

@Aspect
@Component
@Slf4j

public class AutoFillAspect {
    /*
    * 切入点
    */
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }
    /*
    * 前置通知
    * */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段的自动填充");
        //获取到切入点方法的参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();
        //获取到实体
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据不同的操作，进行不同的自动填充
        if (operationType==OperationType.INSERT){
            try {
                Method setCreateTimes = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTimes = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, Long.class);

                setCreateTimes.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTimes.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
                }catch (Exception e){
                e.printStackTrace();
            }
        }else if(operationType==OperationType.UPDATE){
            try {
                Method setCreateTimes = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                setCreateTimes.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
