/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

/**
 *
 * @author droidme
 */
public enum Stage {
    LOCAL,
    DEVELOPMENT,
    TEST,
    PRELIVE,
    PRODUCTION;
    
    public boolean is(Stage stage) {
        return this == stage;
    }
}
