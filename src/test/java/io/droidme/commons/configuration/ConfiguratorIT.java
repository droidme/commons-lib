package io.droidme.commons.configuration;

import io.droidme.commons.logging.LogProducer;
import java.io.File;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
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
    
    @Deployment
    public static WebArchive createDeployment() {
        
        // Import Maven runtime dependencies
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
        
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        LogProducer.class,
                        Configurable.class,
                        Encrypted.class,
                        ConfigurationProvider.class,
                        Configurator.class,
                        Stage.class,
                        StageProducer.class)
                .addAsLibraries(files)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("system.config")
                .addAsResource("stage/local.config")
                .addAsResource("stage/development.config")
                .addAsResource("stage/test.config");
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
    }
    
    
}
