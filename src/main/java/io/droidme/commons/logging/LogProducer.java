/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
