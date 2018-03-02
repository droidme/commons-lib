package io.droidme.commons.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author droidme
 */
public class LogProducer {
    
    @Produces
    public Logger produce(InjectionPoint ip) {
        return LoggerFactory.getLogger(
                ip.getMember().getDeclaringClass());
    }
    
}
