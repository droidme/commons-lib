package io.droidme.commons.dummy;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

/**
 *
 * @author ga2merf
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SingletonTestClass {
    
    private boolean active;

    public SingletonTestClass() {
    }
    

    @PostConstruct
    private void init() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }
    
}
