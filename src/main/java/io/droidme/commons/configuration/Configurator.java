/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

import io.droidme.commons.logging.Traceable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
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

    Properties configuration;

    @Inject
    Logger log;

    @Inject
    Stage stage;

    @PostConstruct
    public void loadDefaults() {
        configuration = new Properties();
        List<String> configFiles = Arrays.asList(
                "system.config",
                "stage/" + stage.name().toLowerCase() + ".config");
        configFiles.forEach(file -> {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(file);
            if (inputStream != null) {
                try {
                    configuration.load(inputStream);
                } catch (IOException ex) {
                    log.error("Error loading {}", file, ex);
                }
            }

        });
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
