/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * 
 * for using the annotation @Traceable please have in mind that you have
 * to configure this interceptor in the beans.xml of the web application 
 * who is using this interceptor binding. 
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
