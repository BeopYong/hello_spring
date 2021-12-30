package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LogAspect {

	/**
	 * aspect-context.xml에서 등록하던 것들을 @Component @Aspect @Pointcut @Around로 대체 가능
	 */
	@Pointcut("execution(* com.kh.spring.memo..*(..))")
	public void pointcut() {}
	
	@Around("pointcut()") // 위 메소드를 호출
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature(); // 메소드정보
		String type = signature.getDeclaringTypeName(); // 클래스명
		String methodName = signature.getName(); // 메소드명
		
		// joinPoint 호출전
		log.debug("[Before] {}.{}", type, methodName);
		
		// 주업무로직의 특정메소드 호출
		Object retObj = joinPoint.proceed();
		
		// joinPoint 호출후
		log.debug("[After] {}.{}", type, methodName);
		
		return retObj;
		
	}
}
