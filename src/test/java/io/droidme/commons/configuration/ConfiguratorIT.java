package io.droidme.commons.configuration;

import io.droidme.commons.logging.LogProducer;
import io.droidme.commons.logging.Traceable;
import io.droidme.commons.logging.TracingInterceptor;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author ga2merf
 */
@RunWith(Arquillian.class)
public class ConfiguratorIT {

    @Inject
    Instance<ConfiguratorTestSupport> cts;
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        Configurator.class,
                        Stage.class,
                        StageProducer.class,
                        LogProducer.class,
                        Traceable.class,
                        TracingInterceptor.class,
                        Configurable.class,
                        Encrypted.class,
                        ConfigurationProvider.class,
                        ConfiguratorTestSupport.class
                        )
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test @Ignore
    public void configured() {
        assertNotNull(cts.get());
    }
}
