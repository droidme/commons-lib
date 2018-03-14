package io.droidme.commons.logging;

import javax.inject.Inject;
import static org.hamcrest.core.Is.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

/**
 *
 * @author ga2merf
 */
@RunWith(Arquillian.class)
public class LogProducerIT {

    @Inject
    Logger log;

    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "io.droidme.commons")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testProducer() {
        assertNotNull(log);
        assertThat(log.getName(), is(getClass().getName()));
        log.info("The Logger seems to running fine :)");
    }

}
