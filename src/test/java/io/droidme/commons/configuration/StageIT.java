package io.droidme.commons.configuration;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author droidme
 */
@RunWith(Arquillian.class)
public class StageIT {

    @Inject
    Instance<Stage> stage;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "io.droidme.commons")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void defaultStage() {
        System.clearProperty(StageProducer.STAGE_KEY);
        Stage current = stage.get();
        Stage expected = Stage.valueOf(StageProducer.DEFAULT_STAGE);
        assertThat(current, is(expected));
    }

    @Test
    public void testDevStage() {
        System.setProperty(StageProducer.STAGE_KEY, Stage.DEVELOPMENT.name());
        Stage current = stage.get();
        Stage expected = Stage.DEVELOPMENT;
        assertThat(current, is(expected));
    }

    @Test
    public void testTestStage() {
        System.setProperty(StageProducer.STAGE_KEY, Stage.TEST.name());
        Stage current = stage.get();
        Stage expected = Stage.TEST;
        assertThat(current, is(expected));
    }

}
