package io.droidme.commons.configuration;

import io.droidme.commons.logging.LogProducer;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author ga2merf
 */
@RunWith(Arquillian.class)
public class ConfiguratorIT {

    @Inject
    Configurator configurator;

    @Inject
    @Configurable("configured")
    private boolean configured;

    @Inject
    String message;

    @Inject
    @Configurable("some_port")
    private int port;
    
    @Inject
    private Instance<String> dynamicMessage;

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap
                .create(WebArchive.class, "configurator.war")
                .addClasses(
                        LogProducer.class,
                        Configurable.class,
                        ConfigurationProvider.class,
                        Configurator.class,
                        Stage.class,
                        StageProducer.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void injection() {
        assertNotNull(configurator);
    }

    @Test
    public void testSystemConfig() {
        assertTrue(configured);
        assertEquals("Hello Configurator", message);
    }

    @Test
    public void testStaging(Stage stage) {
        System.out.println("port: " + port);
        if (stage.is(Stage.LOCAL)) {
            assertEquals(port, 100);
        }
        if (stage.is(Stage.DEVELOPMENT)) {
            assertEquals(port, 200);
        }
        if (stage.is(Stage.TEST)) {
            assertEquals(port, 300);
        }
        if (stage.is(Stage.PRELIVE)) {
            assertEquals(port, 400);
        }
        if (stage.is(Stage.PRODUCTION)) {
            assertEquals(port, 500);
        }
    }
    
/*
    @Test
    public void dynamicReconfigure() {
        String message = dynamicMessage.get();
        assertNotNull(message);
        assertThat(message, is("DYNAMIC"));
        
        // just reconfigure
        String expected = "reconfigured";
    }
*/
    
}
