package com.example.showtime.app.aspect;

import android.util.Log;
import android.widget.Button;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {
    //    private static final String TAG = LoggingAspect.class.getName();
    private static final String TAG = "AspectJ";

    @Around("execution(* com.example.showtime.app.*(..) )")
    public void anyMethodAround(JoinPoint joinPoint) {
        Log.d(TAG, joinPoint.getSignature().getName());
    }

    @Pointcut("execution(* android.view.View.OnClickListener.onClick(..))")
    public void onClickEntryPoint() {
    }

    @Around("onClickEntryPoint()")
    public void onClickAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "Clicked on : " + ((Button) joinPoint.getArgs()[0]).getText());

        joinPoint.proceed();
    }
}