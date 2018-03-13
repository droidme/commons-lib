package io.droidme.commons.configuration;

import io.droidme.commons.crypt.Crypter;
import io.droidme.commons.crypt.Encrypted;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ejb.Singleton;
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
@Singleton
public class Configurator {

    private Properties configuration;

    @Inject
    Logger log;

    @Inject
    Stage stage;

    @Inject
    private Instance<ConfigurationProvider> configurationProvider;

    @Inject
    private Crypter crypter;

    @PostConstruct
    private void init() {
        this.configuration = defaults();
        this.configuration.putAll(custom());
    }

    private Properties defaults() {
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

    private Properties custom() {
        Properties props = new Properties();
        configurationProvider
                .forEach(provider -> {
                    props.putAll(provider.getConfiuration());
                });
        return props;
    }

    public Properties getConfiguration() {
        return configuration;
    }

    @Produces
    public String getString(InjectionPoint ip) {
        String key = obtainConfigurable(ip);
        String value = configuration.getProperty(key);
        if (value == null) {
            log.warn("Key {} not properly configured.", key);
        } else {
            Encrypted encrypted = obtainEncryptedAnnotation(ip);
            if (encrypted != null) {
                try {
                    value = crypter.decrypt(value);
                } catch (InvalidKeyException
                        | IllegalBlockSizeException
                        | BadPaddingException ex) {
                    log.error("Error decrypt congigured value {}, {}", value, ex);
                }
            }
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

    private String obtainConfigurable(InjectionPoint ip) {
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

    private Encrypted obtainEncryptedAnnotation(InjectionPoint ip) {
        AnnotatedField field = (AnnotatedField) ip.getAnnotated();
        return field.getAnnotation(Encrypted.class);
    }

}
