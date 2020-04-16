package unideb.diploma.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationAspect {
	
	private static final Logger LOG = LoggerFactory.getLogger(AnnotationAspect.class);
	
	@Around("onExecutionTimeAnnotation()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object obejctReturnedByMethod = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - start;
		LOG.info("Method: " + joinPoint.getSignature().toShortString() + ", Execution time: " + executionTime + " ms");
		return obejctReturnedByMethod;
	}
	
	@Around("onLogAnnotation()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] arguments = joinPoint.getArgs();
		String argumentsString = "";
		for(Object arg : arguments) {
			argumentsString += arg.toString() + "; ";
		}
		LOG.info("Method: " + joinPoint.getSignature().toShortString() + ", Arguments: " + argumentsString);
		Object objectReturnedByMethod = joinPoint.proceed();
		LOG.info("Method: " + joinPoint.getSignature().toShortString() + ", Return value: " + objectReturnedByMethod);
		return objectReturnedByMethod;
	}
	
	@Pointcut("@annotation(unideb.diploma.annotation.ExecutionTime)")
	public void onExecutionTimeAnnotation() {}
	
	@Pointcut("@annotation(unideb.diploma.annotation.Log)")
	public void onLogAnnotation() {}
	
}
