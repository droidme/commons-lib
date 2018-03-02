package io.droidme.commons.logging;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;

/**
 *
 * @author droidme
 */

@Traceable @Interceptor
public class TracingInterceptor {

    @Inject
    Logger log;

    @AroundInvoke
    public Object trace(InvocationContext ic) throws Exception {
        String icDesc = getInvocationDescripton(ic);
        log.info("before invocation of {}", icDesc);
        long t0 = System.currentTimeMillis();
        Object result = ic.proceed();
        log.info("after invocation of {}. {} ms elapsed.", icDesc, 
                System.currentTimeMillis() -t0);
        return result;
    }

    private String getInvocationDescripton(InvocationContext ic) {
        Method method = ic.getMethod();
        return MessageFormat
                .format("[class={0}, method={1}]",
                        method.getDeclaringClass().getName(),
                        method.getName());
    }
}
