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
