package duson.java.solutionConf.concurrentRetry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

/**
 * 乐观锁重试机制
 */
@Aspect
public class OptimisticLockAOP implements Ordered {

	private static final int DEFAULT_MAX_RETRIES = 2;

	private int maxRetries = DEFAULT_MAX_RETRIES;
	private int order = 1;

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Around("execution (* xxx.service.modules..*.*(..)) && @annotation (duson.java.solutionConf.concurrentRetry.OptimisticLock)")
	public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
		int numAttempts = 0;
		Exception lockFailureException;
		do {
			numAttempts++;
			try {
				return pjp.proceed();
			} catch (Exception ex) {
				lockFailureException = ex;
			}
		} while (numAttempts <= this.maxRetries);
		
		throw lockFailureException;
	}

}
