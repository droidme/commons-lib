package io.droidme.commons.configuration;

import javax.inject.Inject;

/**
 *
 * @author ga2merf
 */
public class ConfiguratorTestSupport {

    @Inject
    @Configurable("configured")
    boolean configured;

    @Inject
    @Configurable("server")
    String server;
}
