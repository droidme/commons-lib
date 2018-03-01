/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

import javax.enterprise.inject.Produces;

/**
 *
 * @author droidme
 */
public class StageProducer {

    public static final String STAGE_KEY = "stage";
    private static final String DEFAULT_STAGE = Stage.LOCAL.name();

    @Produces
    public Stage produce() {
        String stage = System.getProperty(STAGE_KEY);
        if (stage == null) {
            stage = System.getenv(STAGE_KEY);
        }
        if (stage == null) {
            stage = DEFAULT_STAGE;
        }
        return Stage.valueOf(stage.toUpperCase());
    }

}
