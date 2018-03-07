/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

import com.sun.enterprise.deployment.node.EnvEntryNode;
import io.droidme.commons.logging.LogProducer;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
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
    
    public static final String STAGE_KEY = "stage";
    
    @Inject
    Instance<Stage> stage;
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(Stage.class,StageProducer.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void testStage() {
        // set stage environment to development
        System.setProperty(STAGE_KEY, "development");
        assertEquals(Stage.DEVELOPMENT, stage.get());
        // set stage environment to test
        System.setProperty(STAGE_KEY, "test");
        assertEquals(Stage.TEST, stage.get());
    }
    
}
