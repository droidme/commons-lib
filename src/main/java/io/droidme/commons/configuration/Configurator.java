package io.droidme.commons.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author droidme
 */
public class Configurator {

    private Properties configuration;

    @Inject
    Logger log;

    @Inject
    Stage stage;

    @Inject
    private Instance<ConfigurationProvider> configurationProvider;

    @PostConstruct
    private void init() {
        configuration = defaults();
        configuration.putAll(custom());
    }

    public Properties defaults() {
        Properties props = new Properties();
        List<String> configFiles = Arrays.asList(
                "system.config",
                "stage/" + stage.name().toLowerCase() + ".config");
        configFiles.forEach(file -> {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(file);
            if (inputStream != null) {
                try {
                    log.debug("loading file: {}", file);
                    props.load(inputStream);
                } catch (IOException ex) {
                    log.error("Error loading {}", file, ex);
                }
            }

        });
        return props;
    }

    public Properties custom() {
        Properties props = new Properties();
        configurationProvider
                .forEach(provider -> {
                    props.putAll(provider.getConfiuration());
                });
        return props;
    }

    @Produces
    public String getString(InjectionPoint ip) {
        String key = obtainConfigurable(ip);
        String value = configuration.getProperty(key);
        if (value == null) {
            log.warn("Key {} not properly configured.", key);
        }
        return value;
    }

    @Produces
    public int getInteger(InjectionPoint ip) {
        String value = getString(ip);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    @Produces
    public boolean getBoolean(InjectionPoint ip) {
        String value = getString(ip);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    String obtainConfigurable(InjectionPoint ip) {
        AnnotatedField field = (AnnotatedField) ip.getAnnotated();
        Configurable configurable = field
                .getAnnotation(Configurable.class);
        if (configurable != null) {
            return configurable.value();
        } else {
            String clazzName = ip
                    .getMember().getDeclaringClass().getName();
            String memberName = ip
                    .getMember().getName();
            return clazzName + "." + memberName;
        }
    }

}
