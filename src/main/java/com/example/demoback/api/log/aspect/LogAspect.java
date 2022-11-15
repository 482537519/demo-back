package com.example.demoback.api.log.aspect;

import com.example.demoback.api.log.model.SysLog;
import com.example.demoback.api.log.service.LogService;
import com.example.demoback.common.util.RequestHolder;
import com.example.demoback.common.util.SecurityUtils;
import com.example.demoback.common.util.StringUtils;
import com.example.demoback.common.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private LogService logService;

    private long currentTime = 0L;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.example.demoback.api.log.aop.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        currentTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        SysLog log = new SysLog("INFO",System.currentTimeMillis() - currentTime);
        logService.save(getUsername(), getName(), StringUtils.getIp(RequestHolder.getHttpServletRequest()),joinPoint, log);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog log = new SysLog("ERROR",System.currentTimeMillis() - currentTime);
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
        logService.save(getUsername(), getName(),StringUtils.getIp(RequestHolder.getHttpServletRequest()), (ProceedingJoinPoint)joinPoint, log);
    }

    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        }catch (Exception e){
            return "";
        }
    }
    public String getName() {
        try {
            return SecurityUtils.getName();
        }catch (Exception e){
            return "";
        }
    }
}
